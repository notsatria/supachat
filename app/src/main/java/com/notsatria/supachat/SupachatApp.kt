package com.notsatria.supachat

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.notsatria.supachat.navigation.Screen
import com.notsatria.supachat.ui.screen.home.chat_list.ChatListRoute
import com.notsatria.supachat.ui.screen.home.room_chat.ChatRoomRoute
import com.notsatria.supachat.ui.screen.login.LoginRoute
import com.notsatria.supachat.ui.screen.register.verification.OTPVerificationRoute
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
                RegisterRoute(
                    modifier = modifier.padding(p), navigateToLoginScreen = {
                        navController.navigateAndClearBackStack(
                            route = Screen.Login.route,
                            popUpTarget = Screen.Register.route
                        )
                    },
                    navigateToOTPVerificationScreen = { email ->
                        navController.navigate(Screen.OTPVerification.createRoute(email))
                    })
            }

            composable(Screen.OTPVerification.route, arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                OTPVerificationRoute(modifier = modifier.padding(p), navigateBack = {
                    navController.navigateUp()
                }, email = email)
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
                ChatListRoute(modifier.padding(p), navigateToChatRoom = {
                    navController.navigate(Screen.ChatRoom.route)
                })
            }

            composable(Screen.ChatRoom.route) {
                ChatRoomRoute(modifier.padding(p))
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