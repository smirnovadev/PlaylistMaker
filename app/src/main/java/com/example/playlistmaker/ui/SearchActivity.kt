package com.example.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var userText: String = DEFAULT_TEXT

    private val getTrackSearchQueryUseCase = Creator.provideGetTrackSearchQueryUseCase()
    private val clearTrackHistoryUseCase = Creator.provideClearTrackHistoryUseCase()
    private val saveTrackToHistoryUseCase = Creator.provideSaveTrackToHistoryUseCase()
    private val getTrackHistoryUseCase = Creator.provideGetTrackHistoryUseCase()
    private val saveTrackToCacheUseCase = Creator.provideSaveTrackToCacheUseCase()

    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val trackList = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = TrackAdapter(onClickAction = { trackData ->
            saveTrackToHistoryUseCase.execute(trackData)
            navigateToAudioPlayerActivity(trackData)
        })
        historyAdapter = TrackAdapter(onClickAction = { trackData ->
            saveTrackToHistoryUseCase.execute(trackData)
            navigateToAudioPlayerActivity(trackData)
        })

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editText = binding.searchEditText
        editText.setText(userText)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text?.isNotEmpty() == true) {
                    searchForTrack(editText.text.toString())
                    hideKeyboard(editText)
                }
                true
            } else {
                false
            }
        }

        editText.addTextChangedListener(
            beforeTextChanged = { text, start, count, after ->

            },
            onTextChanged = { text, start, before, count ->
                binding.clearButton.visibility = clearButtonVisibility(text)
                searchDebounce()
            },
            afterTextChanged = { text: Editable? ->
                if (text != null) {
                    userText = text.toString()
                }
            }
        )
        binding.clearButton.setOnClickListener {
            editText.setText(DEFAULT_TEXT)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        binding.retryButton.setOnClickListener {
            if (editText.text?.isNotEmpty() == true) {
                searchForTrack(editText.text.toString())
            }
        }
        searchAdapter.trackList = trackList
        val recyclerView = binding.searchRecycler
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        recyclerView.adapter = searchAdapter

        val historyRecyclerView = binding.historyRecycler
        historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        historyRecyclerView.adapter = historyAdapter
        showHistory()

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && (editText.text?.isEmpty() == true)) {
                showHistory()
            }
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.hasFocus() && s?.isEmpty() == true) {
                    showHistory()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        val clearHistoryButton = binding.clearHistoryButton
        clearHistoryButton.setOnClickListener {
            clearTrackHistoryUseCase.execute()
            historyAdapter.trackList.clear()
            historyAdapter.notifyDataSetChanged()
            binding.historyContainer.visibility = View.GONE
        }

    }

    private fun navigateToAudioPlayerActivity(trackData: Track) {
        saveTrackToCacheUseCase.execute(trackData)
        val playerIntent = Intent(this, AudioPlayerActivity::class.java)
        startActivity(playerIntent)
    }

    private val searchRunnable = Runnable {
        if (clickDebounce() && binding.searchEditText.text?.isNotEmpty() == true) {
            searchForTrack(binding.searchEditText.text.toString())
        }
    }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, CLICK_DEBOUNCE_DELAY)
        binding.errorContainer.visibility = View.GONE
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun searchForTrack(text: String) {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            val tracks = getTrackSearchQueryUseCase.execute(text)
            withContext(Dispatchers.Main) {
                if (tracks != null) {
                    showContent(tracks)
                } else {
                    showError()
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.historyContainer.visibility = View.GONE
        binding.searchRecycler.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyContainer.visibility = View.GONE
    }

    private fun showHistory() {
        binding.searchRecycler.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyContainer.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        val tracks = getTrackHistoryUseCase.execute()
        if (tracks.isEmpty()) {
            binding.historyContainer.visibility = View.GONE
        } else {
            binding.historyContainer.visibility = View.VISIBLE
        }
        historyAdapter.trackList.clear()
        historyAdapter.trackList.addAll(tracks)
        historyAdapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>?) {
        trackList.clear()
        if (tracks?.isNotEmpty() == true) {
            binding.emptyContainer.visibility = View.GONE
            binding.searchRecycler.visibility = View.VISIBLE
            trackList.addAll(tracks)
            searchAdapter.notifyDataSetChanged()
        } else {
            binding.emptyContainer.visibility = View.VISIBLE
            binding.searchRecycler.visibility = View.GONE
        }
        binding.errorContainer.visibility = View.GONE
        binding.historyContainer.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showError() {
        binding.historyContainer.visibility = View.GONE
        binding.searchRecycler.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
        binding.emptyContainer.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_TEXT, userText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        userText = savedInstanceState.getString(USER_TEXT, DEFAULT_TEXT)
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val USER_TEXT = "USER_TEXT"
        const val DEFAULT_TEXT = ""
        const val CLICK_DEBOUNCE_DELAY = 2000L
    }
}
