package com.notsatria.supachat.ui.screen.home.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.notsatria.supachat.data.model.Conversation
import com.notsatria.supachat.data.model.Message
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.data.preferences.UserPreferences
import com.notsatria.supachat.ui.model.ConversationUiModel
import com.notsatria.supachat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.Forest.d
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val supabase: SupabaseClient,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _conversationsState =
        MutableStateFlow<Resource<List<ConversationUiModel>>>(Resource.Initial())
    val conversationsState = _conversationsState.asStateFlow()

    private val _logoutState = MutableStateFlow(false)
    val logoutState: Flow<Boolean> = _logoutState.asStateFlow()

    init {
        updateFcmTokenIfNeeded()
    }

    fun updateFcmTokenIfNeeded() {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                viewModelScope.launch(Dispatchers.IO) {
                    d("Current fcm token: $token")
                    supabase.from("profiles")
                        .upsert(UserProfile(id = currentUserId, fcm_token = token))
                }
            }
            .addOnFailureListener {
                Timber.e("Fetching FCM registration token failed: ${it.message}")
            }
    }


    /**
     * Function to load conversation list based only on current user. Then, this function are also
     * responsible to map conversation list and combining it to profile and also latest messages that is shown on `ChatRow` composable
     */
    fun loadConversations() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _conversationsState.value = Resource.Loading()
                val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return@launch

                val conversationList = supabase.from("conversations")
                    .select {
                        filter {
                            or {
                                Conversation::user1_id eq currentUserId
                                Conversation::user2_id eq currentUserId
                            }
                        }
                    }
                    .decodeList<Conversation>()

                val conversationUiModels = conversationList.map { conversation ->
                    async {
                        val otherUserId =
                            if (conversation.user1_id == currentUserId) conversation.user2_id else conversation.user1_id

                        val otherUser = async {
                            supabase.from("profiles")
                                .select {
                                    filter { UserProfile::id eq otherUserId }
                                }
                                .decodeSingle<UserProfile>()
                        }

                        val latestMessage = async {
                            supabase.from("messages")
                                .select {
                                    filter {
                                        Message::conversation_id eq conversation.id
                                    }
                                    order("created_at", Order.DESCENDING)
                                    limit(1)
                                }
                                .decodeSingle<Message>()
                                .content
                        }

                        d("Latest message: $latestMessage")

                        ConversationUiModel(
                            conversationId = conversation.id!!,
                            otherUser = otherUser.await(),
                            latestMessage = latestMessage.await()
                        )
                    }
                }.awaitAll()

                _conversationsState.value = Resource.Success(conversationUiModels)
            } catch (e: Exception) {
                _conversationsState.value = Resource.Error(e.message.toString())
                Timber.e("Error on loadConversations: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                supabase.auth.signOut()
                _logoutState.value = true
                userPreferences.setLoginState(false)
            } catch (e: Exception) {
                Timber.e("Error on logout: ${e.message}")
                _logoutState.value = false
            }
        }
    }
}