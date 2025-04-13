package com.notsatria.supachat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.supachat.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(private val userPreferences: UserPreferences) :
    ViewModel() {
    val isLogin: StateFlow<Boolean> = userPreferences.isLogin
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun setIsLoggedIn() {
        viewModelScope.launch {
            userPreferences.setLoginState(true)
        }
    }
}