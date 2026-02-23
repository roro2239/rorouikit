package com.roro.uikit.components.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppDialogColors(
    val scrimColor: Color,
    val containerColor: Color,
)

@Immutable
data class AppDialogStyle(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
)

object AppDialogDefaults {
    @Composable
    fun colors() = AppDialogColors(
        scrimColor = Color.Black.copy(alpha = 0.4f),
        containerColor = AppColors.surface,
    )

    @Composable
    fun style() = AppDialogStyle(
        horizontalPadding = 24.dp,
        verticalPadding = 24.dp,
    )
}
