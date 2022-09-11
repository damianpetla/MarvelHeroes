package app.tilt.marvel.data.local

import app.tilt.marvel.domain.Hero
import kotlinx.coroutines.flow.Flow

interface LocalHeroDataSource {

    fun getAllHeroes(): Flow<List<Hero>>

    suspend fun addHeroes(heroList: List<Hero>)

    suspend fun clearHeroes()

    suspend fun getHeroesCount(): Int

    suspend fun getHeroWithId(heroId: Int): Hero
}
