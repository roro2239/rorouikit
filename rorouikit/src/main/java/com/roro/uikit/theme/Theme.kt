package com.roro.uikit.theme

import android.os.Build
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.kyant.capsule.ContinuousRoundedRectangle
import com.kyant.capsule.continuities.G2Continuity
import com.kyant.capsule.continuities.G2ContinuityProfile

// 禁用 Material 默认涟漪效果，所有点击反馈由各组件自行实现弹性动画
private object NoIndication : IndicationNodeFactory {
    override fun create(interactionSource: androidx.compose.foundation.interaction.InteractionSource): DelegatableNode =
        object : Modifier.Node() {}
    override fun equals(other: Any?) = other === this
    override fun hashCode() = System.identityHashCode(this)
}

/**
 * G2 连续性曲线参数配置，用于全局圆角形状。
 * extendedFraction 和 arcFraction 控制曲线过渡的平滑程度，
 * curvatureScale 参数微调曲率使视觉效果更接近苹果设计语言。
 */
val AppG2 = G2Continuity(
    profile = G2ContinuityProfile.RoundedRectangle.copy(
        extendedFraction = 0.5,
        arcFraction = 0.5,
        bezierCurvatureScale = 1.1,
        arcCurvatureScale = 1.1,
    ),
    capsuleProfile = G2ContinuityProfile.Capsule.copy(
        extendedFraction = 0.25,
        arcFraction = 0.25,
    ),
)

/**
 * 全局主题配置，统一管理颜色、尺寸、字体和系统栏行为。
 * 通过 LocalAppTheme 在组合树中传递，使用 AppColors 快捷属性访问当前值。
 */
@Immutable
data class AppThemeConfig(
    val accent: Color = Color(0xFF2563EB),
    val accentLight: Color = Color(0xFFEFF6FF),
    val background: Color = Color(0xFFF8F9FB),
    val surface: Color = Color(0xFFFFFFFF),
    val surfaceVariant: Color = Color(0xFFF1F3F7),
    val border: Color = Color(0xFFE8EAF0),
    val textPrimary: Color = Color(0xFF111111),
    val textSecondary: Color = Color(0xFF8A8FA8),
    val textTertiary: Color = Color(0xFFB8BCCC),
    val success: Color = Color(0xFF10B981),
    val warning: Color = Color(0xFFF59E0B),
    val error: Color = Color(0xFFEF4444),
    val successLight: Color = Color(0xFFECFDF5),
    val warningLight: Color = Color(0xFFFFFBEB),
    val errorLight: Color = Color(0xFFFEF2F2),
    val cornerRadius: Dp = 16.dp,
    val buttonHeight: Dp = 48.dp,
    val buttonHorizontalPadding: Dp = 20.dp,
    val cardPadding: Dp = 16.dp,
    val inputHeight: Dp = 56.dp,
    val navBarItemHeight: Dp = 44.dp,
    val iconSize: Dp = 18.dp,
    val displayLargeFontSize: TextUnit = 40.sp,
    val displayMediumFontSize: TextUnit = 32.sp,
    val headlineLargeFontSize: TextUnit = 24.sp,
    val headlineMediumFontSize: TextUnit = 20.sp,
    val titleLargeFontSize: TextUnit = 17.sp,
    val titleMediumFontSize: TextUnit = 15.sp,
    val bodyLargeFontSize: TextUnit = 15.sp,
    val bodyMediumFontSize: TextUnit = 13.sp,
    val labelLargeFontSize: TextUnit = 13.sp,
    val labelMediumFontSize: TextUnit = 11.sp,
    val labelSmallFontSize: TextUnit = 10.sp,
    val enableMonet: Boolean = true,
    val accentPresets: List<Color> = defaultAccentPresets,
    val transparentStatusBar: Boolean = true,
    val transparentNavBar: Boolean = true,
    val edgeToEdgeStatusBar: Boolean = false,
    val edgeToEdgeNavBar: Boolean = true,
)

val defaultAccentPresets = listOf(
    Color(0xFF2563EB),
    Color(0xFF7C3AED),
    Color(0xFFDB2777),
    Color(0xFF059669),
    Color(0xFFD97706),
    Color(0xFFDC2626),
    Color(0xFF0891B2),
    Color(0xFF111111),
)

// 深色模式预设，仅覆盖颜色相关字段，尺寸和字体继承浅色配置
val darkThemeConfig = AppThemeConfig(
    accent = Color(0xFF60A5FA),
    accentLight = Color(0xFF1E3A5F),
    background = Color(0xFF0F1117),
    surface = Color(0xFF1A1D27),
    surfaceVariant = Color(0xFF252836),
    border = Color(0xFF2E3347),
    textPrimary = Color(0xFFF0F2F8),
    textSecondary = Color(0xFF8A8FA8),
    textTertiary = Color(0xFF4A5068),
    successLight = Color(0xFF0D2E22),
    warningLight = Color(0xFF2E2008),
    errorLight = Color(0xFF2E0F0F),
)

val LocalAppTheme = compositionLocalOf { AppThemeConfig() }

val AppColors @Composable get() = LocalAppTheme.current

@Deprecated("Use LocalAppTheme.current directly", ReplaceWith("LocalAppTheme.current"))
val LocalAppColors = LocalAppTheme

@Immutable
data class AppColorsScheme(val accent: Color, val accentLight: Color)

val accentPresets get() = defaultAccentPresets.map { AppColorsScheme(it, it.copy(alpha = 0.12f)) }

val AppCornerRadius @Composable get() = LocalAppTheme.current.cornerRadius

private fun buildTypography(c: AppThemeConfig) = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.Black, fontSize = c.displayLargeFontSize, letterSpacing = (-1.5).sp, lineHeight = c.displayLargeFontSize * 1.2),
    displayMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = c.displayMediumFontSize, letterSpacing = (-1).sp, lineHeight = c.displayMediumFontSize * 1.25),
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = c.headlineLargeFontSize, letterSpacing = (-0.3).sp, lineHeight = c.headlineLargeFontSize * 1.33),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = c.headlineMediumFontSize, lineHeight = c.headlineMediumFontSize * 1.4),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = c.titleLargeFontSize, lineHeight = c.titleLargeFontSize * 1.41),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = c.titleMediumFontSize, lineHeight = c.titleMediumFontSize * 1.47),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = c.bodyLargeFontSize, lineHeight = c.bodyLargeFontSize * 1.6),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = c.bodyMediumFontSize, lineHeight = c.bodyMediumFontSize * 1.54),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = c.labelLargeFontSize, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = c.labelMediumFontSize, letterSpacing = 0.2.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = c.labelSmallFontSize, letterSpacing = 0.3.sp),
)

/**
 * 全局主题提供者，自动适配深色模式并同步系统栏外观。
 * 深色模式下保留用户自定义的 accent 颜色和所有尺寸/字体配置，仅替换颜色系统。
 *
 * @param config 主题配置，默认使用浅色预设。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTheme(config: AppThemeConfig = AppThemeConfig(), content: @Composable () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val lightDefault = AppThemeConfig()
    val effectiveConfig = if (isDark) darkThemeConfig.copy(
        accent = if (config.accent != lightDefault.accent) config.accent else darkThemeConfig.accent,
        cornerRadius = config.cornerRadius,
        buttonHeight = config.buttonHeight,
        buttonHorizontalPadding = config.buttonHorizontalPadding,
        cardPadding = config.cardPadding,
        inputHeight = config.inputHeight,
        navBarItemHeight = config.navBarItemHeight,
        iconSize = config.iconSize,
        displayLargeFontSize = config.displayLargeFontSize,
        displayMediumFontSize = config.displayMediumFontSize,
        headlineLargeFontSize = config.headlineLargeFontSize,
        headlineMediumFontSize = config.headlineMediumFontSize,
        titleLargeFontSize = config.titleLargeFontSize,
        titleMediumFontSize = config.titleMediumFontSize,
        bodyLargeFontSize = config.bodyLargeFontSize,
        bodyMediumFontSize = config.bodyMediumFontSize,
        labelLargeFontSize = config.labelLargeFontSize,
        labelMediumFontSize = config.labelMediumFontSize,
        labelSmallFontSize = config.labelSmallFontSize,
        enableMonet = config.enableMonet,
        accentPresets = config.accentPresets,
    ) else config

    val shapes = remember(effectiveConfig.cornerRadius) {
        Shapes(
            extraSmall = ContinuousRoundedRectangle(effectiveConfig.cornerRadius, AppG2),
            small = ContinuousRoundedRectangle(effectiveConfig.cornerRadius, AppG2),
            medium = ContinuousRoundedRectangle(effectiveConfig.cornerRadius, AppG2),
            large = ContinuousRoundedRectangle(effectiveConfig.cornerRadius, AppG2),
            extraLarge = ContinuousRoundedRectangle(effectiveConfig.cornerRadius, AppG2),
        )
    }
    val colorScheme = remember(effectiveConfig.accent, isDark) {
        if (isDark) darkColorScheme(
            primary = effectiveConfig.accent,
            onPrimary = Color.Black,
            primaryContainer = effectiveConfig.accentLight,
            onPrimaryContainer = effectiveConfig.accent,
            background = effectiveConfig.background,
            onBackground = effectiveConfig.textPrimary,
            surface = effectiveConfig.surface,
            onSurface = effectiveConfig.textPrimary,
            surfaceVariant = effectiveConfig.surfaceVariant,
            outline = effectiveConfig.border,
            error = effectiveConfig.error,
        ) else lightColorScheme(
            primary = effectiveConfig.accent,
            onPrimary = Color.White,
            primaryContainer = effectiveConfig.accentLight,
            onPrimaryContainer = effectiveConfig.accent,
            background = effectiveConfig.background,
            onBackground = effectiveConfig.textPrimary,
            surface = effectiveConfig.surface,
            onSurface = effectiveConfig.textPrimary,
            surfaceVariant = effectiveConfig.surfaceVariant,
            outline = effectiveConfig.border,
            error = effectiveConfig.error,
        )
    }
    val typography = remember(
        effectiveConfig.labelLargeFontSize, effectiveConfig.bodyLargeFontSize, effectiveConfig.bodyMediumFontSize,
        effectiveConfig.titleMediumFontSize, effectiveConfig.titleLargeFontSize,
    ) { buildTypography(effectiveConfig) }

    val view = LocalView.current
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        val controller = WindowCompat.getInsetsController(window, view)
        WindowCompat.setDecorFitsSystemWindows(window, !config.edgeToEdgeStatusBar && !config.edgeToEdgeNavBar)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            @Suppress("DEPRECATION")
            window.statusBarColor = if (config.transparentStatusBar) android.graphics.Color.TRANSPARENT else android.graphics.Color.BLACK
            @Suppress("DEPRECATION")
            window.navigationBarColor = if (config.transparentNavBar) android.graphics.Color.TRANSPARENT else android.graphics.Color.BLACK
        }
        controller.isAppearanceLightStatusBars = !isDark
        controller.isAppearanceLightNavigationBars = !isDark
    }

    CompositionLocalProvider(
        LocalAppTheme provides effectiveConfig,
        LocalIndication provides NoIndication,
        LocalRippleConfiguration provides null,
        LocalContentColor provides effectiveConfig.textPrimary,
    ) {
        MaterialTheme(colorScheme = colorScheme, typography = typography, shapes = shapes, content = content)
    }
}

/**
 * 获取 Material You 动态取色方案，Android 12 以下返回 null。
 * 提取系统壁纸主色作为 accent，accentLight 为其 12% 透明度版本。
 */
@Composable
fun monetAccentScheme(): AppColorsScheme? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return null
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    val accent = if (isDark) dynamicDarkColorScheme(context).primary else dynamicLightColorScheme(context).primary
    return AppColorsScheme(accent = accent, accentLight = accent.copy(alpha = 0.12f))
}
