package com.waqas028.super_gauge_view_speedo_meter.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpeedometerApp() {
    var downloadSpeed by remember { mutableStateOf(0f) }
    var uploadSpeed by remember { mutableStateOf(0f) }
    var isRunning by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        color = Color.Black
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Speedometer",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SpeedCard(
                    title = "Download",
                    speed = downloadSpeed,
                    onClick = { downloadSpeed = (0..100).random().toFloat() }
                )
                SpeedCard(
                    title = "Upload",
                    speed = uploadSpeed,
                    onClick = { uploadSpeed = (0..100).random().toFloat() }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier.size(100.dp),
                shape = CircleShape
            ) {
                Text(text = if (isRunning) "STOP" else "START", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun SpeedCard(title: String, speed: Float, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Speedometer(speed)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$speed Mbps",
            color = Color.Yellow,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onClick,
            modifier = Modifier.size(100.dp),
            shape = CircleShape
        ) {
            Text(text = "Update", fontSize = 16.sp)
        }
    }
}

@Composable
fun Speedometer(speed: Float) {
    val maxSpeed = 80
    val speedAngle = (speed / maxSpeed) * 270f // Scale it for the 270-degree arc

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(150.dp)
    ) {
        Canvas(
            modifier = Modifier.size(150.dp)
        ) {
            // Draw the circular arc for the gauge
            drawArc(
                color = Color.DarkGray,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw the filled part of the gauge
            drawArc(
                color = Color.Yellow,
                startAngle = 135f,
                sweepAngle = speedAngle,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )

            // Draw the speed needle
            val needleLength = size.minDimension / 2.5f
            val needleAngle = Math.toRadians((135 + speedAngle).toDouble())
            val needleX = center.x + cos(needleAngle) * needleLength
            val needleY = center.y + sin(needleAngle) * needleLength

            drawLine(
                color = Color.Yellow,
                start = center,
                end = androidx.compose.ui.geometry.Offset(needleX.toFloat(), needleY.toFloat()),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Display speed value at the center
        Text(
            text = String.format("%.2f", speed),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpeedometerAppPreview() {
    SpeedometerApp()
}