package com.roro.uikit.components.toast

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.roro.uikit.theme.AppColors

data class AppToastColors(val containerColor: Color, val textColor: Color, val iconColor: Color)

object AppToastDefaults {
    @Composable fun defaultColors() = AppToastColors(Color(0xFF1A1A1A), Color.White, Color.White)
    @Composable fun accentColors() = AppToastColors(AppColors.accent, Color.White, Color.White)
    @Composable fun successColors() = AppToastColors(AppColors.success, Color.White, Color.White)
    @Composable fun errorColors() = AppToastColors(AppColors.error, Color.White, Color.White)
    @Composable fun warningColors() = AppToastColors(AppColors.warning, Color.White, Color.White)
}
