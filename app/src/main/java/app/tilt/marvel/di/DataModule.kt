package app.tilt.marvel.di

import app.tilt.marvel.data.ComicRepository
import app.tilt.marvel.data.DefaultComicRepository
import app.tilt.marvel.data.DefaultHeroRepository
import app.tilt.marvel.data.HeroRepository
import app.tilt.marvel.data.local.InMemoryDataSource
import app.tilt.marvel.data.local.LocalHeroDataSource
import app.tilt.marvel.data.remote.MarvelApi
import app.tilt.marvel.data.remote.RemoteHeroDataSource
import app.tilt.marvel.data.remote.RetrofitHeroDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun providesLocalHeroDataSource(): LocalHeroDataSource =
        InMemoryDataSource()

    @Provides
    @Singleton
    fun providesRemoteHeroDataSource(marvelApi: MarvelApi): RemoteHeroDataSource =
        RetrofitHeroDataSource(marvelApi)

    @Provides
    @Singleton
    fun providesHeroRepository(
        localHeroDataSource: LocalHeroDataSource,
        remoteHeroDataSource: RemoteHeroDataSource
    ): HeroRepository = DefaultHeroRepository(localHeroDataSource, remoteHeroDataSource)

    @Provides
    @Singleton
    fun providesMarvelApi(): MarvelApi {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MarvelApi::class.java)
    }

    @Provides
    @Singleton
    fun providesComicRepository(
        marvelApi: MarvelApi
    ): ComicRepository {
        return DefaultComicRepository(marvelApi)
    }
}
