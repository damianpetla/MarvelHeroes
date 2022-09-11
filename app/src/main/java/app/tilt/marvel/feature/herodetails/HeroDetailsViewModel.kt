package app.tilt.marvel.feature.herodetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tilt.marvel.data.ComicRepository
import app.tilt.marvel.data.HeroRepository
import app.tilt.marvel.domain.Comic
import app.tilt.marvel.domain.Hero
import app.tilt.marvel.feature.navigation.NavigationRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailsViewModel @Inject constructor(
    private val heroRepository: HeroRepository,
    private val comicRepository: ComicRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val heroId: String =
        checkNotNull(savedStateHandle[NavigationRoutes.HeroDetailsHeroIdParam])
    val hero = mutableStateOf<Hero?>(null)
    val comics = mutableStateOf(emptyList<Comic>())

    init {
        viewModelScope.launch {
            hero.value = heroRepository.getHeroWithId(heroId.toInt())
        }
        viewModelScope.launch {
            comics.value = comicRepository.getComicsForHero(heroId.toInt())
        }
    }
}
