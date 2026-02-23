package com.roro.uikit.components.sticky

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.roro.uikit.theme.AppColors

/**
 * 在 LazyColumn 中使用的吸顶分组标题，背景色与页面一致实现吸顶效果。
 */
fun LazyListScope.appStickyHeader(
    key: Any,
    title: String,
) {
    stickyHeader(key = key) {
        AppStickyHeaderContent(title)
    }
}

@Composable
fun AppStickyHeaderContent(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = AppColors.textSecondary,
        )
    }
}
