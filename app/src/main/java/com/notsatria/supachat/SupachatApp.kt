package com.notsatria.supachat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.notsatria.supachat.navigation.Screen
import com.notsatria.supachat.ui.screen.home.chat_list.ChatListRoute
import com.notsatria.supachat.ui.screen.home.room_chat.ChatRoomRoute
import com.notsatria.supachat.ui.screen.login.LoginRoute
import com.notsatria.supachat.ui.screen.register.RegisterRoute
import com.notsatria.supachat.ui.screen.register.verification.OTPVerificationRoute
import com.notsatria.supachat.ui.theme.SupachatTheme
import com.notsatria.supachat.utils.navigateAndClearBackStack

@Composable
fun SupachatApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val isLogin by sessionViewModel.isLogin.collectAsState()

    val startDestination = if (isLogin) {
        Screen.ChatList.route
    } else {
        Screen.Register.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Register.route) {
            RegisterRoute(
                navigateToLoginScreen = {
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
            OTPVerificationRoute(
                navigateBack = {
                    navController.navigateUp()
                }, navigateToChatList = {
                    navController.navigateAndClearBackStack(
                        Screen.ChatList.route,
                        popUpTarget = Screen.OTPVerification.route
                    )
                }, email = email
            )
        }

        composable(Screen.Login.route) {
            LoginRoute(navigateToLoginScreen = {
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
            ChatListRoute(
                navigateToChatRoom = { conversationId, otherUserId ->
                    navController.navigate(
                        Screen.ChatRoom.createRoute(
                            conversationId,
                            otherUserId
                        )
                    )
                })
        }

        composable(
            Screen.ChatRoom.route,
            arguments = listOf(
                navArgument("conversationId") {
                    type = NavType.StringType
                },
                navArgument("otherUserId") {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
            ChatRoomRoute(
                conversationId = conversationId,
                otherUserId = otherUserId,
                navigateBack = {
                    navController.navigateUp()
                })
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