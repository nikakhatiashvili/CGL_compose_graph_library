package com.example.cgl.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke

data class PieChartSegment(
    val color: Color,
    val percentage: Float,
    val gradientColors: List<Color>? = null,
    val shadow: Shadow? = null,
    val stroke: Stroke? = null,
    val imageRes: Int? = null
)