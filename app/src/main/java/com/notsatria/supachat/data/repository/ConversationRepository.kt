package com.notsatria.supachat.data.repository

import com.notsatria.supachat.data.model.Conversation
import com.notsatria.supachat.utils.Resource
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.Uuid

@Singleton
class ConversationRepository @Inject constructor(private val supabase: SupabaseClient) {

    fun getConversationId(currentUserId: String, otherUserId: String): Flow<String> = flow {
        try {
//            emit(Resource.Loading())
            val conversationId = supabase.from("conversations").select {
                filter {
                    or {
                        and {
                            Conversation::user1_id eq currentUserId
                            Conversation::user2_id eq otherUserId
                        }
                        and {
                            Conversation::user1_id eq otherUserId
                            Conversation::user2_id eq currentUserId
                        }
                    }
                }
            }.decodeSingle<String>()
            emit(conversationId)
        } catch (e: Exception) {
            Timber.w("Conversation not found, creating new one ${e.message}")

            try {
                val newConversation = Conversation(
                    id = UUID.randomUUID().toString(),
                    user1_id = currentUserId,
                    user2_id = otherUserId,
                    created_at = null,
                    updated_at = null
                )

                val inserted = supabase.from("conversations")
                    .insert(newConversation)
                    .decodeSingle<Conversation>()

                emit(inserted.id!!)
            } catch (insertError: Exception) {
                Timber.e("Failed to create new conversation: ${insertError.message}")
                throw insertError
            }
        }
    }
}