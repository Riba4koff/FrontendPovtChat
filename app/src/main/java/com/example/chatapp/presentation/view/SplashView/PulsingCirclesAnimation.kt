package com.example.chatapp.presentation.view.SplashView

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chatapp.ui.theme.Purple200


@Composable
fun PulsingCirclesAnimation(
    modifier: Modifier = Modifier,
    circleColor: Color = Purple200,
    circleSize: Dp = 24.dp,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val firstScale by rememberInfiniteState(
        transition = infiniteTransition,
        delay = 0
    )
    val secondScale by rememberInfiniteState(
        transition = infiniteTransition,
        delay = 500
    )
    val thirdScale by rememberInfiniteState(
        transition = infiniteTransition,
        delay = 1000
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Circle(scale = firstScale, color = circleColor)
        Circle(scale = secondScale, color = circleColor)
        Circle(scale = thirdScale, color = circleColor)
    }
}

@Composable
fun Circle(
    circleSize: Dp = 24.dp,
    scale: Float,
    color: Color
) {
    Spacer(
        Modifier
            .size(circleSize)
            .scale(scale)
            .background(
                color = color,
                shape = CircleShape
            )
    )
}


@Composable
fun rememberInfiniteState(
    transition: InfiniteTransition,
    delay: Int,
): State<Float> {
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0f at delay with LinearEasing
                1f at delay + 500 with LinearEasing
                0f at delay + 1000
            }
        )
    )
}