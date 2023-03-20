package com.example.chatapp.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.chatapp.domain.models.Message
import com.example.chatapp.presentation.view.destinations.ChatsDestination
import com.example.chatapp.presentation.viewModel.ChatViewModel
import com.example.chatapp.ui.theme.Purple200
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

    val state by viewModel.chatState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Purple200) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(
                        Modifier.padding(start = 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            navigator.navigate(ChatsDestination) {
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
                            text = "Общий чат",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
                }
            }
        },
    ) { padding ->
        GeneralChatContent(
            modifier = Modifier.padding(padding),
            messages = state.messages,
            message = state.message,
            onMessageChange = viewModel::onMessageChange,
            sendMessage = viewModel::sendMessage,
            username = state.username
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
) {
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

