package com.suma.sumaapp.presentation.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import com.suma.sumaapp.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.suma.sumaapp.ui.theme.CaribbeanGreen

@Composable
fun FingerprintButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFFE0FAEF))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_fingerprint),
            contentDescription = null,
            tint = CaribbeanGreen,
            modifier = Modifier.size(42.dp)
        )
    }
}
