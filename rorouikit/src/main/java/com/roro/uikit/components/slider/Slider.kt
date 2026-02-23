package com.roro.uikit.components.slider

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * 滑动条，支持拖拽和点击，thumb 带阴影，弹性动画。
 */
@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    colors: AppSliderColors = AppSliderDefaults.colors(),
    trackHeight: androidx.compose.ui.unit.Dp = 4.dp,
) {
    val thumbSize = trackHeight * 5f
    val p by animateFloatAsState(
        targetValue = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "slider",
    )
    var trackSize by remember { mutableStateOf(IntSize.Zero) }

    fun offsetToValue(x: Float): Float {
        val fraction = (x / trackSize.width).coerceIn(0f, 1f)
        return valueRange.start + fraction * (valueRange.endInclusive - valueRange.start)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbSize)
            .pointerInput(onValueChange) {
                detectTapGestures { offset -> onValueChange(offsetToValue(offset.x)) }
            }
            .pointerInput(onValueChange) {
                detectHorizontalDragGestures { change, _ -> onValueChange(offsetToValue(change.position.x)) }
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center)
                .onSizeChanged { trackSize = it },
        ) {
            val r = CornerRadius(size.height / 2f)
            drawRoundRect(colors.trackColor, cornerRadius = r)
            drawRoundRect(colors.activeTrackColor, size = size.copy(width = size.width * p), cornerRadius = r)
        }
        Box(Modifier.fillMaxWidth(p).height(thumbSize), contentAlignment = Alignment.CenterEnd) {
            Box(
                Modifier
                    .size(thumbSize)
                    .shadow(4.dp, androidx.compose.foundation.shape.CircleShape)
                    .drawBehind { drawCircle(colors.thumbColor) }
            )
        }
    }
}
