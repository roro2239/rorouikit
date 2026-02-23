package com.roro.uikit.components.selection

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp

/**
 * 复选框，自定义 Canvas 绘制，支持选中/未选中/禁用状态，带弹性动画。
 * 选中时填充 accent 色背景并绘制对勾，未选中时仅显示描边边框。
 */
@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: AppCheckboxColors = AppCheckboxDefaults.colors(),
    style: AppCheckboxStyle = AppCheckboxDefaults.style(),
    animation: AppCheckboxAnimation = AppCheckboxDefaults.animation(),
) {
    val progress by animateFloatAsState(if (checked) 1f else 0f, animation.spec, label = "check")
    val boxColor = when {
        !enabled && checked -> colors.disabledCheckedBoxColor
        !enabled -> colors.uncheckedBoxColor
        else -> lerp(colors.uncheckedBoxColor, colors.checkedBoxColor, progress)
    }
    val borderColor = when {
        !enabled && checked -> colors.disabledCheckedBoxColor
        !enabled -> colors.disabledUncheckedBorderColor
        else -> lerp(colors.uncheckedBorderColor, colors.checkedBoxColor, progress)
    }
    val checkColor = if (!enabled) colors.disabledCheckedCheckColor else colors.checkedCheckColor

    Canvas(
        modifier = modifier
            .size(style.size)
            .then(
                if (enabled) Modifier.clickable(remember { MutableInteractionSource() }, null) { onCheckedChange(!checked) }
                else Modifier
            )
    ) {
        val stroke = style.strokeWidth.toPx()
        val r = CornerRadius(size.minDimension * 0.25f)
        drawRoundRect(boxColor, cornerRadius = r)
        drawRoundRect(borderColor, cornerRadius = r, style = Stroke(stroke))
        if (progress > 0f) {
            val w = size.width
            val h = size.height
            val p1 = Offset(w * 0.2f, h * 0.5f)
            val mid = Offset(w * 0.42f, h * 0.68f)
            val p2 = Offset(w * 0.78f, h * 0.28f)
            val seg1Len = (mid - p1).getDistance()
            val seg2Len = (p2 - mid).getDistance()
            val total = seg1Len + seg2Len
            val drawn = progress * total
            if (drawn <= seg1Len) {
                val t = drawn / seg1Len
                drawLine(checkColor, p1, lerp(p1, mid, t), stroke, androidx.compose.ui.graphics.StrokeCap.Round)
            } else {
                drawLine(checkColor, p1, mid, stroke, androidx.compose.ui.graphics.StrokeCap.Round)
                val t = (drawn - seg1Len) / seg2Len
                drawLine(checkColor, mid, lerp(mid, p2, t), stroke, androidx.compose.ui.graphics.StrokeCap.Round)
            }
        }
    }
}

/**
 * 单选按钮，Canvas 绘制外圆环和内实心圆点，带弹性缩放动画。
 */
@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: AppRadioColors = AppRadioDefaults.colors(),
    style: AppRadioStyle = AppRadioDefaults.style(),
    animation: AppRadioAnimation = AppRadioDefaults.animation(),
) {
    val progress by animateFloatAsState(if (selected) 1f else 0f, animation.spec, label = "radio")
    val ringColor = when {
        !enabled && selected -> colors.disabledSelectedColor
        !enabled -> colors.disabledUnselectedColor
        else -> lerp(colors.unselectedColor, colors.selectedColor, progress)
    }

    Canvas(
        modifier = modifier
            .size(style.size)
            .then(
                if (enabled) Modifier.clickable(remember { MutableInteractionSource() }, null) { onClick() }
                else Modifier
            )
    ) {
        val stroke = style.strokeWidth.toPx()
        drawCircle(ringColor, radius = size.minDimension / 2f - stroke / 2f, style = Stroke(stroke))
        if (progress > 0f) {
            drawCircle(ringColor, radius = style.dotSize.toPx() / 2f * progress)
        }
    }
}

private fun lerp(a: Offset, b: Offset, t: Float) = Offset(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t)
