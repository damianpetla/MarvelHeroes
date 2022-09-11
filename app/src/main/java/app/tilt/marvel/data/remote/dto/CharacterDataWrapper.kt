package app.tilt.marvel.data.remote.dto

data class CharacterDataWrapper(
    val code: Int,
    val status: String,
    val data: CharacterDataContainer
)
