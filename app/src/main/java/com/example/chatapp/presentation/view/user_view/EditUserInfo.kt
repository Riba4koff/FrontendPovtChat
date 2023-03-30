package com.example.chatapp.presentation.view.user_view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.data.util.Result
import com.example.chatapp.presentation.view.AntaresAppButton
import com.example.chatapp.presentation.view.AntaresAppTextField
import com.example.chatapp.presentation.view.ChatScaffold
import com.example.chatapp.presentation.view.SendMessageTextField
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.view.destinations.ProfileDestination
import com.example.chatapp.presentation.viewModel.EditUserInfoViewModel
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun EditUserInfo(
    viewmodel: EditUserInfoViewModel = getViewModel(),
    navigator: DestinationsNavigator,
) {
    val state by viewmodel.viewModelState.collectAsState()
    val context = LocalContext.current

    ChatScaffold(navigator = navigator, title = "Изменить профиль", backDestination = ProfileDestination) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditUserInfoContent(
                login = state.login,
                username = state.username,
                email = state.email,
                onLoginChange = viewmodel::onLoginChange,
                onUsernameChange = viewmodel::onUsernameChange,
                onEmailChange = viewmodel::onEmailChange,
                saveChanges = {
                    viewmodel.save(
                        navigateToProfile = {
                            navigator.navigate(ProfileDestination) {
                                popUpTo(ProfileDestination) {
                                    inclusive = true
                                }
                            }
                        },
                        context = context
                    )
                }
            )
        }
    }
}

@Composable
fun EditUserInfoContent(
    login: String,
    username: String,
    email: String,
    onLoginChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    saveChanges: () -> Unit,
) {
    AntaresAppTextField(
        text = login,
        onTextChange = onLoginChange,
        label = { Text("Введите логин") })
    AntaresAppTextField(
        text = username,
        onTextChange = onUsernameChange,
        label = { Text("Введите имя пользователя") })
    AntaresAppTextField(
        text = email,
        onTextChange = onEmailChange,
        label = { Text("Введите почту") })
    AntaresAppButton(
        modifier = Modifier
            .fillMaxWidth(0.5f),
        text = "Сохранить", onClick = saveChanges
    )

}
