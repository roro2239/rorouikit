package com.roro.uikit.components.panel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppSidePanelColors(
    val containerColor: Color,
    val scrimColor: Color,
)

@Immutable
data class AppSidePanelStyle(
    val width: Dp,
    val cornerRadius: Dp,
)

object AppSidePanelDefaults {
    @Composable
    fun colors() = AppSidePanelColors(
        containerColor = AppColors.surface,
        scrimColor = Color.Black.copy(alpha = 0.32f),
    )

    @Composable
    fun style() = AppSidePanelStyle(
        width = 210.dp,
        cornerRadius = AppColors.cornerRadius,
    )
}
