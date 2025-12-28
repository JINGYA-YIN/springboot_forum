# OnlineChat

一个基于 Spring Boot 的简易在线留言 / 论坛项目。

## 项目结构
```plaintext
onlineChat
├─ .idea
├─ .mvn
│  └─ wrapper
│     └─ maven-wrapper.properties
└─ src
   └─ main
      ├─ java
      │  └─ com.example.onlinechat
      │     ├─ controller      # 控制器：登录/发帖/回复/验证码
      │     ├─ model           # 实体类：User、Message、Reply
      │     ├─ service         # ForumService 业务逻辑
      │     └─ OnlineChatApplication # 启动类
      └─ resources
         ├─ static             # js / css / 图片
         ├─ templates          # 页面：login / list / detail / register
         └─ application.properties
```

## 功能说明

### 用户相关
| 功能 | 描述 |
|------|------|
| 用户注册 | 输入用户名、密码、验证码完成注册 |
| 用户登录 | 验证账号密码+验证码后进入系统 |
| 验证码校验 | /captcha 生成带文字的图片验证码 |
| 退出登录 | 注销 Session，返回登录页面 |

---

### 帖子（Message）
| 功能 | 请求方式 | 路径 | 描述 |
|------|----------|------|------|
| 查看帖子列表 | GET | `/messages` | 支持分页，按时间倒序 |
| 查看帖子详情 | GET | `/messages/detail/{id}` | 展示内容 + 全部回复树状结构 |
| 发布帖子 | POST | `/messages/create` | 登录后可发帖，保存作者与时间 |
| 删除帖子 | POST | `/messages/delete` | 仅作者本人可以删除自己的帖子 |

---

### 回复（Reply）
| 功能 | 请求方式 | 路径 | 描述 |
|------|----------|------|------|
| 回复帖子 | POST | `/reply/add` | 支持根回复或子回复（嵌套） |
| 嵌套回复 | POST | `/reply/add?parentReplyID=x` | 子回复会挂载到父回复上 |
| 删除回复 | POST | `/reply/delete` | 用户只能删自己的回复 |
| 递归删除子回复 | 内部逻辑 | 自动处理 | 删除父回复会把子回复一起删除 |

---

### 验证码
| 功能 | 路径 | 描述 |
|------|------|------|
| 获取验证码图片 | GET `/captcha` | 返回背景图 + 4位随机字符 |
| 验证逻辑 | 登录/注册接口中使用 | 匹配 Session 中存储的验证码 |

---

### 页面（Thymeleaf 模板）
| 页面 | 路径 | 内容说明 |
|------|------|----------|
| 登录页 | `/auth/login` | 输入账号/密码/验证码 |
| 注册页 | `/auth/register` | 注册新用户 |
| 帖子列表页 | `/messages` | 帖子分页展示与发帖按钮 |
| 帖子详情页 | `/messages/detail/{id}` | 显示帖子与树状回复结构 |

---

### 权限与安全
- 未登录状态禁止发帖/评论
- 删除操作需要匹配作者本人
- 验证码防止暴力提交
- Session 用户有效期默认 24 小时

---

## 访问主页
http://10.100.164.6:8080/forum_new/auth/login


 
