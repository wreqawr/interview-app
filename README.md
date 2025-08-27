## Interview App - AI 模拟面试系统（详解版）

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)

**简介**：Interview App 是一个以“AI 驱动的模拟面试”为核心的后端项目，采用模块化与自定义 Starter 架构，支持 WebFlux 与 WebMvc 两种运行形态。系统将“AI 聊天/面试能力”抽象为独立的 Starter，便于在不同项目中复用；同时提供认证 Starter，开箱即用地集成安全能力。

---

## 系统功能点与设计意图

- **AI 通用聊天与面试问答**
  - 通过 Spring AI 与阿里云 DashScope 整合，提供通用对话与“基于简历内容的模拟面试”。
  - 设计意图：将模型调用、提示词渲染、对话上下文记忆、对话轮次控制分层抽象，形成可组合的能力单元（Advisor/Memory/Renderer）。

- **提示词模板渲染（TemplateRenderer）**
  - 支持 StringTemplate(`StTemplateRenderer`) 与“自定义多字符分隔渲染器”(`CustomMultiCharTemplateRenderer`) 两种。
  - 设计意图：解耦提示词模板与业务逻辑，允许团队自由选择单字符或多字符分隔符，兼容多语言与复杂模板。

- **对话记忆（ChatMemory）与存储（Repository）**
  - 可配置 Memory、Redis、MongoDB 三种存储；配合 `MessageChatMemoryAdvisor` 自动注入上下文历史。
  - 设计意图：将“上下文记忆”独立为能力，既能用于面试场景，也能复用于其他 AI 对话业务。

- **对话轮次限制（RoundLimitAdvisor）**
  - 可配置最大轮次、执行顺序、默认会话 ID、默认任务类型；应用侧实现 `RoundLimitRepository` 挂载面试“停止/结束”逻辑（如返回总结提示词）。
  - 设计意图：用 Advisor 对对话过程施加规则控制，保持 ChatClient 默认行为的简洁与可扩展。

- **认证与鉴权（Authentication Starter）**
  - 提供 WebMvc/WebFlux 两套安全自动配置、JWT 验证、验证码、登录登出处理器、异常处理等。
  - 设计意图：安全能力模块化，Starter 化后在任意应用可即插即用。

- **丰富的领域能力（WebMvc 应用）**
  - 简历模块：上传、解析（AI 驱动）、结构化存储、综合评估。
  - 用户模块：公司/角色/权限管理，配套 MyBatis XML 与初始化 SQL。
  - MinIO 文件存储：上传下载与统一封装。

---

## 目录结构（逐层说明）

```
interview-app/                                 # 父项目 (pom)
├── interview-app-starters/                    # 自定义 Starter 聚合模块
│   ├── authentication-spring-boot-autoconfigure/
│   │   ├── src/main/java/cn/minglg/authentication/
│   │   │   ├── autoconfig/                   # WebMvc/WebFlux 安全自动配置入口
│   │   │   ├── config/                        # Security 配置（Mvc/WebFlux）
│   │   │   ├── filter/handler/exception/...   # 认证过滤器/处理器/异常
│   │   │   ├── properties/                    # 安全属性类（WebMvc/WebFlux）
│   │   │   └── utils/                          # Jwt/RSA/Captcha 等工具
│   │   └── META-INF/spring/AutoConfiguration.imports
│   ├── authentication-spring-boot-starter/    # 认证 Starter（声明式引入上面的自动配置）
│   ├── ai-spring-boot-autoconfigure/
│   │   ├── src/main/java/cn/minglg/ai/
│   │   │   ├── autoconfig/AiAutoConfiguration.java      # AI 自动配置(核心)
│   │   │   ├── advisors/                                # RoundLimitAdvisor 等
│   │   │   ├── config/                                  # ChatMemory Repository 配置(InMemory/Redis/Mongo)
│   │   │   ├── properties/                              # AiProperties/RoundLimitProperties
│   │   │   ├── render/                                  # 自定义多字符模板渲染器
│   │   │   └── constant/context/pojo/...                # 常量/上下文/POJO
│   │   └── META-INF/
│   │       ├── spring/AutoConfiguration.imports         # 自动配置入口
│   │       └── additional-spring-configuration-metadata.json # 属性元数据(IDE 补全)
│   └── ai-spring-boot-starter/               # AI Starter（声明式引入 AI 自动配置）
│
├── interview-app-webflux/                     # WebFlux 应用（反应式接口）
│   ├── src/main/java/cn/minglg/interview/
│   │   ├── WebFluxApplication.java            # 应用入口
│   │   └── ai/
│   │       ├── controller/ChatController.java # 聊天 REST 接口（Flux 返回）
│   │       ├── service/ChatService.java       # ChatClient 调用封装 + 上下文参数组装
│   │       ├── advisor/RoundLimitRepositoryImpl.java     # 应用侧实现轮次限制仓库
│   │       └── config/AiConfig.java           # 组合 Prompt、Repository 的装配桥接
│   └── src/main/resources/
│       ├── config/application.yml             # WebFlux 应用配置（端口、Redis/Mongo、AI 模型等）
│       └── prompt/                            # Prompt 模板（general/interview/resume）
│
└── interview-app-webmvc/                      # WebMvc 应用（业务更完整）
    ├── src/main/java/cn/minglg/interview/
    │   ├── WebMvcApplication.java
    │   ├── ai/                                # 简历/面试核心 (解析、问答、评估)
    │   ├── user/                              # 用户/公司/权限
    │   ├── minio/                             # 文件存储封装
    │   └── common/mapper/properties/...       # 公共组件/配置/持久化
    ├── src/main/resources/
    │   ├── config/application.yml
    │   ├── mapper/                            # MyBatis XML（user/company/role 等）
    │   ├── prompt/                            # 简历/面试模板
    │   └── init/ddl & dml                     # 初始化建表与数据脚本
    └── test/java/...                          # 单元/集成测试
```

---

## 架构设计（分层与装配）

- **Starter 抽象层**：
  - `ai-spring-boot-autoconfigure` 将 AI 能力抽象为：`ChatClient` + `Advisor` + `ChatMemory` + `TemplateRenderer` + `Properties`。
  - 通过 `@AutoConfiguration` 与 `AutoConfiguration.imports` 自动生效，应用只需引入 `ai-spring-boot-starter` 依赖即可使用。

- **应用装配层（WebFlux/WebMvc）**：
  - 业务侧（如 WebFlux）根据自身需求提供 `RoundLimitRepository` 实现，桥接 Prompt 模板与“结束语/终止逻辑”。
  - 应用可自定义 `AiConfig`，选择合适的 Prompt 模板、设置 TaskType 上下文参数等。

- **可插拔存储层（ChatMemoryRepository）**：
  - InMemory（默认）、Redis、MongoDB 三种实现，按属性开关装配，满足从开发到生产的阶段性需求。

- **Advisor 链式编排**：
  - 核心默认组合：`SimpleLoggerAdvisor` + `MessageChatMemoryAdvisor` + `RoundLimitAdvisor`。
  - 顺序与开关由 `RoundLimitProperties.order` 与 `enabled` 控制，便于按需开启。

---

## AI 模块（Starter 能力详解）

### 1) ChatClient 映射
- 自动提供 `Map<ChatClientType, ChatClient>`：
  - `GENERAL_WITHOUT_MEMORY`：仅日志 Advisor，适合一次性问答或健康检查。
  - `GENERAL_WITH_MEMORY`：叠加 `MessageChatMemoryAdvisor` 与 `RoundLimitAdvisor`，适合连续对话与面试流程。
- 设计理由：通过枚举键选择不同默认能力组合，简化业务层的获取与切换。

### 2) TemplateRenderer（模板渲染器）
- `stTemplateRenderer`（默认，`StTemplateRenderer`）：单字符分隔符，默认 `<` 与 `>`。
- `customTemplateRenderer`（备选，`CustomMultiCharTemplateRenderer`）：多字符分隔符，默认 `#{` 与 `}`。
- 选择策略：若未定义 `stTemplateRenderer`，则装配 `customTemplateRenderer`。

### 3) ChatMemory 与 Repository
- `ChatMemory`：使用 `MessageWindowChatMemory`，`max-messages` 默认 50，可通过属性调整。
- Repository 三选一：
  - `memory`（默认）：无外部依赖，开发/测试友好。
  - `redis`：支持集群、过期控制（`chat-memory-redis-expire-days`）。
  - `mongodb`：适合更复杂查询与持久需求。

### 4) RoundLimitAdvisor（对话轮次限制）
- 属性类：`RoundLimitProperties` 前缀 `interview.ai.advisor.round`
- 关键字段：
  - `enabled`：是否启用（默认 false）
  - `max-rounds`：最大轮次（默认 5）
  - `order`：执行顺序（建议早于 ChatMemory）
  - `default-conversation-id`：默认会话 ID
  - `default-task-type-string`：默认任务类型字符串（如 `GENERAL_CHAT`）
- 装配条件：`ChatMemory` + `RoundLimitProperties` + `RoundLimitRepository`
- 应用职责：实现 `RoundLimitRepository`，定义“到达上限时如何修改请求/响应”（如返回“结束”模板）。

### 5) 关键类与条件注解（节选）
- `AiAutoConfiguration.chatClient(...)`：`@ConditionalOnClass(ChatClient.class)`
- `AiAutoConfiguration.chatMemory(...)`：`@ConditionalOnMissingBean(ChatMemory.class)`
- `AiAutoConfiguration.chatMemoryAdvisor(...)`：`@ConditionalOnBean(ChatMemory.class)`
- `AiAutoConfiguration.roundLimitAdvisor(...)`：`@ConditionalOnBean({ChatMemory.class, RoundLimitProperties.class, RoundLimitRepository.class})`

---

## 配置清单（additional-spring-configuration-metadata.json 已维护）

### interview.ai（通用）
```yaml
interview:
  ai:
    chat-memory-repository: memory | redis | mongodb  # 默认 memory
    chat-memory-redis-key-prefix: "chat:history"
    chat-memory-redis-expire-days: 30
    max-chat-messages: 50

    # 单字符分隔符（默认）
    start-delimiter-character: "<"
    end-delimiter-character: ">"

    # 或多字符分隔符（二选一）
    start-delimiter-string: "#{"
    end-delimiter-string: "}"
```

### interview.ai.advisor.round（轮次限制）
```yaml
interview:
  ai:
    advisor:
      round:
        enabled: true
        max-rounds: 3
        # order: 100  # 可选，建议早于 ChatMemory
        # default-conversation-id: default
        # default-task-type-string: GENERAL_CHAT
```

---

## WebFlux 应用（cn.minglg.interview:webflux）

- `ChatController`：暴露聊天接口（通常以 POST/stream 形式返回），将参数传递给 `ChatService`。
- `ChatService`：
  - 根据请求与业务场景，组装 `ChatClient` 所需上下文（如 `ChatMemory.CONVERSATION_ID`、`RoundLimitRepository.TASK_TYPE_STRING`）。
  - 选择 `ChatClientType`（是否启用记忆/轮次限制）。
- `RoundLimitRepositoryImpl`：
  - 基于不同 `TaskType` 选择结束/总结类 Prompt 模板。
  - 在达到最大轮次时，修改 `request/response` 以返回终结性内容。
- `AiConfig`：
  - 管理 Prompt 模板映射、公开 `RoundLimitRepository` Bean 等。
- `application.yml`：
  - 端口：`server.port=8082`
  - Redis Cluster：`REDIS_NODE_1..6`、`REDIS_PASSWORD`
  - MongoDB：`MONGODB_HOST`/`PORT`/`USERNAME`/`PASSWORD`
  - DashScope 模型：`spring.ai.dashscope.chat.options.model=qwen-plus`

---

## WebMvc 应用（cn.minglg.interview:webmvc）

- 领域模块：
  - `ai/`：简历解析、面试流程、评估
  - `user/`：公司、角色、权限、用户管理
  - `minio/`：对象存储，简历文件管理
  - `common/`：AOP、通用配置、异常/响应封装、工具等
- 数据层：
  - `mapper/*.xml`：MyBatis 映射文件
  - `init/ddl & dml`：初始化表结构与基础数据

---

## 认证 Starter（简述）

- WebMvc/WebFlux 双栈支持：安全过滤器链、JWT 验证、验证码校验、异常处理器
- 可配置项：见 `authentication-spring-boot-autoconfigure` 下 `properties` 包
- 扩展点：覆盖/新增 AuthenticationProvider、Token 生成策略、Captcha 实现等

---

## 构建与运行

- 先安装自定义 Starters（供 IDE/应用识别元数据与自动配置）：
```bash
mvn -q -f interview-app-starters/pom.xml clean install
```

- 运行 WebFlux：
```bash
mvn -q -f interview-app-webflux/pom.xml spring-boot:run
```

- 运行 WebMvc：
```bash
mvn -q -f interview-app-webmvc/pom.xml spring-boot:run
```

> 提示：请准备好 Redis/MongoDB 与 DashScope 的访问凭据。

---

## 常见问题（排障）

- 启动提示缺少 `MessageChatMemoryAdvisor`：
  - 确认 `AiAutoConfiguration.chatMemory(...)` 带有 `@Bean` 且已装配 `ChatMemoryRepository`。
- 启动提示缺少 `RoundLimitAdvisor`：
  - 确认应用侧已提供 `RoundLimitRepository` Bean（如 WebFlux 的 `RoundLimitRepositoryImpl`）。
  - 确认 `interview.ai.advisor.round.enabled=true` 且 `ChatMemory` 存在。
- 依赖构建异常：
  - 先执行 Starters 的 `clean install`，再编译/运行应用模块。

---

## 版本与兼容性

- Java 17+
- Spring Boot 3.4.6
- Spring AI（通过 `spring-ai-alibaba-starter-dashscope` 1.0.0.x）

> 若升级 Spring Boot/Spring AI，请同步验证 `spring-ai-*` 组件版本与 API 的兼容性（尤其是 ChatClient 与 Advisor 相关）。

---

## 许可证

MIT

