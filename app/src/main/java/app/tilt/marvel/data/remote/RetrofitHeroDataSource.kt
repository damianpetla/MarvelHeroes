package app.tilt.marvel.data.remote

import app.tilt.marvel.domain.Hero

class RetrofitHeroDataSource(
    private val marvelApi: MarvelApi
) : RemoteHeroDataSource {

    override suspend fun getHeroes(offset: Int, limit: Int, searchCriteria: String?): List<Hero> {
        return marvelApi.getAllCharacters(
            offset = offset,
            limit = limit,
            nameStartsWith = searchCriteria
        ).data.results.toDomain()
    }

}
