package com.suma.sumaapp.presentation.components.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Avatar(image: Int) {
    Box {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
    }
}
