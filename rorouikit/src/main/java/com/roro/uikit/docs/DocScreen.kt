package com.roro.uikit.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.mikepenz.markdown.m3.Markdown
import com.roro.uikit.components.LocalBackdrop
import com.roro.uikit.components.LocalOverlay
import com.roro.uikit.components.misc.AppAccentPicker
import com.roro.uikit.components.toast.AppToastHost
import com.roro.uikit.components.toast.AppToastHostState
import com.roro.uikit.components.toast.LocalToastHostState
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppG2
import com.roro.uikit.theme.AppTheme
import com.roro.uikit.theme.AppThemeConfig
import com.roro.uikit.theme.LocalAppTheme

@Composable private fun r() = ContinuousRoundedRectangle(LocalAppTheme.current.cornerRadius, AppG2)

@Composable
fun DocScreen(docs: List<DocSection>) {
    var config by remember { mutableStateOf(AppThemeConfig()) }
    AppTheme(config) {
        val backgroundColor = AppColors.background
        val backdrop = rememberLayerBackdrop {
            drawRect(backgroundColor)
            drawContent()
        }
        val overlayState = remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
        val toastState = remember { AppToastHostState() }
        CompositionLocalProvider(LocalBackdrop provides backdrop, LocalOverlay provides overlayState, LocalToastHostState provides toastState) {
            Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().background(backgroundColor).layerBackdrop(backdrop),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp),
                ) {
                    item {
                        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                        Spacer(Modifier.height(16.dp))
                        Text("UI Kit", style = MaterialTheme.typography.displayMedium)
                        Text("com.roro.uikit", style = MaterialTheme.typography.bodyMedium, color = AppColors.textSecondary)
                        Spacer(Modifier.height(16.dp))
                        AppAccentPicker(
                            presets = config.accentPresets,
                            current = config.accent,
                            onSelect = { config = config.copy(accent = it, accentLight = it.copy(alpha = 0.12f)) },
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                    items(docs) { section ->
                        SectionBlock(section)
                        Spacer(Modifier.height(24.dp))
                    }
                }
                overlayState.value?.invoke()
                AppToastHost(toastState)
            }
        }
    }
}

@Composable
private fun SectionBlock(section: DocSection) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(Modifier.width(3.dp).height(16.dp).background(AppColors.accent, r()))
            Text(section.title, style = MaterialTheme.typography.titleLarge)
        }
        section.components.forEach { doc -> ComponentBlock(doc) }
    }
}

@Composable
private fun ComponentBlock(doc: ComponentDoc) {
    Column(
        modifier = Modifier.fillMaxWidth().border(1.dp, AppColors.border, r()).clip(r()),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(AppColors.surfaceVariant).padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(doc.name, style = MaterialTheme.typography.labelLarge, color = AppColors.accent)
            if (doc.markdown.isNotEmpty()) Markdown(doc.markdown)
        }
        HorizontalDivider(color = AppColors.border, thickness = 0.5.dp)
        Box(Modifier.fillMaxWidth().padding(16.dp)) { doc.demo() }
    }
}
