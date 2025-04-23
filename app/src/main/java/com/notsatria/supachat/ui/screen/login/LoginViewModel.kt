package com.notsatria.supachat.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.supachat.utils.Resource
import com.notsatria.supachat.utils.dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val supabase: SupabaseClient) : ViewModel() {

    private val _loginState = Channel<Resource<String>>()
    val loginState = _loginState.receiveAsFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch(dispatcher) {
            try {
                _loginState.send(Resource.Loading())
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                _loginState.send(Resource.Success("Login success"))
            } catch (e: Exception) {
                _loginState.send(Resource.Error(e.message.toString()))
                Timber.e( "Error on login: ${e.message}")
            }
        }
    }
}