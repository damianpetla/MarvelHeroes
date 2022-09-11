@file:OptIn(ExperimentalMotionApi::class, ExperimentalFoundationApi::class)

package app.tilt.marvel.feature.herodetails

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.hilt.navigation.compose.hiltViewModel
import app.tilt.marvel.R
import app.tilt.marvel.domain.Comic
import app.tilt.marvel.domain.Hero
import app.tilt.marvel.ui.components.HERO_CARD_ASPECT_RATIO
import app.tilt.marvel.ui.components.HeroCard
import app.tilt.marvel.ui.theme.MarvelHeroesTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HeroDetailsScreen(
    viewModel: HeroDetailsViewModel = hiltViewModel()
) {
    val hero by viewModel.hero
    val comics by viewModel.comics
    Box(modifier = Modifier.fillMaxSize()) {
        hero?.let {
            HeroDetailsScreen(hero = it, comics = comics)
        }
    }
}

@Composable
fun HeroDetailsScreen(hero: Hero, comics: List<Comic> = emptyList()) {
    val currentState = remember { MutableTransitionState(HeroDetailState.Entered) }
    currentState.targetState = HeroDetailState.Finished

    val transition = updateTransition(currentState, label = "HeroDetailsTransition")
    val backgroundColor by transition.animateColor(
        label = "BackgroundColor"
    ) { state ->
        when (state) {
            HeroDetailState.Entered -> Color.Transparent
            HeroDetailState.Finished -> MaterialTheme.colorScheme.surface
        }
    }
    val screenProgress by transition.animateFloat(label = "SceneProgress") { state ->
        when (state) {
            HeroDetailState.Entered -> 0f
            HeroDetailState.Finished -> 1f
        }
    }
    val nameShapeTop by transition.animateDp(label = "NameShapeTop") { state ->
        when (state) {
            HeroDetailState.Entered -> 8.dp
            HeroDetailState.Finished -> 0.dp
        }
    }
    MotionLayout(
        start = enteredConstraintSet(),
        end = finishedConstraintSet(),
        progress = screenProgress,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        HeroCard(hero = hero, modifier = Modifier.layoutId("image"))
        Text(
            text = hero.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .layoutId("name")
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(
                        topStart = nameShapeTop,
                        topEnd = nameShapeTop,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        )
        Column(modifier = Modifier.layoutId("description")) {
            Text(
                text = stringResource(R.string.details_header_description),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = hero.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
        Column(Modifier.layoutId("comics")) {
            Text(
                text = stringResource(R.string.details_header_comics),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
                items(comics) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(it.imageUrl)
                            .crossfade(true)
                            .build(), contentDescription = null,
                        modifier = Modifier.aspectRatio(HERO_CARD_ASPECT_RATIO)
                    )
                }
            }
        }

    }
}

enum class HeroDetailState {
    Entered,
    Finished
}

@Preview(showBackground = true)
@Composable
fun HeroDetailsScreenPreview() {
    MarvelHeroesTheme {
        HeroDetailsScreen(
            hero = Hero(
                123,
                "Hero name",
                "Description",
                ""
            )
        )
    }
}
