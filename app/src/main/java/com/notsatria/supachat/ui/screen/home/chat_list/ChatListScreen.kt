package com.notsatria.supachat.ui.screen.home.chat_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun ChatListRoute(modifier: Modifier = Modifier, navigateToChatRoom: () -> Unit) {
    ChatListScreen(
        modifier,
        profile = UserProfile(id = "", username = "username", avatarUrl = null),
        navigateToChatRoom = navigateToChatRoom
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    profile: UserProfile,
    navigateToChatRoom: () -> Unit = {}
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(4) {
                    ChatRow(profile = profile, onClick = navigateToChatRoom)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatListScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        ChatListScreen(profile = UserProfile(id = "", username = "username", avatarUrl = null))
    }
}