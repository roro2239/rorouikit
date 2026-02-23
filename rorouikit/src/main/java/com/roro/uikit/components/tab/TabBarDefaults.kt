package com.roro.uikit.components.tab

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppTabBarColors(
    val containerColor: Color,
    val selectedTextColor: Color,
    val unselectedTextColor: Color,
    val indicatorColor: Color,
)

@Immutable
data class AppTabBarStyle(
    val height: Dp = 44.dp,
    val horizontalPadding: Dp = 4.dp,
    val verticalPadding: Dp = 4.dp,
)

@Immutable
data class AppTabBarAnimation(
    val indicatorSpec: AnimationSpec<Float> = spring(dampingRatio = 0.7f, stiffness = 500f),
)

object AppTabBarDefaults {
    @Composable
    fun colors() = AppTabBarColors(
        containerColor = AppColors.surfaceVariant,
        selectedTextColor = AppColors.textPrimary,
        unselectedTextColor = AppColors.textSecondary,
        indicatorColor = AppColors.surface,
    )
    fun style() = AppTabBarStyle()
    fun animation() = AppTabBarAnimation()
}
