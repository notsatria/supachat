package com.notsatria.supachat.ui.screen.home.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.data.repository.ConversationRepository
import com.notsatria.supachat.data.repository.UserRepository
import com.notsatria.supachat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val supabase: SupabaseClient,
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private val _userListState = MutableStateFlow<Resource<List<UserProfile>>>(Resource.Initial())
    val userListState = _userListState.asStateFlow()

    private val _conversationId = MutableStateFlow< Resource<String>>(Resource.Loading())
    val conversationId = _conversationId.asStateFlow()

    init {
        getUserList()
    }

    private fun getUserList() {
        viewModelScope.launch {
            val currentUser = supabase.auth.currentUserOrNull() ?: return@launch
            userRepository.getUserList(currentUser.id).collect {
                _userListState.value = it
            }
        }
    }

    fun getConversationId(otherUserId: String, onSuccess: (conversationId: String) -> Unit, onError: (message: String) -> Unit) {
        viewModelScope.launch {
            try {
                val currentUser = supabase.auth.currentUserOrNull() ?: return@launch
                conversationRepository.getConversationId(currentUser.id, otherUserId).collect {
                    onSuccess(it)
                }
            } catch (e: Exception) {
                Timber.e("Error on getConversationId: ${e.message}")
                onError(e.message ?: "Unknown error")
            }
        }
    }
}