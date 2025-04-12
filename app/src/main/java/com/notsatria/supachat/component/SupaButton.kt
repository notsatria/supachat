package com.notsatria.supachat.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun SupaButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = !isLoading
    ) {
        if (isLoading) CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = MaterialTheme.colorScheme.onPrimary
        ) else Text(
            text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SupaButtonPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        Column {
            SupaButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Button",
                isLoading = true,
                onClick = {}
            )
            SupaButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Button",
                isLoading = false,
                onClick = {}
            )
        }
    }
}