package com.notsatria.supachat.ui.screen.home.chat_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notsatria.supachat.component.AvatarImage
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun ChatRow(modifier: Modifier = Modifier, profile: UserProfile, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            },
    ) {
        AvatarImage(profile = profile, size = 48)
        Spacer(Modifier.width(8.dp))
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(profile.username, fontWeight = FontWeight.SemiBold)
                Text(
                    "12.00",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                "message",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatRowPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        ChatRow(
            profile = UserProfile(id = "", username = "username", avatar_url = null),
            onClick = {})
    }
}