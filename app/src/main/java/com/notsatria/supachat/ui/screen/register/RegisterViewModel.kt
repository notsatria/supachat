package com.notsatria.supachat.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger.Companion.e
import com.notsatria.supachat.utils.Resource
import com.notsatria.supachat.utils.dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {
    private val _registerState = Channel<Resource<UserInfo>>()
    val registerState = _registerState.receiveAsFlow()

    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun register() {
        viewModelScope.launch(dispatcher) {
            try {
                _registerState.send(Resource.Loading())
                val user = supabase.auth.signUpWith(Email) {
                    this.email = this@RegisterViewModel.email
                    this.password = this@RegisterViewModel.password
                }

                if (user != null) {
                    _registerState.send(Resource.Success(user))
                }
            } catch (e: Exception) {
                _registerState.send(Resource.Error(e.message.toString()))
                e("Error: ${e.message}")
            }
        }
    }

    fun updateUsername(value: String) {
        username = value
    }

    fun updateEmail(value: String) {
        email = value
    }

    fun updatePassword(value: String) {
        password = value
    }
}