@file:OptIn(ExperimentalMaterial3Api::class)

package app.tilt.marvel.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.tilt.marvel.domain.Hero
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import timber.log.Timber

const val HERO_CARD_ASPECT_RATIO = 300f / 450f

@Composable
fun HeroCard(hero: Hero, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .aspectRatio(HERO_CARD_ASPECT_RATIO),
        elevation = CardDefaults.elevatedCardElevation(),
        onClick = onClick
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(hero.image)
                .crossfade(true)
                .build(),
            loading = {
                ShimmerBox()
            },
            contentDescription = null,
            onError = {
                Timber.e(it.result.throwable, "Failed loading hero image")
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
