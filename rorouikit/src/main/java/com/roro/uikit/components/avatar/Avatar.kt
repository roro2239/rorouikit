package com.roro.uikit.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme

enum class AppAvatarShape { Circle, Rounded }

/**
 * 头像组件，支持文字首字母或图标，圆形或方形两种形状。
 */
@Composable
fun AppAvatar(
    modifier: Modifier = Modifier,
    initials: String? = null,
    icon: ImageVector? = null,
    size: Dp = 40.dp,
    shape: AppAvatarShape = AppAvatarShape.Circle,
    containerColor: Color = AppColors.accentLight,
    contentColor: Color = AppColors.accent,
) {
    val clipShape: Shape = when (shape) {
        AppAvatarShape.Circle -> CircleShape
        AppAvatarShape.Rounded -> ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius * 0.6f, AppG2)
    }
    Box(
        modifier = modifier.size(size).clip(clipShape).background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        when {
            initials != null -> Text(
                text = initials.take(2).uppercase(),
                color = contentColor,
                fontSize = (size.value * 0.35f).sp,
                style = MaterialTheme.typography.labelMedium,
            )
            icon != null -> Icon(icon, null, tint = contentColor, modifier = Modifier.size(size * 0.5f))
        }
    }
}
