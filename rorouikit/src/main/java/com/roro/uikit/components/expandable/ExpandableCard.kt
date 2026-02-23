package com.roro.uikit.components.expandable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)

/**
 * 可展开卡片，点击标题行切换内容区的展开/收起状态。
 * 内容区高度通过 layout 修饰符裁切实现平滑动画，避免 AnimatedVisibility 的重组开销。
 * outerContent 不为空时接管整个布局，state.modifier 和 header/content lambda 均可复用。
 */
@Composable
fun AppExpandableCard(
    header: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    colors: AppExpandableCardColors = AppExpandableCardDefaults.colors(),
    style: AppExpandableCardStyle = AppExpandableCardDefaults.style(),
    animation: AppExpandableCardAnimation = AppExpandableCardDefaults.animation(),
    outerContent: (@Composable (state: AppExpandableCardState, header: @Composable RowScope.() -> Unit, innerContent: @Composable ColumnScope.() -> Unit) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val state = rememberAppExpandableCardState(expanded, onExpandedChange, animation)
    if (outerContent != null) { outerContent(state, header, content); return }
    Column(modifier = modifier.clip(r())) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.headerBackgroundColor)
                .clickable(indication = null, interactionSource = null) { state.onToggle() }
                .padding(horizontal = style.headerPaddingHorizontal, vertical = style.headerPaddingVertical),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (style.chevronPosition == AppExpandableCardChevronPosition.Start) {
                Icon(Lucide.ChevronDown, null, tint = colors.chevronColor, modifier = Modifier.size(style.chevronSize).rotate(state.chevronRotation))
                Spacer(Modifier.width(8.dp))
            }
            header()
            if (style.chevronPosition == AppExpandableCardChevronPosition.End) {
                Spacer(Modifier.weight(1f))
                Icon(Lucide.ChevronDown, null, tint = colors.chevronColor, modifier = Modifier.size(style.chevronSize).rotate(state.chevronRotation))
            }
        }
        if (state.heightFraction > 0f) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val h = (placeable.height * state.heightFraction).toInt()
                        layout(placeable.width, h) { placeable.placeRelative(0, 0) }
                    }
                    .graphicsLayer { alpha = state.heightFraction },
            ) {
                HorizontalDivider(color = colors.dividerColor, thickness = 0.5.dp)
                Column(
                    modifier = Modifier.fillMaxWidth().background(colors.containerColor)
                        .padding(horizontal = style.contentPaddingHorizontal, vertical = style.contentPaddingVertical),
                    content = content,
                )
            }
        }
    }
}
