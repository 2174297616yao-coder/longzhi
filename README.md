姓名：姚童

# Android 作业 - 网络请求 & RecyclerView

## 🎯 目标

使用 Retrofit 或其他网络工具，调用以下 API 并展示角色列表：

https://rickandmortyapi.com/api/character

该 API **无需 API Key**，直接 GET 即可。默认返回第 1 页，共 20 条数据。

API 文档参考：https://rickandmortyapi.com/documentation

每个列表条目需要显示：

| 字段 | JSON 字段 | 说明 |
|------|-----------|------|
| 头像 | `image` | 网络图片 URL，需加载展示 |
| 名字 | `name` | 角色名称 |
| 状态 | `status` | `Alive` / `Dead` / `unknown` |
| 物种 | `species` | 如 `Human`、`Alien` 等 |

## ✅ 基本要求

- 使用 **RecyclerView** 显示数据
- 使用 **Glide** 或 **Coil** 加载头像（`image` 字段）
- **不同 status 使用不同颜色** 区分展示，例如：
  - `Alive` → 绿色
  - `Dead` → 灰色或红色
  - `unknown` → 橙色或黄色  
  （具体色值可自行选择，需能明显区分三种状态）
- 数据加载成功后更新 UI
- 如果加载失败，显示 **Toast** 提示
- 在 `AndroidManifest.xml` 中添加网络权限（`INTERNET`）

## 💡 加分项

- 显示 **Loading** 状态
- 使用 **ViewModel + LiveData / Flow** 架构
- 分页加载（使用 `info.next` 或 `?page=2` 加载更多）
- 美化 UI（Material 组件、CardView、布局优化）
- 点击 item 进入详情页（可使用 `url` 字段请求单个角色详情）

## 🚀 如何开始

1. 使用我们发送的链接创建属于你自己的仓库副本：  
   https://github.com/shawnX7/android-interview-template/generate
2. 点击 **Create repository**（仓库需设置为 **Public**，或 Private 并 invite 面试官）
3. Clone 到本地，用 Android Studio 打开项目
4. 在本 README **第一行** 填写你的真实姓名
5. 完成作业后 push 到你的仓库

## 📦 提交方式

1. 将代码 **push** 到你的 GitHub 仓库
2. 在本 README **第一行填写真实姓名**，并在下方「作业说明」中补充开发过程、遇到的问题、心得、架构说明、截图等
3. 确保仓库为 **Public**（公开），以便面试官查看
4. 完成提交后，请联系 HR 确认，并附上你的仓库链接，例如：  
   `https://github.com/YOUR_USERNAME/YOUR_REPO_NAME`

## ⏱ 建议时间

本作业预计花费约 **3 小时** 完成，若需更多时间可自行延长。

---

## 📝 作业说明（完成后填写）

### 开发过程

1. 首先阅读 API 文档，使用 Retrofit + Gson 搭建了网络请求层，定义了数据模型和接口。
2. 实现了基础的 RecyclerView + Adapter 列表展示，使用 Glide 加载角色头像。
3. 对不同 status（Alive/Dead/unknown）分别设置了绿色、红色、橙色的胶囊标签进行区分。
4. **加分项实现**：
    * 增加了首屏 Loading 状态和加载失败的 Toast / 错误重试界面。
    * 引入 **ViewModel + StateFlow** 架构，实现了数据与 UI 的分离。
    * 实现了**分页加载**（滑动到底部自动加载下一页，底部显示"正在加载"或"到底啦"提示）。
    * 实现了**点击进入详情页**，使用 Intent 传递 ID，展示角色头像、性别、起源地、出场集数等信息。
    * 实现了**顶部搜索功能**，带有 500ms 防抖，并处理了搜索无结果（404）的状态。
    * UI 采用 Material 组件美化（CardView 圆角卡片、圆形头像、NestedScrollView 滑动）。

### 遇到的问题 & 解决方案

1. **环境兼容性**：由于本地 AS 版本较老，而模板默认使用了最新的 AGP 9.x，导致 Gradle Sync 失败。通过降级 AGP、Gradle 和 compileSdk (至 34) 解决。
2. **Kotlin 插件缺失**：运行初期出现 `ClassNotFoundException` 闪退，排查发现未引入 Kotlin 插件，在 build.gradle 中补充后解决。
3. **Material 组件主题崩溃**：在使用 `Chip` 和 `TextInputLayout` 时出现了 `InflateException`。由于全局主题适配问题，最终采用原生 `TextView` 和 `EditText` 配合 Material 背景和卡片，既保留了美观又彻底解决了兼容性闪退。

### 架构说明

* **Model**：`Character` 数据类，`RickAndMortyApi` 接口负责网络请求。
* **ViewModel**：`CharacterViewModel` 负责管理状态、分页逻辑和搜索防抖。
* **View**：`MainActivity` 和 `DetailActivity` 监听 ViewModel 的 `StateFlow`，实现 UI = f(State) 的数据驱动渲染。

### 截图

APP运行截图

<img width="300" alt="搜索" src="https://github.com/user-attachments/assets/06ddafc0-8d22-43d0-899f-94e0bc542d18" />
<img width="300" alt="错误页" src="https://github.com/user-attachments/assets/617bb981-21b6-4687-b76b-af66687fd3ec" />
<img width="300" alt="加载页" src="https://github.com/user-attachments/assets/ad1bef72-6dcc-459a-bbc0-b1dec5b72dbc" />
<img width="300" alt="详情页" src="https://github.com/user-attachments/assets/bc0c0b6f-9bcd-4460-ba69-dd211ddc61c3" />
<img width="300" alt="主页面" src="https://github.com/user-attachments/assets/4600087b-ca55-432f-b011-fad91a52ff51" />


---

感谢你花时间完成本次招聘作业，期待你的精彩作品！
