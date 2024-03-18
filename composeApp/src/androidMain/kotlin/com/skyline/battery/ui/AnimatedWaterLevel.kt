package com.skyline.battery.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedWaterLevel(progress: Float, isCharging: Boolean) {
    var waterLevel by remember { mutableFloatStateOf(progress) }

    if (isCharging) {
        val infiniteTransition = rememberInfiniteTransition()
        val boilingOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    0.5f at 500
                },
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        LaunchedEffect(boilingOffset) {
            waterLevel = boilingOffset
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Canvas(
                modifier = Modifier.size(120.dp)
            ) {
                drawCircle(
                    color = Color.Blue,
                    style = Stroke(width = 5.dp.toPx()),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width / 2f
                )

                val waterHeight = size.height * waterLevel
                val waterOffset = (size.height - waterHeight) / 2
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(size.width * 0.25f, size.height - waterOffset),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.5f, waterHeight),
                )

                if (isCharging) {
                    drawCircle(
                        color = Color.Red,
                        radius = 5.dp.toPx(),
                        center = Offset(size.width / 2f, size.height / 2f + waterHeight)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = waterLevel,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

