package com.example.chatapp.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.presentation.view.destinations.*
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
    navigator: DestinationsNavigator,
) {
    val state by viewModel.viewModelState.collectAsState()
    val context = LocalContext.current

    ChatScaffold(navigator = navigator, navController = navController, title = "Профиль") { padding ->
        ProfileContent(
            padding = padding,
            username = state.username,
            login = state.login,
            email = state.email,
            editProfile = {
                navigator.navigate(EditUserInfoDestination)
            },
            exit = {
                viewModel.logout(
                    context = context,
                    navigateToLogin = {
                        navigator.navigate(LoginDestination){
                            popUpTo(ChatsDestination){
                                inclusive = true
                            }
                        }
                    }
                )
            },
            navigateToAdminPanel = {
                navigator.navigate(AdminPanelDestination)
            }
        )
    }
}

@Composable
fun ProfileContent(
    padding: PaddingValues,
    username: String,
    login: String,
    email: String,
    editProfile: () -> Unit,
    exit: () -> Unit,
    navigateToAdminPanel: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(padding)) {
        RowInfo(info = login, text = "Логин")
        RowInfo(info = username, text = "Имя пользователя")
        RowInfo(info = email, text = "Почта")
        AnimatedVisibility(visible = login == "admin") {
            ChatButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    .height(45.dp),
                onClick = navigateToAdminPanel,
                title = "Админ панель",
                color = Purple200.copy(0.7f)
            )
        }
        ChatButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                .height(45.dp),
            onClick = editProfile,
            title = "Изменить",
            color = Purple200.copy(0.7f)
        )
        ChatButton(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .height(45.dp),
            onClick = exit,
            title = "Выйти",
            color = Purple200.copy(0.7f))
    }
}

@Composable
fun RowInfo(
    info: String,
    text: String,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Card(elevation = 5.dp) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Purple200.copy(0.3f)),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        text = text,
                        fontSize = 16.sp
                    )
                }
                Divider()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Purple200.copy(0.05f)),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = info,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
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

