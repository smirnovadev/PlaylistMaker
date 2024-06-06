import com.example.playlistmaker.createPlaylist.domain.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.createPlaylist.domain.impl.SaveImageToPrivateStorageUseCaseImpl
import com.example.playlistmaker.db.entity.domain.AddFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.AddTrackToPlaylistUseCase
import com.example.playlistmaker.db.entity.domain.ClearFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.GetAllFavoriteTrackUseCase
import com.example.playlistmaker.db.entity.domain.GetAllPlaylistUseCase
import com.example.playlistmaker.db.entity.domain.GetPlaylistByIdUseCase
import com.example.playlistmaker.db.entity.domain.SavePlaylistUseCase
import com.example.playlistmaker.db.entity.domain.impl.AddFavoriteTrackUseCaseImpl
import com.example.playlistmaker.db.entity.domain.impl.AddTrackToPlaylistUseCaseImpl
import com.example.playlistmaker.db.entity.domain.impl.ClearFavoriteTrackUseCaseImpl
import com.example.playlistmaker.db.entity.domain.impl.GetAllFavoriteTrackUseCaseImpl
import com.example.playlistmaker.db.entity.domain.impl.GetAllPlaylistUseCaseImpl
import com.example.playlistmaker.db.entity.domain.impl.GetPlaylistByIdUseCaseImpl
import com.example.playlistmaker.db.entity.domain.impl.SavePlaylistUseCaseImpl
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCase
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCaseImpl
import com.example.playlistmaker.playlist.domain.DeletePlaylistUseCase
import com.example.playlistmaker.playlist.domain.DeleteTrackFromPlaylistUseCase
import com.example.playlistmaker.playlist.domain.GetAllTracksForPlaylistUseCase
import com.example.playlistmaker.playlist.domain.impl.DeletePlaylistUseCaseImpl
import com.example.playlistmaker.playlist.domain.impl.DeleteTrackFromPlaylistUseCaseImpl
import com.example.playlistmaker.playlist.domain.impl.GetAllTracksForPlaylistUseCaseImpl
import com.example.playlistmaker.search.domain.api.ClearTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetTrackHistoryUseCase
import com.example.playlistmaker.search.domain.api.GetTracksSearchQueryUseCase
import com.example.playlistmaker.search.domain.api.SaveTrackToCacheUseCase
import com.example.playlistmaker.search.domain.api.SaveTrackToHistoryUseCase
import com.example.playlistmaker.search.domain.impl.ClearTrackHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.GetTrackHistoryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.GetTracksSearchQueryUseCaseImpl
import com.example.playlistmaker.search.domain.impl.SaveTrackToCacheUseCaseImpl
import com.example.playlistmaker.search.domain.impl.SaveTrackToHistoryUseCaseImpl
import com.example.playlistmaker.settings.domain.GetIsNightThemeUseCase
import com.example.playlistmaker.settings.domain.GetIsNightThemeUseCaseImpl
import com.example.playlistmaker.settings.domain.SaveThemeUseCase
import com.example.playlistmaker.settings.domain.SaveThemeUseCaseImpl
import org.koin.dsl.module


val useCaseModule = module {
    single<GetTrackFromCacheUseCase> {
        GetTrackFromCacheUseCaseImpl(get())
    }

    single<ClearTrackHistoryUseCase> {
        ClearTrackHistoryUseCaseImpl(get())
    }


    single<GetTracksSearchQueryUseCase> {
        GetTracksSearchQueryUseCaseImpl(get())
    }

    single<SaveTrackToCacheUseCase> {
        SaveTrackToCacheUseCaseImpl(get())
    }

    single<SaveTrackToHistoryUseCase> {
        SaveTrackToHistoryUseCaseImpl(get())
    }

    single<GetIsNightThemeUseCase> {
        GetIsNightThemeUseCaseImpl(get())
    }

    single<SaveThemeUseCase> {
        SaveThemeUseCaseImpl(get())
    }

    single<GetTrackHistoryUseCase> {
        GetTrackHistoryUseCaseImpl(get())
    }
    single<AddFavoriteTrackUseCase> {
        AddFavoriteTrackUseCaseImpl(get())
    }
    single<ClearFavoriteTrackUseCase> {
        ClearFavoriteTrackUseCaseImpl(get())
    }
    single<GetAllFavoriteTrackUseCase> {
        GetAllFavoriteTrackUseCaseImpl(get())
    }

    single<SaveImageToPrivateStorageUseCase> {
        SaveImageToPrivateStorageUseCaseImpl(get())
    }
    single<SavePlaylistUseCase> {
        SavePlaylistUseCaseImpl(get())
    }

    single<GetAllPlaylistUseCase>  {
        GetAllPlaylistUseCaseImpl(get())
    }

    single<AddTrackToPlaylistUseCase> {
        AddTrackToPlaylistUseCaseImpl(get())
    }

    single<GetPlaylistByIdUseCase> {
        GetPlaylistByIdUseCaseImpl(get())
    }

    single<GetAllTracksForPlaylistUseCase> {
        GetAllTracksForPlaylistUseCaseImpl(get())
    }

    single<DeleteTrackFromPlaylistUseCase> {
        DeleteTrackFromPlaylistUseCaseImpl(get())
    }

    single<DeletePlaylistUseCase> {
        DeletePlaylistUseCaseImpl(get())
    }

}