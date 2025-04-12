package com.notsatria.supachat.ui.screen.home.chat_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.notsatria.supachat.data.model.Conversation
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun ChatListRoute(
    modifier: Modifier = Modifier,
    navigateToChatRoom: (String) -> Unit,
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val conversations by viewModel.conversations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    ChatListScreen(
        modifier,
        conversations = conversations,
        profile = UserProfile(id = "", username = "username", avatar_url = null),
        navigateToChatRoom = { conversationId -> navigateToChatRoom(conversationId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    conversations: List<Conversation> = listOf(),
    profile: UserProfile,
    navigateToChatRoom: (String) -> Unit = {}
) {
    Scaffold(modifier, topBar = {
        TopAppBar(title = {
            Text("Supachat", fontWeight = FontWeight.Bold)
        }, actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Search, "Search")
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Default.Message, "Add Message")
        }
    }) { p ->
        Box(
            modifier = Modifier
                .padding(p)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            ) {
                items(conversations) {
                    ChatRow(
                        profile = profile,
                        onClick = { navigateToChatRoom(it.id) })
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
            conversations = listOf(
                Conversation(
                    id = "",
                    user1_id = "",
                    user2_id = "",
                    updated_at = "",
                    created_at = ""
                )
            ), profile = UserProfile(id = "", username = "username", avatar_url = null)
        )
    }
}