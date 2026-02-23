package com.roro.uikit.components.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color = Color.Transparent,
    val disabledContainerColor: Color = Color(0xFFE8EAF0),
    val disabledContentColor: Color = Color(0xFFB8BCCC),
    val disabledBorderColor: Color = Color.Transparent,
)

@Immutable
data class AppButtonStyle(
    val height: Dp,
    val horizontalPadding: Dp,
    val textStyle: TextStyle,
    val iconSize: Dp = 18.dp,
    val iconSpacing: Dp = 6.dp,
    val borderWidth: Dp = 1.5.dp,
)

/**
 * 三种按钮变体的颜色、尺寸和动画默认值。
 * 所有 @Composable 方法在调用时从 LocalAppTheme 读取当前主题色，支持运行时主题切换。
 */
object AppButtonDefaults {
    @Composable
    fun primaryColors() = AppButtonColors(
        containerColor = AppColors.textPrimary,
        contentColor = AppColors.background,
        disabledContainerColor = AppColors.surfaceVariant,
        disabledContentColor = AppColors.textTertiary,
    )

    @Composable
    fun outlineColors() = AppButtonColors(
        containerColor = Color.Transparent,
        contentColor = AppColors.textPrimary,
        borderColor = AppColors.textPrimary,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = AppColors.textTertiary,
        disabledBorderColor = AppColors.border,
    )

    @Composable
    fun accentColors() = AppButtonColors(
        containerColor = AppColors.accent,
        contentColor = Color.White,
        disabledContainerColor = AppColors.surfaceVariant,
        disabledContentColor = AppColors.textTertiary,
    )

    @Composable
    fun style() = AppButtonStyle(
        height = AppColors.buttonHeight,
        horizontalPadding = AppColors.buttonHorizontalPadding,
        textStyle = MaterialTheme.typography.labelLarge,
        iconSize = AppColors.iconSize,
    )
}
