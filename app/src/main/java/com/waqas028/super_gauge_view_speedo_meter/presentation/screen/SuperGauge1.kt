package com.waqas028.super_gauge_view_speedo_meter.presentation.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waqas028.super_gauge_view_speedo_meter.R
import com.waqas028.super_gauge_view_speedo_meter.data.model.UiState
import com.waqas028.super_gauge_view_speedo_meter.data.utils.CompletePreviews
import com.waqas028.super_gauge_view_speedo_meter.presentation.component.StartButton
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.Blue
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.BlueGradient
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.Green200
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.Green500
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.GreenGradient
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.LightColor
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.Maya_Blue
import com.waqas028.super_gauge_view_speedo_meter.ui.theme.SuperGaugeViewSpeedoMeterTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

@Composable
fun SuperGauge1(){
    val coroutineScope = rememberCoroutineScope()

    val animation = remember { Animatable(0f) }
    val state = animation.toUiState()

    var isDownload by rememberSaveable { mutableStateOf(false) }
    var isUpload by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isDownload, isUpload) {
        if(isDownload || isUpload) coroutineScope.launch {
            startAnimation(
                isDownload = isDownload,
                animation = animation,
                onDownloadComplete = {
                    isDownload = false
                    isUpload = true
                },
                onUploadComplete = {
                    isDownload = false
                    isUpload = false
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.speed_test),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(50.dp))
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            CircularSpeedIndicator(modifier = Modifier.fillMaxSize(), state.arcValue, 240f, isDownload = isDownload)
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = if(isDownload) R.string.download else R.string.upload).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = state.speed,
                    fontSize = 45.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Text(
                    text = stringResource(id = R.string.mbps),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        StartButton(
            isEnabled = !state.inProgress,
            onClick = {
                isDownload = true
            }
        )
    }
}


@Composable
fun CircularSpeedIndicator(modifier: Modifier = Modifier, value: Float, angle: Float, isDownload: Boolean) {
    Canvas(
        modifier = modifier
            .padding(40.dp)
    ) {
        drawLines(value, angle)
        drawArcs(value, angle, isDownload)
    }
}

fun DrawScope.drawArcs(progress: Float, maxValue: Float, isDownload: Boolean) {
    val startAngle = 270 - maxValue / 2
    val sweepAngle = maxValue * progress

    val topLeft = Offset(50f, 50f)
    val size = Size(size.width - 100f, size.height - 100f)

    val blueColor = if (isDownload) Maya_Blue else Green200
    val strokeColor = if (isDownload) Blue else Green500
    val gradient = if (isDownload) BlueGradient else GreenGradient

    fun drawBlur() {
        for (i in 0..20) {
            drawArc(
                color = blueColor.copy(alpha = i / 900f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)
            )
        }
    }

    fun drawStroke() {
        drawArc(
            color = strokeColor,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 86f, cap = StrokeCap.Round)
        )
    }

    fun drawGradient() {
        drawArc(
            brush = gradient,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 80f, cap = StrokeCap.Round)
        )
    }

    drawBlur()
    drawStroke()
    drawGradient()
}

fun DrawScope.drawLines(progress: Float, maxValue: Float, numberOfLines: Int = 40) {
    val oneRotation = maxValue / numberOfLines
    val startValue = if (progress == 0f) 0 else floor(progress * numberOfLines).toInt() + 1

    for (i in startValue..numberOfLines) {
        rotate(i * oneRotation + (180 - maxValue) / 2) {
            drawLine(
                LightColor,
                Offset(if (i % 5 == 0) 80f else 30f, size.height / 2),
                Offset(0f, size.height / 2),
                8f,
                StrokeCap.Round
            )
        }
    }
}

suspend fun startAnimation(isDownload: Boolean, animation: Animatable<Float, AnimationVector1D>, onDownloadComplete: () -> Unit, onUploadComplete: () -> Unit) {
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
    delay(9000)
    if(isDownload) onDownloadComplete() else onUploadComplete()
}

fun Animatable<Float, AnimationVector1D>.toUiState() = UiState(
    arcValue = value,
    speed = "%.1f".format(value * 100),
    inProgress = isRunning
)

@Preview
@CompletePreviews
@Composable
private fun SuperGauge1Preview(){
    SuperGaugeViewSpeedoMeterTheme {
        SuperGauge1()
    }
}