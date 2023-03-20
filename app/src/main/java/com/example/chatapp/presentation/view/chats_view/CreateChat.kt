package com.example.chatapp.presentation.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination(route = "create_chat_route")
@Composable
fun CreateChat(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Purple200) {
                Row(
                    Modifier.padding(start = 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.navigate(ChatsDestination) {
                            popUpTo(ChatsDestination) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "go back")
                    }
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Чаты",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
        ) {
            CreateChatContent(
                value = "",
                onTextChange = {

                }
            )
        }
    }
}

@Composable
fun CreateChatContent(
    value: String,
    onTextChange: (String) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ChatTextField(text = value, onTextChange = onTextChange, label = { Text("Название чата") })
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ChatButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp), onClick = { /*TODO*/ }, title = "Создать чат"
        )
    }
}

@Composable
fun ChatTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AntaresAppTextField(
            text = text,
            onTextChange = onTextChange,
            showErrorMessage = false,
            label = label
        )
    }
}

@Preview
@Composable
fun CreateChatPreview() {
    CreateChat(rememberNavController())
}
