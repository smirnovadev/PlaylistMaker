package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var userText: String = DEFAULT_TEXT
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)
    private val trackList = ArrayList<Track>()

    private lateinit var searchHistory: SearchHistory
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        searchAdapter = TrackAdapter(searchHistory)
        historyAdapter = TrackAdapter(searchHistory)

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
            searchHistory.clearHistory()
            historyAdapter.trackList.clear()
            historyAdapter.notifyDataSetChanged()
            binding.historyContainer.visibility = View.GONE
        }

    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun searchForTrack(text: String) {
        itunesService.search(text).enqueue(object : Callback<MusicResponse> {
            override fun onResponse(
                call: Call<MusicResponse>,
                response: Response<MusicResponse>
            ) {
                if (response.code() == 200) {
                    val body = response.body()
                    showContent(body?.results)
                } else {
                    showError()
                }
            }

            override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                showError()
            }
        })
    }

    private fun showHistory() {
        binding.searchRecycler.visibility = View.GONE
        binding.errorContainer.visibility = View.GONE
        binding.emptyContainer.visibility = View.GONE

        val tracks = searchHistory.getAllTracks()
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
    }

    private fun showError() {
        binding.historyContainer.visibility = View.GONE
        binding.searchRecycler.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE
        binding.emptyContainer.visibility = View.GONE
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

    companion object {
        const val USER_TEXT = "USER_TEXT"
        const val DEFAULT_TEXT = ""
        const val BASE_URL = "https://itunes.apple.com"
        const val SEARCH_HISTORY_PREFERENCES = "key_for_search_history"
    }
}
