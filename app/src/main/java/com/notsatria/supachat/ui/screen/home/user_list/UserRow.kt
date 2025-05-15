package com.notsatria.supachat.ui.screen.home.user_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notsatria.supachat.component.AvatarImage
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun UserRow(
    modifier: Modifier = Modifier,
    profile: UserProfile,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarImage(modifier = Modifier.padding(start = 12.dp), profile = profile, size = 48)
        Spacer(Modifier.width(8.dp))
        Text(profile.username, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
fun UserRowPreview(modifier: Modifier = Modifier) {
    SupachatTheme {
        UserRow(
            profile = UserProfile(id = "", username = "username", avatar_url = null),
            onClick = {},
        )
    }
}