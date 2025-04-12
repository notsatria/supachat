package com.notsatria.supachat

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.notsatria.supachat.navigation.Screen
import com.notsatria.supachat.ui.screen.home.chat_list.ChatListRoute
import com.notsatria.supachat.ui.screen.home.room_chat.ChatRoomRoute
import com.notsatria.supachat.ui.screen.login.LoginRoute
import com.notsatria.supachat.ui.screen.register.RegisterRoute
import com.notsatria.supachat.ui.theme.SupachatTheme
import com.notsatria.supachat.utils.navigateAndClearBackStack

@Composable
fun SupachatApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold { p ->
        NavHost(navController = navController, startDestination = Screen.Register.route) {
            composable(Screen.Register.route) {
                RegisterRoute(modifier = modifier.padding(p), navigateToLoginScreen = {
                    navController.navigateAndClearBackStack(
                        route = Screen.Login.route,
                        popUpTarget = Screen.Register.route
                    )
                })
            }

            composable(Screen.Login.route) {
                LoginRoute(modifier = modifier.padding(p), navigateToLoginScreen = {
                    navController.navigateAndClearBackStack(
                        route = Screen.Register.route,
                        popUpTarget = Screen.Login.route
                    )
                }, navigateToChatScreen = {
                    navController.navigateAndClearBackStack(
                        route = Screen.ChatList.route,
                        popUpTarget = Screen.Login.route
                    )
                })
            }

            composable(Screen.ChatList.route) {
                ChatListRoute(navigateToChatRoom = {
                    navController.navigate(Screen.ChatRoom.route)
                })
            }

            composable(Screen.ChatRoom.route) {
                ChatRoomRoute()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SupachatAppPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        SupachatApp()
    }
}