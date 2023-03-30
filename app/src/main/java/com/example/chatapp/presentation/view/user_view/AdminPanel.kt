package com.example.chatapp.presentation.view.user_view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.presentation.Navigation.ChatBottomNavigation
import com.example.chatapp.presentation.view.ChatButton
import com.example.chatapp.presentation.view.ChatScaffold
import com.example.chatapp.presentation.view.ChatTextField
import com.example.chatapp.presentation.view.destinations.ProfileDestination
import com.example.chatapp.presentation.viewModel.AdminPanelViewModel
import com.example.chatapp.presentation.viewModel.IAdminViewModel
import com.example.chatapp.ui.theme.Purple200
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import org.koin.androidx.compose.getViewModel
import kotlin.random.Random

@Destination
@Composable
fun AdminPanel(
    navigator: DestinationsNavigator,
    viewModel: AdminPanelViewModel = getViewModel()
) {
    val state by viewModel.viewModelState.collectAsState()
    val context = LocalContext.current

    ChatScaffold(navigator = navigator, title = "Админ панель", backDestination = ProfileDestination) { padding ->
        AdminPanelContent(
            modifier = Modifier.padding(padding),
            deleteAllMessages = {
                viewModel.deleteAllMessages(context)
            },
            deleteAllUsers = {
                viewModel.deleteAllUsers(context)
            },
            deleteUserById = {
                viewModel.deleteUserById(context)
            },
            changeId = viewModel::idChange,
            id = state.id
        )
    }
}

@Composable
fun AdminPanelContent(
    modifier: Modifier = Modifier,
    deleteAllUsers: () -> Unit,
    deleteAllMessages: () -> Unit,
    deleteUserById: () -> Unit,
    changeId: (String) -> Unit,
    id: String,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ChatButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = deleteAllUsers,
            title = "Удалить всех пользователей"
        )
        ChatButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = deleteAllMessages,
            title = "Удалить все сообщения"
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChatTextField(
                modifier = Modifier.padding(end = 8.dp).fillMaxWidth(0.72f),
                text = id,
                onTextChange = changeId,
                label = {
                    Text(
                        text = "Введите логин"
                    )
                })
            OutlinedButton(
                onClick = deleteUserById,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Purple200,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(text = "Удалить")
            }
        }
    }
}