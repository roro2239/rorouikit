package com.roro.uikit.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.components.core.bounceClick
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)

/**
 * 主要按钮，深色填充背景，用于页面主操作。
 * content 参数不为空时完全接管渲染，可实现自定义外观同时复用交互逻辑。
 */
@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    colors: AppButtonColors = AppButtonDefaults.primaryColors(),
    style: AppButtonStyle = AppButtonDefaults.style(),
    content: (@Composable (text: String, onClick: () -> Unit, enabled: Boolean, icon: ImageVector?, interactionSource: MutableInteractionSource) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    if (content != null) { content(text, onClick, enabled, icon, interactionSource); return }
    Button(
        onClick = onClick,
        modifier = modifier.height(style.height).bounceClick(interactionSource),
        enabled = enabled,
        shape = r(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor,
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        contentPadding = PaddingValues(horizontal = style.horizontalPadding),
        interactionSource = interactionSource,
    ) {
        if (icon != null) { Icon(icon, null, modifier = Modifier.size(style.iconSize)); Spacer(Modifier.width(style.iconSpacing)) }
        Text(text, style = style.textStyle)
    }
}

/**
 * 轮廓按钮，透明背景加描边，用于次要操作。
 * 边框通过 drawBehind 手动绘制以支持 G2 连续圆角形状，避免 Material 边框裁切问题。
 */
@Composable
fun AppOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    colors: AppButtonColors = AppButtonDefaults.outlineColors(),
    style: AppButtonStyle = AppButtonDefaults.style(),
    content: (@Composable (text: String, onClick: () -> Unit, enabled: Boolean, icon: ImageVector?, interactionSource: MutableInteractionSource) -> Unit)? = null,
) {
    val shape = r()
    val borderColor = if (enabled) colors.borderColor else colors.disabledBorderColor
    val interactionSource = remember { MutableInteractionSource() }
    if (content != null) { content(text, onClick, enabled, icon, interactionSource); return }
    Button(
        onClick = onClick,
        modifier = modifier.height(style.height).bounceClick(interactionSource).drawBehind {
            val outline = shape.createOutline(size, layoutDirection, this)
            val path = Path().apply { addOutline(outline) }
            drawPath(path, borderColor, style = Stroke(width = style.borderWidth.toPx()))
        },
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = colors.contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = colors.disabledContentColor,
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        contentPadding = PaddingValues(horizontal = style.horizontalPadding),
        interactionSource = interactionSource,
    ) {
        if (icon != null) { Icon(icon, null, modifier = Modifier.size(style.iconSize)); Spacer(Modifier.width(style.iconSpacing)) }
        Text(text, style = style.textStyle)
    }
}

//强调按钮，使用主题 accent 色填充，用于最高优先级操作。
@Composable
fun AppAccentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    colors: AppButtonColors = AppButtonDefaults.accentColors(),
    style: AppButtonStyle = AppButtonDefaults.style(),
    content: (@Composable (text: String, onClick: () -> Unit, enabled: Boolean, icon: ImageVector?, interactionSource: MutableInteractionSource) -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    if (content != null) { content(text, onClick, enabled, icon, interactionSource); return }
    Button(
        onClick = onClick,
        modifier = modifier.height(style.height).bounceClick(interactionSource),
        enabled = enabled,
        shape = r(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor,
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        contentPadding = PaddingValues(horizontal = style.horizontalPadding),
        interactionSource = interactionSource,
    ) {
        if (icon != null) { Icon(icon, null, modifier = Modifier.size(style.iconSize)); Spacer(Modifier.width(style.iconSpacing)) }
        Text(text, style = style.textStyle)
    }
}
