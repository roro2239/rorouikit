package com.roro.uikit.components.expandable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

enum class AppExpandableCardChevronPosition { Start, End, None }

@Immutable
data class AppExpandableCardColors(
    val containerColor: Color,
    val headerBackgroundColor: Color,
    val dividerColor: Color,
    val chevronColor: Color,
)

@Immutable
data class AppExpandableCardStyle(
    val headerPaddingHorizontal: Dp,
    val headerPaddingVertical: Dp,
    val contentPaddingHorizontal: Dp,
    val contentPaddingVertical: Dp,
    val chevronSize: Dp,
    val chevronPosition: AppExpandableCardChevronPosition,
)

@Immutable
data class AppExpandableCardAnimation(
    val chevronSpec: AnimationSpec<Float> = spring(dampingRatio = 0.75f, stiffness = 400f),
    val expandSpec: FiniteAnimationSpec<IntSize> = spring(dampingRatio = 0.5f, stiffness = 400f),
    val collapseSpec: FiniteAnimationSpec<IntSize> = spring(dampingRatio = 1f, stiffness = 500f),
    val fadeInSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 1f, stiffness = 300f),
    val fadeOutSpec: FiniteAnimationSpec<Float> = spring(dampingRatio = 1f, stiffness = 500f),
)

object AppExpandableCardDefaults {
    @Composable
    fun colors(containerColor: Color = AppColors.surface) = AppExpandableCardColors(
        containerColor = containerColor,
        headerBackgroundColor = containerColor,
        dividerColor = AppColors.border,
        chevronColor = AppColors.textSecondary,
    )

    @Composable
    fun style() = AppExpandableCardStyle(
        headerPaddingHorizontal = 16.dp,
        headerPaddingVertical = 12.dp,
        contentPaddingHorizontal = 16.dp,
        contentPaddingVertical = 12.dp,
        chevronSize = 18.dp,
        chevronPosition = AppExpandableCardChevronPosition.End,
    )

    fun animation() = AppExpandableCardAnimation()
}

/**
 * 可展开卡片运行时状态，包含展开标志、切换回调、箭头旋转角度和内容区高度比例。
 * heightFraction 通过 Animatable 驱动，展开时弹性伸展，收起时快速收缩。
 *
 * @param expanded 外部受控展开状态，传 null 时组件自行管理内部状态。
 */
@Stable
class AppExpandableCardState(
    val isExpanded: Boolean,
    val onToggle: () -> Unit,
    val chevronRotation: Float,
    val heightFraction: Float,
)

@Composable
fun rememberAppExpandableCardState(
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    animation: AppExpandableCardAnimation = AppExpandableCardDefaults.animation(),
): AppExpandableCardState {
    var internalExpanded by remember { mutableStateOf(false) }
    val isExpanded = expanded ?: internalExpanded
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = animation.chevronSpec, label = "chevron",
    )
    val heightFraction = remember { Animatable(if (isExpanded) 1f else 0f) }
    LaunchedEffect(isExpanded) {
        if (isExpanded) heightFraction.animateTo(1f, spring(dampingRatio = 0.5f, stiffness = 400f))
        else heightFraction.animateTo(0f, spring(dampingRatio = 1f, stiffness = 500f))
    }
    val onToggle: () -> Unit = {
        val next = !isExpanded
        if (expanded == null) internalExpanded = next
        onExpandedChange?.invoke(next)
    }
    return AppExpandableCardState(isExpanded, onToggle, chevronRotation, heightFraction.value)
}
