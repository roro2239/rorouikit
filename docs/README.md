# RoroUIKit

基于 Jetpack Compose 的 Android 纯 UI 组件库，无需引入任何资源文件（res），零配置接入。

---

## 目录

- [接入方式](#接入方式)
- [主题系统](#主题系统)
- [组件列表](#组件列表)
  - [按钮 Button](#按钮-button)
  - [卡片 Card](#卡片-card)
  - [输入框 Input](#输入框-input)
  - [头像 Avatar](#头像-avatar)
  - [选择控件 Selection](#选择控件-selection)
  - [列表项 ListItem](#列表项-listitem)
  - [加载指示器 Loading](#加载指示器-loading)
  - [进度条 ProgressBar](#进度条-progressbar)
  - [滑块 Slider](#滑块-slider)
  - [标签栏 TabBar](#标签栏-tabbar)
  - [分段选择器 SegmentedControl](#分段选择器-segmentedcontrol)
  - [导航栏 NavBar](#导航栏-navbar)
  - [对话框 Dialog](#对话框-dialog)
  - [底部弹窗 BottomSheet](#底部弹窗-bottomsheet)
  - [Toast 提示](#toast-提示)
  - [可展开卡片 ExpandableCard](#可展开卡片-expandablecard)
  - [下拉刷新 PullToRefresh](#下拉刷新-pulltorefresh)
  - [滑动操作 SwipeAction](#滑动操作-swipeaction)
  - [步骤指示器 StepIndicator](#步骤指示器-stepindicator)
  - [粘性头部 StickyHeader](#粘性头部-stickyheader)
  - [杂项 Misc](#杂项-misc)

---

## 接入方式

本库为纯 UI 库，**无需引入任何 res 资源**，使用方也无需在自己项目中添加任何资源文件。

### 必要依赖

本库依赖以下库，接入时需一并引入：

```kotlin
dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.xx.xx"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")

    // G2 连续圆角（Capsule）
    implementation("com.kyant:capsule:x.x.x")

    // 毛玻璃 Backdrop 效果（Toast / BottomSheet glass 模式需要）
    implementation("com.kyant:backdrop:x.x.x")

    // Lucide 图标（搜索框等内置图标）
    implementation("com.composables:icons-lucide:x.x.x")

    // Activity Compose（BackHandler）
    implementation("androidx.activity:activity-compose:x.x.x")
}
```

---

## 主题系统

所有组件颜色、尺寸、字体均从主题读取，**必须用 `AppTheme` 包裹根组件**。

### 快速开始

```kotlin
import com.roro.uikit.theme.AppTheme
import com.roro.uikit.theme.AppThemeConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // 你的页面内容
            }
        }
    }
}
```

### AppThemeConfig 参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `accent` | `Color` | `#2563EB` | 主强调色 |
| `accentLight` | `Color` | `#EFF6FF` | 强调色浅版（用于背景） |
| `background` | `Color` | `#F8F9FB` | 页面背景色 |
| `surface` | `Color` | `#FFFFFF` | 卡片/组件表面色 |
| `surfaceVariant` | `Color` | `#F1F3F7` | 次级表面色 |
| `border` | `Color` | `#E8EAF0` | 边框色 |
| `textPrimary` | `Color` | `#111111` | 主文本色 |
| `textSecondary` | `Color` | `#8A8FA8` | 次文本色 |
| `textTertiary` | `Color` | `#B8BCCC` | 三级文本色（占位符等） |
| `success` | `Color` | `#10B981` | 成功色 |
| `warning` | `Color` | `#F59E0B` | 警告色 |
| `error` | `Color` | `#EF4444` | 错误色 |
| `cornerRadius` | `Dp` | `16.dp` | 全局圆角半径 |
| `buttonHeight` | `Dp` | `48.dp` | 按钮高度 |
| `inputHeight` | `Dp` | `56.dp` | 输入框高度 |
| `cardPadding` | `Dp` | `16.dp` | 卡片内边距 |
| `enableMonet` | `Boolean` | `true` | 是否启用 Material You 取色 |
| `transparentStatusBar` | `Boolean` | `true` | 状态栏透明 |
| `transparentNavBar` | `Boolean` | `true` | 导航栏透明 |
| `edgeToEdgeNavBar` | `Boolean` | `true` | 沉浸式导航栏 |

### 自定义主题

```kotlin
AppTheme(
    config = AppThemeConfig(
        accent = Color(0xFF7C3AED),   // 紫色主题
        cornerRadius = 12.dp,
        enableMonet = false,
    )
) {
    // 内容
}
```

### 在组件中读取主题值

```kotlin
import com.roro.uikit.theme.AppColors
import com.roro.uikit.theme.LocalAppTheme

@Composable
fun MyComponent() {
    val theme = LocalAppTheme.current   // 完整配置对象
    val colors = AppColors              // 快捷访问（等同于 LocalAppTheme.current）

    Box(Modifier.background(colors.surface))
}
```

### Material You（Monet）动态取色

```kotlin
import com.roro.uikit.theme.monetAccentScheme

@Composable
fun MyScreen() {
    val monet = monetAccentScheme()  // Android 12 以下返回 null
    AppTheme(
        config = AppThemeConfig(
            accent = monet?.accent ?: Color(0xFF2563EB),
            accentLight = monet?.accentLight ?: Color(0xFFEFF6FF),
        )
    ) { /* ... */ }
}
```

### 深色模式

`AppTheme` 自动检测系统深色模式并切换预设深色配色，无需手动处理。用户自定义的 `accent` 和所有尺寸/字体配置在深色模式下保留。

---

## 组件列表

---

### 按钮 Button

**包路径**：`com.roro.uikit.components.button`

#### AppPrimaryButton

主要按钮，深色背景，用于页面主操作。

```kotlin
import com.roro.uikit.components.button.AppPrimaryButton
import com.roro.uikit.components.button.AppButtonDefaults

AppPrimaryButton(
    text = "确认",
    onClick = { /* 点击回调 */ },
    modifier = Modifier.fillMaxWidth(),
    enabled = true,
    icon = Icons.Default.Check,         // 可选，左侧图标
    colors = AppButtonDefaults.primaryColors(),
    style = AppButtonDefaults.style(),
)
```

#### AppOutlineButton

轮廓按钮，透明背景加描边，用于次要操作。

```kotlin
import com.roro.uikit.components.button.AppOutlineButton

AppOutlineButton(
    text = "取消",
    onClick = { /* 点击回调 */ },
    enabled = true,
    icon = null,
)
```

#### AppAccentButton

强调按钮，使用主题 accent 色填充，用于最高优先级操作。

```kotlin
import com.roro.uikit.components.button.AppAccentButton

AppAccentButton(
    text = "立即开始",
    onClick = { /* 点击回调 */ },
)
```

#### 自定义按钮外观

三种按钮均支持 `content` 参数完全接管渲染，同时复用弹性点击动画等交互逻辑：

```kotlin
AppPrimaryButton(
    text = "自定义",
    onClick = { },
    content = { text, onClick, enabled, icon, interactionSource ->
        // 自行绘制外观，interactionSource 已包含弹性动画
        Button(onClick = onClick, interactionSource = interactionSource) {
            Text(text)
        }
    }
)
```

#### AppButtonColors 参数

| 参数 | 说明 |
|------|------|
| `containerColor` | 背景色 |
| `contentColor` | 前景色（文字/图标） |
| `borderColor` | 边框色（Outline 按钮专用） |
| `disabledContainerColor` | 禁用状态背景色 |
| `disabledContentColor` | 禁用状态前景色 |

#### AppButtonStyle 参数

| 参数 | 类型 | 说明 |
|------|------|------|
| `height` | `Dp` | 按钮高度，默认读取主题 `buttonHeight` |
| `horizontalPadding` | `Dp` | 水平内边距 |
| `textStyle` | `TextStyle` | 文字样式 |
| `iconSize` | `Dp` | 图标尺寸 |
| `borderWidth` | `Dp` | 边框宽度（Outline 专用） |

---

### 卡片 Card

**包路径**：`com.roro.uikit.components.card`

#### AppFlatCard

白色背景平面卡片，与页面灰色背景形成层次感。

```kotlin
import com.roro.uikit.components.card.AppFlatCard
import com.roro.uikit.components.card.AppCardDefaults

AppFlatCard(
    modifier = Modifier.fillMaxWidth(),
    onClick = { /* 可点击，不传则静态 */ },
    colors = AppCardDefaults.flatColors(),
    style = AppCardDefaults.style(),
) {
    // ColumnScope 内容
    Text("卡片标题")
    Text("卡片内容")
}
```

#### AppFilledCard

浅灰背景填充卡片，用于在白色表面上区分内容区域。

```kotlin
import com.roro.uikit.components.card.AppFilledCard

AppFilledCard(onClick = { }) {
    Text("填充卡片")
}
```

#### AppCardStyle 参数

| 参数 | 说明 |
|------|------|
| `paddingStart/End/Top/Bottom` | 四方向内边距，默认均为主题 `cardPadding` |

---

### 输入框 Input

**包路径**：`com.roro.uikit.components.input`

#### AppTextField

标准带边框输入框，支持标签、占位符、前后图标、错误提示和密码模式。

```kotlin
import com.roro.uikit.components.input.AppTextField
import com.roro.uikit.components.input.AppTextFieldDefaults

var text by remember { mutableStateOf("") }

AppTextField(
    value = text,
    onValueChange = { text = it },
    label = "邮箱",
    placeholder = "请输入邮箱地址",
    leadingIcon = Icons.Default.Email,
    trailingIcon = Icons.Default.Clear,
    onTrailingIconClick = { text = "" },
    isError = text.isEmpty(),
    errorMessage = "不能为空",
    password = false,
    singleLine = true,
    enabled = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    colors = AppTextFieldDefaults.colors(),
)
```

#### AppSearchBar

固定宽度搜索框，胶囊形状，有内容时显示清除按钮。

```kotlin
import com.roro.uikit.components.input.AppSearchBar

var query by remember { mutableStateOf("") }

AppSearchBar(
    value = query,
    onValueChange = { query = it },
    placeholder = "搜索",
    onSearch = { keyword -> /* 搜索回调 */ },
)
```

#### AppExpandableSearchBar

可展开搜索栏，初始为图标，点击后以动画展开为完整输入框。

```kotlin
import com.roro.uikit.components.input.AppExpandableSearchBar

AppExpandableSearchBar(
    modifier = Modifier.fillMaxWidth(),
    placeholder = "搜索",
    iconAlignment = Alignment.End,       // 收起时图标的水平位置
    onSearch = { keyword -> /* 回调 */ },
)
```

---

### 头像 Avatar

**包路径**：`com.roro.uikit.components.avatar`

```kotlin
import com.roro.uikit.components.avatar.AppAvatar
import com.roro.uikit.components.avatar.AppAvatarShape

// 文字首字母头像
AppAvatar(
    initials = "张三",
    size = 40.dp,
    shape = AppAvatarShape.Circle,         // Circle 或 Rounded
    containerColor = AppColors.accentLight,
    contentColor = AppColors.accent,
)

// 图标头像
AppAvatar(
    icon = Icons.Default.Person,
    size = 48.dp,
    shape = AppAvatarShape.Rounded,
)
```

---

### 选择控件 Selection

**包路径**：`com.roro.uikit.components.selection`

#### AppCheckbox

Canvas 自绘复选框，带弹性对勾动画。

```kotlin
import com.roro.uikit.components.selection.AppCheckbox
import com.roro.uikit.components.selection.AppCheckboxDefaults

var checked by remember { mutableStateOf(false) }

AppCheckbox(
    checked = checked,
    onCheckedChange = { checked = it },
    enabled = true,
    colors = AppCheckboxDefaults.colors(),
    style = AppCheckboxDefaults.style(),
)
```

#### AppRadioButton

Canvas 自绘单选按钮，带缩放动画。

```kotlin
import com.roro.uikit.components.selection.AppRadioButton
import com.roro.uikit.components.selection.AppRadioDefaults

var selected by remember { mutableStateOf(0) }

Column {
    listOf("选项A", "选项B", "选项C").forEachIndexed { i, label ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppRadioButton(
                selected = selected == i,
                onClick = { selected = i },
                enabled = true,
            )
            Text(label)
        }
    }
}
```

---

### 列表项 ListItem

**包路径**：`com.roro.uikit.components.listitem`

`AppListGroup` 是列表容器，内部使用 `AppListGroupScope.item {}` 添加每行，自动插入分割线。

```kotlin
import com.roro.uikit.components.listitem.AppListGroup
import com.roro.uikit.components.listitem.AppListItem

AppListGroup(modifier = Modifier.fillMaxWidth()) {
    item {
        AppListItem(
            title = "账号设置",
            subtitle = "管理你的账号信息",        // 可选副标题
            leadingIcon = Icons.Default.Person,   // 可选前置图标
            onClick = { /* 点击 */ },
            // trailing = { Switch(...) }         // 可选自定义尾部
        )
    }
    item {
        AppListItem(
            title = "通知",
            leadingIcon = Icons.Default.Notifications,
            trailing = {
                Switch(checked = true, onCheckedChange = {})
            },
        )
    }
}
```

---

### 加载指示器 Loading

**包路径**：`com.roro.uikit.components.loading`

#### AppLoadingIndicator

双弧追逐效果，两条弧线一顺一逆旋转。

```kotlin
import com.roro.uikit.components.loading.AppLoadingIndicator

AppLoadingIndicator(
    size = 24.dp,
    strokeWidth = 2.5.dp,
    color = AppColors.accent,
    secondaryColor = AppColors.accent.copy(alpha = 0.35f),
)
```

#### AppSpinnerIndicator

单弧旋转，固定弧长持续旋转。

```kotlin
import com.roro.uikit.components.loading.AppSpinnerIndicator

AppSpinnerIndicator(
    size = 24.dp,
    strokeWidth = 2.5.dp,
    color = AppColors.accent,
    trackColor = AppColors.accent.copy(alpha = 0.15f),
    sweepAngle = 270f,
)
```

#### AppDotsIndicator

三点跳动动画。

```kotlin
import com.roro.uikit.components.loading.AppDotsIndicator

AppDotsIndicator(
    dotSize = 7.dp,
    color = AppColors.accent,
    spacing = 5.dp,
)
```

---

### 进度条 ProgressBar

**包路径**：`com.roro.uikit.components.progress`

```kotlin
import com.roro.uikit.components.progress.AppProgressBar

AppProgressBar(
    progress = 0.65f,       // 0f ~ 1f
    modifier = Modifier.fillMaxWidth(),
)
```

---

### 滑块 Slider

**包路径**：`com.roro.uikit.components.slider`

```kotlin
import com.roro.uikit.components.slider.AppSlider
import com.roro.uikit.components.slider.AppSliderDefaults

var value by remember { mutableStateOf(0.5f) }

AppSlider(
    value = value,
    onValueChange = { value = it },
    valueRange = 0f..1f,
    steps = 0,              // 0 = 连续，>0 = 步进
    colors = AppSliderDefaults.colors(),
    style = AppSliderDefaults.style(),
)
```

---

### 标签栏 TabBar

**包路径**：`com.roro.uikit.components.tab`

顶部标签页切换，胶囊滑动背景指示器。

```kotlin
import com.roro.uikit.components.tab.AppTabBar
import com.roro.uikit.components.tab.AppTabBarDefaults

var selectedTab by remember { mutableStateOf(0) }

AppTabBar(
    tabs = listOf("全部", "进行中", "已完成"),
    selected = selectedTab,
    onSelect = { selectedTab = it },
    modifier = Modifier.fillMaxWidth(),
    colors = AppTabBarDefaults.colors(),
    style = AppTabBarDefaults.style(),
)
```

---

### 分段选择器 SegmentedControl

**包路径**：`com.roro.uikit.components.segmented`

比 TabBar 更紧凑的分段控制器。

```kotlin
import com.roro.uikit.components.segmented.AppSegmentedControl

var selected by remember { mutableStateOf(0) }

AppSegmentedControl(
    options = listOf("天", "周", "月"),
    selected = selected,
    onSelect = { selected = it },
    modifier = Modifier.fillMaxWidth(),
    containerColor = AppColors.surfaceVariant,
    indicatorColor = AppColors.surface,
    selectedTextColor = AppColors.textPrimary,
    unselectedTextColor = AppColors.textSecondary,
)
```

---

### 导航栏 NavBar

**包路径**：`com.roro.uikit.components.navbar`

#### 数据模型

```kotlin
import com.roro.uikit.components.AppNavItem

val navItems = listOf(
    AppNavItem(label = "首页", icon = Icons.Default.Home),
    AppNavItem(label = "发现", icon = Icons.Default.Explore),
    AppNavItem(label = "我的", icon = Icons.Default.Person),
)
```

#### AppFloatingNavBar

浮动胶囊导航栏，选中项展开显示标签，带弹性动画。

```kotlin
import com.roro.uikit.components.navbar.AppFloatingNavBar
import com.roro.uikit.components.navbar.AppNavBarDefaults

var selected by remember { mutableStateOf(0) }

Box(Modifier.fillMaxSize()) {
    // 页面内容
    AppFloatingNavBar(
        items = navItems,
        selected = selected,
        onSelect = { selected = it },
        modifier = Modifier.align(Alignment.BottomCenter),
        transparentNavBar = true,
        colors = AppNavBarDefaults.colors(),
    )
}
```

#### AppSlidingNavBar

滑动指示器导航栏，所有项始终显示，背景指示器平滑滑动。

```kotlin
import com.roro.uikit.components.navbar.AppSlidingNavBar

AppSlidingNavBar(
    items = navItems,
    selected = selected,
    onSelect = { selected = it },
    modifier = Modifier.align(Alignment.BottomCenter),
)
```

#### AppNavScaffold

完整导航脚手架，集成页面切换动画与底部导航栏。

```kotlin
import com.roro.uikit.components.navbar.AppNavScaffold

var selected by remember { mutableStateOf(0) }

AppNavScaffold(
    items = navItems,
    selected = selected,
    onSelect = { selected = it },
    slidingNavBar = false,   // false = 浮动胶囊；true = 滑动指示器
) { page ->
    when (page) {
        0 -> HomeScreen()
        1 -> ExploreScreen()
        2 -> ProfileScreen()
    }
}
```

---

### 对话框 Dialog

**包路径**：`com.roro.uikit.components.dialog`

居中弹出对话框，入场弹性缩放动画，点击遮罩或返回键关闭。

```kotlin
import com.roro.uikit.components.dialog.AppDialog
import com.roro.uikit.components.dialog.AppDialogDefaults

var showDialog by remember { mutableStateOf(false) }

if (showDialog) {
    AppDialog(
        onDismissRequest = { showDialog = false },
        colors = AppDialogDefaults.colors(),
        style = AppDialogDefaults.style(),
    ) { dismiss ->
        // ColumnScope，dismiss() 可主动关闭
        Text("确认删除？", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppOutlineButton("取消", onClick = dismiss, modifier = Modifier.weight(1f))
            AppAccentButton("删除", onClick = { /* 操作 */; dismiss() }, modifier = Modifier.weight(1f))
        }
    }
}
```

---

### 底部弹窗 BottomSheet

**包路径**：`com.roro.uikit.components.sheet`

支持拖拽关闭，底部圆角跟随屏幕物理圆角，可选毛玻璃背景。

**注意**：`glass = true` 时需在提供 `LocalBackdrop` 的作用域内使用（通常在 `AppTheme` 中已配置）。

```kotlin
import com.roro.uikit.components.sheet.AppBottomSheet
import com.roro.uikit.components.sheet.AppBottomSheetDefaults

var showSheet by remember { mutableStateOf(false) }

if (showSheet) {
    AppBottomSheet(
        onDismissRequest = { showSheet = false },
        glass = false,          // true = 毛玻璃背景
        showHandle = true,      // 顶部拖拽手柄
        colors = AppBottomSheetDefaults.colors(),
        style = AppBottomSheetDefaults.style(),
    ) { dismiss ->
        // ColumnScope，内部自动支持滚动
        Text("底部弹窗标题", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        AppPrimaryButton("完成", onClick = dismiss, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
    }
}
```

#### AppBottomSheetStyle 参数

| 参数 | 说明 |
|------|------|
| `horizontalPadding` | 弹窗两侧与屏幕边缘的间距 |
| `bottomPadding` | 底部额外内边距 |
| `maxHeightFraction` | 最大高度占屏幕比例，默认 `0.9f` |
| `fillNavBar` | 是否填充系统导航栏区域 |
| `handleWidth/handleHeight` | 拖拽手柄尺寸 |

---

### Toast 提示

**包路径**：`com.roro.uikit.components.toast`

全局 Toast，固定在屏幕顶部，从顶部滑入，带毛玻璃背景。

#### 步骤 1：在根布局放置 AppToastHost

```kotlin
import com.roro.uikit.components.toast.AppToastHost
import com.roro.uikit.components.toast.AppToastHostState
import com.roro.uikit.components.toast.LocalToastHostState

val toastState = remember { AppToastHostState() }

CompositionLocalProvider(LocalToastHostState provides toastState) {
    Box(Modifier.fillMaxSize()) {
        // 页面内容
        YourPageContent()

        // Toast 宿主，放在最顶层
        AppToastHost(state = toastState)
    }
}
```

#### 步骤 2：在任意子组件中触发 Toast

```kotlin
import com.roro.uikit.components.toast.AppToastData
import com.roro.uikit.components.toast.AppToastType
import com.roro.uikit.components.toast.LocalToastHostState

val toast = LocalToastHostState.current

Button(onClick = {
    toast.show(AppToastData(
        message = "操作成功",
        type = AppToastType.Success,
        icon = Icons.Default.Check,
        durationMs = 2000,
    ))
}) { Text("触发 Toast") }
```

#### AppToastType 枚举

| 值 | 说明 |
|----|------|
| `Default` | 默认灰色 |
| `Accent` | 主题色 |
| `Success` | 绿色 |
| `Error` | 红色 |
| `Warning` | 橙色 |

---

### 可展开卡片 ExpandableCard

**包路径**：`com.roro.uikit.components.expandable`

```kotlin
import com.roro.uikit.components.expandable.AppExpandableCard
import com.roro.uikit.components.expandable.AppExpandableCardDefaults

AppExpandableCard(
    title = "高级设置",
    modifier = Modifier.fillMaxWidth(),
    colors = AppExpandableCardDefaults.colors(),
    style = AppExpandableCardDefaults.style(),
) {
    // 展开后的内容（ColumnScope）
    Text("展开后显示的详细内容")
}
```

---

### 下拉刷新 PullToRefresh

**包路径**：`com.roro.uikit.components.refresh`

```kotlin
import com.roro.uikit.components.refresh.AppPullToRefresh

var refreshing by remember { mutableStateOf(false) }

AppPullToRefresh(
    refreshing = refreshing,
    onRefresh = {
        refreshing = true
        // 执行刷新操作
        refreshing = false
    },
) {
    // 可滚动内容
    LazyColumn { /* ... */ }
}
```

---

### 滑动操作 SwipeAction

**包路径**：`com.roro.uikit.components.swipe`

列表项左右滑动显示操作按钮。

```kotlin
import com.roro.uikit.components.swipe.AppSwipeAction

AppSwipeAction(
    startActions = { /* 左滑显示的操作，例如收藏 */ },
    endActions = { /* 右滑显示的操作，例如删除 */ },
) {
    // 主内容（列表行）
    AppListItem(title = "可滑动的列表项")
}
```

---

### 步骤指示器 StepIndicator

**包路径**：`com.roro.uikit.components.step`

```kotlin
import com.roro.uikit.components.step.AppStepIndicator

AppStepIndicator(
    steps = 4,
    currentStep = 2,       // 从 0 开始
    modifier = Modifier.fillMaxWidth(),
)
```

---

### 粘性头部 StickyHeader

**包路径**：`com.roro.uikit.components.sticky`

```kotlin
import com.roro.uikit.components.sticky.AppStickyHeader

LazyColumn {
    AppStickyHeader(title = "今天") {
        // 分组内的列表项
        items(todayItems) { item ->
            AppListItem(title = item.name)
        }
    }
    AppStickyHeader(title = "昨天") {
        items(yesterdayItems) { item ->
            AppListItem(title = item.name)
        }
    }
}
```

---

### 杂项 Misc

**包路径**：`com.roro.uikit.components.misc`

杂项组件，包含常用的小型 UI 元素（分割线、标签、徽章等），详见 `MiscDefaults`。

---

## 设计原则

- **零资源引入**：库内不包含任何 drawable、color、string 等 res 资源，所有视觉效果通过 Compose 代码实现。
- **G2 连续圆角**：全局使用苹果风格的 G2 连续曲线圆角（通过 `kyant/capsule` 实现），比普通圆角更柔和。
- **弹性交互**：所有可点击组件内置弹性缩放动画（spring），替代 Material 默认涟漪效果。
- **主题即配置**：所有颜色、尺寸通过 `AppThemeConfig` 统一配置，支持运行时热切换。
- **深色模式自动适配**：`AppTheme` 自动检测系统主题，无需手动处理深浅模式切换。
