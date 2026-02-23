package com.roro.uikit.components.input

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions as KO
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X

@Composable
private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)
private val capsule get() = ContinuousCapsule(AppG2)

@Composable
private fun resolveTextFieldColors(c: AppTextFieldColors) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = c.focusedBorderColor,
    unfocusedBorderColor = c.unfocusedBorderColor,
    errorBorderColor = c.errorBorderColor,
    disabledBorderColor = c.disabledBorderColor,
    focusedContainerColor = c.containerColor,
    unfocusedContainerColor = c.containerColor,
    errorContainerColor = c.containerColor,
    disabledContainerColor = c.containerColor,
    focusedLabelColor = c.focusedLabelColor,
    unfocusedLabelColor = c.unfocusedLabelColor,
    errorLabelColor = c.errorLabelColor,
    disabledLabelColor = c.disabledLabelColor,
    cursorColor = c.cursorColor,
    focusedTextColor = c.textColor,
    unfocusedTextColor = c.textColor,
    errorTextColor = c.errorTextColor,
    disabledTextColor = c.disabledTextColor,
    focusedLeadingIconColor = c.focusedIconColor,
    unfocusedLeadingIconColor = c.unfocusedIconColor,
    errorLeadingIconColor = c.unfocusedIconColor,
    focusedTrailingIconColor = c.focusedIconColor,
    unfocusedTrailingIconColor = c.unfocusedIconColor,
    errorTrailingIconColor = c.errorBorderColor,
)

/**
 * 标准带边框输入框，支持标签、占位符、前后图标、错误提示和密码模式。
 * 错误信息显示在标签行右侧，避免占用额外垂直空间。
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    password: Boolean = false,
    keyboardOptions: KO = KO.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    colors: AppTextFieldColors = AppTextFieldDefaults.colors(),
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = label?.let { {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                    if (isError && errorMessage != null)
                        Text(errorMessage, style = MaterialTheme.typography.labelMedium, color = colors.errorLabelColor)
                }
            } },
            placeholder = placeholder?.let { { Text(it, style = MaterialTheme.typography.bodyMedium, color = colors.placeholderColor) } },
            leadingIcon = leadingIcon?.let { { Icon(it, null, modifier = Modifier.size(AppColors.iconSize)) } },
            trailingIcon = trailingIcon?.let {
                {
                    if (onTrailingIconClick != null) {
                        IconButton(onClick = onTrailingIconClick, modifier = Modifier.size(36.dp)) {
                            Icon(it, null, modifier = Modifier.size(AppColors.iconSize))
                        }
                    } else {
                        Icon(it, null, modifier = Modifier.size(AppColors.iconSize))
                    }
                }
            },
            isError = isError,
            visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            enabled = enabled,
            shape = r(),
            colors = resolveTextFieldColors(colors),
            textStyle = MaterialTheme.typography.bodyLarge,
        )
    }
}

//固定宽度搜索框，胶囊形状，有内容时显示清除按钮。
@Composable
fun AppSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索",
    onSearch: ((String) -> Unit)? = null,
    colors: AppTextFieldColors = AppTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium, color = colors.placeholderColor) },
        leadingIcon = { Icon(Lucide.Search, null, modifier = Modifier.size(AppColors.iconSize)) },
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onValueChange("") }, modifier = Modifier.size(36.dp)) {
                    Icon(Lucide.X, null, modifier = Modifier.size(16.dp))
                }
            }
        } else null,
        singleLine = true,
        shape = capsule,
        colors = resolveTextFieldColors(colors),
        textStyle = MaterialTheme.typography.bodyLarge,
        keyboardOptions = KO(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke(value) }),
    )
}

/**
 * 可展开搜索栏，初始状态为单个图标，点击后以动画展开为完整输入框。
 * 展开时通过 drawBehind 绘制圆角矩形背景，避免 clip 导致的动画裁切问题。
 *
 * @param iconAlignment 收起状态下图标的水平对齐位置。
 */
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AppExpandableSearchBar(
    modifier: Modifier = Modifier,
    placeholder: String = "搜索",
    iconAlignment: Alignment.Horizontal = Alignment.End,
    onSearch: ((String) -> Unit)? = null,
    colors: AppTextFieldColors = AppTextFieldDefaults.colors(),
    animation: AppSearchBarAnimation = AppSearchBarAnimationDefaults.animation(),
) {
    var expanded by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val height = 44.dp
    val borderColor = if (expanded) colors.focusedBorderColor else colors.unfocusedBorderColor

    LaunchedEffect(expanded) {
        if (expanded) focusRequester.requestFocus() else focusManager.clearFocus()
    }

    val expandFraction by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = animation.expandSpec,
        label = "search_expand",
    )

    BoxWithConstraints(modifier = modifier.height(height)) {
        val density = LocalDensity.current
        val fullPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()
        val radius = heightPx / 2f
        val layoutDir = LocalLayoutDirection.current
        val iconLeftPx = iconAlignment.align(heightPx.toInt(), fullPx.toInt(), layoutDir).toFloat()
            .coerceIn(0f, fullPx - heightPx)
        val left = iconLeftPx - iconLeftPx * expandFraction
        val right = iconLeftPx + heightPx + (fullPx - iconLeftPx - heightPx) * expandFraction

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .drawBehind {
                    val path = Path().apply {
                        addRoundRect(RoundRect(left, 0f, right, heightPx, CornerRadius(radius)))
                    }
                    drawPath(path, colors.containerColor)
                    drawPath(path, borderColor, style = Stroke(width = 2.dp.toPx()))
                },
        ) {
            if (expandFraction > 0.5f) {
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth()
                        .padding(start = with(density) { (left + 16.dp.toPx()).toDp() }, end = height)
                        .focusRequester(focusRequester),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.textColor),
                    keyboardOptions = KO(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch?.invoke(value); expanded = false; value = "" }),
                    decorationBox = { inner ->
                        if (value.isEmpty()) Text(placeholder, style = MaterialTheme.typography.bodyLarge, color = colors.placeholderColor)
                        inner()
                    },
                )
            }
            if (!expanded) {
                Box(
                    modifier = Modifier
                        .size(height)
                        .absoluteOffset(x = with(density) { iconLeftPx.toDp() })
                        .clip(capsule)
                        .clickable(remember { MutableInteractionSource() }, null) { expanded = true },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Lucide.Search, null, tint = colors.unfocusedIconColor, modifier = Modifier.size(20.dp))
                }
            }
            if (expanded) {
                Box(
                    modifier = Modifier
                        .size(height)
                        .align(Alignment.CenterEnd)
                        .clip(capsule)
                        .clickable(remember { MutableInteractionSource() }, null) { expanded = false; value = "" },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Lucide.X, null, tint = colors.focusedBorderColor, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
