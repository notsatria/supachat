package com.notsatria.supachat.navigation

sealed class Screen(val route: String) {
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object ChatRoom : Screen("chat_room/{conversationId}/{otherUserId}") {
        fun createRoute(conversationId: String, otherUserId: String) = "chat_room/$conversationId/$otherUserId"
    }
    data object ChatList : Screen("chat_list/")
    data object OTPVerification : Screen("otp_verification/{email}") {
        fun createRoute(email: String) = "otp_verification/$email"
    }
}
