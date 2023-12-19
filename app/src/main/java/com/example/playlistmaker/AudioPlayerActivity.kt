package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.AudioPlayerBinding


class AudioPlayerActivity: AppCompatActivity() {
    private lateinit var binding: AudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val trackData = intent.extras?.getSerializable(KEY_TRACK_DATA) as Track
        setTrackData(trackData)
    }

    private fun setTrackData(track: Track){
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_312)
            .transform(RoundedCorners(8))
            .into(binding.trackCover)
        val formattedTrackTimes = track.formattedDuration()
        val formattedTrackYear = track.extractYear()
        binding.trackTime.text = formattedTrackTimes

        if (track.collectionName == null) {
            binding.album.visibility = View.GONE
            binding.collectionName.visibility = View.GONE
        } else {
            binding.album.text = track.collectionName
        }

        binding.trackYear.text = formattedTrackYear
        binding.genreTrack.text = track.primaryGenreName
        binding.trackName.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.countryTrack.text = track.country
    }

    companion object{
        const val KEY_TRACK_DATA = "KEY_TRACK_DATA"
    }
}
