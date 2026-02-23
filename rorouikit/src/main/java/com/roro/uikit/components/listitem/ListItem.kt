package com.roro.uikit.components.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)

/**
 * 列表项容器，surface 圆角卡片，通过 AppListGroupScope 添加子项，自动在项之间插入分割线。
 */
class AppListGroupScope(private val items: MutableList<@Composable () -> Unit>) {
    fun item(content: @Composable () -> Unit) { items.add(content) }
}

@Composable
fun AppListGroup(
    modifier: Modifier = Modifier,
    colors: AppListGroupColors = AppListItemDefaults.groupColors(),
    content: AppListGroupScope.() -> Unit,
) {
    val items = remember { mutableListOf<@Composable () -> Unit>() }.also {
        it.clear()
        AppListGroupScope(it).content()
    }
    Column(
        modifier = modifier.fillMaxWidth().clip(r()).background(colors.containerColor),
    ) {
        items.forEachIndexed { i, item ->
            item()
            if (i < items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    color = colors.dividerColor,
                    thickness = 0.5.dp,
                )
            }
        }
    }
}

/**
 * 通用列表行，纯图标（accent 色），右侧支持任意 trailing。
 * 配合 AppListGroup 使用时自动获得卡片背景和分割线。
 */
@Composable
fun AppListItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    colors: AppListItemColors = AppListItemDefaults.colors(),
    style: AppListItemStyle = AppListItemDefaults.style(),
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(remember { MutableInteractionSource() }, null) { onClick() }
                else Modifier
            )
            .padding(horizontal = style.horizontalPadding, vertical = style.verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(style.spacing),
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, null, tint = colors.iconColor, modifier = Modifier.size(style.iconSize))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = colors.titleColor)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = colors.subtitleColor)
            }
        }
        when {
            trailing != null -> trailing()
            onClick != null -> Icon(
                Lucide.ChevronRight, null,
                tint = colors.trailingColor,
                modifier = Modifier.size(style.trailingIconSize),
            )
        }
    }
}
