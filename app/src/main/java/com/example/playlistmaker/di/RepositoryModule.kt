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
        TrackHistoryRepositoryImpl(get(), get())
    }

    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get(), get())
    }
}