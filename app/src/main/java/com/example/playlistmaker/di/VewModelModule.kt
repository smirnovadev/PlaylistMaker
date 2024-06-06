import com.example.playlistmaker.createPlaylist.ui.CreatePlaylistViewModel
import com.example.playlistmaker.music.ui.AllPlaylistsViewModel
import com.example.playlistmaker.music.ui.FavoritesMusicViewModel
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import com.example.playlistmaker.playlist.ui.PlaylistViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModel = module {
    viewModel {
        SearchViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        FavoritesMusicViewModel(get(), get())
    }

    viewModel {
        CreatePlaylistViewModel(get(), get(), get())
    }
    viewModel {
        AllPlaylistsViewModel(get())
    }
    viewModel {
        PlaylistViewModel(get(), get(), get(), get(), get(), get())
    }

}