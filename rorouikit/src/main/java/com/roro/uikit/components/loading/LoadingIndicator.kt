package com.roro.uikit.components.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

/**
 * 双弧追逐加载指示器，两条弧线一顺一逆旋转，弧长随 sin 波动形成头吃尾效果。
 */
@Composable
fun AppLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    strokeWidth: Dp = 2.5.dp,
    color: Color = AppColors.accent,
    secondaryColor: Color = color.copy(alpha = 0.35f),
) {
    val transition = rememberInfiniteTransition(label = "loading")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "t",
    )
    Canvas(modifier = modifier.size(size)) {
        val stroke = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        val sweep1 = 40f + 220f * ((sin(t * 2 * PI.toFloat()) + 1f) / 2f)
        val sweep2 = 40f + 220f * ((sin(t * 2 * PI.toFloat() + PI.toFloat()) + 1f) / 2f)
        drawArc(secondaryColor, -t * 360f + 180f, sweep2, false, style = stroke)
        drawArc(color, t * 360f, sweep1, false, style = stroke)
    }
}

/**
 * 单弧旋转加载指示器，固定弧长持续旋转。
 */
@Composable
fun AppSpinnerIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    strokeWidth: Dp = 2.5.dp,
    color: Color = AppColors.accent,
    trackColor: Color = color.copy(alpha = 0.15f),
    sweepAngle: Float = 270f,
) {
    val transition = rememberInfiniteTransition(label = "spinner")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "t",
    )
    Canvas(modifier = modifier.size(size)) {
        val stroke = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        drawArc(trackColor, 0f, 360f, false, style = Stroke(strokeWidth.toPx()))
        drawArc(color, t, sweepAngle, false, style = stroke)
    }
}

/**
 * 三点跳动加载指示器，三个圆点依次上下弹跳。
 */
@Composable
fun AppDotsIndicator(
    modifier: Modifier = Modifier,
    dotSize: Dp = 7.dp,
    color: Color = AppColors.accent,
    spacing: Dp = 5.dp,
) {
    val transition = rememberInfiniteTransition(label = "dots")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Restart),
        label = "t",
    )
    val jump = dotSize.value * 1.2f
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(spacing), verticalAlignment = Alignment.CenterVertically) {
        repeat(3) { i ->
            val phase = (t - i * 0.18f).mod(1f)
            val offset = -abs(sin(phase * PI.toFloat())) * jump
            Canvas(Modifier.size(dotSize)) {
                drawCircle(color, radius = size.minDimension / 2f, center = center.copy(y = center.y + offset))
            }
        }
    }
}
