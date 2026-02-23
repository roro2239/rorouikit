package com.roro.uikit.components.tab

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)

/**
 * 顶部标签页切换栏，胶囊形滑动背景指示器，整体圆角容器。
 * 风格与 FloatingNavBar 一致：surfaceVariant 底色 + surface 色胶囊滑块。
 */
@Composable
fun AppTabBar(
    tabs: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    colors: AppTabBarColors = AppTabBarDefaults.colors(),
    style: AppTabBarStyle = AppTabBarDefaults.style(),
    animation: AppTabBarAnimation = AppTabBarDefaults.animation(),
) {
    val density = LocalDensity.current
    var tabWidths by remember { mutableStateOf(IntArray(tabs.size) { 0 }) }
    val padPx = with(density) { style.horizontalPadding.toPx() }
    val padVPx = with(density) { style.verticalPadding.toPx() }

    val indicatorX by animateFloatAsState(
        targetValue = tabWidths.take(selected).sum().toFloat() + padPx,
        animationSpec = animation.indicatorSpec,
        label = "tab_x",
    )
    val indicatorW by animateFloatAsState(
        targetValue = tabWidths.getOrElse(selected) { 0 }.toFloat(),
        animationSpec = animation.indicatorSpec,
        label = "tab_w",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(style.height)
            .background(colors.containerColor, r())
            .padding(horizontal = style.horizontalPadding, vertical = style.verticalPadding)
            .drawBehind {
                val h = size.height
                val cr = h / 2f
                drawRoundRect(
                    color = colors.indicatorColor,
                    topLeft = Offset(indicatorX - padPx, 0f),
                    size = Size(indicatorW, h),
                    cornerRadius = CornerRadius(cr),
                )
            },
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { i, tab ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(remember { MutableInteractionSource() }, null) { onSelect(i) }
                        .onSizeChanged { sz ->
                            if (tabWidths.getOrElse(i) { 0 } != sz.width) {
                                tabWidths = IntArray(tabs.size) { j ->
                                    if (j == i) sz.width else tabWidths.getOrElse(j) { 0 }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        tab,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (i == selected) colors.selectedTextColor else colors.unselectedTextColor,
                    )
                }
            }
        }
    }
}

private fun IntArray.take(n: Int): List<Int> = (0 until minOf(n, size)).map { this[it] }
private fun List<Int>.sum(): Int = fold(0) { acc, v -> acc + v }
