package com.notsatria.supachat.navigation

sealed class Screen(val route: String) {
    data object Register : Screen("register")
    data object Login : Screen("login")
    data object ChatRoom : Screen("chat_room")
    data object ChatList : Screen("chat_list")
}
