package com.roro.uikit.components.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppBottomSheetColors(
    val scrimColor: Color,
    val containerColor: Color,
    val handleColor: Color,
)

@Immutable
data class AppBottomSheetStyle(
    val horizontalPadding: Dp,
    val bottomPadding: Dp,
    val maxHeightFraction: Float,
    val handleTopPadding: Dp,
    val handleWidth: Dp,
    val handleHeight: Dp,
    val fillNavBar: Boolean = false,
)

object AppBottomSheetDefaults {
    @Composable
    fun colors() = AppBottomSheetColors(
        scrimColor = Color.Black.copy(alpha = 0.4f),
        containerColor = AppColors.surface,
        handleColor = AppColors.textTertiary,
    )

    @Composable
    fun style() = AppBottomSheetStyle(
        horizontalPadding = 8.dp,
        bottomPadding = 8.dp,
        maxHeightFraction = 0.5f,
        handleTopPadding = 12.dp,
        handleWidth = 36.dp,
        handleHeight = 4.dp,
    )
}
