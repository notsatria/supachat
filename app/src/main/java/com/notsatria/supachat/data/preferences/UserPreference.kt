package com.notsatria.supachat.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val dataStore: DataStore<Preferences>) {
    companion object {
        val IS_LOGIN = booleanPreferencesKey("is_login")
    }

    val isLogin: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_LOGIN] ?: false
        }

    suspend fun setLoginState(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN] = isLogin
        }
    }
}