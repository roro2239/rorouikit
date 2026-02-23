# RoroUIKit — `com.roro.uikit`

Android Jetpack Compose UI 组件库，现代扁平风格，无阴影，G2 连续圆角。

## 集成

```groovy
// build.gradle
implementation project(':rorouikit')
```

无需定义 `themes.xml` 或 `colors.xml`，库通过 manifest merge 自动注入默认主题。

---

## 主题系统

```kotlin
// 全部使用默认值（蓝色主题，12dp 圆角）
AppTheme { ... }

// 按需覆盖任意参数
AppTheme(AppThemeConfig(
    accent = Color(0xFF7C3AED),
    cornerRadius = 8.dp,
    buttonHeight = 44.dp,
)) { ... }
```

### `AppThemeConfig` 参数

**颜色**

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `accent` | `#2563EB` | 主强调色 |
| `accentLight` | `#EFF6FF` | 强调色浅色背景 |
| `background` | `#F8F9FB` | 页面背景 |
| `surface` | `#FFFFFF` | 卡片/白色表面 |
| `surfaceVariant` | `#F1F3F7` | 次级表面 |
| `border` | `#E8EAF0` | 边框 |
| `textPrimary` | `#111111` | 主文字 |
| `textSecondary` | `#8A8FA8` | 次要文字 |
| `textTertiary` | `#B8BCCC` | 占位文字 |
| `success/warning/error` | — | 语义色 |
| `successLight/errorLight` | — | 语义色浅色背景 |

**尺寸**

| 参数 | 默认值 |
|------|--------|
| `cornerRadius` | `12.dp` |
| `buttonHeight` | `48.dp` |
| `buttonHorizontalPadding` | `20.dp` |
| `cardPadding` | `16.dp` |
| `navBarItemHeight` | `44.dp` |
| `iconSize` | `18.dp` |

**字体大小**

`displayLargeFontSize` / `displayMediumFontSize` / `headlineLargeFontSize` / `headlineMediumFontSize` / `titleLargeFontSize` / `titleMediumFontSize` / `bodyLargeFontSize` / `bodyMediumFontSize` / `labelLargeFontSize` / `labelMediumFontSize` / `labelSmallFontSize`

**其他**

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `enableMonet` | `true` | 是否支持莫奈取色 |
| `accentPresets` | 8 种预设色 | 颜色选择器预设列表 |

### 在组件内读取 Token

```kotlin
AppColors.accent
AppColors.border
AppColors.textSecondary
```

---

## 主题动态切换

```kotlin
var config by remember { mutableStateOf(AppThemeConfig()) }
AppTheme(config) {
    MyApp(
        onAccentChange = { color ->
            config = config.copy(accent = color, accentLight = color.copy(alpha = 0.12f))
        }
    )
}
```

---

## 组件重写系统

所有组件支持两种定制层级：

**1. 参数覆盖**（颜色/尺寸/动画）
```kotlin
AppPrimaryButton("确认", onClick, colors = AppButtonDefaults.primaryColors().copy(containerColor = Color.Red))
```

**2. 完全替换**（`content` lambda）

无动画状态的组件，`content` 接收数据参数：
```kotlin
AppPrimaryButton("确认", onClick) { text, onClick, enabled, icon, interactionSource ->
    Box(Modifier.bounceClick(interactionSource).clickable { onClick() }) { Text(text) }
}

AppTag("标签") { text -> Box(Modifier.background(Color.Red)) { Text(text) } }

AppStatusBadge("成功", AppBadgeType.Success) { text, type, colors ->
    Text(text, color = colors.contentColor)
}
```

有动画状态的组件，先用 `rememberXxxState` 获取 state，再传给组件：
```kotlin
// Switch：state 包含 trackColor / thumbColor / thumbOffset / checked / onCheckedChange
val state = rememberAppSwitchState(checked, onCheckedChange)
AppSwitch(checked, onCheckedChange) { state ->
    Box(Modifier.size(56.dp, 32.dp).clickable { state.onCheckedChange(!state.checked) }) {
        Canvas(Modifier.fillMaxSize()) { drawRoundRect(state.trackColor) }
        Box(Modifier.offset(x = 4.dp + 28.dp * state.thumbOffset).size(24.dp).background(state.thumbColor))
    }
}

// NavBar：state 包含 items / selected / onSelect / itemTints
AppFloatingNavBar(items, selected, onSelect) { state ->
    Row { state.items.forEachIndexed { i, item ->
        Icon(item.icon, tint = state.itemTints[i], modifier = Modifier.clickable { state.onSelect(i) })
    }}
}

// ExpandableCard：outerContent 接收 state / header / innerContent
// state 包含 isExpanded / onToggle / chevronRotation / heightFraction
AppExpandableCard(header = { Text("标题") }) { state, header, innerContent ->
    Column {
        Row(Modifier.clickable { state.onToggle() }) {
            header()
            Icon(Lucide.ChevronDown, null, Modifier.rotate(state.chevronRotation))
        }
        if (state.heightFraction > 0f) innerContent()
    }
}
```

---

## 组件

### 按钮

三种变体：`AppPrimaryButton`、`AppOutlineButton`、`AppAccentButton`。

```kotlin
AppPrimaryButton("确认", onClick = {})
AppOutlineButton("取消", onClick = {})
AppAccentButton("购买", onClick = {})

AppPrimaryButton("下载", onClick = {}, icon = Lucide.Download)
AppPrimaryButton("禁用", onClick = {}, enabled = false)

AppPrimaryButton(
    "确认", onClick = {},
    colors = AppButtonDefaults.primaryColors().copy(containerColor = Color.Red),
    style = AppButtonDefaults.style().copy(height = 40.dp),
)
```

**`AppButtonColors`**：`containerColor` / `contentColor` / `borderColor` / `disabledContainerColor` / `disabledContentColor` / `disabledBorderColor`

**`AppButtonStyle`**：`height` / `horizontalPadding` / `textStyle` / `iconSize` / `iconSpacing` / `borderWidth`

---

### 卡片

```kotlin
AppFlatCard(Modifier.fillMaxWidth()) { Text("内容") }
AppFilledCard(Modifier.fillMaxWidth()) { Text("内容") }

AppFlatCard(Modifier.fillMaxWidth(), onClick = {}) { Text("可点击") }

AppFlatCard(colors = AppCardDefaults.flatColors().copy(borderColor = Color.Red)) { ... }
AppFlatCard(style = AppCardStyle(horizontal = 20.dp, vertical = 12.dp)) { ... }
```

**`AppCardStyle`** 构造函数：`AppCardStyle(all)` / `AppCardStyle(horizontal, vertical)` / `AppCardStyle(start, top, end, bottom)`

---

### 输入框

```kotlin
AppTextField(value, onValueChange, label = "邮箱", leadingIcon = Lucide.Mail)
AppTextField(value, onValueChange, password = true)
AppTextField(value, onValueChange, isError = true, errorMessage = "已被占用")
AppTextField(value, onValueChange, enabled = false)
AppTextField(value, onValueChange, trailingIcon = Lucide.Eye, onTrailingIconClick = {})

AppSearchBar(value, onValueChange)
AppExpandableSearchBar(Modifier.fillMaxWidth(), iconAlignment = Alignment.Start)

AppExpandableSearchBar(
    Modifier.fillMaxWidth(),
    animation = AppSearchBarAnimationDefaults.animation().copy(
        expandSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
    ),
)

AppTextField(
    value, onValueChange,
    colors = AppTextFieldDefaults.colors().copy(focusedBorderColor = Color.Green),
)
```

**`AppTextFieldColors`**：`focusedBorderColor` / `unfocusedBorderColor` / `containerColor` / `focusedLabelColor` / `unfocusedLabelColor` / `cursorColor` / `textColor` / `placeholderColor` / `focusedIconColor` / `unfocusedIconColor` / `errorBorderColor` / `errorLabelColor` / `errorTextColor` / `disabledBorderColor` / `disabledTextColor` / `disabledLabelColor`

---

### 导航栏

```kotlin
AppFloatingNavBar(items, selected, onSelect)

AppFloatingNavBar(
    items, selected, onSelect,
    colors = AppNavBarDefaults.colors().copy(activeColor = Color(0xFF7C3AED)),
    style = AppNavBarDefaults.style().copy(itemHeight = 40.dp, iconSize = 20.dp),
    animation = AppNavBarAnimationDefaults.animation().copy(tintSpec = spring()),
)

AppNavScaffold(items, selected, onSelect) { page ->
    when (page) {
        0 -> HomePage()
        1 -> SettingsPage()
    }
}

AppNavScaffold(
    items, selected, onSelect,
    animation = AppNavScaffoldAnimationDefaults.animation().copy(pageSpec = tween(400)),
) { ... }
```

**`AppNavBarStyle`**：`itemHeight` / `itemHorizontalPadding` / `iconSize`

**`AppNavBarAnimation`**：`tintSpec` / `labelEnterSpec` / `labelExpandSpec` / `labelExitSpec` / `labelShrinkSpec`

**`AppNavScaffoldAnimation`**：`pageSpec` / `pageFadeOutSpec`

#### AppSlidingNavBar

所有标签常显，选中项用滑动指示器标记，带 iOS 风格压缩弹跳动画。

```kotlin
AppSlidingNavBar(items, selected, onSelect)

AppSlidingNavBar(
    items, selected, onSelect,
    colors = AppNavBarDefaults.colors().copy(activeColor = Color(0xFF7C3AED)),
    animation = AppSlidingNavBarAnimationDefaults.animation().copy(
        indicatorXSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
    ),
)
```

**`AppSlidingNavBarAnimation`**：`tintSpec` / `indicatorXSpec` / `indicatorScaleCompressSpec` / `indicatorScaleBounceSpec`

---

### 开关

```kotlin
AppSwitch(checked = true, onCheckedChange = { ... })
AppSwitch(checked = false, onCheckedChange = { ... }, enabled = false)
AppSwitch(checked = true, onCheckedChange = { ... }, variant = AppSwitchVariant.Slim)

AppSwitch(
    checked = true, onCheckedChange = { ... },
    colors = AppSwitchDefaults.colors().copy(checkedTrackColor = Color.Green),
    animation = AppSwitchAnimationDefaults.animation().copy(
        thumbOffsetSpec = spring(dampingRatio = 0.4f, stiffness = 600f),
    ),
)
```

**`AppSwitchAnimation`**：`trackSpec` / `thumbColorSpec` / `thumbOffsetSpec`

---

### 复选框 & 单选按钮

```kotlin
AppCheckbox(checked = true, onCheckedChange = { ... })
AppCheckbox(checked = false, onCheckedChange = { ... }, enabled = false)

AppRadioButton(selected = true, onClick = { ... })
AppRadioButton(selected = false, onClick = { ... }, enabled = false)
```

**`AppCheckboxColors`**：`checkedBoxColor` / `uncheckedBoxColor` / `checkmarkColor` / `disabledCheckedBoxColor` / `disabledUncheckedBoxColor`

**`AppRadioColors`**：`selectedColor` / `unselectedColor` / `disabledSelectedColor` / `disabledUnselectedColor`

---

### 可展开卡片

```kotlin
AppExpandableCard(
    header = { Text("标题") },
    modifier = Modifier.fillMaxWidth(),
) {
    Text("展开内容")
}

AppExpandableCard(
    header = { Text("标题") },
    animation = AppExpandableCardDefaults.animation().copy(
        expandSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
    ),
) { ... }

// 受控模式
var expanded by remember { mutableStateOf(false) }
AppExpandableCard(
    header = {
        Text("标题")
        Spacer(Modifier.weight(1f))
        AppSwitch(checked = expanded, onCheckedChange = { expanded = it })
    },
    expanded = expanded,
    onExpandedChange = { expanded = it },
    style = AppExpandableCardDefaults.style().copy(
        chevronPosition = AppExpandableCardChevronPosition.None,
    ),
) { ... }
```

**`AppExpandableCardColors`**：`containerColor` / `borderColor` / `headerBackgroundColor` / `dividerColor` / `chevronColor`

**`AppExpandableCardStyle`**：`headerPaddingHorizontal` / `headerPaddingVertical` / `contentPaddingHorizontal` / `contentPaddingVertical` / `chevronSize` / `chevronPosition`

**`AppExpandableCardAnimation`**：`chevronSpec` / `expandSpec` / `collapseSpec` / `fadeInSpec` / `fadeOutSpec`

**`AppExpandableCardChevronPosition`**：`Start` / `End` / `None`

---

### 标签页

```kotlin
AppTabBar(tabs = listOf("全部", "进行中", "已完成"), selected = 0, onSelect = { ... })

AppTabBar(
    tabs, selected, onSelect,
    colors = AppTabBarDefaults.colors().copy(selectedTextColor = Color(0xFF7C3AED)),
    style = AppTabBarDefaults.style().copy(indicatorHeight = 3.dp),
    animation = AppTabBarDefaults.animation().copy(indicatorSpec = spring()),
)
```

---

### 底部弹窗

```kotlin
AppBottomSheet(onDismissRequest = { ... }) { dismiss ->
    Text("内容")
    AppPrimaryButton("关闭", onClick = dismiss)
}

// 毛玻璃效果
AppBottomSheet(onDismissRequest = { ... }, glass = true) { dismiss -> ... }

// 隐藏拖拽条
AppBottomSheet(onDismissRequest = { ... }, showHandle = false) { dismiss -> ... }
```

---

### 对话框

```kotlin
AppDialog(onDismissRequest = { ... }) { dismiss ->
    Text("确认删除？")
    Row {
        AppOutlineButton("取消", onClick = dismiss)
        AppPrimaryButton("确认", onClick = { /* 操作 */; dismiss() })
    }
}
```

---

### 进度条

```kotlin
AppProgressBar(progress = 0.6f)

// 可拖动
AppProgressBar(progress = value, onProgressChange = { value = it })

AppProgressBar(
    progress = 0.6f,
    color = Color(0xFF7C3AED),
    trackColor = Color(0xFFEDE9FE),
    height = 6.dp,
)
```

---

### 滑块

```kotlin
AppSlider(value = 0.5f, onValueChange = { ... })

AppSlider(
    value = 0.5f,
    onValueChange = { ... },
    valueRange = 0f..100f,
    colors = AppSliderDefaults.colors().copy(thumbColor = Color(0xFF7C3AED)),
)
```

---

### 列表项

```kotlin
AppListGroup {
    item { AppListItem("设置", leadingIcon = Lucide.Settings, onClick = { ... }) }
    item { AppListItem("关于", subtitle = "版本 1.0.0", onClick = { ... }) }
    item {
        AppListItem("通知", leadingIcon = Lucide.Bell) {
            AppSwitch(checked, onCheckedChange)
        }
    }
}
```

---

### 加载指示器

```kotlin
AppLoadingIndicator()
AppLoadingIndicator(size = 32.dp, strokeWidth = 3.dp, color = Color(0xFF7C3AED))

AppSpinnerIndicator()
AppSpinnerIndicator(size = 32.dp, sweepAngle = 240f)

AppDotsIndicator()
AppDotsIndicator(dotSize = 10.dp, spacing = 8.dp)
```

---

### Toast

```kotlin
// 在 AppTheme 内放置宿主
val toastState = remember { AppToastHostState() }
CompositionLocalProvider(LocalToastHostState provides toastState) {
    Box {
        MyApp()
        AppToastHost()
    }
}

// 在任意子组件中触发
val toastState = LocalToastHostState.current
toastState.show(AppToastData("操作成功", AppToastType.Success))
toastState.show(AppToastData("网络错误", AppToastType.Error, icon = Lucide.WifiOff))
toastState.show(AppToastData("提示", durationMs = 3000))
```

**`AppToastType`**：`Default` / `Accent` / `Success` / `Error` / `Warning`

---

### 下拉刷新

```kotlin
AppPullToRefresh(isRefreshing = isRefreshing, onRefresh = { ... }) {
    LazyColumn { ... }
}
```

---

### 分段选择器

```kotlin
AppSegmentedControl(
    options = listOf("日", "周", "月"),
    selected = 0,
    onSelect = { ... },
)

AppSegmentedControl(
    options = listOf("日", "周", "月"),
    selected = 0,
    onSelect = { ... },
    containerColor = Color(0xFFF1F3F7),
    indicatorColor = Color.White,
)
```

---

### 头像

```kotlin
AppAvatar(initials = "AB")
AppAvatar(icon = Lucide.User, size = 56.dp)
AppAvatar(initials = "RK", shape = AppAvatarShape.Rounded)
AppAvatar(
    initials = "AB",
    containerColor = Color(0xFFEDE9FE),
    contentColor = Color(0xFF7C3AED),
)
```

**`AppAvatarShape`**：`Circle` / `Rounded`

---

### 标签与徽章

```kotlin
AppTag("标签")
AppTag("自定义", colors = AppTagDefaults.colors().copy(containerColor = Color(0xFF111111), contentColor = Color.White))

AppStatusBadge("成功", AppBadgeType.Success)
AppStatusBadge("错误", AppBadgeType.Error)
AppStatusBadge("强调", AppBadgeType.Accent)
AppStatusBadge("默认", AppBadgeType.Default)
```

**`AppBadgeType`**：`Default` / `Success` / `Error` / `Accent`

---

### 排版

```kotlin
AppSectionHeader("区块标题")
AppDivider(Modifier.fillMaxWidth())
```

---

### 颜色选择器

```kotlin
AppAccentPicker(
    presets = config.accentPresets,
    current = config.accent,
    onSelect = { newColor -> config = config.copy(accent = newColor) },
)
```

支持预设色块、莫奈自动取色（Android 12+）、Hex 自定义输入。

---

## 文档框架

```kotlin
val myDocs = listOf(
    DocSection("按钮", listOf(
        ComponentDoc("MyButton", "Markdown 描述") {
            MyButton("示例", onClick = {})
        }
    ))
)

DocScreen(docs = myDocs)
```

---

## 许可证

本项目采用 [ANCL-1.0（署名-非商业-著佐权许可证）](LICENSE-zh) 授权。

- 允许自由使用与修改
- 禁止商业用途（需单独获取商业授权）
- 二次分发必须开源并保留本许可证
- 必须在软件 UI 内（如"关于"页面）注明使用了本项目
