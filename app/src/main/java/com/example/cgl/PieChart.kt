package com.example.cgl

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cgl.ui.theme.CGLTheme


@Composable
fun PieChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(200.dp))
        PieChart(
            modifier = Modifier
                .size(width = 200.dp, height = 200.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PieChart(modifier:Modifier = Modifier){

    Box(modifier = modifier){
        Canvas(modifier = modifier) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val center = Offset(x = size.width / 2, y = size.height / 2)

            val radius = minOf(size.width, size.height) / 2
            drawCircle(
                color = Color.Blue,
                center = center,
                radius = radius
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun previewPieChar(){
    CGLTheme() {
        PieChart()
    }
}


@Composable
@Preview(showBackground = true)
fun previewPieChartScreen(){
    CGLTheme() {
        PieChartScreen()
    }
}
