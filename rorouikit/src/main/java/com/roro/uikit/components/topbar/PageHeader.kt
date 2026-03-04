package com.roro.uikit.components.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

/**
 * 页面头部，封装标题 + 副标题 + 可选标签行。
 */
@Composable
fun AppPageHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    tags: (@Composable RowScope.() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp),
    topSpacing: Dp = 12.dp,
    tagSpacing: Dp = 8.dp,
) {
    androidx.compose.foundation.layout.Column(modifier = modifier.fillMaxWidth().padding(contentPadding)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Spacer(Modifier.height(topSpacing))
        Text(title, style = MaterialTheme.typography.displayMedium)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = AppColors.textSecondary)
        }
        if (tags != null) {
            Spacer(Modifier.height(topSpacing))
            Row(horizontalArrangement = Arrangement.spacedBy(tagSpacing), content = tags)
        }
    }
}