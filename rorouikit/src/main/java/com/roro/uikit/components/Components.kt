package com.roro.uikit.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.kyant.backdrop.backdrops.emptyBackdrop

val LocalBackdrop = staticCompositionLocalOf { emptyBackdrop() }
val LocalOverlay = staticCompositionLocalOf<MutableState<(@Composable () -> Unit)?>> { mutableStateOf(null) }

// 导航项数据模型
data class AppNavItem(val label: String, val icon: ImageVector)
