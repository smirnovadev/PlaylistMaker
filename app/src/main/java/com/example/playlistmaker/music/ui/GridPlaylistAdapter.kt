package com.example.playlistmaker.music.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.databinding.ItemGridPlaylistBinding
import java.io.File

class GridPlaylistAdapter : RecyclerView.Adapter<GridPlaylistAdapter.ViewHolder>() {
    var playlists = ArrayList<Playlist>()
    private var onClickAction: (Playlist) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemGridPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onClickAction.invoke(playlist)
        }
    }
    fun setOnClickListener(onClick: (Playlist) -> Unit) {
        onClickAction = onClick
    }


    class ViewHolder(
        private val binding: ItemGridPlaylistBinding
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