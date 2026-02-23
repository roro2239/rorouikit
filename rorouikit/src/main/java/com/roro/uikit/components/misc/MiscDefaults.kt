package com.roro.uikit.components.misc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppTagStyle(val horizontalPadding: Dp = 8.dp, val verticalPadding: Dp = 4.dp)

@Immutable
data class AppTagColors(val containerColor: Color, val contentColor: Color)

object AppTagDefaults {
    @Composable fun colors() = AppTagColors(containerColor = AppColors.accentLight, contentColor = AppColors.accent)
    fun style() = AppTagStyle()
}

@Immutable
data class AppBadgeColors(val containerColor: Color, val contentColor: Color)

object AppBadgeDefaults {
    @Composable fun defaultColors() = AppBadgeColors(AppColors.surfaceVariant, AppColors.textSecondary)
    @Composable fun successColors() = AppBadgeColors(AppColors.successLight, AppColors.success)
    @Composable fun errorColors() = AppBadgeColors(AppColors.errorLight, AppColors.error)
    @Composable fun accentColors() = AppBadgeColors(AppColors.accentLight, AppColors.accent)
}

@Immutable
data class AppSectionHeaderColors(val accentBarColor: Color, val textColor: Color)

object AppSectionHeaderDefaults {
    @Composable fun colors() = AppSectionHeaderColors(AppColors.accent, AppColors.textPrimary)
}

@Immutable
data class AppDividerStyle(val thickness: Dp = 0.5.dp, val color: Color = Color.Unspecified)

// 开关轨道和滑块颜色，禁用状态使用固定灰色而非主题色，避免视觉误导
@Immutable
data class AppSwitchColors(
    val checkedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedTrackColor: Color,
    val uncheckedThumbColor: Color,
    val disabledCheckedTrackColor: Color = Color(0xFFE8EAF0),
    val disabledCheckedThumbColor: Color = Color(0xFFB8BCCC),
    val disabledUncheckedTrackColor: Color = Color(0xFFE8EAF0),
    val disabledUncheckedThumbColor: Color = Color(0xFFB8BCCC),
)

object AppSwitchDefaults {
    @Composable
    fun colors() = AppSwitchColors(
        checkedTrackColor = AppColors.accent,
        checkedThumbColor = AppColors.surface,
        uncheckedTrackColor = AppColors.surfaceVariant,
        uncheckedThumbColor = AppColors.textSecondary,
        disabledCheckedTrackColor = AppColors.border,
        disabledCheckedThumbColor = AppColors.textTertiary,
        disabledUncheckedTrackColor = AppColors.border,
        disabledUncheckedThumbColor = AppColors.textTertiary,
    )
}

@Immutable
data class AppSwitchAnimation(
    val trackSpec: androidx.compose.animation.core.AnimationSpec<Color> = androidx.compose.animation.core.spring(dampingRatio = 0.75f, stiffness = 400f),
    val thumbColorSpec: androidx.compose.animation.core.AnimationSpec<Color> = androidx.compose.animation.core.spring(dampingRatio = 0.75f, stiffness = 400f),
    val thumbOffsetSpec: androidx.compose.animation.core.AnimationSpec<Float> = androidx.compose.animation.core.spring(dampingRatio = 0.6f, stiffness = 500f),
)

object AppSwitchAnimationDefaults {
    fun animation() = AppSwitchAnimation()
}

@Immutable
data class AppAccentPickerColors(
    val containerColor: Color,
    val inputBackgroundColor: Color,
    val titleColor: Color,
    val hexTextColor: Color,
    val hexPlaceholderColor: Color,
    val counterColor: Color,
    val hashColor: Color,
)

object AppAccentPickerDefaults {
    @Composable
    fun colors() = AppAccentPickerColors(
        containerColor = AppColors.surface,
        inputBackgroundColor = AppColors.surfaceVariant,
        titleColor = AppColors.textPrimary,
        hexTextColor = AppColors.textPrimary,
        hexPlaceholderColor = AppColors.textTertiary,
        counterColor = AppColors.textTertiary,
        hashColor = AppColors.textTertiary,
    )
}
