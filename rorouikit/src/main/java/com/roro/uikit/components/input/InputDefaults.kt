package com.roro.uikit.components.input

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.roro.uikit.theme.AppColors

/**
 * 输入框全状态颜色配置，覆盖聚焦、未聚焦、错误、禁用四种状态下的边框、标签、图标和文字颜色。
 * 通过 resolveTextFieldColors 映射到 Material3 OutlinedTextFieldDefaults.colors。
 */
@Immutable
data class AppTextFieldColors(
    val focusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val containerColor: Color,
    val focusedLabelColor: Color,
    val unfocusedLabelColor: Color,
    val cursorColor: Color,
    val textColor: Color,
    val placeholderColor: Color,
    val focusedIconColor: Color,
    val unfocusedIconColor: Color,
    val errorBorderColor: Color,
    val errorLabelColor: Color,
    val errorTextColor: Color,
    val disabledBorderColor: Color,
    val disabledTextColor: Color,
    val disabledLabelColor: Color,
)

object AppTextFieldDefaults {
    @Composable
    fun colors() = AppTextFieldColors(
        focusedBorderColor = AppColors.accent,
        unfocusedBorderColor = AppColors.border,
        containerColor = AppColors.surface,
        focusedLabelColor = AppColors.accent,
        unfocusedLabelColor = AppColors.textSecondary,
        cursorColor = AppColors.accent,
        textColor = AppColors.textPrimary,
        placeholderColor = AppColors.textTertiary,
        focusedIconColor = AppColors.accent,
        unfocusedIconColor = AppColors.textSecondary,
        errorBorderColor = AppColors.error,
        errorLabelColor = AppColors.error,
        errorTextColor = AppColors.textPrimary,
        disabledBorderColor = AppColors.border,
        disabledTextColor = AppColors.textTertiary,
        disabledLabelColor = AppColors.textTertiary,
    )
}

@Immutable
data class AppSearchBarAnimation(
    val expandSpec: AnimationSpec<Float> = spring(dampingRatio = 0.8f, stiffness = 350f),
)

object AppSearchBarAnimationDefaults {
    fun animation() = AppSearchBarAnimation()
}
