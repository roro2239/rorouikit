package com.roro.uikit.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X
import com.roro.uikit.components.AppNavItem
import com.roro.uikit.components.LocalOverlay
import com.roro.uikit.components.avatar.AppAvatar
import com.roro.uikit.components.button.AppAccentButton
import com.roro.uikit.components.button.AppOutlineButton
import com.roro.uikit.components.button.AppPrimaryButton
import com.roro.uikit.components.card.AppFilledCard
import com.roro.uikit.components.card.AppFlatCard
import com.roro.uikit.components.dialog.AppDialog
import com.roro.uikit.components.expandable.AppExpandableCard
import com.roro.uikit.components.input.AppExpandableSearchBar
import com.roro.uikit.components.input.AppSearchBar
import com.roro.uikit.components.input.AppTextField
import com.roro.uikit.components.listitem.AppListGroup
import com.roro.uikit.components.listitem.AppListItem
import com.roro.uikit.components.loading.AppDotsIndicator
import com.roro.uikit.components.loading.AppLoadingIndicator
import com.roro.uikit.components.loading.AppSpinnerIndicator
import com.roro.uikit.components.misc.AppAccentPicker
import com.roro.uikit.components.misc.AppDivider
import com.roro.uikit.components.misc.AppSectionHeader
import com.roro.uikit.components.misc.AppStatusBadge
import com.roro.uikit.components.misc.AppSwitch
import com.roro.uikit.components.misc.AppSwitchVariant
import com.roro.uikit.components.misc.AppTag
import com.roro.uikit.components.navbar.AppNavScaffold
import com.roro.uikit.components.progress.AppProgressBar
import com.roro.uikit.components.refresh.AppPullToRefresh
import com.roro.uikit.components.segmented.AppSegmentedControl
import com.roro.uikit.components.selection.AppCheckbox
import com.roro.uikit.components.selection.AppRadioButton
import com.roro.uikit.components.panel.AppSidePanel
import com.roro.uikit.components.panel.AppSidePanelSide
import com.roro.uikit.components.sheet.AppBottomSheet
import com.roro.uikit.components.slider.AppSlider
import com.roro.uikit.components.step.AppStepIndicator
import com.roro.uikit.components.sticky.appStickyHeader
import com.roro.uikit.components.swipe.AppSwipeAction
import com.roro.uikit.components.swipe.AppSwipeActionItem
import com.roro.uikit.components.tab.AppTabBar
import com.roro.uikit.components.toast.AppToastData
import com.roro.uikit.components.toast.AppToastHost
import com.roro.uikit.components.toast.AppToastHostState
import com.roro.uikit.components.toast.AppToastType
import com.roro.uikit.components.toast.LocalToastHostState
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.AppTheme
import com.roro.uikit.theme.AppThemeConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DemoApp() {
    var config by remember { mutableStateOf(AppThemeConfig()) }
    AppTheme(config) {
        val background = AppColors.background
        val overlayState = remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
        val toastState = remember { AppToastHostState() }

        CompositionLocalProvider(
            LocalOverlay provides overlayState,
            LocalToastHostState provides toastState,
        ) {
            Box(Modifier.fillMaxSize().background(background)) {
                DemoNavScaffold(
                    config = config,
                    onConfigChange = { config = it },
                )
                overlayState.value?.invoke()
                AppToastHost(toastState)
            }
        }
    }
}

@Composable
private fun DemoNavScaffold(
    config: AppThemeConfig,
    onConfigChange: (AppThemeConfig) -> Unit,
) {
    val items = listOf(
        AppNavItem("概览", Lucide.ChevronRight),
        AppNavItem("任务", Lucide.Search),
        AppNavItem("控制", Lucide.Check),
    )
    var selected by remember { mutableIntStateOf(0) }
    var slidingNavBar by remember { mutableStateOf(false) }

    AppNavScaffold(
        items = items,
        selected = selected,
        onSelect = { selected = it },
        slidingNavBar = slidingNavBar,
    ) { page ->
        when (page) {
            0 -> DashboardPage()
            1 -> WorkPage()
            else -> ControlsPage(
                config = config,
                onConfigChange = onConfigChange,
                slidingNavBar = slidingNavBar,
                onSlidingNavBarChange = { slidingNavBar = it },
            )
        }
    }
}

@Composable
private fun DashboardPage() {
    var segment by remember { mutableIntStateOf(0) }
    var progress by remember { mutableFloatStateOf(0.42f) }
    var slider by remember { mutableFloatStateOf(0.68f) }
    var step by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Spacer(Modifier.height(12.dp))
            Text("产品概览", style = MaterialTheme.typography.displayMedium)
            Text("展示关键状态与指标", style = MaterialTheme.typography.bodyMedium, color = AppColors.textSecondary)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AppTag("已发布")
                AppStatusBadge("运行中", type = com.roro.uikit.components.misc.AppBadgeType.Success)
                AppStatusBadge("轻度告警", type = com.roro.uikit.components.misc.AppBadgeType.Default)
            }
        }
        item {
            AppFilledCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppSectionHeader("阶段进度")
                    AppStepIndicator(
                        steps = listOf("需求", "设计", "开发", "交付"),
                        current = step,
                    )
                    AppSegmentedControl(
                        options = listOf("迭代一", "迭代二", "迭代三", "迭代四"),
                        selected = segment,
                        onSelect = { segment = it; step = it },
                    )
                }
            }
        }
        item {
            AppFlatCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    AppSectionHeader("实时指标")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AppAvatar(initials = "UI")
                        Column {
                            Text("Roro UI Kit", style = MaterialTheme.typography.titleMedium)
                            Text("交互流畅度 96%", style = MaterialTheme.typography.bodySmall, color = AppColors.textSecondary)
                        }
                    }
                    AppProgressBar(progress = progress, onProgressChange = { progress = it })
                    AppSlider(value = slider, onValueChange = { slider = it })
                }
            }
        }
        item {
            AppFilledCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppSectionHeader("运行状态")
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        AppLoadingIndicator()
                        AppSpinnerIndicator()
                        AppDotsIndicator()
                    }
                    AppDivider()
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppStatusBadge("健康", type = com.roro.uikit.components.misc.AppBadgeType.Success)
                        AppStatusBadge("待优化", type = com.roro.uikit.components.misc.AppBadgeType.Default)
                        AppStatusBadge("高优先", type = com.roro.uikit.components.misc.AppBadgeType.Accent)
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkPage() {
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AppPullToRefresh(
        isRefreshing = refreshing,
        onRefresh = {
            scope.launch {
                refreshing = true
                delay(900)
                refreshing = false
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                Text("任务流", style = MaterialTheme.typography.displayMedium)
                Text("下拉刷新与卡片清单", style = MaterialTheme.typography.bodyMedium, color = AppColors.textSecondary)
            }
            appStickyHeader(key = "today", title = "今天")
            item {
                AppListGroup {
                    item {
                        AppListItem(
                            title = "检查设计交付",
                            subtitle = "剩余 2 项",
                            leadingIcon = Lucide.Check,
                            onClick = {},
                        )
                    }
                    item {
                        AppListItem(
                            title = "准备发布说明",
                            subtitle = "晚上 8:00",
                            leadingIcon = Lucide.Search,
                            onClick = {},
                        )
                    }
                }
            }
            item {
                AppSwipeAction(
                    actions = listOf(
                        AppSwipeActionItem("完成", Lucide.Check, AppColors.success) {},
                        AppSwipeActionItem("删除", Lucide.X, AppColors.error) {},
                    ),
                ) {
                    AppListItem(
                        title = "滑动操作示例",
                        subtitle = "向左滑动触发",
                        leadingIcon = Lucide.ChevronRight,
                        onClick = {},
                    )
                }
            }
            appStickyHeader(key = "plan", title = "计划")
            item {
                AppExpandableCard(
                    header = {
                        Text("本周里程碑", style = MaterialTheme.typography.titleMedium)
                    },
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("1. 完成组件验收", style = MaterialTheme.typography.bodyMedium)
                        Text("2. 补充交互文案", style = MaterialTheme.typography.bodyMedium)
                        Text("3. 上线 demo 模块", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlsPage(
    config: AppThemeConfig,
    onConfigChange: (AppThemeConfig) -> Unit,
    slidingNavBar: Boolean,
    onSlidingNavBarChange: (Boolean) -> Unit,
) {
    var tab by remember { mutableIntStateOf(0) }
    var checked by remember { mutableStateOf(true) }
    var radio by remember { mutableIntStateOf(0) }
    var search by remember { mutableStateOf("") }
    var input by remember { mutableStateOf("") }
    val overlay = LocalOverlay.current
    val toast = LocalToastHostState.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 140.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Text("控制面板", style = MaterialTheme.typography.displayMedium)
            Text("交互组件与主题设置", style = MaterialTheme.typography.bodyMedium, color = AppColors.textSecondary)
        }
        item {
            AppTabBar(
                tabs = listOf("按钮", "输入", "选择", "外观"),
                selected = tab,
                onSelect = { tab = it },
            )
        }
        when (tab) {
            0 -> {
                item {
                    AppFilledCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            AppSectionHeader("按钮与弹层")
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                AppPrimaryButton("主按钮", onClick = {
                                    toast.show(AppToastData("已保存", AppToastType.Success, Lucide.Check))
                                })
                                AppOutlineButton("次按钮", onClick = {
                                    toast.show(AppToastData("已复制", AppToastType.Accent))
                                })
                                AppAccentButton("强调", onClick = {
                                    toast.show(AppToastData("需要确认", AppToastType.Warning))
                                })
                            }
                            AppDivider()
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                AppOutlineButton("弹出对话", onClick = {
                                    overlay.value = {
                                        AppDialog(onDismissRequest = { overlay.value = null }) { dismiss ->
                                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                                Text("确认操作", style = MaterialTheme.typography.titleLarge)
                                                Text("该操作将影响当前项目设置。", style = MaterialTheme.typography.bodyMedium, color = AppColors.textSecondary)
                                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    AppOutlineButton("取消", onClick = dismiss)
                                                    AppPrimaryButton("继续", onClick = dismiss)
                                                }
                                            }
                                        }
                                    }
                                })
                                AppPrimaryButton("底部弹层", onClick = {
                                    overlay.value = {
                                        AppBottomSheet(onDismissRequest = { overlay.value = null }, glass = false) { dismiss ->
                                            Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                                AppSectionHeader("快速操作")
                                                AppListItem("同步数据", subtitle = "预计 3 秒", leadingIcon = Lucide.Search, onClick = dismiss)
                                                AppListItem("导出报告", subtitle = "PDF", leadingIcon = Lucide.Check, onClick = dismiss)
                                                AppListItem("关闭", leadingIcon = Lucide.X, onClick = dismiss)
                                            }
                                        }
                                    }
                                })
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                AppOutlineButton("左侧面板", onClick = {
                                    overlay.value = {
                                        AppSidePanel(
                                            onDismissRequest = { overlay.value = null },
                                            side = AppSidePanelSide.Start,
                                        ) { dismiss ->
                                            Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                                AppSectionHeader("项目导航")
                                                AppListItem("总览", leadingIcon = Lucide.Search, onClick = dismiss)
                                                AppListItem("任务", leadingIcon = Lucide.Check, onClick = dismiss)
                                                AppListItem("设置", leadingIcon = Lucide.ChevronRight, onClick = dismiss)
                                            }
                                        }
                                    }
                                })
                                AppOutlineButton("右侧面板", onClick = {
                                    overlay.value = {
                                        AppSidePanel(
                                            onDismissRequest = { overlay.value = null },
                                            side = AppSidePanelSide.End,
                                        ) { dismiss ->
                                            Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                                AppSectionHeader("快捷操作")
                                                AppListItem("分享链接", leadingIcon = Lucide.ChevronRight, onClick = dismiss)
                                                AppListItem("导出报告", leadingIcon = Lucide.Check, onClick = dismiss)
                                                AppListItem("关闭", leadingIcon = Lucide.X, onClick = dismiss)
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    }
                }
            }
            1 -> {
                item {
                    AppFilledCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            AppSectionHeader("输入")
                            AppTextField(
                                value = input,
                                onValueChange = { input = it },
                                label = "标题",
                                placeholder = "输入任务标题",
                                leadingIcon = Lucide.Search,
                                trailingIcon = if (input.isNotEmpty()) Lucide.X else null,
                                onTrailingIconClick = { input = "" },
                                isError = input.length in 1..2,
                                errorMessage = if (input.length in 1..2) "至少 3 个字" else null,
                            )
                            AppSearchBar(
                                value = search,
                                onValueChange = { search = it },
                                placeholder = "搜索任务",
                            )
                            AppExpandableSearchBar()
                        }
                    }
                }
            }
            2 -> {
                item {
                    AppFilledCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            AppSectionHeader("选择")
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AppCheckbox(checked = checked, onCheckedChange = { checked = it })
                                Text("启用推送", style = MaterialTheme.typography.bodyMedium)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AppRadioButton(selected = radio == 0, onClick = { radio = 0 })
                                Text("每日提醒", style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.size(12.dp))
                                AppRadioButton(selected = radio == 1, onClick = { radio = 1 })
                                Text("仅工作日", style = MaterialTheme.typography.bodyMedium)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                AppSwitch(checked = slidingNavBar, onCheckedChange = onSlidingNavBarChange)
                                Text(if (slidingNavBar) "滑动导航栏" else "浮动导航栏", style = MaterialTheme.typography.bodyMedium)
                            }
                            AppSwitch(checked = checked, onCheckedChange = { checked = it }, variant = AppSwitchVariant.Slim)
                        }
                    }
                }
            }
            else -> {
                item {
                    AppFilledCard {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            AppSectionHeader("主题")
                            AppAccentPicker(
                                presets = config.accentPresets,
                                current = config.accent,
                                onSelect = { onConfigChange(config.copy(accent = it, accentLight = it.copy(alpha = 0.12f))) },
                            )
                        }
                    }
                }
            }
        }
    }
}
