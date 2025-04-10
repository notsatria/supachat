package com.notsatria.supachat.ui.screen.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun register(email: String, password: String) {
        viewModelScope.launch(dispatcher) {
            try {
                _registerState.send(Resource.Loading())
                val user = supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                if (user != null) {
                    _registerState.send(Resource.Success(user))
                }
            } catch (e: Exception) {
                _registerState.send(Resource.Error(e.message.toString()))
                Log.e("RegisterViewModel", "Error on register: ${e.message}")
            }
        }
    }
}