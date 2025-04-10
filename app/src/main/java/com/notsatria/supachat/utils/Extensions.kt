package com.notsatria.supachat.utils

import androidx.navigation.NavController

fun NavController.navigateAndClearBackStack(route: String, popUpTarget: String) {
    navigate(route) {
        popUpTo(popUpTarget) { inclusive = true }
    }
}