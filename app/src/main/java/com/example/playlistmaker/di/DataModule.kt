import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.ItunesApi
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single<NetworkClient> { RetrofitNetworkClient(get()) }

    single<SharedPreferences> {
        androidContext()
            .getSharedPreferences("key_for_playlistMaker", Context.MODE_PRIVATE)
    }

    factory { Gson() }


    single {
        TrackMapper()
    }


}

