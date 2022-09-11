package app.tilt.marvel.data

import app.tilt.marvel.domain.Hero
import kotlinx.coroutines.flow.Flow

interface HeroRepository {

    fun getAllHeroes(): Flow<List<Hero>>

    suspend fun loadHeroes(searchCriteria: String? = null)

    suspend fun loadMoreHeroes(searchCriteria: String? = null)

    suspend fun getHeroWithId(heroId: Int): Hero
}
