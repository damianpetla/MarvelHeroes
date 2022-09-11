package app.tilt.marvel.data.remote

import app.tilt.marvel.BuildConfig
import app.tilt.marvel.data.remote.dto.CharacterDataWrapper
import app.tilt.marvel.data.remote.dto.ComicDataWrapper
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @Headers("Referer: http://marvel.tilt.app")
    @GET("v1/public/characters")
    suspend fun getAllCharacters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("nameStartsWith") nameStartsWith: String? = null,
        @Query("apikey") apikey: String = BuildConfig.API_KEY
    ): CharacterDataWrapper

    @Headers("Referer: http://marvel.tilt.app")
    @GET("v1/public/characters/{characterId}/comics")
    suspend fun getComicsForHero(
        @Path("characterId") characterId: Int,
        @Query("apikey") apikey: String = BuildConfig.API_KEY
    ): ComicDataWrapper
}
