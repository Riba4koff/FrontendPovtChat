package com.example.chatapp.presentation.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.domain.models.ChatModel
import com.example.chatapp.presentation.Navigation.ChatBottomNavigation
import com.example.chatapp.presentation.view.destinations.CreateChatDestination
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate

@Destination(
    route = "chats_route"
)
@Composable
fun Chats(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Purple200) {
                Row(Modifier.padding(start = 16.dp)) {
                    Text("Чаты", fontSize = 24.sp, color = Color.White)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(CreateChatDestination)
            }, backgroundColor = Color.LightGray) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            ChatBottomNavigation(navController = navController)
        }
    ) { padding ->
        ChatsContent(
            modifier = Modifier.padding(padding),
            onClickMore = {

            },
            chats = emptyList()
        )
    }
}

@Composable
fun ChatsContent(
    modifier: Modifier = Modifier,
    onClickMore: (ChatModel) -> Unit,
    chats: List<ChatModel>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListChats(chats, onClickMore)
    }
}

@Composable
fun ListChats(
    chats: List<ChatModel>,
    onClickMore: (ChatModel) -> Unit,
) {
    chats.forEach { chat ->
        ListChatsItem(
            chatModel = chat,
            onClickMore = onClickMore
        )
    }
}

@Composable
fun ListChatsItem(
    chatModel: ChatModel,
    onClickMore: (ChatModel) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(0.06f))
            .clickable {
                onClickMore(chatModel)
            },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(chatModel.title, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Text(chatModel.timeLastMessage)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = chatModel.lastUser.username + ": ", fontSize = 16.sp, fontWeight = FontWeight.W500)
            Text(text = chatModel.lastMessage, fontSize = 16.sp)
        }
        Divider()
    }
}

@Preview
@Composable
fun ChatsPreview() {
    Chats(rememberNavController())
}