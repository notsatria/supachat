package com.notsatria.supachat.data.repository

import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.utils.Resource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val supabase: SupabaseClient) {

    fun getUserList(currentUserId: String): Flow<Resource<List<UserProfile>>> = flow {
        try {
            emit(Resource.Loading())
            val userList = supabase.from("profiles").select {
                filter {
                    UserProfile::id neq currentUserId
                }
            }.decodeList<UserProfile>()
            emit(Resource.Success(userList))
        } catch (e: Exception) {
            Timber.e("Error on getUserList: ${e.message}")
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }
}