package com.notsatria.supachat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String? = null,
    val conversation_id: String,
    val sender_id: String,
    val content: String,
    val attachment_url: String? = null,
    val attachment_type: String? = null,
    val created_at: String
)