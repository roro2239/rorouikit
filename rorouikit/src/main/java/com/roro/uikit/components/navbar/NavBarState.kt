package com.roro.uikit.components.navbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.roro.uikit.components.AppNavItem

/**
 * 导航栏运行时状态，包含各项目的动画颜色列表。
 * itemTints 在每次重组时由 rememberAppNavBarState 计算，无需手动管理。
 */
@Stable
class AppNavBarState(
    val items: List<AppNavItem>,
    val selected: Int,
    val onSelect: (Int) -> Unit,
    val itemTints: List<Color>,
)

@Composable
fun rememberAppNavBarState(
    items: List<AppNavItem>,
    selected: Int,
    onSelect: (Int) -> Unit,
    colors: AppNavBarColors = AppNavBarDefaults.colors(),
    animation: AppNavBarAnimation = AppNavBarAnimationDefaults.animation(),
): AppNavBarState {
    val tints = mutableListOf<Color>()
    for (i in items.indices) {
        tints += animateColorAsState(
            if (i == selected) colors.activeColor else colors.inactiveColor,
            animation.tintSpec, label = "nav_tint_$i",
        ).value
    }
    return AppNavBarState(items, selected, onSelect, tints)
}
