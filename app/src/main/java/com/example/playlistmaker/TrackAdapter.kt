package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ItemSearchBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    var trackList = ArrayList<Music>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    class TrackViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(music: Music) {
            Glide.with(itemView)
                .load(music.artworkUrl100)
                .centerCrop()
                .placeholder(R.drawable.track_placeholder)
                .transform(RoundedCorners(10))
                .into(binding.trackCoverImage)
            val formatter = SimpleDateFormat("mm:ss", Locale.getDefault())
            val formattedDuration = formatter.format(music.trackTimeMillis)

            binding.trackName.text = music.trackName
            binding.trackDescription.text = itemView.context.getString(
                R.string.format_track_description,
                music.artistName,
                formattedDuration
            )
        }
    }
}