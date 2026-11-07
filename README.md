姓名：（请在此填写你的真实姓名）

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

（请在此描述你的实现思路）

### 遇到的问题 & 解决方案

（请在此描述）

### 架构说明

（请在此描述，如 ViewModel、Repository 等）

### 截图

（可选，可贴图片链接或说明）

---

感谢你花时间完成本次招聘作业，期待你的精彩作品！
