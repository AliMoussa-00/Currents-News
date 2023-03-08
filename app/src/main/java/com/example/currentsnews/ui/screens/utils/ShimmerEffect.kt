package com.example.currentsnews.ui.screens.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.currentsnews.ui.theme.CurrentsNewsTheme

@Composable
fun ShimmerList(){
    repeat(10){
        ShimmerListItem()
    }
}
@Composable
fun ShimmerListItem(modifier: Modifier = Modifier){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        elevation = 2.dp
    ) {
        Row {
           Box(
               modifier = Modifier
                   .size(120.dp)
                   .shimmerEffect()
           )

            Column(
                modifier= modifier
                    .height(120.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

fun Modifier.shimmerEffect() : Modifier = composed {
    var size by remember{ mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition()

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2* size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(0.2f),
                Color.LightGray.copy(0.9f),
                Color.LightGray.copy(0.2f),
            ),
            start = Offset(startOffsetX,0f),
            end = Offset(startOffsetX + size.width.toFloat() ,size.height.toFloat() )
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewShimmer(){
    CurrentsNewsTheme {
        ShimmerList()
    }
}