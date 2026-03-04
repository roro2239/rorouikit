package com.roro.uikit.components.panel

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class AppSidePanelSide { Start, End }

/**
 * 侧滑面板，支持左右方向、遮罩点击/返回键/拖拽关闭。
 */
@Composable
fun AppSidePanel(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    side: AppSidePanelSide = AppSidePanelSide.Start,
    colors: AppSidePanelColors = AppSidePanelDefaults.colors(),
    style: AppSidePanelStyle = AppSidePanelDefaults.style(),
    content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
) {
    val density = LocalDensity.current
    val widthPx = with(density) { style.width.toPx().coerceAtLeast(1f) }
    val closedOffset = if (side == AppSidePanelSide.Start) -widthPx else widthPx
    val offsetX = remember(widthPx, side) { Animatable(closedOffset) }
    val scope = rememberCoroutineScope()

    val dismiss: () -> Unit = remember(onDismissRequest, closedOffset) {
        {
            scope.launch {
                offsetX.animateTo(closedOffset, spring(dampingRatio = 0.85f, stiffness = 500f))
                onDismissRequest()
            }
        }
    }

    LaunchedEffect(widthPx, side) {
        offsetX.snapTo(closedOffset)
        offsetX.animateTo(0f, spring(dampingRatio = 0.78f, stiffness = 420f))
    }

    BackHandler(onBack = dismiss)

    val progress = (1f - abs(offsetX.value) / widthPx).coerceIn(0f, 1f)
    val shape = if (side == AppSidePanelSide.Start) {
        RoundedCornerShape(topEnd = style.cornerRadius, bottomEnd = style.cornerRadius)
    } else {
        RoundedCornerShape(topStart = style.cornerRadius, bottomStart = style.cornerRadius)
    }

    val panelAlignment = if (side == AppSidePanelSide.Start) Alignment.CenterStart else Alignment.CenterEnd
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = progress }
                .background(colors.scrimColor)
                .clickable(interactionSource = null, indication = null, onClick = dismiss)
        )
        Column(
            modifier = modifier
                .width(style.width)
                .fillMaxSize()
                .align(panelAlignment)
                .graphicsLayer { translationX = offsetX.value }
                .clip(shape)
                .background(colors.containerColor)
                .pointerInput(side, widthPx) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val shouldDismiss = progress < 0.6f
                            if (shouldDismiss) {
                                dismiss()
                            } else {
                                scope.launch {
                                    offsetX.animateTo(0f, spring(dampingRatio = 0.78f, stiffness = 420f))
                                }
                            }
                        },
                    ) { _, dragAmount ->
                        scope.launch {
                            val next = (offsetX.value + dragAmount).coerceIn(
                                if (side == AppSidePanelSide.Start) closedOffset else 0f,
                                if (side == AppSidePanelSide.Start) 0f else closedOffset,
                            )
                            offsetX.snapTo(next)
                        }
                    }
                },
            horizontalAlignment = if (side == AppSidePanelSide.Start) Alignment.Start else Alignment.End,
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                content(dismiss)
            }
        }
    }
}
