package com.notsatria.supachat.ui.screen.home.room_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.supachat.data.model.Message
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.utils.dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock.System.now
import timber.log.Timber
import timber.log.Timber.Forest.d
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {

    private val _messages = MutableStateFlow(mutableListOf<Message>())
    val messages = _messages.asStateFlow()

    private val _otherUser = MutableStateFlow<UserProfile?>(null)
    val otherUser = _otherUser.asStateFlow()

    val currentUserId = supabase.auth.currentUserOrNull()?.id

    fun loadMessages(conversationId: String) {
        viewModelScope.launch(dispatcher) {
            supabase.from("messages").select {
                filter {
                    Message::conversation_id eq conversationId
                }
            }
                .decodeList<Message>()
                .also { _messages.value = it.reversed().toMutableList() }
        }
    }

    fun sendMessage(conversationId: String, content: String) {
        viewModelScope.launch(dispatcher) {
            supabase.from("messages").insert(
                Message(
                    conversation_id = conversationId,
                    sender_id = currentUserId.toString(),
                    content = content,
                    created_at = now().toString(),
                )
            )
        }
    }

    @OptIn(SupabaseExperimental::class)
    suspend fun subscribeToMessages(conversationId: String) {
        try {
            val flow: Flow<List<Message>> =
                supabase.from("messages").selectAsFlow(Message::conversation_id)
            flow.collect {
                for (message in it) {
                    if (message.conversation_id == conversationId) {
                        _messages.update { (it + message).toMutableList() }
                    }
                }
            }
            d("WebSocket connected successfully")
        } catch (e: Exception) {
            d("WebSocket failed: ${e.message}")
        }
    }

    fun loadOtherUser(otherUserId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                _otherUser.value = supabase.from("profiles").select {
                    filter {
                        UserProfile::id eq otherUserId
                    }
                }
                    .decodeSingle<UserProfile>()

                d("Other user: ${_otherUser.value}")
            } catch (e: Exception) {
                Timber.e("Error on loadOtherUser: ${e.message}")
            }
        }
    }
}