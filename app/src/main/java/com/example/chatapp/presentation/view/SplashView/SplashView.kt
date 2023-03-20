package com.example.chatapp.presentation.view.SplashView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.chatapp.data.preferencesDataStore.SessionManager
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.view.destinations.LoginDestination
import com.example.chatapp.presentation.view.destinations.SplashViewDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashView(
    navigator: DestinationsNavigator
) {
    val sessionManager = SessionManager(LocalContext.current)

    LaunchedEffect(Unit) {
        delay(1500L)
        val token = sessionManager.getJwtToken().first()
        if (token.isBlank()){
            navigator.navigate(LoginDestination){
                popUpTo(SplashViewDestination){
                    inclusive = true
                }
            }
        }
        else {
            navigator.navigate(ChatsDestination){
                popUpTo(SplashViewDestination){
                    inclusive = true
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PulsingCirclesAnimation()
        }
    }
}