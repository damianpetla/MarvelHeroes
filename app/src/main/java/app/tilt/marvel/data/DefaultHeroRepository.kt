package app.tilt.marvel.data

import app.tilt.marvel.data.local.LocalHeroDataSource
import app.tilt.marvel.data.remote.RemoteHeroDataSource
import app.tilt.marvel.domain.Hero
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class DefaultHeroRepository(
    private val localHeroDataSource: LocalHeroDataSource,
    private val remoteHeroDataSource: RemoteHeroDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HeroRepository {
    override fun getAllHeroes(): Flow<List<Hero>> {
        return localHeroDataSource.getAllHeroes().flowOn(dispatcher)
    }

    override suspend fun loadHeroes(searchCriteria: String?) {
        withContext(dispatcher) {
            localHeroDataSource.clearHeroes()
            val heroes = remoteHeroDataSource.getHeroes(searchCriteria = searchCriteria)
            localHeroDataSource.addHeroes(heroes)
        }
    }

    override suspend fun loadMoreHeroes(searchCriteria: String?) {
        withContext(dispatcher) {
            val heroesCount = localHeroDataSource.getHeroesCount()
            val moreHeroes = remoteHeroDataSource.getHeroes(offset = heroesCount, searchCriteria = searchCriteria)
            localHeroDataSource.addHeroes(moreHeroes)
        }
    }

    override suspend fun getHeroWithId(heroId: Int): Hero {
        return withContext(dispatcher) {
            localHeroDataSource.getHeroWithId(heroId)
        }
    }
}
