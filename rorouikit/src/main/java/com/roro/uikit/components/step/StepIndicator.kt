package com.roro.uikit.components.step

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.roro.uikit.theme.AppColors

/**
 * 步骤条，连接线带前进动画，圆点颜色和缩放动画过渡。
 */
@Composable
fun AppStepIndicator(
    steps: List<String>,
    current: Int,
    modifier: Modifier = Modifier,
) {
    val accent = AppColors.accent
    val accentLight = AppColors.accentLight
    val border = AppColors.border
    val surface = AppColors.surface

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        steps.forEachIndexed { i, label ->
            val done = i < current
            val active = i == current

            val dotColor by animateColorAsState(
                targetValue = if (done || active) accent else accentLight,
                animationSpec = spring(stiffness = 400f),
                label = "dot_$i",
            )
            val leftProgress by animateFloatAsState(
                targetValue = if (done) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.9f, stiffness = 300f),
                label = "left_$i",
            )
            val rightProgress by animateFloatAsState(
                targetValue = if (i < current) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.9f, stiffness = 300f),
                label = "right_$i",
            )
            val scale by animateFloatAsState(
                targetValue = if (active) 1.2f else 1f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
                label = "scale_$i",
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // 线条层：用 Canvas 精确绘制，避免 fillMaxWidth fraction 的坐标系问题
                    Row(
                        modifier = Modifier.fillMaxWidth().height(28.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (i > 0) {
                            Canvas(Modifier.weight(1f).height(2.dp)) {
                                drawLine(border, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), 2.dp.toPx())
                                drawLine(accent, Offset(0f, size.height / 2), Offset(size.width * leftProgress, size.height / 2), 2.dp.toPx())
                            }
                        } else {
                            Box(Modifier.weight(1f))
                        }
                        Box(Modifier.size(28.dp))
                        if (i < steps.lastIndex) {
                            Canvas(Modifier.weight(1f).height(2.dp)) {
                                drawLine(border, Offset(0f, size.height / 2), Offset(size.width, size.height / 2), 2.dp.toPx())
                                drawLine(accent, Offset(0f, size.height / 2), Offset(size.width * rightProgress, size.height / 2), 2.dp.toPx())
                            }
                        } else {
                            Box(Modifier.weight(1f))
                        }
                    }
                    // 圆点层
                    Box(
                        modifier = Modifier.size(28.dp).scale(scale).clip(CircleShape).background(dotColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (done) {
                            Icon(Lucide.Check, null, tint = surface, modifier = Modifier.size(14.dp))
                        } else {
                            Text(
                                "${i + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (active) surface else accent,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (active) AppColors.textPrimary else AppColors.textSecondary,
                )
            }
        }
    }
}
