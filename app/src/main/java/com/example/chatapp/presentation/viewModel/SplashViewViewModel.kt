package com.example.chatapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.preferencesDataStore.SessionManager
import com.example.chatapp.domain.irepository.IUserRepository
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.view.destinations.LoginDestination
import com.example.chatapp.presentation.view.destinations.SplashViewDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashViewViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val _splashViewState = MutableStateFlow(false)
    private val splashViewState = _splashViewState.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.getJwtToken().first().let { token ->
                if (token.isNotBlank()) _splashViewState.emit(true)
                else _splashViewState.emit(false)
            }
        }
    }

    fun checkLoggedIn(navigator: DestinationsNavigator){
        viewModelScope.launch {
            delay(1500L)
            if (splashViewState.value){
                navigator.navigate(ChatsDestination){
                    popUpTo(SplashViewDestination){
                        inclusive = true
                    }
                }
            } else {
                navigator.navigate(LoginDestination){
                    popUpTo(SplashViewDestination){
                        inclusive = true
                    }
                }
            }
        }
    }
}