package app.tilt.marvel.data.remote

import app.tilt.marvel.domain.Hero

interface RemoteHeroDataSource {

    suspend fun getHeroes(offset: Int = 0, limit: Int = 10, searchCriteria: String? = null): List<Hero>
}
