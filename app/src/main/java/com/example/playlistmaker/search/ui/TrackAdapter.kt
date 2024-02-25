package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemSearchBinding
import com.example.playlistmaker.search.domain.model.Track

class TrackAdapter(
    private val onClickAction: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    var trackList = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackData = trackList[position]
        holder.bind(trackData)
        holder.itemView.setOnClickListener {
            onClickAction.invoke(trackData)
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
            val formattedDuration = track.formattedDuration

            binding.trackName.text = track.trackName
            binding.trackDescription.text = itemView.context.getString(
                R.string.format_track_description,
                track.artistName,
                formattedDuration
            )
        }
    }
}
