package com.waqas028.super_gauge_view_speedo_meter.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waqas028.super_gauge_view_speedo_meter.R

@Composable
fun StartButton(isEnabled: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 24.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 2.dp, color = if(isEnabled) MaterialTheme.colorScheme.onSurface else Color.Gray)
        ) {
        Text(
            text = stringResource(id = R.string.start),
            color = if(isEnabled) MaterialTheme.colorScheme.onPrimary else Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )
    }
}