package app.tilt.marvel.data

import app.tilt.marvel.data.remote.MarvelApi
import app.tilt.marvel.domain.Comic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultComicRepository(
    private val marvelApi: MarvelApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ComicRepository {
    override suspend fun getComicsForHero(heroId: Int): List<Comic> {
        return withContext(dispatcher) {
            marvelApi.getComicsForHero(heroId).data.results.map {
                Comic(
                    it.id,
                    it.thumbnail.url,
                    it.title
                )
            }
        }
    }
}
