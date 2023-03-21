package com.example.chatapp.presentation.Navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.presentation.view.NavGraphs
import com.example.chatapp.presentation.view.appCurrentDestinationAsState
import com.example.chatapp.presentation.view.destinations.*
import com.example.chatapp.presentation.view.destinations.Destination
import com.example.chatapp.presentation.view.startAppDestination
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo

enum class BottomBarDestinations(
    val direction: DirectionDestination,
    val title: String,
    val icon: Int,
) {
    Chats(direction = ChatsDestination, title = "Чаты", icon = R.drawable.chat),
    Profile(direction = ProfileDestination, title = "Профиль", icon = R.drawable.profile),
    GeneralChatDest(direction = GeneralChatDestination, title = "", R.drawable.genereal_chat)
}

@Composable
fun ChatBottomNavigation(navController: NavController) {

    val currentDestination: Destination =
        navController.appCurrentDestinationAsState().value ?: NavGraphs.root.startAppDestination

    BottomNavigation(
        backgroundColor = Purple200
    ) {
        BottomBarDestinations.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                icon = { Icon(modifier = Modifier.size(25.dp), painter = painterResource(id = destination.icon), contentDescription = "Icon: ${destination.title}") },
                label = { Text(text = destination.title) },
                onClick = {
                    navController.navigate(destination.direction){
                        popUpTo(destination.direction){
                            saveState = false
                        }
                        launchSingleTop = true
                    }
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = false,
            )
        }
    }
}
