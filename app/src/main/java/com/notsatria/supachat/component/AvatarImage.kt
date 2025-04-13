package com.notsatria.supachat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.notsatria.supachat.data.model.UserProfile
import com.notsatria.supachat.ui.theme.SupachatTheme

@Composable
fun AvatarImage(
    modifier: Modifier = Modifier,
    size: Int = 24,
    profile: UserProfile
) {
    if (profile.avatar_url != null) AsyncImage(
        profile.avatar_url,
        "Avatar",
        contentScale = ContentScale.Crop,
        placeholder = BrushPainter(
            Brush.linearGradient(
                listOf(
                    Color(color = 0xffF7374F),
                    Color(color = 0xff88304E),
                )
            )
        ),
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)

    ) else
        Box(
            modifier = modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = if (profile.username.isNotBlank()) profile.username.first()
                    .toString() else "",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onTertiary,
                fontSize = (size / 2).sp
            )
        }
}

@Preview
@Composable
fun AvatarImagePreview(
    modifier: Modifier = Modifier,
    profile: UserProfile = UserProfile(
        id = "",
        username = "username",
        avatar_url = null
    )
) {
    SupachatTheme {
        Column {
            AvatarImage(profile = profile, size = 50)
            AvatarImage(profile = profile, size = 50)
            AvatarImage(profile = profile)
            AvatarImage(profile = profile, size = 50)
        }
    }
}