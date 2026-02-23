package com.roro.uikit.components.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2
import kotlinx.coroutines.launch

/**
 * 居中对话框，支持点击遮罩或返回键关闭，入场带弹性缩放动画。
 */
@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    colors: AppDialogColors = AppDialogDefaults.colors(),
    style: AppDialogStyle = AppDialogDefaults.style(),
    content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
) {
    val scale = remember { Animatable(0.85f) }
    val alpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val dismiss: () -> Unit = remember(onDismissRequest) {
        {
            scope.launch {
                launch { scale.animateTo(0.85f, spring(dampingRatio = 0.85f, stiffness = 500f)) }
                launch { alpha.animateTo(0f, spring(dampingRatio = 0.85f, stiffness = 500f)) }
            }.invokeOnCompletion { onDismissRequest() }
        }
    }

    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, spring(dampingRatio = 0.72f, stiffness = 380f)) }
        launch { alpha.animateTo(1f, spring(dampingRatio = 0.72f, stiffness = 380f)) }
    }

    BackHandler(onBack = dismiss)

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { this.alpha = alpha.value }
                .background(colors.scrimColor)
                .clickable(interactionSource = null, indication = null, onClick = dismiss)
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .graphicsLayer { this.alpha = alpha.value; scaleX = scale.value; scaleY = scale.value }
                .clip(ContinuousRoundedRectangle(AppColors.cornerRadius, AppG2))
                .background(colors.containerColor)
                .padding(horizontal = style.horizontalPadding, vertical = style.verticalPadding),
        ) {
            content(dismiss)
        }
    }
}
