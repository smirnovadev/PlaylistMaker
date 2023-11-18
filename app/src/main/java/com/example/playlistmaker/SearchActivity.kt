package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var userText: String = DEFAULT_TEXT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clearButton: View = binding.clearButton
        val editText: EditText = binding.searchEditText
        editText.setText(userText)

        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }

        editText.addTextChangedListener(
            beforeTextChanged = { text, start, count, after ->

            },
            onTextChanged = { text, start, before, count ->
                clearButton.visibility = clearButtonVisibility(text)
            },
            afterTextChanged = { text: Editable? ->
                if (text != null) {
                    userText = text.toString()
                }
            }
        )
        clearButton.setOnClickListener {
            editText.setText(DEFAULT_TEXT)
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        val trackList = getTracksFromAssets()
        val recyclerView = binding.searchRecycler

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrackAdapter(trackList)
    }

    private fun getTracksFromAssets(): MutableList<Track> {
        val arguments = assets
            .open("tracks.txt")
            .bufferedReader()
            .use { it.readText() }
            .split("\n")
            .map { it.trim() }
        val trackList = mutableListOf<Track>()
        var i = 0
        while (i + 3 < arguments.size) {
            trackList.add(
                Track(
                    arguments[i],
                    arguments[i + 1],
                    arguments[i + 2],
                    arguments[i + 3]
                )
            )
            i += 4
        }
        return trackList
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

    }
}