# AccountCacher2 项目分析文档

## 一、项目概述

**项目名称**: AccountCacher2
**项目类型**: Android 账号缓存库
**核心功能**: 测试环境账号加密保存到SD卡，方便开发和测试人员快速选择历史账号，避免重复输入

## 二、功能特性

### 2.1 核心功能

1. **账号存储**: 将测试环境的账号（用户名+密码）加密存储到SD卡数据库
2. **账号选择**: 提供UI对话框展示历史账号列表，支持快速选择
3. **环境过滤**: 默认只存储dev和test环境的账号，不存储release环境账号（可配置）
4. **no-op支持**: 提供空实现模块，release环境可完全移除功能，避免上线风险

### 2.2 配置特性

- `storeReleaseAccount`: 是否存储正式环境账号，默认false
- `configHostType(int dev, int test, int release)`: 自定义环境类型标识
- `init(String appName, boolean hasAdaptScopedStorage)`: 初始化，支持分区存储适配

### 2.3 使用场景

- 开发/测试人员快速切换测试账号
- 避免重复输入常用测试账号
- 多环境（dev/test/release）账号隔离存储

## 三、架构设计

### 3.1 模块结构

项目采用多模块Gradle架构：

```
accountCacher2/
├── accountcache/     # 核心库模块（功能实现）
├── no-op/           # 空实现模块（release环境使用）
├── app/             # Demo演示模块
├── basedao/         # 基础DAO相关
└── build.gradle     # 根构建配置
```

### 3.2 依赖关系

- **accountcache**: 依赖 GreenDao、XXPermissions、迁移工具
- **no-op**: 空实现，仅包含接口定义，无任何功能
- **app**: 依赖 accountcache，用于演示

### 3.3 架构分层

```
┌─────────────────────────────────────────────────────────┐
│                      应用层                              │
│  app (Demo)  │  accountcache (核心库)  │  no-op (空实现) │
└─────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                    AccountCacher API                     │
│  init() / selectAccount() / saveAccount()               │
└─────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                      数据层                              │
│  MyDbUtil │ GreenDao ORM │ MyDBContext (SD卡存储)        │
└─────────────────────────────────────────────────────────┘
```

## 四、核心技术实现

### 4.1 数据库层

**框架**: GreenDao 3.3.0
**数据库文件**: `/sdcard/.yuv2/databases/test_account.db`
**数据迁移**: 使用 `com.github.yuweiguocn.library.greendao.MigrationHelper`

### 4.2 存储策略

1. **路径自定义**: 通过继承 `ContextWrapper` 的 `MyDBContext` 类，重写 `getDatabasePath()` 方法，将数据库路径重定向到SD卡
2. **加密方式**: 密码使用 Base64 简单加密存储（`android.util.Base64`）
3. **权限处理**: 使用 XXPermissions 库请求存储权限，支持 Android 11 分区存储适配

### 4.3 权限请求

```java
XXPermissions.with(activity)
    .permission(Permission.MANAGE_EXTERNAL_STORAGE);  // Android 11+
    // 或
    .permission(Permission.Group.STORAGE);            // Android 10及以下
```

### 4.4 异步处理

- 数据库操作在子线程执行
- UI更新切换回主线程（Handler + Looper）

## 五、核心流程

### 5.1 选择账号流程 (selectAccount)

```
1. 检查是否存储release账号配置
2. 请求存储权限 (XXPermissions)
3. 权限授予后，子线程查询数据库 (MyDbUtil.getAll)
4. 按使用次数降序排列，最近使用的排在最前
5. 主线程显示单选对话框 (AlertDialog)
6. 用户选择后回调 AccountCallback.onSuccess(account)
```

### 5.2 保存账号流程 (saveAccount)

```
1. 检查hostType是否为dev/test（或允许存release）
2. 请求存储权限
3. 权限授予后，子线程查询账号是否已存在
4. 存在则更新: usedNum+1, updateTime=当前时间
5. 不存在则新增记录
```

## 六、关键类说明

| 类名 | 文件位置 | 职责描述 |
|------|----------|----------|
| AccountCacher | accountcache/.../AccountCacher.java | 核心API类，提供init/selectAccount/saveAccount方法 |
| DebugAccount | accountcache/.../DebugAccount.java | 账号实体类，使用GreenDao注解定义 |
| MyDbUtil | accountcache/.../MyDbUtil.java | 数据库工具类，管理GreenDao初始化和CRUD操作 |
| MyDBContext | accountcache/.../MyDBContext.java | 自定义Context，重定向数据库路径到SD卡 |
| MySQLiteUpgradeOpenHelper | accountcache/.../MySQLiteUpgradeOpenHelper.java | 数据库升级帮助类，支持表结构迁移 |
| DaoMaster | accountcache/.../db/DaoMaster.java | GreenDao生成的DAO管理器 |
| DaoSession | accountcache/.../db/DaoSession.java | GreenDao生成的会话对象 |
| DebugAccountDao | accountcache/.../db/DebugAccountDao.java | GreenDao生成的DAO接口 |
| AccountCallback | accountcache/.../AccountCallback.java | 账号选择回调接口 |

## 七、数据模型

### 7.1 DebugAccount 表结构

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，自增 |
| appName | String | 应用名称标识 |
| account | String | 账号用户名 |
| pw | String | 密码（Base64加密） |
| countryCode | String | 国家码（如"ch"） |
| hostType | int | 环境类型（1-dev, 3-test, 0-release） |
| usedNum | int | 使用次数 |
| updateTime | long | 最后更新时间戳 |
| position | int | 位置（预留字段） |

### 7.2 查询条件

- `AppName` = 当前应用名
- `HostType` = 当前环境类型
- `CountryCode` = 当前国家码
- 排序：`UsedNum` 降序

## 八、总结

AccountCacher2 是一个轻量级的Android账号管理库，通过GreenDao实现本地SQLite存储，结合SD卡存储和Base64加密，为开发和测试人员提供便捷的测试账号管理能力。架构清晰，模块分离，支持组件化集成，特别适合需要频繁切换测试账号的Android项目。