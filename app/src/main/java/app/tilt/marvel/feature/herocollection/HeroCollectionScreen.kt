@file:OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)

package app.tilt.marvel.feature.herocollection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import app.tilt.marvel.R
import app.tilt.marvel.domain.Hero
import app.tilt.marvel.ui.components.HERO_CARD_ASPECT_RATIO
import app.tilt.marvel.ui.components.HeroCard
import app.tilt.marvel.ui.components.ShimmerBox
import app.tilt.marvel.ui.components.VerticalProgressBar
import app.tilt.marvel.ui.theme.MarvelHeroesTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

const val LOAD_MORE_THRESHOLD = 2

@Composable
fun HeroCollectionScreen(
    viewModel: HeroCollectionViewModel = hiltViewModel(),
    onNavigateToHero: (Hero) -> Unit = {}
) {
    val heroes by viewModel.heroList.collectAsState()
    val searchValue by viewModel.searchValue
    val loadingHeroes by viewModel.heroListLoading
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val showScrollBack by remember {
        derivedStateOf {
            pagerState.currentPage > 5
        }
    }

    LaunchedEffect(heroes.firstOrNull()) {
        pagerState.scrollToPage(0)
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .map { page -> page > heroes.size - LOAD_MORE_THRESHOLD && heroes.isNotEmpty() }
            .distinctUntilChanged()
            .collect { closeToEnd ->
                if (closeToEnd) {
                    viewModel.tryLoadingMoreHeroes()
                }
            }
    }
    HeroCollectionScreen(
        heroes = heroes,
        loadingHeroes = loadingHeroes,
        showScrollBack = showScrollBack,
        searchValue = searchValue,
        onSearch = viewModel::onSearch,
        onCardClicked = onNavigateToHero,
        onScrollClicked = {
            scope.launch {
                pagerState.animateScrollToPage(0)
            }
        },
        pagerState = pagerState
    )
}

@Composable
fun HeroCollectionScreen(
    heroes: List<Hero>,
    loadingHeroes: Boolean = false,
    showScrollBack: Boolean = false,
    searchValue: String = "",
    onSearch: (String) -> Unit = {},
    onCardClicked: (Hero) -> Unit = {},
    onScrollClicked: () -> Unit = {},
    pagerState: PagerState = rememberPagerState()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.2f,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background, BlendMode.Color)
        )
        AnimatedVisibility(
            visible = loadingHeroes,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            VerticalProgressBar()
        }
        AnimatedVisibility(
            visible = showScrollBack,
            modifier = Modifier
                .align(BottomStart)
                .padding(32.dp)
        ) {
            FloatingActionButton(onClick = onScrollClicked) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 128.dp, bottom = 16.dp)
        ) {
            val pagerPadding = 64.dp
            if (heroes.isEmpty()) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = pagerPadding)
                        .aspectRatio(HERO_CARD_ASPECT_RATIO)
                )
            } else {
                HorizontalPager(
                    count = heroes.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = pagerPadding),
                    key = { heroes[it].id }
                ) { index ->
                    val hero = heroes[index]
                    Box(modifier = Modifier.graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(index).absoluteValue
                        // We animate the scaleX + scaleY, between 85% and 100%
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // We animate the alpha, between 50% and 100%
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {
                        HeroCard(
                            hero = hero,
                            onClick = { onCardClicked(hero) },
                        )
                        Text(
                            text = hero.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                    shape = ShapeDefaults.Medium.copy(
                                        bottomEnd = ZeroCornerSize,
                                        bottomStart = ZeroCornerSize
                                    )
                                )
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HeroSearchField(
                searchValue = searchValue,
                padding = pagerPadding,
                onSearch = onSearch,
                modifier = Modifier.align(CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeroCollectionScreenPreview() {
    MarvelHeroesTheme {
        HeroCollectionScreen(
            heroes = listOf(
                Hero(
                    123,
                    "Hero name",
                    "Description",
                    "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784/portrait_fantastic.jpg"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeroCollectionScreenPreviewEmpty() {
    MarvelHeroesTheme {
        HeroCollectionScreen(
            heroes = emptyList()
        )
    }
}
