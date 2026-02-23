package com.roro.uikit.components.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class AppSwipeActionItem(
    val label: String,
    val icon: ImageVector,
    val color: Color,
    val onAction: () -> Unit,
)

/**
 * 滑动操作容器，向左滑动露出操作按钮，松手自动回弹或触发操作。
 */
@Composable
fun AppSwipeAction(
    actions: List<AppSwipeActionItem>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val r = LocalAppTheme.current.cornerRadius
    val contentShape = ContinuousRoundedRectangle(r, AppG2)
    val actionWidthDp = 72.dp
    val density = LocalDensity.current
    val actionWidthPx = with(density) { actionWidthDp.toPx() }
    val maxOffsetPx = -(actionWidthPx * actions.size)
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.clip(contentShape)) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.End,
        ) {
            actions.forEachIndexed { i, action ->
                val isLast = i == actions.lastIndex
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(actionWidthDp)
                        .then(if (isLast) Modifier.clip(RoundedCornerShape(topEnd = r, bottomEnd = r)) else Modifier)
                        .background(action.color)
                        .clickable {
                            action.onAction()
                            scope.launch { offsetX.animateTo(0f, spring(dampingRatio = 0.7f, stiffness = 400f)) }
                        }
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(action.icon, null, tint = Color.White)
                    Text(action.label, style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .clip(contentShape)
                .pointerInput(maxOffsetPx) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                val target = if (offsetX.value < maxOffsetPx * 0.4f) maxOffsetPx else 0f
                                offsetX.animateTo(target, spring(dampingRatio = 0.7f, stiffness = 400f))
                            }
                        }
                    ) { _, dragAmount ->
                        scope.launch {
                            offsetX.snapTo((offsetX.value + dragAmount).coerceIn(maxOffsetPx, 0f))
                        }
                    }
                },
        ) { content() }
    }
}
