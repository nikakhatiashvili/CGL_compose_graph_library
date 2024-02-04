package com.example.cgl

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import com.example.cgl.ui.theme.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Rect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cgl.model.PieChartSegment
import com.example.cgl.ui.theme.CGLTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(200.dp))


        val pieChartData3 = listOf(
            PieChartSegment(color = red, percentage = 46f),
            PieChartSegment(color = cyan, percentage = 16f),
            PieChartSegment(color = lightGreen, percentage = 10f),
            PieChartSegment(color = yellow, percentage = 9f),
            PieChartSegment(color = darkPink, percentage = 8f),
            PieChartSegment(color = orange, percentage = 6f),
            PieChartSegment(color = gray, percentage = 2f),
            PieChartSegment(color = lightYellow, percentage = 1.5f),
            PieChartSegment(color = lightGray, percentage = 1f),
        )

        PieChart(
            modifier = Modifier
                .size(width = 300.dp, height = 300.dp)
                .align(Alignment.CenterHorizontally),
            pieChartData3,
            25f
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
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    innerRadius = innerRadius,
                    outerRadius = outerRadius,
                    segment = segment,
                )
                startAngle += sweepAngle
            }
        }
    }
}

fun DrawScope.drawPieChartSegment(
    segment: PieChartSegment,
    startAngle: Float,
    sweepAngle: Float,
    innerRadius: Float,
    outerRadius: Float
) {
    val actualStartAngle = startAngle
    val actualSweepAngle = if (sweepAngle == 360f) 359.999f else sweepAngle

    val outerRect = Rect(
        Offset(size.width / 2 - outerRadius, size.height / 2 - outerRadius),
        Size(outerRadius * 2, outerRadius * 2)
    )
    val innerRect = Rect(
        Offset(size.width / 2 - innerRadius, size.height / 2 - innerRadius),
        Size(innerRadius * 2, innerRadius * 2)
    )

    val path = Path().apply {
        arcTo(outerRect, actualStartAngle, actualSweepAngle, false)

        val angle = Math.toRadians((actualStartAngle + actualSweepAngle).toDouble()).toFloat()
        lineTo(
            (size.width / 2 + innerRadius * cos(angle)),
            (size.height / 2 + innerRadius * sin(angle))
        )

        arcTo(innerRect, actualStartAngle + actualSweepAngle, -actualSweepAngle, false)

        close()
    }

    val brush = when {
        segment.gradientColors.isNullOrEmpty() -> SolidColor(segment.color)
        segment.gradientColors.size == 1 -> SolidColor(segment.gradientColors.first())
        else -> {
            val gradientStartAngle = Math.toRadians(actualStartAngle.toDouble()).toFloat()
            val gradientEndAngle =
                Math.toRadians((actualStartAngle + actualSweepAngle).toDouble()).toFloat()
            Brush.linearGradient(
                colors = segment.gradientColors,
                start = Offset(
                    (size.width / 2 + outerRadius * cos(gradientStartAngle)),
                    (size.height / 2 + outerRadius * sin(gradientStartAngle))
                ),
                end = Offset(
                    (size.width / 2 + outerRadius * cos(gradientEndAngle)),
                    (size.height / 2 + outerRadius * sin(gradientEndAngle))
                )
            )
        }
    }

    drawPath(path = path, brush = brush)
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
