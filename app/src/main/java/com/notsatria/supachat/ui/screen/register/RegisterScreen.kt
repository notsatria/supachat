package com.notsatria.supachat.ui.screen.register

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
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
import com.notsatria.supachat.component.PasswordTextField
import com.notsatria.supachat.component.SupaButton
import com.notsatria.supachat.ui.theme.SupachatTheme
import com.notsatria.supachat.utils.Resource

@Composable
fun RegisterRoute(
    modifier: Modifier = Modifier, navigateToLoginScreen: () -> Unit = {},
    navigateToOTPVerificationScreen: (String) -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val registerState by viewModel.registerState.collectAsState(null)
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        when (registerState) {
            is Resource.Loading -> {
                isLoading.value = true
            }

            is Resource.Success -> {
                isLoading.value = false
                navigateToOTPVerificationScreen((registerState as Resource.Success).data?.email.toString())
            }

            is Resource.Error -> {
                isLoading.value = false
                Toast.makeText(
                    context,
                    (registerState as Resource.Error).message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            else -> {}
        }
    }

    RegisterScreen(
        modifier,
        isLoading = isLoading,
        username = viewModel.username,
        email = viewModel.email,
        password = viewModel.password,
        onUsernameChange = { newUsername ->
            viewModel.updateUsername(newUsername)
        },
        onEmailChange = { newEmail ->
            viewModel.updateEmail(newEmail)
        },
        onPasswordChange = { newPass ->
            viewModel.updatePassword(newPass)
        },
        navigateToLoginScreen = navigateToLoginScreen,
        onRegisterClicked = {
            viewModel.register()
        })
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    isLoading: MutableState<Boolean> = remember { mutableStateOf(false) },
    username: String = "",
    email: String = "",
    password: String = "",
    onUsernameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    navigateToLoginScreen: () -> Unit = {},
    onRegisterClicked: () -> Unit = {},
) {
    Scaffold(modifier) { p ->
        Box(Modifier.padding(p)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Welcome to Supachat!")
                Text("Please register your account first")
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = username,
                    onValueChange = onUsernameChange,
                    leadingIcon = {
                        Icon(Icons.Filled.Person, null)
                    },
                    label = {
                        Text("Username")
                    },
                    placeholder = {
                        Text("Please input your username")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = onEmailChange,
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
                    value = password,
                    onValueChange = onPasswordChange,
                )
                Spacer(Modifier.height(20.dp))
                SupaButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Register",
                    isLoading = isLoading.value,
                    onClick = onRegisterClicked
                )
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Already have an account?")
                    TextButton(onClick = navigateToLoginScreen) {
                        Text("Login")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        RegisterScreen()
    }
}