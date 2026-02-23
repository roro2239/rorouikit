package com.roro.uikit.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.components.core.bounceClickable
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)

/**
 * 白色背景平面卡片，用于需要与页面背景形成层次感的内容块。
 * onClick 不为空时附加弹性点击动画并裁切内容至圆角边界。
 */
@Composable
fun AppFlatCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: AppCardColors = AppCardDefaults.flatColors(),
    style: AppCardStyle = AppCardDefaults.style(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val base = modifier
        .then(if (onClick != null) Modifier.bounceClickable(onClick) else Modifier)
        .background(colors.containerColor, r())
        .then(if (onClick != null) Modifier.clip(r()) else Modifier)
        .padding(style.paddingStart, style.paddingTop, style.paddingEnd, style.paddingBottom)
    Column(modifier = base, content = content)
}

/**
 * 浅灰背景填充卡片，用于在白色表面上区分内容区域。
 * onClick 不为空时附加弹性点击动画并裁切内容至圆角边界。
 */
@Composable
fun AppFilledCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    colors: AppCardColors = AppCardDefaults.filledColors(),
    style: AppCardStyle = AppCardDefaults.style(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val base = modifier
        .then(if (onClick != null) Modifier.bounceClickable(onClick) else Modifier)
        .background(colors.containerColor, r())
        .then(if (onClick != null) Modifier.clip(r()) else Modifier)
        .padding(style.paddingStart, style.paddingTop, style.paddingEnd, style.paddingBottom)
    Column(modifier = base, content = content)
}
