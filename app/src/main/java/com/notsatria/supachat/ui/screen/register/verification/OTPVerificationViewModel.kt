package com.notsatria.supachat.ui.screen.register.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.supachat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber.Forest.d
import timber.log.Timber.Forest.e
import javax.inject.Inject

@HiltViewModel
class OTPVerificationViewModel @Inject constructor(private val supabase: SupabaseClient) :
    ViewModel() {

    private val _verificationState = Channel<Resource<String>>()
    val verificationState = _verificationState.receiveAsFlow()

    private val _resendOTPState = Channel<Resource<String>>()
    val resendOTPState = _resendOTPState.receiveAsFlow()

    fun verifyOTP(email: String, otpCode: String) {
        viewModelScope.launch {
            try {
                d("OTP code: $otpCode")
                _verificationState.send(Resource.Loading())
                supabase.auth.verifyEmailOtp(OtpType.Email.EMAIL, email = email, token = otpCode)
                _verificationState.send(Resource.Success("Verification success"))
            } catch (e: Exception) {
                _verificationState.send(Resource.Error("Error: ${e.message}"))
                e("Error on verifyOTP: ${e.message}")
            }
        }
    }

    fun resendOTP(email: String) {
        viewModelScope.launch {
            try {
                _resendOTPState.send(Resource.Loading())
                supabase.auth.resendEmail(OtpType.Email.SIGNUP, email = email)
                _resendOTPState.send(Resource.Success("OTP resend successfully"))
            } catch (e: Exception) {
                _resendOTPState.send(Resource.Error("Error: ${e.message}"))
            }
        }
    }

}