package com.roro.uikit.components.segmented

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)

/**
 * 分段选择器，胶囊滑块指示选中项，带阴影，风格与 AppTabBar 一致但更紧凑。
 */
@Composable
fun AppSegmentedControl(
    options: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AppColors.surfaceVariant,
    indicatorColor: Color = AppColors.surface,
    selectedTextColor: Color = AppColors.textPrimary,
    unselectedTextColor: Color = AppColors.textSecondary,
) {
    val density = LocalDensity.current
    var widths by remember { mutableStateOf(IntArray(options.size) { 0 }) }
    val pad = with(density) { 3.dp.toPx() }

    val indicatorX by animateFloatAsState(
        targetValue = widths.take(selected).sum().toFloat() + pad,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f),
        label = "seg_x",
    )
    val indicatorW by animateFloatAsState(
        targetValue = widths.getOrElse(selected) { 0 }.toFloat(),
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 500f),
        label = "seg_w",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(containerColor, r())
            .padding(3.dp)
            .drawBehind {
                val h = size.height
                val cr = h / 2f
                drawRoundRect(
                    color = indicatorColor,
                    topLeft = Offset(indicatorX - pad, 0f),
                    size = Size(indicatorW, h),
                    cornerRadius = CornerRadius(cr),
                )
            },
    ) {
        Row(Modifier.fillMaxWidth()) {
            options.forEachIndexed { i, label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(remember { MutableInteractionSource() }, null) { onSelect(i) }
                        .onSizeChanged { sz ->
                            if (widths.getOrElse(i) { 0 } != sz.width) {
                                widths = IntArray(options.size) { j ->
                                    if (j == i) sz.width else widths.getOrElse(j) { 0 }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (i == selected) selectedTextColor else unselectedTextColor,
                    )
                }
            }
        }
    }
}

private fun IntArray.take(n: Int): List<Int> = (0 until minOf(n, size)).map { this[it] }
private fun List<Int>.sum(): Int = fold(0) { acc, v -> acc + v }
