package com.roro.uikit.components.navbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppNavBarColors(
    val containerColor: Color,
    val activeColor: Color,
    val activeBackground: Color,
    val inactiveColor: Color,
)

@Immutable
data class AppNavBarStyle(
    val itemHeight: Dp,
    val itemHorizontalPadding: Dp,
    val iconSize: Dp,
)

object AppNavBarDefaults {
    @Composable
    fun colors() = AppNavBarColors(
        containerColor = AppColors.surface,
        activeColor = AppColors.accent,
        activeBackground = AppColors.accentLight,
        inactiveColor = AppColors.textSecondary,
    )

    @Composable
    fun style() = AppNavBarStyle(
        itemHeight = AppColors.navBarItemHeight,
        itemHorizontalPadding = 16.dp,
        iconSize = AppColors.iconSize,
    )
}

private val appSpringColor = spring<Color>(dampingRatio = 0.75f, stiffness = 400f)
private val appSpringIntOffset = spring<IntOffset>(dampingRatio = 0.85f, stiffness = 300f)

// 浮动导航栏标签展开/收缩动画参数，dampingRatio < 1 产生弹性过冲效果
@Immutable
data class AppNavBarAnimation(
    val tintSpec: AnimationSpec<Color> = appSpringColor,
    val labelEnterSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 1f, stiffness = 300f),
    val labelExpandSpec: FiniteAnimationSpec<IntSize> = spring(dampingRatio = 0.8f, stiffness = 350f),
    val labelExitSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 1f, stiffness = 500f),
    val labelShrinkSpec: FiniteAnimationSpec<IntSize> = spring(dampingRatio = 1f, stiffness = 500f),
)

object AppNavBarAnimationDefaults {
    fun animation() = AppNavBarAnimation()
}

// 滑动导航栏指示器动画：先快速压缩再弹性回弹，模拟物理弹跳感
@Immutable
data class AppSlidingNavBarAnimation(
    val tintSpec: AnimationSpec<Color> = appSpringColor,
    val indicatorXSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 0.6f, stiffness = 700f),
    val indicatorScaleCompressSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 1f, stiffness = 1800f),
    val indicatorScaleBounceSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 0.25f, stiffness = 250f),
)

object AppSlidingNavBarAnimationDefaults {
    fun animation() = AppSlidingNavBarAnimation()
}

@Immutable
data class AppNavScaffoldAnimation(
    val pageSpec: FiniteAnimationSpec<IntOffset> = appSpringIntOffset,
    val pageFadeOutSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 1f, stiffness = 400f),
)

object AppNavScaffoldAnimationDefaults {
    fun animation() = AppNavScaffoldAnimation()
}
