import com.example.playlistmaker.createPlaylist.data.ImageStorageRepositoryImpl
import com.example.playlistmaker.createPlaylist.domain.ImageStorageRepository
import com.example.playlistmaker.db.entity.PlaylistConverter
import com.example.playlistmaker.db.entity.TrackDbConvertor
import com.example.playlistmaker.db.entity.data.db.FavoritesTrackRepositoryImpl
import com.example.playlistmaker.db.entity.data.db.PlaylistRepositoryImpl
import com.example.playlistmaker.db.entity.domain.FavoritesTrackRepository
import com.example.playlistmaker.db.entity.domain.PlaylistRepository
import com.example.playlistmaker.player.data.TrackCacheRepositoryImpl
import com.example.playlistmaker.player.domain.TrackCacheRepository
import com.example.playlistmaker.search.data.SearchTrackRepositoryImpl
import com.example.playlistmaker.search.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.SearchTrackRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackCacheRepository> {
        TrackCacheRepositoryImpl()
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
    single<SearchHistoryRepository> {
        TrackHistoryRepositoryImpl(get(), get(), get())
    }

    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get(), get(), get())
    }

    single<FavoritesTrackRepository> {
        FavoritesTrackRepositoryImpl(get(), get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }

    single<ImageStorageRepository> {
        ImageStorageRepositoryImpl(get())
    }
    single { TrackDbConvertor() }

    single { PlaylistConverter() }
}