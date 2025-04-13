package com.notsatria.supachat.ui.screen.home.room_chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notsatria.supachat.component.AvatarImage
import com.notsatria.supachat.component.UserInput
import com.notsatria.supachat.data.model.Message
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun ChatRoomRoute(
    modifier: Modifier = Modifier,
    conversationId: String,
    otherUserId: String,
    navigateBack: () -> Unit,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val otherUser by viewModel.otherUser.collectAsState()

    LaunchedEffect(conversationId, otherUserId) {
        viewModel.loadMessages(conversationId)
        viewModel.subscribeToMessages(conversationId)
    }

    LaunchedEffect(otherUserId) {
        viewModel.loadOtherUser(otherUserId)
    }

    ChatRoomScreen(
        modifier,
        messages,
        profile = otherUser ?: UserProfile("", "username", null),
        currentUserId = viewModel.currentUserId.toString(),
        navigateBack = navigateBack,
        onMessageClicked = { message ->
            viewModel.sendMessage(conversationId, message)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    modifier: Modifier = Modifier,
    messages: MutableList<Message> = mutableStateListOf(),
    profile: UserProfile,
    currentUserId: String,
    navigateBack: () -> Unit = {},
    onMessageClicked: (String) -> Unit = {}
) {
    val message = remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 12.dp),
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AvatarImage(profile = profile, size = 36)
                        Spacer(Modifier.width(12.dp))
                        Text(profile.username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }, navigationIcon = {
                    IconButton(onClick = navigateBack, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            "Back",
                        )
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier
                .padding(p)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom,
                state = listState
            ) {
                itemsIndexed(messages) { i, message ->
                    Spacer(Modifier.height(8.dp))
                    MessageRow(content = message.content, isMe = message.sender_id == currentUserId)
                }
            }
            UserInput(onMessageSent = onMessageClicked)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        ChatRoomScreen(
            currentUserId = "",
            profile = UserProfile(id = "", username = "username", avatar_url = null)
        )
    }
}