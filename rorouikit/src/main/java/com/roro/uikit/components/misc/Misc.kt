package com.roro.uikit.components.misc

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme
import com.roro.uikit.theme.monetAccentScheme

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)
private val capsule get() = ContinuousCapsule(AppG2)

@Composable
fun AppTag(
    text: String,
    colors: AppTagColors = AppTagDefaults.colors(),
    style: AppTagStyle = AppTagDefaults.style(),
    content: (@Composable (text: String) -> Unit)? = null,
) {
    if (content != null) { content(text); return }
    Box(
        modifier = Modifier
            .background(colors.containerColor, r())
            .padding(horizontal = style.horizontalPadding, vertical = style.verticalPadding),
    ) { Text(text, style = MaterialTheme.typography.labelMedium, color = colors.contentColor) }
}

enum class AppBadgeType { Default, Success, Error, Accent }

@Composable
fun AppStatusBadge(
    text: String,
    type: AppBadgeType = AppBadgeType.Default,
    colors: AppBadgeColors? = null,
    content: (@Composable (text: String, type: AppBadgeType, colors: AppBadgeColors) -> Unit)? = null,
) {
    val resolved = colors ?: when (type) {
        AppBadgeType.Success -> AppBadgeDefaults.successColors()
        AppBadgeType.Error -> AppBadgeDefaults.errorColors()
        AppBadgeType.Accent -> AppBadgeDefaults.accentColors()
        AppBadgeType.Default -> AppBadgeDefaults.defaultColors()
    }
    if (content != null) { content(text, type, resolved); return }
    Box(modifier = Modifier.background(resolved.containerColor, capsule).padding(horizontal = 10.dp, vertical = 4.dp)) {
        Text(text, style = MaterialTheme.typography.labelMedium, color = resolved.contentColor)
    }
}

@Composable
fun AppSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    colors: AppSectionHeaderColors = AppSectionHeaderDefaults.colors(),
    content: (@Composable (title: String) -> Unit)? = null,
) {
    if (content != null) { content(title); return }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(3.dp).height(18.dp).background(colors.accentBarColor, r()))
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = colors.textColor)
    }
}

@Composable
fun AppDivider(modifier: Modifier = Modifier, style: AppDividerStyle = AppDividerStyle()) {
    HorizontalDivider(
        modifier = modifier,
        color = if (style.color == Color.Unspecified) AppColors.border else style.color,
        thickness = style.thickness,
    )
}

@Stable
class AppSwitchState(
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
    val enabled: Boolean,
    val trackColor: Color,
    val thumbColor: Color,
    val thumbOffset: Float,
)

@Composable
fun rememberAppSwitchState(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    colors: AppSwitchColors = AppSwitchDefaults.colors(),
    animation: AppSwitchAnimation = AppSwitchAnimationDefaults.animation(),
): AppSwitchState {
    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> colors.disabledCheckedTrackColor
            !enabled -> colors.disabledUncheckedTrackColor
            checked -> colors.checkedTrackColor
            else -> colors.uncheckedTrackColor
        },
        animationSpec = animation.trackSpec, label = "track",
    )
    val thumbColor by animateColorAsState(
        targetValue = when {
            !enabled && checked -> colors.disabledCheckedThumbColor
            !enabled -> colors.disabledUncheckedThumbColor
            checked -> colors.checkedThumbColor
            else -> colors.uncheckedThumbColor
        },
        animationSpec = animation.thumbColorSpec, label = "thumb",
    )
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = animation.thumbOffsetSpec, label = "offset",
    )
    return AppSwitchState(checked, onCheckedChange, enabled, trackColor, thumbColor, thumbOffset)
}

enum class AppSwitchVariant { Default, Slim }

/**
 * 开关组件，自定义轨道和滑块动画，替代 Material3 Switch 以获得更精细的视觉控制。
 * 轨道通过 Canvas 绘制，滑块通过 padding 偏移实现位移，避免使用 offset 引起重组。
 * Slim 形态：轨道更窄，滑块超出轨道高度形成大头效果。
 */
@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: AppSwitchVariant = AppSwitchVariant.Default,
    colors: AppSwitchColors = AppSwitchDefaults.colors(),
    animation: AppSwitchAnimation = AppSwitchAnimationDefaults.animation(),
    content: (@Composable (state: AppSwitchState) -> Unit)? = null,
) {
    val state = rememberAppSwitchState(checked, onCheckedChange, enabled, colors, animation)
    if (content != null) { content(state); return }
    val isSlim = variant == AppSwitchVariant.Slim
    val trackW = 48.dp
    val trackH = if (isSlim) 18.dp else 28.dp
    val thumbSize = if (isSlim) 26.dp else 20.dp
    val thumbPadding = if (isSlim) 0.dp else (trackH - thumbSize) / 2
    val travel = trackW - thumbSize - thumbPadding * 2
    Box(
        modifier = modifier
            .size(trackW, if (isSlim) thumbSize else trackH)
            .then(if (enabled) Modifier.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onCheckedChange(!checked) } else Modifier),
        contentAlignment = Alignment.CenterStart,
    ) {
        val verticalInset = if (isSlim) (thumbSize - trackH) / 2 else 0.dp
        Canvas(Modifier.matchParentSize().padding(vertical = verticalInset)) {
            drawRoundRect(state.trackColor, cornerRadius = CornerRadius(size.height / 2f))
        }
        val thumbBorderColor by animateColorAsState(
            targetValue = if (checked) state.trackColor else Color.Transparent,
            animationSpec = androidx.compose.animation.core.spring(dampingRatio = 0.75f, stiffness = 400f), label = "thumbBorder",
        )
        Box(
            Modifier
                .padding(start = (thumbPadding + travel * state.thumbOffset).coerceAtLeast(0.dp))
                .size(thumbSize)
                .background(state.thumbColor, capsule)
                .drawWithContent {
                    drawContent()
                    drawCircle(
                        color = thumbBorderColor,
                        radius = size.minDimension / 2f - 1.dp.toPx(),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()),
                    )
                }
        )
    }
}

/**
 * 主题色选择器，提供预设颜色网格、Material You 莫奈取色和自定义 HEX 输入三种方式。
 * 莫奈取色按钮仅在 Android 12+ 且系统支持动态取色时显示。
 */
@Composable
fun AppAccentPicker(
    presets: List<Color>,
    onSelect: (Color) -> Unit,
    modifier: Modifier = Modifier,
    current: Color = AppColors.accent,
    colors: AppAccentPickerColors = AppAccentPickerDefaults.colors(),
    content: (@Composable (presets: List<Color>, current: Color, onSelect: (Color) -> Unit) -> Unit)? = null,
) {
    if (content != null) { content(presets, current, onSelect); return }
    var hex by remember { mutableStateOf("") }
    val monet = monetAccentScheme()
    Column(
        modifier = modifier.fillMaxWidth().background(colors.containerColor, r()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("主题色", style = MaterialTheme.typography.titleMedium, color = colors.titleColor)
            if (monet != null) {
                Row(
                    modifier = Modifier.background(monet.accent.copy(alpha = 0.1f), capsule)
                        .clickable(remember { MutableInteractionSource() }, null) { onSelect(monet.accent) }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(10.dp).background(monet.accent, capsule))
                    Text("莫奈取色", style = MaterialTheme.typography.labelMedium, color = monet.accent)
                }
            }
        }
        androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            presets.forEach { color ->
                val selected = color == current
                val scale by animateFloatAsState(targetValue = if (selected) 1f else 0.82f, animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f), label = "s")
                Box(modifier = Modifier.size(36.dp).graphicsLayer { scaleX = scale; scaleY = scale }.background(color, r())
                    .then(if (selected) Modifier.border(2.5.dp, AppColors.surface, r()).border(4.5.dp, color, r()) else Modifier)
                    .clickable(remember { MutableInteractionSource() }, null) { hex = ""; onSelect(color) })
            }
        }
        Row(modifier = Modifier.fillMaxWidth().background(colors.inputBackgroundColor, r()).padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.size(20.dp).background(if (hex.length == 6) runCatching { Color(android.graphics.Color.parseColor("#$hex")) }.getOrElse { current } else current, r()))
            Text("#", style = MaterialTheme.typography.labelLarge, color = colors.hashColor)
            BasicTextField(
                value = hex,
                onValueChange = { v -> hex = v.filter { it.isLetterOrDigit() }.take(6).uppercase(); if (hex.length == 6) runCatching { onSelect(Color(android.graphics.Color.parseColor("#$hex"))) } },
                modifier = Modifier.weight(1f), singleLine = true,
                textStyle = MaterialTheme.typography.labelLarge.copy(color = colors.hexTextColor),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Done),
                decorationBox = { inner -> if (hex.isEmpty()) Text("自定义颜色", style = MaterialTheme.typography.labelLarge, color = colors.hexPlaceholderColor); inner() },
            )
            Text("${hex.length}/6", style = MaterialTheme.typography.labelSmall, color = if (hex.length == 6) current else colors.counterColor)
        }
    }
}
