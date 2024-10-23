# Super Garage Speedo Meter

Welcome to the **Super Garage Speedo Meter**! This project features a visually appealing speedometer that displays download and upload speeds using Kotlin's Compose framework. The speedometer utilizes Canvas and arcs to create an engaging and animated experience, complete with gradient effects to enhance the design.

# Demo

https://github.com/user-attachments/assets/5769bbb6-d490-4624-99aa-bc89e50b0eec

## Features

- **Download and Upload Speed Measurement**: Monitor your network speeds in real-time.
- **Animated Speed Display**: Smooth animations for speed changes enhance user experience.
- **Gradient Design**: Beautiful gradient effects on the speedometer for a modern look.
- **Responsive UI**: Designed with Jetpack Compose for a responsive and adaptive layout.

# Code
```kotlin
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
```

## draw Arcs
```kotlin
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
```

## Draw Lines
```kotlin
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
```

## Technologies Used

- **Kotlin**: The primary programming language for Android development.
- **Jetpack Compose**: A modern toolkit for building native Android UI.
- **Canvas API**: Used for custom drawing of the speedometer.
- **Coroutines**: For asynchronous programming to update speeds in real time.

## Installation

To get started with the Super Garage Speedo Meter, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/waqas028/Super-Gauge-View-Speedo-Meter

## 🙌 Contributions
Feel free to fork the repo and create pull requests for any improvements or additional features. We appreciate your contributions!

## Contact

For any inquiries, please contact waqaswaseem679@gmail.com.
