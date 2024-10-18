package com.waqas028.super_gauge_view_speedo_meter.presentation.screen

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waqas028.super_gauge_view_speedo_meter.R
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeedometerScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedometerScreen() {

    val scope = rememberCoroutineScope()
    var isDownload by remember { mutableStateOf(true) }
    val animation = remember { Animatable(0f) }

    // Start animation
    LaunchedEffect(isDownload) {
        startAnimation(isDownload, animation)
    }

    var speed by remember { mutableStateOf(0f) }
    var isRunning by remember { mutableStateOf(true) }
    val animatedSpeed by animateFloatAsState(targetValue = speed, label = "")
    var uploadSpeed by remember { mutableStateOf(5.4f) }
    var downloadSpeed by remember { mutableStateOf(26.3f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (speed < 60f) {
                speed += 0.5f
                delay(100)
                speed -= 0.2f
                delay(100)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1C)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Speedometer", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF252525),
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        ToggleButtons(downloadSpeed, uploadSpeed)

        Spacer(modifier = Modifier.height(32.dp))

        Box(contentAlignment = Alignment.Center) {
            SpeedometerArc(speed = animatedSpeed)
            SpeedometerNeedle(speed = animatedSpeed)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${animatedSpeed.toInt()} mbps",
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { isRunning = !isRunning },
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
        ) {
            Text(text = if (isRunning) "STOP" else "START", color = Color.White)
        }
    }
}

@Composable
fun ToggleButtons(downloadSpeed: Float, uploadSpeed: Float) {
    Row(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .clip(CircleShape)
            .background(Color(0xFF252525))
            .padding(4.dp)
    ) {
        Button(
            onClick = { /* Handle download click */ },
            modifier = Modifier
                .weight(1f)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(text = "Download", color = Color.White)
        }
        Button(
            onClick = { /* Handle upload click */ },
            modifier = Modifier
                .weight(1f)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text(text = "Upload", color = Color.White)
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = "↓ $downloadSpeed Mbps", color = Color.Yellow)
        Text(text = "↑ $uploadSpeed Mbps", color = Color.Yellow)
    }
}

@Composable
fun SpeedometerArc(speed: Float) {
    val progress = speed / 60f
    Canvas(
        modifier = Modifier
            .size(300.dp)
            .padding(16.dp),
        onDraw = {
            drawArc(
                color = Color.Gray,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.Yellow,
                startAngle = 135f,
                sweepAngle = 270f * progress,
                useCenter = false,
                style = Stroke(12.dp.toPx(), cap = StrokeCap.Round)
            )

            // Drawing the numbers from 0 to 60
            val radius = size.minDimension / 2.5f
            for (i in 0..60 step 10) {
                val angleInDegrees = 135 + (270 * (i / 60f))
                val angleInRadians = Math.toRadians(angleInDegrees.toDouble())
                val x = center.x + radius * kotlin.math.cos(angleInRadians).toFloat()
                val y = center.y + radius * kotlin.math.sin(angleInRadians).toFloat()
                drawContext.canvas.nativeCanvas.drawText(
                    i.toString(),
                    x,
                    y,
                    Paint().apply {
                        textSize = 32f
                        color = android.graphics.Color.WHITE
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    )
}

@Composable
fun SpeedometerNeedle(speed: Float) {
    val angle = 225f + (270 * (speed / 60f))
    Canvas(
        modifier = Modifier.size(300.dp),
        onDraw = {
            rotate(degrees = angle, pivot = center) {
                drawLine(
                    color = Color.Yellow,
                    start = center,
                    end = Offset(center.x, 50f),
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    )
}

// The animation function you provided, applied to animate arc/needle
suspend fun startAnimation(isDownload: Boolean, animation: Animatable<Float, AnimationVector1D>) {
    animation.animateTo(0.84f, keyframes {
        durationMillis = 9000
        0f at 0 using CubicBezierEasing(0f, 1.5f, 0.8f, 1f)
        0.72f at 1000 using CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.76f at 2000 using CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.78f at 3000 using CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.82f at 4000 using CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.85f at 5000 using CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.89f at 6000 using CubicBezierEasing(0.2f, -1.2f, 0f, 1f)
        0.82f at 7500 using LinearOutSlowInEasing
    })
}

@Preview
@Composable
fun SpeedometerScreenPreview() {
    SpeedometerScreen()
}
