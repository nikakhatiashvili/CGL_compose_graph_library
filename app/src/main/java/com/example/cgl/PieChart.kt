package com.example.cgl

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Rect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cgl.model.PieChartSegment
import com.example.cgl.ui.theme.CGLTheme
import java.lang.Math.cos
import java.lang.Math.sin

@Composable
fun PieChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(200.dp))
        PieChart(
            modifier = Modifier
                .size(width = 300.dp, height = 300.dp)
                .align(Alignment.CenterHorizontally),
            listOf(
                PieChartSegment(Color.Red, 40f),
                PieChartSegment(Color.Green, 20f),
                PieChartSegment(Color.Blue, 40f)
            ),
            30f
        )
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    segments: List<PieChartSegment>,
    thickness: Float
) {

    Box(modifier = modifier) {
        Canvas(modifier = modifier) {
            val totalValue = segments.map { it.percentage }.sum()

            var startAngle = -90f
            val outerRadius = minOf(size.width, size.height) / 2f
            val innerRadius = outerRadius * (1 - thickness / 100)

            segments.forEach { segment ->
                val sweepAngle = if (totalValue > 0f) {
                    (segment.percentage / totalValue) * 360f
                } else {
                    0f
                }

                drawPieChartSegment(
                    segmentColor = segment.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    innerRadius = innerRadius,
                    outerRadius = outerRadius
                )
                startAngle += sweepAngle
            }
        }
    }
}

fun DrawScope.drawPieChartSegment(
    segmentColor: Color,
    startAngle: Float,
    sweepAngle: Float,
    innerRadius: Float,
    outerRadius: Float
) {
    val path = Path().apply {
        moveTo(
            x = (size.width / 2 + outerRadius * cos(Math.toRadians(startAngle.toDouble()))).toFloat(),
            y = (size.height / 2 + outerRadius * sin(Math.toRadians(startAngle.toDouble()))).toFloat()
        )

        arcTo(
            rect = Rect(
                Offset(size.width / 2 - outerRadius, size.height / 2 - outerRadius),
                Size(outerRadius * 2, outerRadius * 2)
            ),
            startAngleDegrees = startAngle,
            sweepAngleDegrees = sweepAngle,
            forceMoveTo = false
        )

        lineTo(
            x = (size.width / 2 + innerRadius * cos(Math.toRadians(startAngle + sweepAngle.toDouble()))).toFloat(),
            y = (size.height / 2 + innerRadius * sin(Math.toRadians(startAngle + sweepAngle.toDouble()))).toFloat()
        )

        arcTo(
            rect = Rect(
                Offset(size.width / 2 - innerRadius, size.height / 2 - innerRadius),
                Size(innerRadius * 2, innerRadius * 2)
            ),
            startAngleDegrees = startAngle + sweepAngle,
            sweepAngleDegrees = -sweepAngle,
            forceMoveTo = false
        )

        close()
    }

    drawPath(path = path, color = segmentColor)
}

@Composable
@Preview(showBackground = true)
fun previewPieChar() {
    CGLTheme() {
        PieChart(segments = emptyList(), thickness = 30f)
    }
}


@Composable
@Preview(showBackground = true)
fun previewPieChartScreen() {
    CGLTheme() {
        PieChartScreen()
    }
}
