package app.tilt.marvel.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .placeholder(
                visible = true,
                color = Color.Gray,
                shape = CardDefaults.shape,
                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
            )
    )
}
