package com.notsatria.supachat.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notsatria.supachat.SessionViewModel
import com.notsatria.supachat.component.PasswordTextField
import com.notsatria.supachat.component.SupaButton
import com.notsatria.supachat.ui.theme.SupachatTheme
import com.notsatria.supachat.utils.Resource

@Composable
fun LoginRoute(
    modifier: Modifier = Modifier,
    navigateToLoginScreen: () -> Unit = {},
    navigateToChatScreen: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState(null)
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        when (loginState) {
            is Resource.Loading -> {
                isLoading.value = true
            }

            is Resource.Success -> {
                isLoading.value = false
                Toast.makeText(context, "Login Success!", Toast.LENGTH_SHORT).show()
                sessionViewModel.setIsLoggedIn()
                navigateToChatScreen()
            }

            is Resource.Error -> {
                isLoading.value = false
                Toast.makeText(context, (loginState as Resource.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {}
        }
    }

    LoginScreen(
        modifier,
        isLoading = isLoading,
        navigateToLoginScreen = navigateToLoginScreen,
        onLoginClicked = { email, password ->
            viewModel.login(email, password)
        })
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    isLoading: MutableState<Boolean> = remember { mutableStateOf(false) },
    navigateToLoginScreen: () -> Unit,
    onLoginClicked: (String, String) -> Unit,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Scaffold(modifier) { p ->
        Box(modifier = Modifier.padding(p)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Hello!")
                Text("Login to enter Supachat!")
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email.value,
                    onValueChange = { newVal ->
                        email.value = newVal
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Email, null)
                    },
                    label = {
                        Text("Email")
                    },
                    placeholder = {
                        Text("Please input your email")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password.value,
                    onValueChange = { newVal ->
                        password.value = newVal
                    },
                )
                Spacer(Modifier.height(20.dp))
                SupaButton(modifier = Modifier.fillMaxWidth(), text = "Login", isLoading = isLoading.value, onClick = {
                    onLoginClicked(email.value, password.value)
                })
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don't have an account?")
                    TextButton(onClick = navigateToLoginScreen) {
                        Text("Register")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        LoginRoute()
    }
}