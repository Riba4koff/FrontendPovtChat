package com.example.chatapp.presentation.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.data.preferencesDataStore.SessionManager
import com.example.chatapp.presentation.Navigation.ChatBottomNavigation
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.view.destinations.EditUserInfoDestination
import com.example.chatapp.presentation.view.destinations.LoginDestination
import com.example.chatapp.presentation.viewModel.ProfileViewModel
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.getViewModel


@Destination(route = "profile_route")
@Composable
fun Profile(
    navController: NavController,
    viewModel: ProfileViewModel = getViewModel(),
    navigator: DestinationsNavigator
) {
    val state by viewModel.profileState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Purple200) {
                Row(Modifier.padding(start = 16.dp)) {
                    Text("Профиль", fontSize = 24.sp, color = Color.White)
                }
            }
        },
        bottomBar = {
            ChatBottomNavigation(navController = navController)
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            ProfileContent(
                username = state.username,
                login = state.login,
                email = state.email,
                editProfile = {
                    navigator.navigate(EditUserInfoDestination)
                },
                exit = {
                    viewModel.logout {
                        navigator.navigate(LoginDestination){
                            popUpTo(ChatsDestination){
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileContent(
    username: String,
    login: String,
    email: String,
    editProfile: () -> Unit,
    exit: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp)
            .border(BorderStroke(1.dp, Color.Gray))
            .background(Color.Gray.copy(0.1f))
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        RowInfo(title = "Имя пользователя", info = username)
        Spacer(modifier = Modifier.height(8.dp))

        RowInfo(title = "Логин", info = login)
        Spacer(modifier = Modifier.height(8.dp))

        RowInfo(title = "Почта", info = email)
        Spacer(modifier = Modifier.height(8.dp))

        ChatButton(
            modifier = Modifier.fillMaxWidth(0.65f),
            onClick = editProfile, title = "Изменить"
        )
        ChatButton(
            modifier = Modifier.fillMaxWidth(0.65f),
            onClick = exit, title = "Выйти"
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun RowInfo(
    title: String,
    info: String,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("$title: $info", fontSize = 16.sp)
    }
}

@Composable
fun ChatButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    color: Color = Purple200,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            modifier = modifier,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = color,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(text = title)
        }
    }
}
