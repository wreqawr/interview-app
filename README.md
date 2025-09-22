# Interview App - AI 模拟面试系统

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-informational.svg)](https://spring.io/projects/spring-cloud)
[![Spring Cloud Alibaba](https://img.shields.io/badge/SCA-2025.0.0.0--preview-blue.svg)](https://github.com/alibaba/spring-cloud-alibaba)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)

## 🚀 项目简介

**Interview App** 是一个基于 Spring Boot 3.5.x 与 Spring Cloud 2025 的 AI 模拟面试系统，采用模块化架构与自定义 Starter 设计，支持 WebMVC 与 WebFlux 两种运行模式。AI 聊天能力与认证安全以独立 Spring Boot Starter 提供，实现高内聚、可复用与可插拔。

### ✨ 核心特性

- 🤖 **AI 驱动的智能面试**：基于阿里云 DashScope（通过自研 AI Starter 集成），提供简历解析、模拟面试、智能评估
- 🔧 **自定义 Starter 架构**：AI 能力、认证安全等核心能力模块化，开箱即用
- 🔄 **双栈支持**：同时支持 WebMVC（同步）与 WebFlux（响应式流式）
- 🗄️ **多数据源**：MySQL、Redis、MongoDB、MinIO
- 🔐 **企业级安全**：JWT、RBAC、验证码、防护与异常处理器
- 📊 **可观测性**：Actuator 健康检查与指标
- 🐳 **容器化友好**：提供基础设施 Compose 编排与示例 Dockerfile

## 🛠️ 技术栈

### 核心框架
- **Java 17+**
- **Spring Boot 3.5.6**
- **Spring Cloud 2025.0.0**、**Spring Cloud Alibaba 2025.0.0.0-preview（Nacos）**
- **Spring Security 6.x**、**Spring Data**

### 数据存储
- **MySQL 8.0**（WebMVC）
- **R2DBC + MySQL**（WebFlux）
- **Redis（含集群模式）**
- **MongoDB 6.0**
- **MinIO**（对象存储）

### AI服务
- **阿里云 DashScope**（通义千问）
- **自研 AI Starter**（提示词渲染、对话记忆、轮次限制）
- **Apache Tika**（文档解析）

### 开发与运维
- **Maven 3.9+**、**Lombok**、**Hutool**、**Druid**
- **Actuator**、示例 **Dockerfile**、基础设施 **Compose**
- **JLink**：JVM 精简与镜像体积优化（可选）

---

## 🤖 AI 功能

### 1. 智能简历解析
- 文档解析：支持 PDF、DOC/DOCX、TXT
- 信息提取：基本信息、教育、经历、技能
- 结构化存储：MongoDB

### 2. 模拟面试系统
- 基于简历与岗位个性化问答
- 多轮对话与轮次控制
- WebFlux 支持流式响应

### 3. 智能评估
- 面向求职者的优化建议
- 面向 HR 的综合能力评估

### 4. 提示词模板
- 多模板：通用聊天、面试开始/结束、简历分析等
- 支持分隔符配置、动态参数渲染

---

## 🔧 自定义 Starter 详解

### 1. AI Spring Boot Starter（独立聚合构建）

#### 核心组件（示例）
- `AiAutoConfiguration`：自动配置
- `ChatClient`：聊天客户端，组合多种 Advisor
- `TemplateRenderer`：提示词模板渲染
- `ChatMemory`：对话记忆
- `Advisor`：轮次限制、记忆管理等可插拔增强

#### 配置示例
```yaml
interview:
  ai:
    chat-memory-repository: mongodb   # memory | redis | mongodb
    max-chat-messages: 50
    start-delimiter-character: "<"
    end-delimiter-character: ">"
    advisor:
      round:
        enabled: true
        max-rounds: 5
```

#### 使用示例（简）
```java
@Autowired
private ChatClient chatClient;

public String chat(String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
}
```

### 2. Authentication Spring Boot Starter

#### 功能特性
- WebMVC 与 WebFlux 双栈支持
- JWT 无状态认证、RBAC 权限
- 图形验证码、异常与权限处理器
- CORS 支持

#### 配置示例（WebMVC）
```yaml
interview:
  webmvc:
    security:
      white-list-patterns:
        - /api/auth/login
        - /api/auth/register
      captcha:
        effective-patterns:
          - /api/auth/login
```

---

## 📁 项目结构

```
interview-app/                                 # 父项目（聚合 WebMVC/WebFlux/Commons）
├── interview-app-starters/                    # Starter 聚合（独立构建）
│   ├── ai-spring-boot-autoconfigure/
│   ├── ai-spring-boot-starter/
│   ├── authentication-spring-boot-autoconfigure/
│   └── authentication-spring-boot-starter/
│   # 说明：父 pom 未纳入此聚合，需要先单独 mvn install
│
├── interview-app-webmvc/                      # WebMVC 应用（同步接口、完整业务）
│   ├── src/main/java/cn/minglg/interview/
│   ├── src/main/resources/
│   │   ├── mapper/                            # MyBatis 映射
│   │   ├── prompt/                            # 提示词模板
│   │   └── init/                              # 数据库初始化脚本
│   └── src/test/java/
│
├── interview-app-webflux/                     # WebFlux 应用（响应式接口、流式响应）
│   ├── src/main/java/cn/minglg/interview/
│   └── src/main/resources/
│
└── interview-app-commons/                     # 公共模块
    └── src/main/java/cn/minglg/commons/
```

## 🚀 快速开始

### 环境要求

- **JDK 17+**、**Maven 3.9+**
- **Docker & Docker Compose**（仅用于本地基础设施）
- **MySQL 8.0+**、**Redis 6.0+**、**MongoDB 6.0+**、**MinIO（可选）**

### 1. 克隆项目

```bash
git clone git@github.com:wreqawr/interview-app.git
cd interview-app
```

### 2. 构建自定义 Starter（必须先执行）

```bash
mvn -q -f interview-app-starters/pom.xml clean install
```

### 3. 启动基础设施（本地 Docker，推荐）

```bash
docker-compose -f docker-compose-infrastructure.yml up -d
```

> 说明：`docker-compose-all.yml` 为未来多模块编排草稿，当前未与实际模块目录对齐，请勿使用。

### 4. Nacos 配置中心

本项目使用 Nacos 管理外部化配置与服务发现，请确保可访问的 Nacos 实例，并准备好对应用的分层配置。

- 建议的命名空间：`dev`、`test`、`prod`
- 建议的分组：`DEFAULT_GROUP`（或依团队规范分组）
- 建议的 dataId 命名规则：`nacos-${spring.application.name}-${spring.profiles.active}.yml`

以 WebMVC（`spring.application.name=interview-app-webmvc`）为例：

```
命名空间：dev
Group：DEFAULT_GROUP
dataId：nacos-interview-app-webmvc-dev.yml
```

示例内容（可按需扩展）：

```yaml
server:
  port: 8081

spring:
  application:
    name: interview-app-webmvc

interview:
  webmvc:
    security:
      white-list-patterns:
        - /api/auth/login
        - /api/auth/register
      captcha:
        effective-patterns:
          - /api/auth/login

  ai:
    chat-memory-repository: mongodb
    max-chat-messages: 50
    advisor:
      round:
        enabled: true
        max-rounds: 5

  data:
    redis:
      cluster:
        nodes:
          - 127.0.0.1:6379
          - 127.0.0.1:6380
      password: your_password
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/interview?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  mongodb:
    host: 127.0.0.1
    port: 27017
    username: root
    password: your_password
    database: interview
```

### 5. 启动应用

#### 方式一：本地运行（推荐）

```bash
# WebMVC（端口 8081）
mvn -q -f interview-app-webmvc/pom.xml spring-boot:run

# WebFlux（端口 8082）
mvn -q -f interview-app-webflux/pom.xml spring-boot:run
```

#### 方式二：构建镜像并运行（可选）

```bash
# 构建
docker build -t interview-app-webmvc:latest -f interview-app-webmvc/Dockerfile .
docker build -t interview-app-webflux:latest -f interview-app-webflux/Dockerfile .

# 运行
docker run -d -p 8081:8081 --name interview-webmvc interview-app-webmvc:latest
docker run -d -p 8082:8082 --name interview-webflux interview-app-webflux:latest
```

### 6. 访问应用

- **WebMVC**：http://localhost:8081
- **WebFlux**：http://localhost:8082
- **健康检查**：http://localhost:8081/actuator/health、http://localhost:8082/actuator/health

---

## 📖 使用指南

### OpenAPI/Swagger 说明

项目未内置 Swagger 依赖。若需交互式 API 文档，建议按以下方式接入任一实现：

- springdoc-openapi（推荐）
  - 依赖：`org.springdoc:springdoc-openapi-starter-webmvc-ui`
  - 访问路径：`/swagger-ui.html` 或 `/swagger-ui/index.html`
- knife4j（增强 UI，可选）

如需，我可以在 `webmvc` 与 `webflux` 两模块分别接入并提供示例配置。

### AI 聊天（WebFlux 示例）

```bash
# 通用聊天
curl -X POST http://localhost:8082/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "test-001",
    "message": "你好，请介绍一下自己",
    "taskType": "GENERAL_CHAT"
  }'

# 基于简历的面试准备
curl -X POST http://localhost:8082/api/interview/chat/prepare \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "interview-001",
    "jobId": "job-123",
    "resumeId": "resume-456"
  }'
```

### 简历解析与分析（WebMVC 示例）

```bash
# 上传简历并解析
curl -X POST http://localhost:8081/api/resume/upload \
  -F "file=@resume.pdf" \
  -F "resumeTitle=Java开发工程师简历"

# 获取简历分析
curl -X GET "http://localhost:8081/api/resume/analyze?resumeId=resume-456"
```

### 认证接口（JWT + 验证码）
### REST API 列表（依据当前代码）

- WebMVC（端口 8081）
  - 认证与账户
    - GET `/api/auth/publicKey`：获取登录加密公钥
    - POST `/api/auth/register`：用户注册
    - GET `/api/auth/captcha`：获取图形验证码（响应为 image/jpeg，Header 返回 captchaId）
  - 简历
    - POST `/api/resume/upload`：上传简历文件，表单字段名为 `resume`
    - GET `/api/resume/download?resumeIds=...`：批量下载
    - DELETE `/api/resume/delete`：删除，Body 为 `string[]` 简历ID
    - GET `/api/resume/getMyResume`：查询当前用户简历元信息列表
    - GET `/api/resume/queryResumeAsyncUploadResult/{taskId}/{resumeId}`：查询异步上传结果
    - GET `/api/resume/preview/{resumeId}`：获取预览地址
    - GET `/api/resume/analyze/{resumeId}`：触发分析（同步返回结果或异步 taskId）
    - GET `/api/resume/queryResumeAsyncAnalyzeResult/{taskId}/{resumeId}`：查询异步分析结果
  - 候选人
    - GET `/api/candidate/getJobList`：获取当前用户投递岗位列表

- WebFlux（端口 8082）
  - AI 助手
    - POST `/api/ai/assistant`（SSE）：参数包含 `conversationId`、`userMessage`、`taskType`、`params`
  - 面试聊天
    - POST `/api/interview/chat/prepare`（SSE）：参数包含 `conversationId`、`jobId`、`resumeId`
    - POST `/api/interview/chat/progress`（SSE）：参数包含 `conversationId`、`userMessage`

```bash
# 获取验证码
curl -X GET http://localhost:8081/api/auth/captcha

# 登录
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "your_username",
    "password": "your_password",
    "captcha": "验证码",
    "captchaId": "验证码ID"
  }'
```

---

## 🔒 安全与配置

### Nacos 配置中心

项目默认启用 Nacos 配置与注册发现，关键片段如下（两端应用一致）：

```yaml
spring:
  profiles:
    active: dev
  cloud:
    nacos:
      server-addr: minglg.cn:10845
      discovery:
        server-addr: ${spring.cloud.nacos.server-addr}
  config:
    import:
      - nacos:nacos-${spring.application.name}-${spring.profiles.active}.yml?refreshEnabled=true
```

请确保你的 Nacos 可用并有对应的 dataId（如：`nacos-interview-app-webmvc-dev.yml`）。

### AI 配置（通过自研 Starter 提供）

```yaml
interview:
  ai:
    chat-memory-repository: mongodb
    max-chat-messages: 50
    start-delimiter-character: "<"
    end-delimiter-character: ">"
    advisor:
      round:
        enabled: true
        max-rounds: 5
```

### 安全配置（WebMVC 示例）

```yaml
interview:
  webmvc:
    security:
      white-list-patterns:
        - /api/auth/login
        - /api/auth/register
        - /actuator/**
      captcha:
        effective-patterns:
          - /api/auth/login
```

### 数据源配置（示例）

```yaml
spring:
  datasource:
    url: ${MYSQL_JDBC_URL}
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}
    type: com.alibaba.druid.pool.DruidDataSource

  data:
    redis:
      cluster:
        nodes:
          - ${REDIS_NODE_1}
          - ${REDIS_NODE_2}
        password: ${REDIS_PASSWORD}

  mongodb:
    host: ${MONGODB_HOST}
    port: ${MONGODB_PORT}
    username: ${MONGODB_USERNAME}
    password: ${MONGODB_PASSWORD}
    database: interview
```

> 安全提示：敏感信息请使用环境变量或密钥管理服务，`.env` 不要提交仓库。

---

## 🐳 Docker 与部署

### 使用 Docker Compose（仅基础设施）

> ⚠️ 仅 `docker-compose-infrastructure.yml` 可直接使用；`docker-compose-all.yml` 为预留草稿，当前不建议使用。

```bash
docker-compose -f docker-compose-infrastructure.yml up -d
docker-compose ps
docker-compose logs -f interview-mysql
```

### 应用镜像（可选）

```bash
# 构建
docker build -t interview-app-webmvc:latest -f interview-app-webmvc/Dockerfile .
docker build -t interview-app-webflux:latest -f interview-app-webflux/Dockerfile .

# 运行
docker run -d -p 8081:8081 --name interview-webmvc interview-app-webmvc:latest
docker run -d -p 8082:8082 --name interview-webflux interview-app-webflux:latest
```

---

## 🏭 生产部署指南

### 镜像分层建议

- 构建阶段与运行阶段多阶段构建；仅复制可执行 JAR 至运行镜像
- 使用 JLink 或 distroless 精简运行时，尽量避免安装多余工具
- 使用非 root 用户运行，减少权限风险

### JVM/GC 参数建议

- 容器环境下优先使用 `-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0`
- G1GC 一般场景表现稳定：`-XX:+UseG1GC -XX:MaxGCPauseMillis=200`
- 按业务流量与延迟目标迭代压测参数，分环境差异化配置

### 资源配额与弹性

- CPU：设置 `requests` 与 `limits`（K8s），确保核心服务稳定
- 内存：为 WebFlux 流式接口预留足够 headroom，避免 OOM
- I/O：简历上传/下载需关注磁盘与网络吞吐，按需使用本地缓存目录

### 配置与密钥

- 使用 Nacos/环境变量/密钥管理服务；严禁将敏感信息写入镜像
- 不同环境使用不同命名空间与分组；禁止复用生产密钥到测试环境

### 可观测性与就绪性

- 暴露 `/actuator/health`、`/actuator/metrics`，配置 liveness/readiness 探针
- 记录关键业务指标（简历处理耗时、AI 调用成功率、SSE 连接数等）

---

## 🔍 故障排除

1. 启动失败：自定义 Starter 相关 Bean 缺失
   ```bash
   # 确认先安装 starters 聚合
   mvn -q -f interview-app-starters/pom.xml clean install
   ```

2. WebFlux 流式异常或 Netty 本地 DNS 报错（macOS）
   - 已引入 `netty-resolver-dns-native-macos`（aarch64），确认依赖未被排除

3. AI 服务调用失败
   ```bash
   # 检查环境变量
   echo $ALIYUN_API_KEY
   ```

4. 数据库/缓存连接失败
   ```bash
   docker-compose ps mysql redis-1 mongodb
   ```

---

## 📊 性能优化

### JVM 优化

```bash
# 使用 JLink 精简镜像（示例脚本与 Dockerfile）
```

### 数据库与缓存优化

- MySQL：连接池与慢查询优化
- Redis：集群与持久化参数
- MongoDB：索引策略

### 应用优化

- 异步/批处理、热点数据缓存、连接池参数

---

## 🤝 贡献指南

1. Fork 到个人仓库
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交更改：`git commit -m "Add your feature"`
4. 推送分支并创建 PR

代码规范：Java 17、Spring 最佳实践、必要的单元测试与文档。

---

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)

---

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba)
- [阿里云 DashScope](https://dashscope.aliyun.com/)
- [Apache Tika](https://tika.apache.org/)
- [Hutool](https://hutool.cn/)

---

## 📞 联系方式

- 项目维护者：kfzx-minglg
- 邮箱：2820996063@qq.com
- 项目地址：`https://github.com/wreqawr/interview-web.git`

---

<div align="center">

**如果这个项目对你有帮助，请给个 ⭐ Star 支持一下！**

</div>

