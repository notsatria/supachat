package com.notsatria.supachat.ui.model

import com.notsatria.supachat.data.model.UserProfile

data class ConversationUiModel(
    val conversationId: String,
    val otherUser: UserProfile,
    val latestMessage: String
)