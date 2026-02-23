package com.roro.uikit.components.navbar

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.kyant.capsule.ContinuousCapsule
import com.roro.uikit.components.AppNavItem
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2

private val capsule get() = ContinuousCapsule(AppG2)

/**
 * 浮动胶囊导航栏，悬浮于内容区底部。
 * 选中项展开显示标签文字，非选中项仅显示图标，切换时带弹性展开/收缩动画。
 * transparentNavBar 为 true 时强制系统导航栏透明并设置浅色图标。
 */
@Composable
fun AppFloatingNavBar(
    items: List<AppNavItem>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    transparentNavBar: Boolean = true,
    colors: AppNavBarColors = AppNavBarDefaults.colors(),
    style: AppNavBarStyle = AppNavBarDefaults.style(),
    animation: AppNavBarAnimation = AppNavBarAnimationDefaults.animation(),
    content: (@Composable (state: AppNavBarState) -> Unit)? = null,
) {
    if (transparentNavBar) {
        val view = LocalView.current
        SideEffect {
            val window = (view.context as android.app.Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                @Suppress("DEPRECATION")
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
            }
        }
    }
    val state = rememberAppNavBarState(items, selected, onSelect, colors, animation)
    if (content != null) { content(state); return }
    Box(modifier = modifier.navigationBarsPadding().padding(horizontal = 24.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .graphicsLayer { shadowElevation = 12.dp.toPx(); shape = capsule; clip = false }
                .background(colors.containerColor, capsule)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items.forEachIndexed { i, item ->
                val active = i == selected
                val tint = state.itemTints.getOrElse(i) { if (active) colors.activeColor else colors.inactiveColor }
                TextButton(
                    onClick = { onSelect(i) },
                    modifier = Modifier.height(style.itemHeight),
                    shape = capsule,
                    colors = ButtonDefaults.textButtonColors(containerColor = if (active) colors.activeBackground else Color.Transparent),
                    contentPadding = PaddingValues(horizontal = style.itemHorizontalPadding, vertical = 0.dp),
                ) {
                    Icon(item.icon, null, tint = tint, modifier = Modifier.size(style.iconSize))
                    AnimatedVisibility(
                        visible = active,
                        enter = fadeIn(animation.labelEnterSpec) + expandHorizontally(animation.labelExpandSpec),
                        exit = fadeOut(animation.labelExitSpec) + shrinkHorizontally(animation.labelShrinkSpec),
                    ) {
                        Row { Spacer(Modifier.width(6.dp)); Text(item.label, style = MaterialTheme.typography.labelLarge, color = tint) }
                    }
                }
            }
        }
    }
}

/**
 * 滑动指示器导航栏，所有项目始终显示图标和标签，选中项背景指示器平滑滑动。
 * 使用 SubcomposeLayout 精确测量每个 Tab 宽度，确保指示器位置与宽度完全对齐。
 */
@Composable
fun AppSlidingNavBar(
    items: List<AppNavItem>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    transparentNavBar: Boolean = true,
    colors: AppNavBarColors = AppNavBarDefaults.colors(),
    style: AppNavBarStyle = AppNavBarDefaults.style(),
    animation: AppSlidingNavBarAnimation = AppSlidingNavBarAnimationDefaults.animation(),
) {
    if (transparentNavBar) {
        val view = LocalView.current
        SideEffect {
            val window = (view.context as android.app.Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                @Suppress("DEPRECATION")
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
            }
        }
    }
    val state = rememberAppNavBarState(items, selected, onSelect, colors, AppNavBarAnimation(tintSpec = animation.tintSpec))
    val tabWidths = remember { mutableStateOf(IntArray(0)) }
    val indicatorX by animateFloatAsState(
        targetValue = tabWidths.value.take(state.selected).sum().toFloat(),
        animationSpec = animation.indicatorXSpec,
        label = "x",
    )
    val indicatorScale = remember { Animatable(1f) }
    LaunchedEffect(state.selected) {
        indicatorScale.animateTo(0.45f, animation.indicatorScaleCompressSpec)
        indicatorScale.animateTo(1f, animation.indicatorScaleBounceSpec)
    }
    SubcomposeLayout(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .shadow(8.dp, CircleShape)
            .background(colors.containerColor, CircleShape),
    ) { constraints ->
        val padH = 8.dp.roundToPx()
        val padV = 8.dp.roundToPx()
        val c = constraints.copy(minWidth = 0, minHeight = 0)
        val tabPlaceables = subcompose("tabs") {
            state.items.forEachIndexed { i, item ->
                val tint = state.itemTints.getOrElse(i) { if (i == state.selected) colors.activeColor else colors.inactiveColor }
                TextButton(
                    onClick = { state.onSelect(i) },
                    modifier = Modifier.height(style.itemHeight),
                    shape = CircleShape,
                    colors = ButtonDefaults.textButtonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(horizontal = style.itemHorizontalPadding, vertical = 0.dp),
                ) {
                    Icon(item.icon, null, tint = tint, modifier = Modifier.size(style.iconSize))
                    Spacer(Modifier.width(6.dp))
                    Text(item.label, style = MaterialTheme.typography.labelLarge, color = tint)
                }
            }
        }.map { it.measure(c) }
        val widths = IntArray(tabPlaceables.size) { tabPlaceables[it].width }
        if (!widths.contentEquals(tabWidths.value)) tabWidths.value = widths
        val contentW = tabPlaceables.sumOf { it.width }
        val contentH = tabPlaceables.maxOf { it.height }
        val totalW = contentW + padH * 2
        val totalH = contentH + padV * 2
        val indicatorW = widths.getOrElse(state.selected) { 0 }
        val indicatorPlaceable = subcompose("indicator") {
            Box(Modifier.fillMaxSize().graphicsLayer { scaleY = indicatorScale.value }.background(colors.activeBackground, CircleShape))
        }.first().measure(c.copy(minWidth = indicatorW, maxWidth = indicatorW, minHeight = contentH, maxHeight = contentH))
        layout(totalW, totalH) {
            indicatorPlaceable.placeRelative(IntOffset(indicatorX.toInt() + padH, padV))
            var x = padH
            tabPlaceables.forEach { p -> p.placeRelative(x, padV); x += p.width }
        }
    }
}

/**
 * 导航脚手架，集成页面内容区与底部导航栏。
 * 页面切换时执行轻微水平位移加淡入淡出动画，位移量为页面宽度的 1/6 以避免过于突兀。
 *
 * @param slidingNavBar 为 true 时使用滑动指示器导航栏，否则使用浮动胶囊导航栏。
 */
@Composable
fun AppNavScaffold(
    items: List<AppNavItem>,
    selected: Int,
    onSelect: (Int) -> Unit,
    navBarColors: AppNavBarColors = AppNavBarDefaults.colors(),
    navBarStyle: AppNavBarStyle = AppNavBarDefaults.style(),
    animation: AppNavScaffoldAnimation = AppNavScaffoldAnimationDefaults.animation(),
    navBarAnimation: AppNavBarAnimation = AppNavBarAnimationDefaults.animation(),
    slidingNavBar: Boolean = false,
    content: @Composable (page: Int) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Scaffold(containerColor = AppColors.background) { padding ->
            Box(Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {
                AnimatedContent(
                    targetState = selected,
                    transitionSpec = {
                        val dir = if (targetState > initialState) 1 else -1
                        (slideInHorizontally(animation.pageSpec) { it / 6 * dir } + fadeIn(animation.pageFadeOutSpec)) togetherWith
                                (slideOutHorizontally(animation.pageSpec) { -it / 6 * dir } + fadeOut(animation.pageFadeOutSpec))
                    },
                    label = "nav_page",
                ) { page -> content(page) }
            }
        }
        if (slidingNavBar) {
            AppSlidingNavBar(items, selected, onSelect, Modifier.align(Alignment.BottomCenter), colors = navBarColors, style = navBarStyle)
        } else {
            AppFloatingNavBar(items, selected, onSelect, Modifier.align(Alignment.BottomCenter), colors = navBarColors, style = navBarStyle, animation = navBarAnimation)
        }
    }
}
