package com.roro.uikit.components.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppCardColors(
    val containerColor: Color,
    val borderColor: Color = Color.Transparent,
)

@Immutable
data class AppCardStyle(
    val paddingStart: Dp,
    val paddingTop: Dp,
    val paddingEnd: Dp,
    val paddingBottom: Dp,
) {
    constructor(all: Dp) : this(all, all, all, all)
    constructor(horizontal: Dp, vertical: Dp) : this(horizontal, vertical, horizontal, vertical)
}

/**
 * 卡片颜色、内边距默认值。
 * flat 使用纯白背景，filled 使用浅灰背景，两者均跟随主题圆角。
 */
object AppCardDefaults {
    @Composable fun flatColors() = AppCardColors(containerColor = AppColors.surface)
    @Composable fun filledColors() = AppCardColors(containerColor = AppColors.surfaceVariant)
    @Composable fun style() = AppCardStyle(all = AppColors.cardPadding)
}
