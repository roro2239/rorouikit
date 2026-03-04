package com.roro.uikit.components.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppTopBarColors(
    val backgroundColor: Color,
    val contentColor: Color,
)

@Immutable
data class AppTopBarStyle(
    val height: Dp,
)

object AppTopBarDefaults {
    @Composable
    fun colors() = AppTopBarColors(
        backgroundColor = AppColors.background,
        contentColor = AppColors.textPrimary,
    )

    @Composable
    fun style() = AppTopBarStyle(
        height = 56.dp,
    )
}