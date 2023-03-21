package com.example.chatapp.presentation.view


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.presentation.viewModel.states.LoginAndRegisterStates.ErrorMessagesState
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.view.destinations.LoginDestination
import com.example.chatapp.presentation.view.destinations.RegisterDestination
import com.example.chatapp.presentation.viewModel.RegisterViewModel
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.getViewModel

@SuppressLint("ShowToast")
@Destination(
    route = "register_route"
)
@Composable
fun Register(
    viewModel: RegisterViewModel = getViewModel(),
    navigator: DestinationsNavigator,
) {
    val state by viewModel.viewModelState.collectAsState()
    val errors by viewModel.errorMessageState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        RegisterContent(
            errors = errors,
            toLogin = {
                navigator.navigate(LoginDestination) {
                    popUpTo(RegisterDestination) {
                        inclusive = true
                    }
                }
            },
            email = state.email,
            onEmailChange = viewModel::onEmailChange,
            login = state.login,
            onLoginChange = viewModel::onLoginChange,
            username = state.username,
            onUsernameChange = viewModel::onUsernameChange,
            password = state.password,
            onPasswordChange = viewModel::onPasswordChange,
            trySignUp = {
                viewModel.trySignUp(
                    navigateToChats = {
                        navigator.navigate(ChatsDestination) {
                            popUpTo(RegisterDestination) {
                                inclusive = true
                            }
                        }
                    },
                    context = context
                )
            },
            visible = state.inProcessing
        )
    }
}

@Composable
fun RegisterContent(
    errors: ErrorMessagesState,
    toLogin: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    login: String,
    onLoginChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    trySignUp: () -> Unit,
    visible: Boolean,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Регистрация", fontSize = 32.sp, color = Purple200
        )
        RegisterTextFields(
            errors = errors,
            email = email,
            onEmailChange = onEmailChange,
            login = login,
            onLoginChange = onLoginChange,
            username = username,
            onUsernameChange = onUsernameChange,
            password = password,
            onPasswordChange = onPasswordChange
        )

        AntaresAppButton(
            modifier = Modifier.padding(top = 16.dp),
            text = "Зарегистрировать пользователя",
            onClick = trySignUp
        )
        ToLogin {
            toLogin()
        }
        Spacer(Modifier.height(16.dp))
        LoadingProgressIndicator(visible)
    }
}

@Composable
fun RegisterTextFields(
    errors: ErrorMessagesState,
    email: String,
    onEmailChange: (String) -> Unit,
    login: String,
    onLoginChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
) {
    AntaresAppTextField(
        text = email,
        onTextChange = onEmailChange,
        label = { Text(text = "email") },
        error = errors.emailError
    )
    AntaresAppTextField(
        text = login,
        onTextChange = onLoginChange,
        label = { Text(text = "login") },
        error = errors.loginError
    )
    AntaresAppTextField(
        text = username,
        onTextChange = onUsernameChange,
        label = { Text(text = "username") },
        error = errors.usernameError
    )
    AntaresAppTextField(
        text = password,
        onTextChange = onPasswordChange,
        visualTransformation = PasswordVisualTransformation(),
        label = { Text(text = "password") },
        error = errors.passwordError
    )
}

@Composable
fun ToLogin(navigate: () -> Unit) {
    Text(
        modifier = Modifier.clickable {
            navigate()
        }, text = "Вход", color = Purple200, textAlign = TextAlign.Center
    )
}
