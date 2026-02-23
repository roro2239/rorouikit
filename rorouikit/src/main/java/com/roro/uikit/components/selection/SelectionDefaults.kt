package com.roro.uikit.components.selection

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppCheckboxColors(
    val checkedBoxColor: Color,
    val checkedCheckColor: Color,
    val uncheckedBoxColor: Color,
    val uncheckedBorderColor: Color,
    val disabledCheckedBoxColor: Color,
    val disabledCheckedCheckColor: Color,
    val disabledUncheckedBorderColor: Color,
)

@Immutable
data class AppCheckboxStyle(val size: Dp = 20.dp, val strokeWidth: Dp = 2.dp)

@Immutable
data class AppCheckboxAnimation(val spec: AnimationSpec<Float> = spring(dampingRatio = 0.6f, stiffness = 500f))

object AppCheckboxDefaults {
    @Composable
    fun colors() = AppCheckboxColors(
        checkedBoxColor = AppColors.accent,
        checkedCheckColor = Color.White,
        uncheckedBoxColor = Color.Transparent,
        uncheckedBorderColor = AppColors.border,
        disabledCheckedBoxColor = AppColors.border,
        disabledCheckedCheckColor = AppColors.textTertiary,
        disabledUncheckedBorderColor = AppColors.border,
    )
    fun style() = AppCheckboxStyle()
    fun animation() = AppCheckboxAnimation()
}

@Immutable
data class AppRadioColors(
    val selectedColor: Color,
    val unselectedColor: Color,
    val disabledSelectedColor: Color,
    val disabledUnselectedColor: Color,
)

@Immutable
data class AppRadioStyle(val size: Dp = 20.dp, val dotSize: Dp = 8.dp, val strokeWidth: Dp = 2.dp)

@Immutable
data class AppRadioAnimation(val spec: AnimationSpec<Float> = spring(dampingRatio = 0.6f, stiffness = 500f))

object AppRadioDefaults {
    @Composable
    fun colors() = AppRadioColors(
        selectedColor = AppColors.accent,
        unselectedColor = AppColors.border,
        disabledSelectedColor = AppColors.border,
        disabledUnselectedColor = AppColors.border,
    )
    fun style() = AppRadioStyle()
    fun animation() = AppRadioAnimation()
}
