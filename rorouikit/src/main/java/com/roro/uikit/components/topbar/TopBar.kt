package com.roro.uikit.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.Lucide
import com.roro.uikit.components.core.bounceClickable
import com.roro.uikit.theme.AppColors

/**
 * 标题栏，左对齐标题，支持可选返回、右侧操作区与底部扩展内容。
 */
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    bottomContent: (@Composable RowScope.() -> Unit)? = null,
    backgroundColor: Color = AppColors.background,
    contentColor: Color = AppColors.textPrimary,
    height: Dp = 56.dp,
    contentPadding: PaddingValues = PaddingValues(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 0.dp),
    contentOffset: DpOffset = DpOffset(0.dp, 0.dp),
    showDivider: Boolean = false,
) {
    val statusPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = statusPadding),
    ) {
        val headerModifier = if (bottomContent == null) {
            Modifier.defaultMinSize(minHeight = height)
        } else {
            Modifier
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(headerModifier)
                .padding(contentPadding)
                .offset(contentOffset.x, contentOffset.y),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (showBack) {
                        val backEnabled = onBack != null
                        Box(
                            modifier = Modifier
                                .bounceClickable { if (backEnabled) onBack?.invoke() }
                                .padding(6.dp),
                        ) {
                            Icon(Lucide.ChevronLeft, null, tint = if (backEnabled) contentColor else AppColors.textTertiary)
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(title, style = MaterialTheme.typography.displayMedium, color = contentColor)
                        if (subtitle != null) {
                            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = AppColors.textSecondary)
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    content = actions ?: {},
                )
            }
            if (bottomContent != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = bottomContent,
                )
            }
        }
        if (showDivider) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(AppColors.border),
            )
        }
    }
}
