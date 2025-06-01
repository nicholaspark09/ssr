package com.cincinnatiai.serversiderenderer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cincinnatiai.serversiderenderer.constants.homeScreenJson
import com.cincinnatiai.serversiderenderer.constants.loadingScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _homeViewState = MutableStateFlow(loadingScreen)
    val homeViewState: StateFlow<String> = _homeViewState

    fun start() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val screen = fetchScreenFromBackend()

                withContext(Dispatchers.Main) {
                    _homeViewState.emit(screen)
                }
            }
        }
    }

    // This is mocked; please use your own backend as this is just an example
    private suspend fun fetchScreenFromBackend(): String {
        delay(900)
        return homeScreenJson
    }
}