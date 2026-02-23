package com.roro.uikit.components.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.capsule.ContinuousRoundedRectangle
import com.roro.uikit.components.LocalBackdrop
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.LocalAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class AppToastType { Default, Accent, Success, Error, Warning }

data class AppToastData(
    val message: String,
    val type: AppToastType = AppToastType.Default,
    val icon: ImageVector? = null,
    val durationMs: Long = 2000,
)

@Stable
class AppToastHostState {
    var current by mutableStateOf<AppToastData?>(null)
        private set

    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null

    fun show(toast: AppToastData) {
        job?.cancel()
        current = toast
        job = scope.launch {
            delay(toast.durationMs)
            current = null
        }
    }
}

val LocalToastHostState = staticCompositionLocalOf { AppToastHostState() }

/**
 * Toast 宿主，固定在屏幕顶部居中，不跟随触发位置。
 * 从顶部滑入，带毛玻璃背景效果。放置在页面根布局顶层。
 */
@Composable
fun AppToastHost(
    state: AppToastHostState = LocalToastHostState.current,
) {
    val toast = state.current
    var lastToast by remember { mutableStateOf(toast) }
    if (toast != null) lastToast = toast
    val cornerRadius = LocalAppTheme.current.cornerRadius
    val shape = ContinuousRoundedRectangle(cornerRadius, AppG2)
    val backdrop = LocalBackdrop.current
    Box(
        modifier = Modifier.fillMaxSize().statusBarsPadding().padding(top = 56.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        AnimatedVisibility(
            visible = toast != null,
            enter = fadeIn() + slideInVertically(spring(dampingRatio = 0.75f, stiffness = 400f)) { -it },
            exit = fadeOut(spring(dampingRatio = 1f, stiffness = 600f)) + slideOutVertically { -it },
        ) {
            val t = lastToast ?: return@AnimatedVisibility
            val colors = when (t.type) {
                AppToastType.Accent -> AppToastDefaults.accentColors()
                AppToastType.Success -> AppToastDefaults.successColors()
                AppToastType.Error -> AppToastDefaults.errorColors()
                AppToastType.Warning -> AppToastDefaults.warningColors()
                AppToastType.Default -> AppToastDefaults.defaultColors()
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { shape },
                        effects = {
                            vibrancy()
                            blur(4f.dp.toPx())
                            lens(16f.dp.toPx(), 32f.dp.toPx(), true)
                        },
                        onDrawSurface = { drawRect(colors.containerColor.copy(alpha = 0.85f)) },
                    )
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (t.icon != null) {
                    Icon(t.icon, null, tint = colors.iconColor, modifier = Modifier.size(16.dp))
                }
                Text(t.message, style = MaterialTheme.typography.bodyMedium, color = colors.textColor)
            }
        }
    }
}
