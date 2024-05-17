package com.example.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.playlist.domain.model.Playlist
import java.io.File

class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    class ViewHolder(
        private val binding: ItemPlaylistBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist) {
            Glide.with(itemView)
                .load(File(playlist.coverArtPath))
                .fitCenter()
                .placeholder(R.drawable.track_placeholder_45)
                .transform(RoundedCorners(8))
                .into(binding.coverPlaylist)

            binding.namePlaylist.text = playlist.playlistName
            binding.numberTrack.text =
                itemView.context.resources.getQuantityString(R.plurals.tracks, playlist.numberTracks, playlist.numberTracks)
        }
    }
}