package com.roro.uikit.components.listitem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppListItemColors(
    val titleColor: Color,
    val subtitleColor: Color,
    val iconColor: Color,
    val trailingColor: Color,
)

@Immutable
data class AppListItemStyle(
    val horizontalPadding: Dp = 16.dp,
    val verticalPadding: Dp = 13.dp,
    val iconSize: Dp = 20.dp,
    val trailingIconSize: Dp = 16.dp,
    val spacing: Dp = 12.dp,
)

@Immutable
data class AppListGroupColors(
    val containerColor: Color,
    val dividerColor: Color,
)

object AppListItemDefaults {
    @Composable
    fun colors() = AppListItemColors(
        titleColor = AppColors.textPrimary,
        subtitleColor = AppColors.textSecondary,
        iconColor = AppColors.accent,
        trailingColor = AppColors.textTertiary,
    )
    fun style() = AppListItemStyle()

    @Composable
    fun groupColors() = AppListGroupColors(
        containerColor = AppColors.surface,
        dividerColor = AppColors.border,
    )
}
