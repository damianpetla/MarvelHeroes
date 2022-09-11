package app.tilt.marvel.data.remote.dto

data class Image(
    val path: String,
    val extension: String
) {
    val url: String
        get() = "$path/portrait_uncanny.$extension".replace("http", "https")
}
