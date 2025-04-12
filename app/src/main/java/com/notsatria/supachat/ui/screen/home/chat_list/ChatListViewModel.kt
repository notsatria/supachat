package com.notsatria.supachat.ui.screen.home.chat_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.supachat.data.model.Conversation
import com.notsatria.supachat.utils.dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations = _conversations.asStateFlow()

    fun loadConversations() {
        viewModelScope.launch(dispatcher) {
            val currentUserId = supabase.auth.currentUserOrNull()?.id ?: return@launch

            supabase.from("conversations")
                .select {
                    filter {
                        or {
                            Conversation::user1_id eq currentUserId
                            Conversation::user2_id eq currentUserId
                        }
                    }
                }
                .decodeList<Conversation>()
                .also { _conversations.value = it }
        }
    }
}