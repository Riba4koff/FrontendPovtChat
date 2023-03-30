package com.example.chatapp.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.presentation.Navigation.ChatBottomNavigation
import com.example.chatapp.presentation.view.destinations.DirectionDestination
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Composable
fun ChatScaffold(
    navigator: DestinationsNavigator,
    backDestination: DirectionDestination ?= null,
    title: String,
    navController: NavController ?= null,
    body: @Composable ((PaddingValues)  -> Unit)
){
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Purple200) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(
                        Modifier.padding(start = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        backDestination?.let { destination ->
                            IconButton(onClick = {
                                navigator.navigate(destination) {
                                    popUpTo(destination) {
                                        inclusive = true
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "go back"
                                )
                            }
                        }
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = title,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }, bottomBar = {
            navController?.let {
                ChatBottomNavigation(navController = it)
            }
        }
    ) { padding ->
        body(padding)
    }
}