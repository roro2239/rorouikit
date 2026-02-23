package com.roro.uikit.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Composable
fun AppProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    onProgressChange: ((Float) -> Unit)? = null,
    color: Color = AppColors.accent,
    trackColor: Color = AppColors.accentLight,
    height: Dp = 4.dp,
) {
    val thumbSize = height * 1.68f
    val containerH = thumbSize
    val p by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "progress",
    )
    var trackSize by remember { mutableStateOf(IntSize.Zero) }

    fun offsetToProgress(x: Float) =
        (x / trackSize.width).coerceIn(0f, 1f)

    val interactionModifier = if (onProgressChange != null) {
        Modifier
            .pointerInput(onProgressChange) {
                detectTapGestures { offset -> onProgressChange(offsetToProgress(offset.x)) }
            }
            .pointerInput(onProgressChange) {
                detectHorizontalDragGestures { change, _ ->
                    onProgressChange(offsetToProgress(change.position.x))
                }
            }
    } else Modifier

    Box(
        modifier = modifier.fillMaxWidth().height(containerH).then(interactionModifier),
        contentAlignment = Alignment.CenterStart,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .align(Alignment.Center)
                .onSizeChanged { trackSize = it },
        ) {
            val r = CornerRadius(size.height / 2f)
            drawRoundRect(trackColor, cornerRadius = r)
            drawRoundRect(color, size = size.copy(width = size.width * p), cornerRadius = r)
        }
        Box(
            Modifier.fillMaxWidth(p).height(containerH),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Box(
                Modifier
                    .size(thumbSize)
                    .alpha(if (p > 0f) 1f else 0f)
                    .drawBehind { drawCircle(color) }
            )
        }
    }
}
