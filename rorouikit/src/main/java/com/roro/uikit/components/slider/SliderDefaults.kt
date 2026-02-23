package com.roro.uikit.components.slider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

@Immutable
data class AppSliderColors(
    val trackColor: Color,
    val activeTrackColor: Color,
    val thumbColor: Color,
)

object AppSliderDefaults {
    @Composable
    fun colors() = AppSliderColors(
        trackColor = AppColors.accentLight,
        activeTrackColor = AppColors.accent,
        thumbColor = AppColors.accent,
    )
}
