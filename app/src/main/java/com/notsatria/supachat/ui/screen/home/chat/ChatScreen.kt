package com.notsatria.supachat.ui.screen.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.touchlab.kermit.Logger.Companion.d
import coil3.compose.AsyncImage
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun ChatRoute(modifier: Modifier = Modifier, viewModel: ChatViewModel = hiltViewModel()) {
    val messages by viewModel.messages.collectAsState()

    LaunchedEffect(messages) {
        d("Message: $messages")
    }

    ChatScreen(modifier, messages, onMessageClicked = { message ->
        viewModel.sendMessage(message)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    messages: MutableList<String> = mutableStateListOf(),
    onMessageClicked: (String) -> Unit = {},
    avatarUrl: String? = null
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
                        if (avatarUrl != null) AsyncImage(
                            avatarUrl,
                            "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)

                        ) else
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxSize(),
                                    text = "U",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontSize = 12.sp
                                )
                            }
                        Spacer(Modifier.width(12.dp))
                        Text("Username", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }, navigationIcon = {
                    IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            "Back",
                        )
                    }
                }, expandedHeight = 52.dp
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
                    MessageRow(content = message, isMe = i % 2 == 0)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = message.value,
                    onValueChange = { newVal ->
                        message.value = newVal
                    },
                    placeholder = {
                        Text("Message")
                    }
                )
                IconButton(
                    onClick = {
                        onMessageClicked(message.value)
                        message.value = ""
                    }, enabled = message.value.isNotBlank()
                ) {
                    Icon(Icons.AutoMirrored.Default.Send, "send")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        ChatScreen()
    }
}