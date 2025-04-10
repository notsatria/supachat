package com.notsatria.supachat.ui.screen.register

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notsatria.supachat.component.PasswordTextField
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun RegisterRoute(
    modifier: Modifier = Modifier, navigateToLoginScreen: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterScreen(modifier, navigateToLoginScreen, onRegisterClicked = { email, password ->
        viewModel.register(email, password)
    })
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navigateToLoginScreen: () -> Unit,
    onRegisterClicked: (String, String) -> Unit,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

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
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onRegisterClicked(email.value, password.value) }
                ) {
                    Text("Register")
                }
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
        RegisterRoute()
    }
}