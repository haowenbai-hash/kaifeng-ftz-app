# 开封自贸区 安卓应用

## 项目概述

面向开封自由贸易试验区官网 `https://zmt.ftz.kaifeng.gov.cn/` 的原生安卓 WebView 应用。

### 主要特性

- **直接访问** — 打开即进入官网，无需登录
- **启动动画** — 政务风格 Splash 页，2.2 秒优雅过渡
- **下拉刷新** — 支持下拉手势刷新页面
- **底部导航栏** — 后退 / 前进 / 刷新 / 首页
- **离线检测** — 自动检测网络状态，断网显示友好提示
- **返回键处理** — 网页内后退，无历史时退出应用
- **硬件加速** — 开启 GPU 渲染，页面滚动流畅
- **全面屏适配** — 沉浸式状态栏，底部导航栏配色协调

---

## 项目结构

```
kaifeng-ftz-app/
├── app/
│   ├── src/main/
│   │   ├── java/cn/gov/kaifeng/ftz/
│   │   │   ├── SplashActivity.java     # 启动页
│   │   │   └── MainActivity.java      # 主界面（WebView）
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_splash.xml
│   │   │   │   └── activity_main.xml
│   │   │   ├── drawable/               # 矢量图标
│   │   │   ├── values/                 # 颜色/文字/主题
│   │   │   ├── anim/                   # 过渡动画
│   │   │   └── xml/
│   │   │       └── network_security_config.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
└── settings.gradle
```

---

## 编译方法

### 方法一：Android Studio（推荐）

1. 下载安装 [Android Studio](https://developer.android.com/studio)
2. 打开项目：`File → Open → 选择 kaifeng-ftz-app 文件夹`
3. 等待 Gradle 同步完成
4. 点击 **Run ▶** 或 `Build → Build Bundle(s)/APK(s) → Build APK(s)`
5. APK 输出路径：`app/build/outputs/apk/debug/app-debug.apk`

### 方法二：命令行

```bash
# Windows
cd kaifeng-ftz-app
gradlew.bat assembleDebug

# APK 位置
app/build/outputs/apk/debug/app-debug.apk
```

> **注意**：需要先安装 Android SDK，并配置 `ANDROID_HOME` 环境变量。

---

## 环境要求

| 项目 | 要求 |
|------|------|
| 编译 SDK | API 34 (Android 14) |
| 最低运行 | API 24 (Android 7.0) |
| 目标 SDK | API 34 |
| Java | 17+ |
| Gradle | 8.6 |

---

## UI 设计规范

| 元素 | 颜色 | 说明 |
|------|------|------|
| 主色调 | `#1A5FAB` | 政务蓝 |
| 强调色 | `#F0A500` | 金色（五角星） |
| 背景色 | `#F5F7FA` | 浅灰白 |
| 顶部栏 | 主色调背景 + 白色文字 | 沉浸式 |
| 底部栏 | 白色背景 + 主色调图标 | 阴影分隔 |

---

## 权限说明

| 权限 | 用途 |
|------|------|
| `INTERNET` | 加载网页内容 |
| `ACCESS_NETWORK_STATE` | 检测网络连接状态 |
| `ACCESS_WIFI_STATE` | 识别 WiFi 连接 |

---

## 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2026-05-29 | 初始版本 |
