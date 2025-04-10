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
                })
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