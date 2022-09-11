package app.tilt.marvel.data.remote

import app.tilt.marvel.data.remote.dto.Character
import app.tilt.marvel.domain.Comic
import app.tilt.marvel.domain.Hero

fun List<Character>.toDomain(): List<Hero> = map { character ->
    Hero(
        id = character.id,
        name = character.name,
        description = character.description,
        image = character.thumbnail.url
    )
}
