package com.notsatria.supachat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    val user1_id: String,
    val user2_id: String,
    val updated_at: String,
    val created_at: String
)