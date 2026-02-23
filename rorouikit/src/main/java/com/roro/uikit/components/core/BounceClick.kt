package com.roro.uikit.components.core

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

// 按下时快速压缩（高刚度无弹跳），松开时带弹性回弹
private val bounceDownSpec = spring<Float>(dampingRatio = 1f, stiffness = 800f)
private val bounceUpSpec = spring<Float>(dampingRatio = 0.5f, stiffness = 600f)

/**
 * 为已有 InteractionSource 的组件附加弹性缩放动画，与组件内部的点击逻辑共享同一交互源。
 * 适用于 Button 等自带 InteractionSource 的 Material 组件。
 */
@Composable
fun Modifier.bounceClick(interactionSource: MutableInteractionSource): Modifier {
    val scale = remember { Animatable(1f) }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> scale.animateTo(0.93f, bounceDownSpec)
                is PressInteraction.Release, is PressInteraction.Cancel -> scale.animateTo(1f, bounceUpSpec)
            }
        }
    }
    return this.graphicsLayer { scaleX = scale.value; scaleY = scale.value }
}

/**
 * 为任意 Modifier 附加弹性点击效果，内部自动创建 InteractionSource。
 * 适用于 Box、Column 等无内置点击状态的布局容器。
 */
@Composable
fun Modifier.bounceClickable(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this
        .bounceClick(interactionSource)
        .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
}
