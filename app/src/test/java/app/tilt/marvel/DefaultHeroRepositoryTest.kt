package app.tilt.marvel

import app.tilt.marvel.data.DefaultHeroRepository
import app.tilt.marvel.data.local.LocalHeroDataSource
import app.tilt.marvel.data.remote.RemoteHeroDataSource
import app.tilt.marvel.domain.Hero
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultHeroRepositoryTest {

    @MockK
    lateinit var localDataSource: LocalHeroDataSource

    @MockK
    lateinit var remoteDataSource: RemoteHeroDataSource

    lateinit var repo: DefaultHeroRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repo = DefaultHeroRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun `store fetched heroes to local data source`() = runTest {
        val heroList = listOf(Hero(0, "me", "empty", ""))
        coEvery { localDataSource.clearHeroes() } returns Unit
        coEvery { remoteDataSource.getHeroes() } returns heroList
        coEvery { localDataSource.addHeroes(any()) } returns Unit
        repo.loadHeroes()
        coVerify(exactly = 1) { localDataSource.addHeroes(heroList) }
    }

    @Test
    fun `load more should offset next request based on heroes in local storage`() = runTest {
        coEvery { localDataSource.getHeroesCount() } returns 10
        coEvery { remoteDataSource.getHeroes(any(), any(), any()) } returns emptyList()
        coEvery { localDataSource.addHeroes(any()) } returns Unit
        repo.loadMoreHeroes()
        coVerify(exactly = 1) { remoteDataSource.getHeroes(offset = 10) }
    }
}
