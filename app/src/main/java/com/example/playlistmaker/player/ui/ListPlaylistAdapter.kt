package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.domain.model.Playlist
import com.example.playlistmaker.databinding.ItemListPlaylistBinding
import java.io.File

class ListPlaylistAdapter : RecyclerView.Adapter<ListPlaylistAdapter.ViewHolder>() {
    var playlists = ArrayList<Playlist>()
    private var onClickListener: ((Playlist) -> Unit)? = null
    fun setOnClickListener(onClickAction: (Playlist) -> Unit) {
        onClickListener = onClickAction
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onClickListener?.invoke(playlist)
        }
    }

    class ViewHolder(
        private val binding: ItemListPlaylistBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist) {
            Glide.with(itemView)
                .load(File(playlist.coverArtPath))
                .fitCenter()
                .placeholder(R.drawable.track_placeholder_45)
                .transform(RoundedCorners(8))
                .into(binding.playlistCoverImage)

            binding.playlistName.text = playlist.playlistName
            binding.tracksNumber.text =
                itemView.context.resources.getQuantityString(
                    R.plurals.tracks,
                    playlist.numberTracks,
                    playlist.numberTracks
                )
        }
    }
}