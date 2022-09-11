package app.tilt.marvel.data.remote.dto

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Image
)
