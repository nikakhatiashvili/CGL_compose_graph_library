package com.example.cgl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cgl.ui.theme.CGLTheme


@Composable
fun PieChartScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("asdasd")
    }
}

fun pieChart(){

}

@Composable
@Preview(showBackground = true)
fun previewPieChartScreen(){
    CGLTheme() {
        PieChartScreen()
    }
}
