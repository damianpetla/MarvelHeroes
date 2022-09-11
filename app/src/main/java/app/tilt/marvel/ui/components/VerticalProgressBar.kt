package app.tilt.marvel.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.tilt.marvel.ui.theme.MarvelHeroesTheme

@Composable
fun VerticalProgressBar(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val firstColor by transition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.onPrimary,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val secondColor by transition.animateColor(
        initialValue = MaterialTheme.colorScheme.secondary,
        targetValue = MaterialTheme.colorScheme.onSecondary,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = modifier
            .width(4.dp)
            .fillMaxHeight()
            .padding(vertical = 16.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        firstColor,
                        secondColor
                    )
                ),
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
            )
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun VerticalProgressColorPreview() {
    MarvelHeroesTheme {
        VerticalProgressBar()
    }
}
