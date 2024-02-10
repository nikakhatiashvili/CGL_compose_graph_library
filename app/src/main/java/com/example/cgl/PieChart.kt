package com.example.cgl

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cgl.model.PieChartSegment
import com.example.cgl.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin



@Composable
fun PieChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(200.dp))


        val pieChartData3 = getChartData()
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Expenses", color = darkGray, fontSize = 17.sp)
                Text(text = "-639.97 ₾", color = Color.Black, fontSize = 27.sp, fontWeight = FontWeight.Bold)
            }
            PieChart(
                modifier = Modifier
                    .size(width = 300.dp, height = 270.dp),
                pieChartData3,
                30f,
                true
            )
        }

    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    segments: List<PieChartSegment>,
    thickness: Float,
    animateChart: Boolean = false
) {
    val animatedSweepAngle = remember { Animatable(0f) }

    if (animateChart) {
        LaunchedEffect(key1 = "pie_chart_animation") {
            animatedSweepAngle.snapTo(0f)
            animatedSweepAngle.animateTo(
                targetValue = 360f,
                animationSpec = tween(durationMillis = 1000, delayMillis = 0)
            )
        }
    }

    Box(modifier = modifier) {
        Canvas(modifier = modifier) {
            var startAngle = -90f
            val outerRadius = minOf(size.width, size.height) / 2f
            val innerRadius = outerRadius * (1 - thickness / 100)
            val totalValue = segments.map { it.percentage }.sum()

            segments.forEach { segment ->
                val segmentSweepAngle = if (totalValue > 0f) {
                    (segment.percentage / totalValue) * 360f
                } else {
                    0f
                }

                if (animateChart) {
                    val currentSweepAngle = minOf(
                        segmentSweepAngle,
                        animatedSweepAngle.value - (startAngle + 90f)
                    )

                    if (animatedSweepAngle.value >= startAngle + 90f) {
                        drawPieChartSegment(
                            startAngle = startAngle,
                            sweepAngle = currentSweepAngle,
                            innerRadius = innerRadius,
                            outerRadius = outerRadius,
                            segment = segment,
                            imageBitmap = segment.imageRes
                        )
                    }
                } else {
                    drawPieChartSegment(
                        startAngle = startAngle,
                        sweepAngle = segmentSweepAngle,
                        innerRadius = innerRadius,
                        outerRadius = outerRadius,
                        segment = segment,
                        imageBitmap = segment.imageRes
                    )
                }

                startAngle += segmentSweepAngle
            }
        }
    }
}

fun DrawScope.drawPieChartSegment(
    segment: PieChartSegment,
    startAngle: Float,
    sweepAngle: Float,
    innerRadius: Float,
    outerRadius: Float,
    imageBitmap:ImageBitmap? = null
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

    val midAngle = startAngle + sweepAngle / 2
    val midAngleRadians = Math.toRadians(midAngle.toDouble())

    val iconRadius = (innerRadius + outerRadius) / 2
    val iconX = (size.width / 2 + iconRadius * cos(midAngleRadians)).toFloat()
    val iconY = (size.height / 2 + iconRadius * sin(midAngleRadians)).toFloat()

    imageBitmap?.let {
        val imageSize = Size(24.dp.toPx(), 24.dp.toPx())
        val imageTopLeft = Offset(iconX - imageSize.width / 2, iconY - imageSize.height / 2)
        drawImage(
            image = it,
            topLeft = imageTopLeft,
            alpha = 1.0f
        )
    }
}

@Composable
fun getChartData(): List<PieChartSegment> {
    val density = LocalDensity.current
    val imageBitmap = vectorResourceToImageBitmap(R.drawable.transaction_svgrepo_com, density)
    val shop = vectorResourceToImageBitmap(R.drawable.shopping_cart_02_svgrepo_com, density)
    val bag = vectorResourceToImageBitmap(R.drawable.shopping_bag_svgrepo_com, density)
    val fork = vectorResourceToImageBitmap(R.drawable.fork_and_knife_combination_svgrepo_com, density)
    val edu = vectorResourceToImageBitmap(R.drawable.education_cap_svgrepo_com, density)
    val bills = vectorResourceToImageBitmap(R.drawable.house_svgrepo_com, density)
    return         listOf(
        PieChartSegment(color = Color.Black, percentage = 46f, imageRes = imageBitmap),
        PieChartSegment(color = cyan, percentage = 16f,imageRes = shop),
        PieChartSegment(color = lightGreen, percentage = 10f, imageRes = fork),
        PieChartSegment(color = yellow, percentage = 9f, imageRes = bag),
        PieChartSegment(color = darkPink, percentage = 8f, imageRes = edu),
        PieChartSegment(color = orange, percentage = 6f, imageRes = bills),
        PieChartSegment(color = gray, percentage = 2f),
        PieChartSegment(color = lightYellow, percentage = 1.5f),
        PieChartSegment(color = Color.Magenta, percentage = 1f),
    )

}

@Composable
fun vectorResourceToImageBitmap(
    resId: Int,
    density: Density
): ImageBitmap {
    val context = LocalContext.current
    val drawable = context.resources.getDrawable(resId, context.theme)
    val width = density.run { drawable.intrinsicWidth.dp.toPx() }
    val height = density.run { drawable.intrinsicHeight.dp.toPx() }

    val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap.asImageBitmap()
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
