package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.AudioPlayerActivity.Companion.KEY_TRACK_DATA
import com.example.playlistmaker.databinding.ItemSearchBinding

class TrackAdapter(
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    var trackList = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            searchHistory.saveTrack(trackList[position])
            val playerIntent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
            playerIntent.putExtra(KEY_TRACK_DATA, trackList[position])
            holder.itemView.context.startActivity(playerIntent)
        }
    }

    class TrackViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .centerCrop()
                .placeholder(R.drawable.track_placeholder_45)
                .transform(RoundedCorners(10))
                .into(binding.trackCoverImage)
            val formattedDuration = track.formattedDuration()

            binding.trackName.text = track.trackName
            binding.trackDescription.text = itemView.context.getString(
                R.string.format_track_description,
                track.artistName,
                formattedDuration
            )
        }
    }
}
