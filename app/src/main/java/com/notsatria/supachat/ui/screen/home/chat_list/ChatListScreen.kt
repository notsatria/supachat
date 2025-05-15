package com.notsatria.supachat.ui.screen.home.chat_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.model.ConversationUiModel
import com.notsatria.supachat.ui.theme.SupachatTheme
import com.notsatria.supachat.utils.Resource

@Composable
fun ChatListRoute(
    modifier: Modifier = Modifier,
    navigateToChatRoom: (conversationId: String, otherUserId: String) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToUserList: () -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val conversationsState by viewModel.conversationsState.collectAsState()
    val logoutState by viewModel.logoutState.collectAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    LaunchedEffect(logoutState) {
        if (logoutState) navigateToLogin()
    }

    var shouldShowLogoutAlert by remember { mutableStateOf(false) }

    if (shouldShowLogoutAlert) AlertDialog(onDismissRequest = {
        shouldShowLogoutAlert = false
    }, title = {
        Text("Logout")
    }, text = {
        Text("Are you sure you want to logout?")
    }, confirmButton = {
        TextButton(onClick = {
            shouldShowLogoutAlert = false
            viewModel.logout()
        }) {
            Text("Logout")
        }
    }, dismissButton = {
        TextButton(onClick = {}) {
            Text("Cancel")
        }
    })

    ChatListScreen(
        modifier,
        conversationsState = conversationsState,
        navigateToChatRoom = { conversationId, otherUserId ->
            navigateToChatRoom(
                conversationId,
                otherUserId
            )
        },
        onLogoutClicked = {
            shouldShowLogoutAlert = true
        },
        navigateToUserList = navigateToUserList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    conversationsState: Resource<List<ConversationUiModel>>,
    navigateToChatRoom: (String, String) -> Unit,
    onLogoutClicked: () -> Unit,
    navigateToUserList: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Supachat", fontWeight = FontWeight.Bold) }, actions = {
                IconButton(onClick = onLogoutClicked) {
                    Icon(Icons.AutoMirrored.Default.Logout, "Logout")
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToUserList) {
                Icon(Icons.AutoMirrored.Default.Message, "Add Message")
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (conversationsState) {
                is Resource.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is Resource.Success -> {
                    val conversations = conversationsState.data!!

                    if (conversations.isEmpty()) {
                        Text(
                            text = "No conversations found! Click icon to start conversation",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.Center),
                        )
                        return@Box
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(conversations) { conversation ->
                            ChatRow(
                                profile = conversation.otherUser,
                                latestMessage = conversation.latestMessage,
                                onClick = {
                                    navigateToChatRoom(
                                        conversation.conversationId,
                                        conversation.otherUser.id
                                    )
                                })
                        }
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = "Failed to load conversations!",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is Resource.Initial -> {
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatListScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        ChatListScreen(
            conversationsState = Resource.Success<List<ConversationUiModel>>(
                data = listOf(
                    ConversationUiModel(
                        conversationId = "",
                        otherUser = UserProfile(
                            id = "",
                            username = "yanto",
                            avatar_url = null
                        ),
                        latestMessage = "halo yono"
                    ),
                    ConversationUiModel(
                        conversationId = "",
                        otherUser = UserProfile(
                            id = "",
                            username = "budi",
                            avatar_url = null
                        ),
                        latestMessage = "halo yono"
                    )
                ),
            ),
            navigateToChatRoom = { a, b -> },
            onLogoutClicked = {},
            navigateToUserList = {}
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyChatListScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        ChatListScreen(
            conversationsState = Resource.Success<List<ConversationUiModel>>(
                data = listOf(

                ),
            ),
            navigateToChatRoom = { a, b -> },
            onLogoutClicked = {},
            navigateToUserList = {}
        )
    }
}