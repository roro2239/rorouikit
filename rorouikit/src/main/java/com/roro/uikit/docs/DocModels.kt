package com.roro.uikit.docs

import androidx.compose.runtime.Composable

//单个组件的文档条目，包含展示名称、Markdown 说明和可运行的演示 Composable。
data class ComponentDoc(
    val name: String,
    val markdown: String = "",
    val demo: @Composable () -> Unit,
)

//文档分区，将同类组件归组展示。
data class DocSection(
    val title: String,
    val components: List<ComponentDoc>,
)
