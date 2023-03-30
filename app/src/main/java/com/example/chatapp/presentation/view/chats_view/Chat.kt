package com.example.chatapp.presentation.view

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.chatapp.domain.models.Message
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.viewModel.ChatViewModel
import com.example.chatapp.ui.theme.Purple200
import com.example.chatapp.ui.theme.isOwnMessageColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.getViewModel

@Destination(route = "general_chat")
@Composable
fun GeneralChat(
    viewModel: ChatViewModel = getViewModel(),
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) viewModel.connect()
            else if (event == Lifecycle.Event.ON_STOP) viewModel.disconnect()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state by viewModel.viewModelState.collectAsState()

    ChatScaffold(navigator = navigator, backDestination = ChatsDestination, title = "Общий чат") { padding ->
        GeneralChatContent(
            modifier = Modifier.padding(padding),
            messages = state.messages,
            message = state.message,
            onMessageChange = viewModel::onMessageChange,
            sendMessage = viewModel::sendMessage,
            username = state.username,
            loading = state.isLoading
        )
    }
}

@Composable
fun GeneralChatContent(
    modifier: Modifier = Modifier,
    messages: List<Message> = emptyList(),
    message: String,
    onMessageChange: (String) -> Unit,
    sendMessage: () -> Unit,
    username: String,
    loading: Boolean
) {
    LoadingIndicator(loading = loading)
    LazyColumn(
        modifier
            .fillMaxSize(), reverseLayout = true
    ) {
        item {
            SendMessageTextField(
                modifier = Modifier.fillMaxWidth(0.88f),
                value = message,
                onValueChange = onMessageChange,
                placeholder = {
                    Text("Напишите сообщение...")
                },
                send = sendMessage
            )
            Spacer(Modifier.height(8.dp))
        }
        items(messages) { message ->
            MessageItem(
                message = message,
                isOwnMessage = username == message.username,
                timeSending = message.formattedTime
            )
        }
    }
}

@Composable
fun LoadingIndicator(loading: Boolean){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        AnimatedVisibility(visible = loading) {
            CircularProgressIndicator(color = Purple200)
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