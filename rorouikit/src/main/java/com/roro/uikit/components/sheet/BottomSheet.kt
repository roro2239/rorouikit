package com.roro.uikit.components.sheet

import android.annotation.SuppressLint
import android.view.RoundedCorner
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.components.LocalBackdrop
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2
import kotlinx.coroutines.launch

/**
 * 底部弹窗运行时状态，供 outerContent 自定义外层布局时使用。
 * modifier 已包含拖拽手势、位移动画和形状裁切，直接应用到容器即可。
 */
@Stable
class AppBottomSheetState(
    val dismiss: () -> Unit,
    val progress: Float,
    val modifier: Modifier,
)

// 读取屏幕底部物理圆角半径，用于计算弹窗底部圆角，使其与屏幕边缘视觉对齐
@SuppressLint("NewApi")
@Composable
private fun screenCornerRadius(): Dp {
    val view = LocalView.current
    val density = LocalDensity.current
    val corner = view.rootWindowInsets?.getRoundedCorner(RoundedCorner.POSITION_BOTTOM_LEFT)
    return with(density) { (corner?.radius ?: 0).toDp() }
}

/**
 * 底部弹窗，支持拖拽关闭手势和毛玻璃背景效果。
 * 底部圆角跟随屏幕物理圆角，顶部圆角使用主题 cornerRadius，视觉上与屏幕边缘自然融合。
 * glass 模式依赖 LocalBackdrop，需在 AppTheme 或手动提供 backdrop 的作用域内使用。
 *
 * @param outerContent 不为空时接管整个弹窗布局，state.modifier 需手动应用到容器。
 */
@Composable
fun AppBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    glass: Boolean = false,
    showHandle: Boolean = true,
    colors: AppBottomSheetColors = AppBottomSheetDefaults.colors(),
    style: AppBottomSheetStyle = AppBottomSheetDefaults.style(),
    outerContent: (@Composable (state: AppBottomSheetState, content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit) -> Unit)? = null,
    content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
) {
    val screenRadius = screenCornerRadius()
    val density = LocalDensity.current
    val bottomRadius = maxOf(screenRadius - style.horizontalPadding, AppColors.cornerRadius + 4.dp)
    val bottomRadiusPx = with(density) { bottomRadius.toPx() }
    val topRadiusPx = with(density) { AppColors.cornerRadius.toPx() }
    val clipShape = GenericShape { size, _ ->
        addRoundRect(RoundRect(0f, 0f, size.width, size.height, CornerRadius(topRadiusPx), CornerRadius(topRadiusPx), CornerRadius(bottomRadiusPx), CornerRadius(bottomRadiusPx)))
    }
    val glassShape = ContinuousRoundedRectangle(AppColors.cornerRadius, AppG2)

    val offsetY = remember { Animatable(9999f) }
    var sheetHeight by remember { mutableFloatStateOf(0f) }
    val progress = if (sheetHeight > 0f) 1f - (offsetY.value / sheetHeight).coerceIn(0f, 1f) else 0f

    val scope = rememberCoroutineScope()
    val dismiss: () -> Unit = remember(onDismissRequest) {
        { scope.launch { offsetY.animateTo(sheetHeight, spring(dampingRatio = 0.85f, stiffness = 500f)); onDismissRequest() } }
    }

    LaunchedEffect(sheetHeight) {
        if (sheetHeight > 0f) {
            offsetY.snapTo(sheetHeight)
            offsetY.animateTo(0f, spring(dampingRatio = 0.72f, stiffness = 380f))
        }
    }

    BackHandler(onBack = dismiss)

    val sheetModifier = modifier
        .onSizeChanged { sheetHeight = it.height.toFloat().coerceAtLeast(1f) }
        .graphicsLayer { translationY = offsetY.value }
        .pointerInput(dismiss) {
            val velThreshold = with(density) { 125.dp.toPx() }
            detectVerticalDragGestures(
                onDragEnd = {
                    scope.launch {
                        if (offsetY.velocity > velThreshold || offsetY.value > sheetHeight * 0.5f) dismiss()
                        else offsetY.animateTo(0f, spring(dampingRatio = 0.72f, stiffness = 380f))
                    }
                },
                onDragCancel = { scope.launch { offsetY.animateTo(0f, spring(dampingRatio = 0.72f, stiffness = 380f)) } },
                onVerticalDrag = { _, dy ->
                    scope.launch { offsetY.snapTo((offsetY.value + dy).coerceAtLeast(0f)) }
                },
            )
        }
        .padding(start = style.horizontalPadding, end = style.horizontalPadding, bottom = style.bottomPadding)
        .clip(clipShape)

    val styledModifier = if (glass) {
        val backdrop = LocalBackdrop.current
        val scrimColor = colors.scrimColor
        sheetModifier.drawBackdrop(
            backdrop = backdrop,
            shape = { glassShape },
            effects = {
                vibrancy()
                blur(4f.dp.toPx())
                lens(24f.dp.toPx(), 48f.dp.toPx(), true)
            },
            onDrawSurface = {
                drawRect(scrimColor)
                drawRect(colors.containerColor.copy(alpha = 0.5f))
            },
        )
    } else {
        sheetModifier.background(colors.containerColor)
    }

    if (outerContent != null) {
        outerContent(AppBottomSheetState(dismiss, progress, styledModifier), content)
        return
    }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = progress }
                .background(colors.scrimColor)
                .clickable(interactionSource = null, indication = null, onClick = dismiss)
        )
        Column(
            modifier = styledModifier
                .fillMaxWidth()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * style.maxHeightFraction)
                .align(Alignment.BottomCenter)
                .then(if (!style.fillNavBar) Modifier.navigationBarsPadding() else Modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (showHandle) {
                Box(
                    Modifier
                        .padding(top = style.handleTopPadding, bottom = 8.dp)
                        .width(style.handleWidth)
                        .height(style.handleHeight)
                        .background(colors.handleColor, com.kyant.capsule.ContinuousCapsule(AppG2)),
                )
            }
            Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState())) {
                content(dismiss)
            }
        }
    }
}
