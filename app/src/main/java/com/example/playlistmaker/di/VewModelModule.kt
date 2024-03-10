import com.example.playlistmaker.music.ui.FavoritesMusicViewModel
import com.example.playlistmaker.music.ui.PlaylistViewModel
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
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
        AudioPlayerViewModel(get())
    }

    viewModel {
        FavoritesMusicViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }
}