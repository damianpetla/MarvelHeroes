package app.tilt.marvel.feature.herocollection

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.tilt.marvel.data.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HeroCollectionViewModel @Inject constructor(
    private val heroRepository: HeroRepository
) : ViewModel() {

    val heroList = heroRepository.getAllHeroes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val heroListLoading = mutableStateOf(false)
    val searchValue = mutableStateOf("")
    private val searchTrigger = MutableSharedFlow<String?>()

    init {
        viewModelScope.launch {
            heroRepository.loadHeroes()
        }
        viewModelScope.launch {
            searchTrigger
                .debounce(500).onEach {
                heroRepository.loadHeroes(it)
            }.collect()
        }
    }

    fun tryLoadingMoreHeroes() {
        Timber.d("Try loading more heroes")
        viewModelScope.launch {
            heroListLoading.value = true
            heroRepository.loadMoreHeroes(searchValue.value.takeIf { it.length >= 2 })
            heroListLoading.value = false
        }
    }

    fun onSearch(search: String) {
        searchValue.value = search
        viewModelScope.launch {
            if (search.length >= 2) {
                searchTrigger.emit(search)
            } else {
                searchTrigger.emit(null)
            }
        }
    }
}
