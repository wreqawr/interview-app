# Interview App - AI 模拟面试系统

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0.3-blue.svg)](https://spring.io/projects/spring-ai)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)

## 🚀 项目简介

**Interview App** 是一个基于 Spring Boot 3.x 的 AI 模拟面试系统，采用模块化架构和自定义 Starter 设计，支持 WebMVC 和 WebFlux 两种运行模式。项目将 AI 聊天能力、认证安全等核心功能抽象为独立的 Spring Boot Starter，实现高度模块化和可复用性。

### ✨ 核心特性

- 🤖 **AI 驱动的智能面试**：基于阿里云 DashScope 大模型，提供简历解析、模拟面试、智能评估
- 🔧 **自定义 Starter 架构**：AI 能力、认证安全等核心功能模块化，开箱即用
- 🔄 **双栈支持**：同时支持 WebMVC（传统）和 WebFlux（响应式）两种运行模式
- 🗄️ **多数据源支持**：MySQL、Redis、MongoDB、MinIO 等存储技术栈
- 🔐 **企业级安全**：JWT 认证、RBAC 权限控制、验证码防护
- 📊 **异步任务处理**：简历解析、AI 分析等耗时操作异步化
- 🐳 **容器化部署**：Docker + Docker Compose 一键部署

## 🛠️ 技术栈

### 核心框架
- **Java 17+**：现代 Java 特性支持
- **Spring Boot 3.4.6**：企业级应用框架
- **Spring AI 1.0.0.3**：AI 能力集成框架
- **Spring Security 6.x**：安全认证框架
- **Spring Data**：数据访问抽象层

### 数据存储
- **MySQL 8.0**：关系型数据库（WebMVC 模式）
- **R2DBC + MySQL**：响应式数据库访问（WebFlux 模式）
- **Redis Cluster**：分布式缓存和会话存储
- **MongoDB 6.0**：文档数据库，存储简历解析结果
- **MinIO**：对象存储，管理简历文件

### AI服务
- **阿里云 DashScope**：大语言模型服务（通义千问）
- **Spring AI Alibaba**：Spring AI 阿里云集成
- **Apache Tika**：文档内容提取

### 开发工具
- **Maven 3.9+**：项目构建管理
- **Lombok**：代码简化
- **Hutool**：Java 工具库
- **Druid**：数据库连接池

### 部署运维
- **Docker & Docker Compose**：容器化部署
- **Spring Boot Actuator**：应用监控
- **JLink**：JVM 优化（镜像大小仅 291MB）

---

## 🤖 AI 功能详解

### 1. 智能简历解析
- **文档解析**：支持 PDF、DOC、DOCX、TXT 格式
- **信息提取**：自动提取基本信息、工作经历、教育背景、技能等
- **智能纠错**：自动修正错别字和技术术语错误
- **结构化存储**：解析结果存储到 MongoDB，支持复杂查询

### 2. 模拟面试系统
- **基于简历的面试**：根据简历内容和职位要求生成个性化面试问题
- **多轮对话**：支持连续多轮面试对话，保持上下文记忆
- **轮次控制**：可配置最大面试轮次，自动结束面试
- **实时流式响应**：WebFlux 模式支持流式输出，提升用户体验

### 3. 智能评估分析
- **简历分析**：面向求职者的简历优化建议
- **综合评估**：面向 HR 的候选人综合能力评估
- **技术匹配度**：分析技能与职位要求的匹配程度

### 4. 提示词模板系统
- **多模板支持**：通用聊天、面试开始、面试结束、简历分析等
- **灵活渲染**：支持单字符（`<>`）和多字符（`#{}`）分隔符
- **动态参数**：支持动态参数注入和模板渲染

---

## 🔧 自定义 Starter 详解

### 1. AI Spring Boot Starter

#### 核心组件
- **`AiAutoConfiguration`**：AI 能力自动配置类
- **`ChatClient`**：聊天客户端，支持多种 Advisor 组合
- **`TemplateRenderer`**：提示词模板渲染器
- **`ChatMemory`**：对话记忆管理
- **`Advisor`**：可插拔的对话增强器

#### 功能特性
```yaml
interview:
  ai:
    # 聊天记忆存储方式：memory | redis | mongodb
    chat-memory-repository: mongodb
    # 最大聊天消息数
    max-chat-messages: 50
    # 模板分隔符配置
    start-delimiter-character: "<"
    end-delimiter-character: ">"
    # 轮次限制配置
    advisor:
      round:
        enabled: true
        max-rounds: 5
```

#### 使用方式
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

#### 核心组件
- **`WebMvcSecurityAutoConfiguration`**：WebMVC 安全配置
- **`WebFluxSecurityAutoConfiguration`**：WebFlux 安全配置
- **JWT 过滤器**：Token 验证和用户认证
- **验证码过滤器**：图形验证码防护
- **权限处理器**：访问控制和异常处理

#### 功能特性
- **双栈支持**：同时支持 WebMVC 和 WebFlux
- **JWT 认证**：无状态 Token 认证
- **RBAC 权限**：基于角色的访问控制
- **验证码防护**：防止暴力破解
- **CORS 支持**：跨域资源共享

#### 使用方式
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
interview-app/                                 # 父项目
├── interview-app-starters/                    # 自定义 Starter 聚合模块
│   ├── ai-spring-boot-autoconfigure/         # AI 自动配置模块
│   │   ├── src/main/java/cn/minglg/ai/
│   │   │   ├── autoconfig/                   # 自动配置类
│   │   │   ├── advisors/                     # 对话增强器
│   │   │   ├── config/                       # 存储配置（内存/Redis/MongoDB）
│   │   │   ├── properties/                   # 配置属性
│   │   │   ├── render/                       # 模板渲染器
│   │   │   └── constant/                     # 常量定义
│   │   └── META-INF/spring/                  # 自动配置入口
│   ├── ai-spring-boot-starter/               # AI Starter（声明式依赖）
│   ├── authentication-spring-boot-autoconfigure/  # 认证自动配置模块
│   │   ├── src/main/java/cn/minglg/authentication/
│   │   │   ├── autoconfig/                   # WebMVC/WebFlux 安全配置
│   │   │   ├── config/                       # 安全配置类
│   │   │   ├── filter/                       # 认证过滤器
│   │   │   ├── handler/                      # 处理器
│   │   │   ├── properties/                   # 安全属性
│   │   │   └── utils/                        # 工具类
│   │   └── META-INF/spring/                  # 自动配置入口
│   └── authentication-spring-boot-starter/   # 认证 Starter（声明式依赖）
│
├── interview-app-webmvc/                     # WebMVC 应用（完整业务功能）
│   ├── src/main/java/cn/minglg/interview/
│   │   ├── ai/                               # AI 核心功能
│   │   │   ├── core/resume/                  # 简历解析服务
│   │   │   ├── config/                       # AI 配置
│   │   │   └── service/                      # AI 服务层
│   │   ├── user/                             # 用户管理
│   │   ├── minio/                            # 文件存储
│   │   └── common/                           # 公共组件
│   ├── src/main/resources/
│   │   ├── mapper/                           # MyBatis 映射文件
│   │   ├── prompt/                           # AI 提示词模板
│   │   └── init/                             # 数据库初始化脚本
│   └── src/test/java/                        # 单元测试
│
├── interview-app-webflux/                    # WebFlux 应用（响应式接口）
│   ├── src/main/java/cn/minglg/interview/
│   │   ├── ai/                               # AI 服务
│   │   │   ├── service/                      # 聊天服务
│   │   │   ├── advisor/                      # 轮次限制实现
│   │   │   └── config/                       # AI 配置
│   │   └── interview/                        # 面试相关
│   │       └── chat/                         # 聊天控制器
│   └── src/main/resources/
│       ├── config/                           # 应用配置
│       └── prompt/                           # 提示词模板
│
└── interview-app-commons/                    # 公共模块
    └── src/main/java/cn/minglg/commons/      # 公共常量和工具
```

## 🚀 快速开始

### 环境要求

- **JDK 17+**
- **Maven 3.9+**
- **Docker & Docker Compose**（可选）
- **MySQL 8.0+**
- **Redis 6.0+**
- **MongoDB 6.0+**
- **MinIO**（可选其他OSS服务）

### 1. 克隆项目

```bash
git clone git@github.com:wreqawr/interview-app.git
cd interview-app
```

### 2. 构建自定义 Starter

```bash
# 先构建自定义 Starter
mvn -q -f interview-app-starters/pom.xml clean install
```

### 3. 配置环境变量

> ⚠️ **安全提醒**：以下配置中的用户名、密码、API Key 等敏感信息仅为示例，请替换为您的实际配置。请将 `.env` 文件添加到 `.gitignore` 中，避免将敏感信息提交到版本控制系统。

创建 `.env` 文件：

```bash
# 数据库配置
MYSQL_JDBC_URL=jdbc:mysql://localhost:3306/interview?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
MYSQL_R2DBC_URL=r2dbc:mysql://localhost:3306/interview?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
MYSQL_USERNAME=your_mysql_username
MYSQL_PASSWORD=your_mysql_password

# Redis 集群配置
REDIS_NODE_1=localhost:6379
REDIS_NODE_2=localhost:6380
REDIS_NODE_3=localhost:6381
REDIS_NODE_4=localhost:6382
REDIS_NODE_5=localhost:6383
REDIS_NODE_6=localhost:6384
REDIS_PASSWORD=your_redis_password

# MongoDB 配置
MONGODB_HOST=localhost
MONGODB_PORT=27017
MONGODB_USERNAME=your_mongodb_username
MONGODB_PASSWORD=your_mongodb_password

# 阿里云 AI 配置
ALIYUN_API_KEY=your_dashscope_api_key

# MinIO 配置（可选）
MINIO_ENDPOINT=http://localhost:9000
MINIO_ACCESS_KEY=your_minio_access_key
MINIO_SECRET_KEY=your_minio_secret_key
```

### 4. 启动应用

#### 方式一：Docker Compose（推荐）

```bash
# 启动基础设施服务
docker-compose -f docker-compose-infrastructure.yml up -d

# 启动应用服务
docker-compose -f docker-compose-all.yml up -d
```

#### 方式二：本地运行

```bash
# 启动 WebMVC 应用（端口 8081）
mvn -q -f interview-app-webmvc/pom.xml spring-boot:run

# 启动 WebFlux 应用（端口 8082）
mvn -q -f interview-app-webflux/pom.xml spring-boot:run
```

### 5. 访问应用

- **WebMVC 应用**：http://localhost:8081
- **WebFlux 应用**：http://localhost:8082
- **健康检查**：http://localhost:8081/actuator/health

---

## 📖 使用指南

### AI 聊天接口

#### WebFlux 模式（流式响应）

```bash
# 通用聊天
curl -X POST http://localhost:8082/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "test-001",
    "message": "你好，请介绍一下自己",
    "taskType": "GENERAL_CHAT"
  }'

# 模拟面试
curl -X POST http://localhost:8082/api/interview/chat/prepare \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "interview-001",
    "jobId": "job-123",
    "resumeId": "resume-456"
  }'
```

#### WebMVC 模式（同步响应）

```bash
# 简历解析
curl -X POST http://localhost:8081/api/resume/upload \
  -F "file=@resume.pdf" \
  -F "resumeTitle=Java开发工程师简历"

# 简历分析
curl -X GET http://localhost:8081/api/resume/analyze?resumeId=resume-456
```

### 认证接口

```bash
# 获取验证码
curl -X GET http://localhost:8081/api/auth/captcha

# 用户登录
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

## 🔒 安全配置

### 环境变量管理

1. **创建 `.env` 文件**（不要提交到版本控制）
```bash
# 添加到 .gitignore
echo ".env" >> .gitignore
```

2. **生产环境建议**
- 使用环境变量而非配置文件存储敏感信息
- 使用密钥管理服务（如 AWS Secrets Manager、Azure Key Vault）
- 定期轮换密码和 API Key
- 使用强密码策略

3. **Docker 环境变量**
```bash
# 使用环境变量文件
docker-compose --env-file .env up -d

# 或直接在命令行传递
docker run -e MYSQL_PASSWORD=your_password interview-app:latest
```

### 数据库安全

- 使用强密码（至少12位，包含大小写字母、数字、特殊字符）
- 限制数据库访问IP范围
- 启用SSL连接
- 定期备份数据

### API 安全

- 使用 HTTPS 协议
- 配置适当的 CORS 策略
- 实施请求频率限制
- 定期更新依赖包

---

## 🔧 配置说明

### AI 配置

```yaml
interview:
  ai:
    # 聊天记忆存储方式
    chat-memory-repository: mongodb  # memory | redis | mongodb
    # 最大聊天消息数
    max-chat-messages: 50
    # 模板分隔符
    start-delimiter-character: "<"
    end-delimiter-character: ">"
    # 轮次限制
    advisor:
      round:
        enabled: true
        max-rounds: 5
```

### 安全配置

```yaml
interview:
  webmvc:
    security:
      # 白名单路径
      white-list-patterns:
        - /api/auth/login
        - /api/auth/register
        - /actuator/**
      # 验证码配置
      captcha:
        effective-patterns:
          - /api/auth/login
```

### 数据源配置

```yaml
spring:
  # MySQL 配置
  datasource:
    url: ${MYSQL_JDBC_URL}
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}
    type: com.alibaba.druid.pool.DruidDataSource
  
  # Redis 配置
  data:
    redis:
      cluster:
        nodes:
          - ${REDIS_NODE_1}
          - ${REDIS_NODE_2}
        password: ${REDIS_PASSWORD}
  
  # MongoDB 配置
    mongodb:
      host: ${MONGODB_HOST}
      port: ${MONGODB_PORT}
      username: ${MONGODB_USERNAME}
      password: ${MONGODB_PASSWORD}
      database: interview
```

> ⚠️ **安全提醒**：请将 `.env` 文件添加到 `.gitignore` 中，避免将敏感信息提交到版本控制系统。生产环境建议使用环境变量或密钥管理服务。

## 🐳 Docker 部署

### 使用 Docker Compose

> ⚠️ **安全提醒**：`docker-compose-infrastructure.yml` 文件中包含默认的数据库密码等敏感信息，仅用于开发环境。生产环境请修改为强密码或使用环境变量。

```bash
# 启动所有服务（包括基础设施和应用）
docker-compose -f docker-compose-all.yml up -d

# 仅启动基础设施服务
docker-compose -f docker-compose-infrastructure.yml up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f interview-app-webmvc
docker-compose logs -f interview-app-webflux
```

### 自定义构建

```bash
# 构建 WebMVC 应用镜像
docker build -t interview-app-webmvc:latest ./interview-app-webmvc

# 构建 WebFlux 应用镜像
docker build -t interview-app-webflux:latest ./interview-app-webflux

# 运行容器
docker run -d -p 8081:8081 --name interview-webmvc interview-app-webmvc:latest
docker run -d -p 8082:8082 --name interview-webflux interview-app-webflux:latest
```

---

## 🔍 故障排除

### 常见问题

1. **启动失败：缺少 MessageChatMemoryAdvisor**
   ```bash
   # 解决方案：确认 AI Starter 已正确安装
   mvn -q -f interview-app-starters/pom.xml clean install
   ```

2. **启动失败：缺少 RoundLimitAdvisor**
   ```bash
   # 解决方案：确认配置文件中启用了轮次限制
   interview:
     ai:
       advisor:
         round:
           enabled: true
   ```

3. **AI 服务调用失败**
   ```bash
   # 解决方案：检查阿里云 API Key 配置
   echo $ALIYUN_API_KEY
   ```

4. **数据库连接失败**
   ```bash
   # 解决方案：检查数据库服务状态
   docker-compose ps mysql redis mongodb
   ```

### 日志查看

```bash
# 查看应用日志
docker-compose logs -f interview-app-webmvc
docker-compose logs -f interview-app-webflux

# 查看特定服务日志
docker logs -f interview-mysql
docker logs -f interview-redis-1
docker logs -f interview-mongodb
```

---

## 📊 性能优化

### JVM 优化

```bash
# 使用 JLink 优化镜像大小
docker build --build-arg JVM_OPTIMIZE=true -t interview-app:optimized .
```

### 数据库优化

- **MySQL**：配置连接池参数
- **Redis**：启用集群模式，配置持久化
- **MongoDB**：配置索引，优化查询性能

### 应用优化

- **异步处理**：简历解析等耗时操作异步化
- **缓存策略**：Redis 缓存热点数据
- **连接池**：合理配置数据库连接池参数

---

## 🤝 贡献指南

### 开发环境搭建

1. Fork 项目到个人仓库
2. 克隆项目到本地
3. 创建功能分支：`git checkout -b feature/your-feature`
4. 提交更改：`git commit -m "Add your feature"`
5. 推送分支：`git push origin feature/your-feature`
6. 创建 Pull Request

### 代码规范

- 使用 Java 17+ 特性
- 遵循 Spring Boot 最佳实践
- 添加必要的单元测试
- 更新相关文档

---

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)

---

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot) - 应用框架
- [Spring AI](https://spring.io/projects/spring-ai) - AI 集成框架
- [阿里云 DashScope](https://dashscope.aliyun.com/) - 大语言模型服务
- [Apache Tika](https://tika.apache.org/) - 文档解析
- [Hutool](https://hutool.cn/) - Java 工具库

---

## 📞 联系方式

- 项目维护者：kfzx-minglg
- 邮箱：2820996063@qq.com
- 项目地址：https://github.com/wreqawr/interview-web.git

---

<div align="center">

**如果这个项目对你有帮助，请给个 ⭐ Star 支持一下！**

</div>

