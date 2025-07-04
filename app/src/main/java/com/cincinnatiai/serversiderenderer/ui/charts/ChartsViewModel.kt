package com.cincinnatiai.serversiderenderer.ui.charts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cincinnatiai.serversiderenderer.constants.chartsScreenJson
import com.cincinnatiai.serversiderenderer.constants.loadingScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChartsViewModel : ViewModel() {

    private val _chartsViewState = MutableStateFlow(loadingScreen)
    val chartsViewState: StateFlow<String> = _chartsViewState

    fun start() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val screen = fetchScreenFromBackend()
                withContext(Dispatchers.Main) {
                    _chartsViewState.emit(screen)
                }
            }
        }
    }

    // This is mocked; please use your own backend as this is just an example
    private suspend fun fetchScreenFromBackend(): String {
        delay(900)
        return chartsScreenJson
    }
}