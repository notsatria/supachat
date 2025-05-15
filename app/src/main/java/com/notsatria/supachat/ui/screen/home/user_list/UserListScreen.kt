package com.notsatria.supachat.ui.screen.home.user_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.theme.SupachatTheme
import com.notsatria.supachat.utils.Resource

@Composable
fun UserListRoute(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToChatRoom: (conversationId: String, otherUserId: String) -> Unit,
    viewModel: UserListViewModel = hiltViewModel()
) {
    var userProfileState = viewModel.userListState.collectAsState()
    var conversationIdState = viewModel.conversationId.collectAsState()

    UserListScreen(
        modifier,
        userProfileState.value,
        navigateBack,
        navigateToChatRoom = { otherUserId ->
            viewModel.getConversationId(otherUserId, onSuccess = {
                navigateToChatRoom(it, otherUserId)
            }, onError = {
            }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    userProfileState: Resource<List<UserProfile>>,
    navigateBack: () -> Unit = {},
    navigateToChatRoom: (otherUserId: String) -> Unit = { },
) {
    Scaffold(modifier, topBar = {
        TopAppBar(title = {
            Text("Users")
        }, navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(Icons.AutoMirrored.Default.ArrowBackIos, "Back")
            }
        })
    }) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (userProfileState) {
                is Resource.Loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                is Resource.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(userProfileState.data!!) {
                            UserRow(profile = it, onClick = {
                                navigateToChatRoom(it.id)
                            })
                        }
                    }
                }

                is Resource.Error -> {
                    Text(
                        text = "Failed to load user list",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {}
            }

        }
    }
}

@Preview
@Composable
fun UserListScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        UserListScreen(
            userProfileState = Resource.Success(
                listOf(
                    UserProfile(id = "", username = "yanto", avatar_url = null),
                    UserProfile(id = "", username = "rudi", avatar_url = null),
                    UserProfile(id = "", username = "budiono", avatar_url = null),
                )
            )
        )
    }
}

@Preview
@Composable
fun UserListErrorScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        UserListScreen(
            userProfileState = Resource.Error(
                message = "Something went wrong"
            )
        )
    }
}