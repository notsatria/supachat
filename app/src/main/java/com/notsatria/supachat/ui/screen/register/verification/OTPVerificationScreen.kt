package com.notsatria.supachat.ui.screen.register.verification

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notsatria.supachat.component.SupaButton
import com.notsatria.supachat.ui.screen.register.OTPField
import com.notsatria.supachat.ui.theme.SupachatTheme
import com.notsatria.supachat.utils.Resource
import kotlinx.coroutines.delay

@Composable
fun OTPVerificationRoute(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    email: String = "",
    viewModel: OTPVerificationViewModel = hiltViewModel()
) {
    val verificationState by viewModel.verificationState.collectAsState(null)
    val resendOTPState by viewModel.resendOTPState.collectAsState(null)
    var isLoading by remember { mutableStateOf(false) }
    val isOtpError = remember { mutableStateOf(false) }
    val otpExpiredTime = remember { mutableIntStateOf(60) }
    val context = LocalContext.current

    LaunchedEffect(otpExpiredTime.intValue) {
        delay(1000)
        otpExpiredTime.intValue -= 1
    }

    LaunchedEffect(resendOTPState) {
        when (verificationState) {
            is Resource.Loading -> {}

            is Resource.Success -> {
                Toast.makeText(
                    context,
                    (verificationState as Resource.Success).data,
                    Toast.LENGTH_SHORT
                ).show()
                otpExpiredTime.intValue = 60
            }

            is Resource.Error -> {
                Toast.makeText(
                    context,
                    (verificationState as Resource.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

    LaunchedEffect(verificationState) {
        when (verificationState) {
            is Resource.Loading -> {
                isOtpError.value = false
                isLoading = true
            }

            is Resource.Success -> {
                isLoading = false
                isOtpError.value = false
                Toast.makeText(
                    context,
                    (verificationState as Resource.Success).data,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is Resource.Error -> {
                isLoading = false
                isOtpError.value = true
                Toast.makeText(
                    context,
                    (verificationState as Resource.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

    OTPVerificationScreen(
        modifier,
        navigateBack = navigateBack,
        email = email,
        isLoading = isLoading,
        isOtpError = isOtpError,
        otpExpiredTime = otpExpiredTime,
        onVerifyClicked = { otp ->
            viewModel.verifyOTP(email = email, otpCode = otp)
        },
        onResendOTP = {
            viewModel.resendOTP(email)
            otpExpiredTime.intValue = 60
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    email: String = "",
    isLoading: Boolean = false,
    otpExpiredTime: MutableIntState = remember { mutableIntStateOf(60) },
    isOtpError: MutableState<Boolean> = remember { mutableStateOf(false) },
    onVerifyClicked: (String) -> Unit = {},
    onResendOTP: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val otpValues = remember { List(otpLength) { mutableStateOf("") } }
    val otpText = remember {
        derivedStateOf {
            otpValues.joinToString("") { it.value }
        }
    }

    Scaffold(modifier, topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(Icons.AutoMirrored.Default.ArrowBackIos, "Back")
            }
        })
    }) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .padding(horizontal = 12.dp)
                .padding(top = 56.dp)
                .fillMaxSize()
        ) {
            Text("OTP Verification", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            Spacer(Modifier.height(4.dp))
            Text("Please input otp verification code that we sent to $email")
            Spacer(Modifier.height(48.dp))
            OTPField(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                length = otpLength,
                otpValues = otpValues,
                onOtpChange = { currentOTP ->
                    if (isOtpError.value && currentOTP.length == 6) {
                        isOtpError.value = false
                    }
                },
                isError = isOtpError.value
            )
            Spacer(Modifier.height(24.dp))
            if (otpExpiredTime.intValue > 0)
                Text(
                    text = buildAnnotatedString {
                        append("This code will expire in ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(otpExpiredTime.intValue.toString())
                        }
                        append(" seconds")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            else
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Didn't receive a code? ")
                    Text(
                        modifier = Modifier.clickable {
                            onResendOTP()
                        },
                        text = "Resend",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            Spacer(Modifier.height(24.dp))
            SupaButton(Modifier.fillMaxWidth(), text = "Verify", isLoading = isLoading, onClick = {
                val fullOTP = otpText.value
                if (fullOTP.length == 6) {
                    onVerifyClicked(fullOTP)
                } else {
                    isOtpError.value = true
                }
            })
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OTPVerificationScreenPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        OTPVerificationScreen(isLoading = true)
    }
}