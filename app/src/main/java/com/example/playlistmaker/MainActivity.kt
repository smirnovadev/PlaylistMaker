package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageSearch = findViewById<Button>(R.id.search)
        imageSearch.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val imageMusic = findViewById<Button>(R.id.music)
        imageMusic.setOnClickListener{
            val musicIntent = Intent(this, MusicActivity::class.java)
            startActivity(musicIntent)
        }

        val imageSetting = findViewById<Button>(R.id.setting)
        imageSetting.setOnClickListener{
            val searchIntent = Intent(this, SettingsActivity::class.java)
            startActivity(searchIntent)
        }


    }
}