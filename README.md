# Compose TakeAway

给即将用Jetpack Compose开发APP的开发人员

### 1. Jetpack Compose vs Xml

Pros：

- 声明式UI，纯kotlin
- 代码更少
- 可直接兼容现有View - 双向兼容
- 性能更好(measure次数减少...)
- UI自定义更简单
  - 没有adapter
  - 更容易override
  - material design(主题、动画等)支持更好
  - KeyEvent/MotionEvent/Gesture 支持更好
  - 常用系统API接口已提供
- API(21+)无关，甚至跨平台Web/Desktop - Unbundled
- 纯compose build更快，APP更小（据官方说法）
- 对新人友好，易上手

Cons：

- priview支持，但不完善(每次修改重新build)
- 实现不透明(编译器-编译时处理)
- recomposition 机制复杂

### 2. with View - Enough for use

- Android(Compose)View用于封装旧的View
  - WebView
  - 三方库
  ...
- 自定义方式
  1. Modifier 或自定义 Modifier
  2. 组合已有组件
  3. 使用drawContent等方法
  4. 很少需要完全自定义

### 3. Architecture for compose

- MVIntent -> MVIi
- MVVMinteractor/Clean Arch

> a demo(TBC)

### 4. Navigation between Composables - Good to go

- 用Composable Screen替代Fragment
- deeplink
- 简单数据传输
- 过度动画

pros:

1. 可以从ViewModel获取数据
2. 与hilt集成完善

cons:

1. 跳转返回数据?
2. 导航传数据，只能基本类型

> 使用enro补充

### 5. State

- single business state for one screen
- 数据(state)驱动UI - 比data-binding好用
- 如何组织state：business state/pure ui state - 统一模式
- state太大怎么办 - 注意命名、分组

#### Share state/data

- Flow/Scoped Bus - 无状态
- Repository/DB数据源

- Composable官方方式，还没有
- 跨Activity/Fragment - navigation, savedState

### 6. 三方库

- accompanist
- dev.enro
- io.coil-kt

- io.kotest
- io.mockk
- app.cash.turbine

### 7. Others

- 什么时候用Activity，而不是Screen - 跨module、业务分割

- 多用Preview
- 优先使用组合，而不是集成
- 多用state，少用event
- 好像不太鼓励开发去了解SDK的实现，相关文档也没有 - 以后？

