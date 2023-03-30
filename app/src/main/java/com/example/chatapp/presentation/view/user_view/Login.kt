package com.example.chatapp.presentation.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.presentation.validators.ValidateResult
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.view.destinations.LoginDestination
import com.example.chatapp.presentation.view.destinations.RegisterDestination
import com.example.chatapp.presentation.viewModel.LoginViewModel
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.getViewModel

@SuppressLint("ShowToast")
@Destination(
    route = "login_route"
)
@Composable
fun Login(
    viewModel: LoginViewModel = getViewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val state by viewModel.viewModelState.collectAsState()

    LoginContent(
        toRegister = {
            navigator.navigate(RegisterDestination){
                popUpTo(LoginDestination) {
                    inclusive = true
                }
            }
        },
        login = state.login,
        onLoginTextChange = viewModel::onLoginChange,
        password = state.password,
        onPasswordChange = viewModel::onPasswordChange,
        visible = state.inProcessing,
        trySignIn = {
            viewModel.tryLogin(
                navigateToChats = {
                    navigator.navigate(ChatsDestination) {
                        popUpTo(LoginDestination) {
                            inclusive = true
                        }
                    }
                },
                context = context
            )
        }
    )


}

@Composable
fun LoginContent(
    toRegister: () -> Unit,
    login: String,
    onLoginTextChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    visible: Boolean,
    trySignIn: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(start = 32.dp, end = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вход",
                fontSize = 32.sp,
                color = Purple200
            )
            AntaresAppTextField(
                text = login,
                onTextChange = onLoginTextChange,
                label = { Text(text = "Логин") },
            )
            AntaresAppTextField(
                text = password,
                onTextChange = onPasswordChange,
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Пароль") }
            )

            AntaresAppButton(text = "Войти", onClick = trySignIn)
            Spacer(Modifier.height(4.dp))

            ToRegister {
                toRegister()
            }

            LoadingProgressIndicator(visible)
        }
    }
}

@Composable
fun AntaresAppTextField(
    modifier: Modifier = Modifier,
    showErrorMessage: Boolean = true,
    text: String,
    onTextChange: (String) -> Unit,
    error: ValidateResult = ValidateResult(),
    label: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val focus = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focus.clearFocus()
            }),
            visualTransformation = visualTransformation,
            isError = !error.isSuccessful,
            colors = TextFieldDefaults.textFieldColors(
                disabledIndicatorColor = Color.Gray.copy(0.15f),
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Gray.copy(0.15f),
                focusedIndicatorColor = Color.Gray.copy(0.15f),
                disabledLabelColor = Purple200.copy(0.15f)
            ),
            label = label
        )
        if (showErrorMessage) ErrorMessage(validateResult = error)
    }
}

@Composable
fun ErrorMessage(
    validateResult: ValidateResult
){
    AnimatedVisibility(visible = !validateResult.isSuccessful) {
        when (validateResult.tag) {
            "invalid_login_or_password" -> ErrorText(message = validateResult.message, fontSize = 16.sp)
            "UNKNOWN_ERROR" -> ErrorText(message = validateResult.message, fontSize = 16.sp)
            "login" -> ErrorText(message = validateResult.message)
            "password" -> ErrorText(message = validateResult.message)
            "credential" -> ErrorText(message = validateResult.message)
            "email" -> ErrorText(message = validateResult.message)
        }
    }
}

@Composable
fun ErrorText(
    modifier: Modifier = Modifier,
    message: String,
    fontSize: TextUnit = 12.sp
){
    Text(
        modifier = modifier.fillMaxWidth(),
        text = message,
        color = MaterialTheme.colors.error,
        textAlign = TextAlign.Start,
        fontSize = fontSize
    )
}

@Composable
fun AntaresAppButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
){
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Purple200,
            contentColor = Color.White
        ),
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun ToRegister(
    navigate: () -> Unit
) {
    Text(
        modifier = Modifier
            .clickable {
                navigate()
            },
        text = "Регистрация",
        color = Purple200,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoadingProgressIndicator(
    visible : Boolean = false
) {
    AnimatedVisibility(visible = visible) {
        CircularProgressIndicator(color = Purple200)
    }
}