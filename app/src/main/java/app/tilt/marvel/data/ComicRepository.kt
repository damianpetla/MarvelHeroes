package app.tilt.marvel.data

import app.tilt.marvel.domain.Comic

interface ComicRepository {

    suspend fun getComicsForHero(heroId: Int): List<Comic>
}
