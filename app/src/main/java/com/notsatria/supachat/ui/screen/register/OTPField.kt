package com.notsatria.supachat.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun OTPField(
    modifier: Modifier = Modifier,
    length: Int = 6,
    isError: Boolean = false,
    otpValues: List<MutableState<String>>,
    onOtpChange: (String) -> Unit
) {
    val focusRequesters = remember { List(length) { FocusRequester() } }
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        otpValues.forEachIndexed { index, otpChar ->
            OutlinedTextField(
                modifier = Modifier
                    .size(width = 42.dp, height = 56.dp)
                    .focusRequester(focusRequesters[index])
                    .focusProperties {
                        next =
                            if (index < length - 1) focusRequesters[index + 1] else focusRequesters[index]
                        previous =
                            if (index > 0) focusRequesters[index - 1] else focusRequesters[index]
                    },
                value = otpChar.value,
                onValueChange = { newVal ->
                    if (newVal.length <= 1 && newVal.all { it.isDigit() }) {
                        otpChar.value = newVal
                        onOtpChange(otpValues.joinToString("") { it.value })
                        when {
                            newVal.isNotEmpty() && index < length - 1 ->
                                focusRequesters[index + 1].requestFocus()

                            newVal.isEmpty() && index > 0 ->
                                focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isError) MaterialTheme.colorScheme.error else LocalTextStyle.current.color
                ),
                /*   colors = OutlinedTextFieldDefaults.colors(
                       focusedBorderColor = if (isError) errorColor else MaterialTheme.colorScheme.primary,
                       unfocusedBorderColor = if (isError) errorColor else MaterialTheme.colorScheme.onSurface.copy(
                           alpha = 0.38f
                       ),
                       cursorColor = if (isError) errorColor else MaterialTheme.colorScheme.primary,
                   ),*/
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index == length - 1) ImeAction.Done else ImeAction.Next
                ),
                isError = isError
            )
            if (index == 2) Box(
                Modifier
                    .width(12.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.76f))
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OTPFieldPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        OTPField(
            isError = false,
            otpValues = listOf(
                remember { mutableStateOf("1") },
                remember { mutableStateOf("2") },
                remember { mutableStateOf("1") },
                remember { mutableStateOf("1") },
                remember { mutableStateOf("1") },
                remember { mutableStateOf("1") }),
            onOtpChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OTPFieldErrorPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        OTPField(
            isError = true,
            otpValues = listOf(
                remember { mutableStateOf("1") },
                remember { mutableStateOf("2") },
                remember { mutableStateOf("1") },
                remember { mutableStateOf("1") },
                remember { mutableStateOf("1") },
                remember { mutableStateOf("1") }),
            onOtpChange = {}
        )
    }
}