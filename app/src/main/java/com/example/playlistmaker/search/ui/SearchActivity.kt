package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val trackList = ArrayList<Track>()
    private val viewModel: SearchViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSearchStateLiveData().observe(this) { searchState ->
            when (searchState) {
                is SearchState.Loading -> showLoading()
                is SearchState.Content -> showContent(searchState.tracks)
                is SearchState.Error -> showError()
                is SearchState.History -> showHistory(searchState.tracks)
            }

        }

        searchAdapter = TrackAdapter(onClickAction = { trackData ->
            viewModel.saveTrackToHistory(trackData)
            navigateToAudioPlayerActivity(trackData)
        })
        historyAdapter = TrackAdapter(onClickAction = { trackData ->
            viewModel.saveTrackToHistory(trackData)
            navigateToAudioPlayerActivity(trackData)
        })

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editText = binding.searchEditText

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text?.isNotEmpty() == true) {
                    viewModel.searchForTrack()
                    hideKeyboard(editText)
                }
                true
            } else {
                false
            }
        }

        editText.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                if (editText.hasFocus() && text?.isEmpty() == true) {
                    viewModel.loadHistoryData()
                }
                binding.clearButton.visibility = clearButtonVisibility(text)
                viewModel.searchDebounce()
            },
            afterTextChanged = { text: Editable? ->
                if (text != null) {
                    viewModel.updateUserText(text.toString())
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
                viewModel.searchForTrack()
            }
        }
        searchAdapter.trackList = trackList
        val recyclerView = binding.searchRecycler
        recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        recyclerView.adapter = searchAdapter

        val historyRecyclerView = binding.historyRecycler
        historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        historyRecyclerView.adapter = historyAdapter
        viewModel.loadHistoryData()

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && (editText.text?.isEmpty() == true)) {
                viewModel.loadHistoryData()
            }
        }
        val clearHistoryButton = binding.clearHistoryButton
        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            historyAdapter.trackList.clear()
            historyAdapter.notifyDataSetChanged()
            binding.historyContainer.visibility = View.GONE
        }

    }

    private fun navigateToAudioPlayerActivity(trackData: Track) {
        viewModel.saveTrackToCache(trackData)
        val playerIntent = Intent(this, AudioPlayerActivity::class.java)
        startActivity(playerIntent)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.historyContainer.visibility = View.GONE
        binding.searchRecycler.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyContainer.visibility = View.GONE
    }

    private fun showHistory(tracks: List<Track>) {
        binding.searchRecycler.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyContainer.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

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

    companion object {
        const val DEFAULT_TEXT = ""
        const val CLICK_DEBOUNCE_DELAY = 2000L
    }
}
