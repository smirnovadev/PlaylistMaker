import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCase
import com.example.playlistmaker.player.domain.GetTrackFromCacheUseCaseImpl
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
}