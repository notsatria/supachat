package com.notsatria.supachat.ui.screen.home.room_chat

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.realtime.channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {

    private val _messages = MutableStateFlow(mutableListOf<String>())
    val messages = _messages.asStateFlow()
    private val myChannel = supabase.channel("test-bro")


    fun sendMessage(message: String) {
        val updatedMessages = _messages.value.toMutableList()
        updatedMessages.add(message)
        _messages.value = updatedMessages
    }
}