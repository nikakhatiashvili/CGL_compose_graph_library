package com.example.cgl

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cgl.ui.theme.CGLTheme


@Composable
fun PieChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(200.dp))
        PieChart(
            modifier = Modifier
                .size(width = 300.dp, height = 300.dp)
                .align(Alignment.CenterHorizontally),
            listOf(
                PieChartSegment(Color.Red, 5f),
                PieChartSegment(Color.Green, 10f),
                PieChartSegment(Color.Blue, 15f),
                PieChartSegment(Color.Yellow, 20f),
                PieChartSegment(Color.Black, 20f)
            )
        )
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    segments: List<PieChartSegment>
) {

    Box(modifier = modifier) {
        Canvas(modifier = modifier) {
            val totalValue = segments.map { it.percentage }.sum()

            var startAngle = -90f

            segments.forEach { segment ->
                val sweepAngle = if (totalValue > 0f) {
                    (segment.percentage / totalValue) * 360f
                } else {
                    0f
                }

                drawPieChartSegment(
                    segmentColor = segment.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle
                )
                startAngle += sweepAngle
            }
        }
    }
}

fun DrawScope.drawPieChartSegment(
    segmentColor: Color,
    startAngle: Float,
    sweepAngle: Float
) {
    drawArc(
        color = segmentColor,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true
    )
}


data class PieChartSegment(val color: Color, val percentage: Float)


@Composable
@Preview(showBackground = true)
fun previewPieChar() {
    CGLTheme() {
        PieChart(segments = emptyList())
    }
}


@Composable
@Preview(showBackground = true)
fun previewPieChartScreen() {
    CGLTheme() {
        PieChartScreen()
    }
}
