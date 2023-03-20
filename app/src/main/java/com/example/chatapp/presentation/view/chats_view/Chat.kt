package com.example.chatapp.presentation.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.domain.models.Message
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.ui.theme.Purple200
import com.example.chatapp.ui.theme.isOwnMessageColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination(route = "chat_route")
@Composable
fun Chat(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Purple200) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(
                        Modifier.padding(start = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ChatsDestination) {
                                popUpTo(ChatsDestination) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "go back"
                            )
                        }
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "Название чата",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                    }
                }
            }
        },
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding), reverseLayout = true
        ) {
            item {
                SendMessageTextField(modifier = Modifier.fillMaxWidth(0.88f),
                    value = "",
                    onValueChange = {

                    },
                    placeholder = {
                        Text("Напишите сообщение...")
                    },
                    send = {

                    })
            }
            items(emptyList<Message>()) { message ->
                MessageItem(message = message, timeSending = message.formattedTime)
            }
        }
    }
}


@Composable
fun MessageItem(
    isOwnMessage: Boolean = true,
    message: Message,
    timeSending: String,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
            backgroundColor = if (isOwnMessage) isOwnMessageColor else Color.LightGray
        ) {
            Column(
                Modifier.padding(top = 4.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isOwnMessage) {
                        Text(
                            text = timeSending
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = message.username,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = message.username,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = timeSending)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Text(message.text)
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun SendMessageTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    send: () -> Unit,
) {
    val focus = LocalFocusManager.current
    Row(Modifier.background(Color.LightGray), verticalAlignment = Alignment.Bottom) {
        Row(
            Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.Gray)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = modifier,
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focus.clearFocus() }),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = placeholder
            )
            IconButton(onClick = send) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send message")
            }
        }
    }
}

@Preview
@Composable
fun PreviewChat() {
    MessageItem(
        message = Message(
            text = "Здарова бродяги ааааааааааааааааааааааааа",
            formattedTime = "5 марта 20:12",
            username = "Riba4koff"
        ), timeSending = "5 марта 20:12"
    )
}