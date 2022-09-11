package app.tilt.marvel.data.local

import app.tilt.marvel.domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class InMemoryDataSource : LocalHeroDataSource {

    private val heroListState = MutableStateFlow(emptyList<Hero>())

    override fun getAllHeroes(): Flow<List<Hero>> {
        return heroListState
    }

    override suspend fun addHeroes(heroList: List<Hero>) {
        heroListState.update {
            it + heroList
        }
    }

    override suspend fun clearHeroes() {
        heroListState.update { emptyList() }
    }

    override suspend fun getHeroesCount(): Int {
        return heroListState.value.size
    }

    override suspend fun getHeroWithId(heroId: Int): Hero {
        return heroListState.value.first { it.id == heroId }
    }
}
