package com.notsatria.supachat.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val username: String,
    val avatar_url: String?,
)
