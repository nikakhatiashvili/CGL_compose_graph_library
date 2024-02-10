package com.example.cgl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cgl.ui.theme.CGLTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.nativeCanvas
import com.example.cgl.model.LineChartDataPoint
import kotlin.math.roundToInt

@Composable
fun LineChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(200.dp))

        val dataPoints = listOf(
            LineChartDataPoint(time = 1, value = 50000f),
            LineChartDataPoint(time = 2, value = 48000f),
            LineChartDataPoint(time = 3, value = 51000f),
            LineChartDataPoint(time = 4, value = 53000f),
            LineChartDataPoint(time = 5, value = 52000f)
        )
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            LineChart(
                modifier = Modifier
                    .size(width = 300.dp, height = 300.dp),
                dataPoints = dataPoints
            )
        }

    }
}

@Composable
fun LineChart(
    dataPoints: List<LineChartDataPoint>,
    modifier: Modifier = Modifier,
) {
    val times = dataPoints.map { it.time.toFloat() }
    val values = dataPoints.map { it.value }
    val minValue = values.minOrNull() ?: 0f
    val maxValue = values.maxOrNull() ?: 0f
    val padding = (maxValue - minValue) * 0.10f
    val yAxisMin = minValue - padding
    val yAxisMax = maxValue + padding

    val labelValues = calculateLabelValues(yAxisMin, yAxisMax, 6)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val stepX = width / (times.maxOrNull() ?: (1f - 1f))

        val labelXPosition = 10f
        val axisLineXPosition = labelXPosition + 100f
        labelValues.forEach { value ->
            val yPosition = (1f - (value - yAxisMin) / (yAxisMax - yAxisMin)) * height
            drawContext.canvas.nativeCanvas.drawText(
                "$value",
                labelXPosition,
                yPosition,
                android.graphics.Paint().apply {
                    textSize = 30f
                    color = android.graphics.Color.BLACK
                }
            )
        }

        drawLine(
            color = Color.Black,
            start = Offset(x = axisLineXPosition, y = 0f),
            end = Offset(x = axisLineXPosition, y = height),
            strokeWidth = Stroke.DefaultMiter
        )

        val points = dataPoints.map { dataPoint ->
            val x = (dataPoint.time - 1) * stepX + axisLineXPosition + 10f
            val y = (1f - (dataPoint.value - yAxisMin) / (yAxisMax - yAxisMin)) * height
            Offset(x, y)
        }

        for (i in 0 until points.size - 1) {
            drawLine(
                start = points[i],
                end = points[i + 1],
                color = Color.Blue,
                strokeWidth = Stroke.DefaultMiter
            )
        }
    }
}

fun calculateLabelValues(min: Float, max: Float, numLabels: Int): List<Int> {
    val range = max - min
    val step = range / (numLabels - 1)
    return List(numLabels) { index ->
        ((min + step * index) / 1000).roundToInt() * 1000
    }
}


@Composable
@Preview(showBackground = true)
fun previewLineChartScreen() {
    CGLTheme() {
        LineChartScreen()
    }
}
