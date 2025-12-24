# Interview App - AI 模拟面试系统

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-informational.svg)](https://spring.io/projects/spring-cloud)
[![Spring Cloud Alibaba](https://img.shields.io/badge/SCA-2025.0.0.0-blue.svg)](https://github.com/alibaba/spring-cloud-alibaba)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)

## 📋 目录

- [项目功能介绍](#-项目功能介绍)
- [技术栈](#-技术栈)
- [项目架构](#-项目架构)

---

## 🎯 项目功能介绍

**Interview App** 是一个基于 Spring Cloud 微服务架构的 **AI 智能模拟面试系统**，旨在为求职者和 HR 提供智能化的面试服务。系统通过 AI 技术实现简历智能解析、个性化模拟面试、智能评估等功能，提升面试准备效率和招聘质量。

### 核心功能模块

#### 1. 🤖 AI 智能服务模块

**1.1 智能简历解析与分析**
- **多格式文档解析**：支持 PDF、DOC、DOCX、TXT 等常见简历格式
- **结构化信息提取**：自动提取基本信息（姓名、联系方式、工作年限等）、教育背景、工作经历、项目经验、技能清单等关键信息
- **智能分析报告**：
  - **面向求职者**：提供简历优化建议、技能匹配度分析、职业发展建议
  - **面向 HR**：提供候选人综合能力评估、岗位匹配度分析、面试建议
- **存储方案**：
  - 简历文件存储在 MinIO 对象存储
  - 解析后的结构化数据存储在 MongoDB
  - 简历元信息存储在 MySQL

**1.2 模拟面试系统**
- **个性化面试准备**：基于用户简历和投递岗位自动生成面试问题
- **多轮对话能力**：支持多轮问答交互，模拟真实面试场景
- **流式响应支持**：基于 WebFlux 实现 Server-Sent Events (SSE) 流式输出，提供实时交互体验
- **面试轮次控制**：可配置最大面试轮次，防止无限对话
- **对话记忆管理**：支持 Redis、MongoDB、内存三种存储方式的对话历史管理

**1.3 通用 AI 助手**
- **通用聊天功能**：提供不依赖记忆的即时问答服务
- **任务类型支持**：支持多种任务类型（通用聊天、简历分析、模拟面试等）
- **提示词模板引擎**：支持自定义分隔符的模板渲染，灵活配置 AI 行为

**1.4 AI Agent 智能体（亮点功能）** ⭐
- **智能体管理**：基于阿里云 ICE（智能通信引擎）的 AI Agent 智能体能力
- **消息聊天 Token 生成**：为实时聊天场景生成认证令牌，支持自定义过期时间
- **RTC 认证令牌**：为实时音视频通信（RTC）生成认证令牌，支持音视频面试场景
- **智能体实例管理**：查询和管理 AI Agent 实例信息，支持多区域部署
- **实时通信支持**：为未来音视频面试、语音面试等实时通信场景提供基础能力

#### 2. 👤 用户管理模块

**2.1 用户认证与授权**
- **用户注册/登录**：支持用户名、密码注册登录
- **JWT 无状态认证**：采用 JWT Token 实现无状态身份认证
- **RSA 加密传输**：登录密码通过 RSA 公钥加密传输，保障传输安全
- **图形验证码**：支持登录接口验证码防护，防止暴力破解
- **RBAC 权限控制**：基于角色的访问控制，支持角色和权限的灵活配置

**2.2 用户信息管理**
- **用户基础信息**：用户ID、用户名、昵称、邮箱、状态等
- **公司关联**：支持用户与公司的关联关系管理
- **角色管理**：支持多种角色（求职者、HR、管理员等）

#### 3. 📄 简历管理模块

**3.1 简历文件管理**
- **简历上传**：支持单文件上传，异步处理上传任务
- **简历下载**：支持批量下载简历文件
- **简历删除**：支持批量删除简历
- **简历预览**：提供简历文件的在线预览功能

**3.2 简历数据处理**
- **异步解析任务**：简历上传后异步执行解析任务，支持任务状态查询
- **简历元信息查询**：查询用户的所有简历列表和详细信息
- **解析结果查询**：支持同步和异步两种方式获取简历分析结果

#### 4. 💼 候选人管理模块

**4.1 岗位投递管理**
- **岗位列表查询**：查询当前用户投递的所有岗位信息
- **岗位信息展示**：显示岗位详情、公司信息、薪资范围等

#### 5. 🎙️ 面试管理模块

**5.1 文本面试**
- **面试准备**：基于简历和岗位信息准备面试会话
- **面试进行中**：支持面试过程中的多轮问答
- **流式响应**：通过 SSE 实现实时流式输出，提供更好的交互体验

**5.2 面试记录**
- **面试历史记录**：记录面试对话历史，支持后续查看和分析

#### 6. 🌐 API 网关模块

**6.1 统一入口**
- **路由转发**：基于 Spring Cloud Gateway 实现微服务路由转发
- **负载均衡**：集成 Spring Cloud LoadBalancer 实现服务负载均衡
- **服务发现**：基于 Nacos 实现服务注册与发现

**6.2 路由规则**
- `/api/user/**` → `user-service`
- `/api/resume/**` → `resume-service`
- `/api/ai/**` → `ai-service`
- `/api/candidate/**` → `candidate-service`
- `/api/interview/**` → `interview-service`

### 核心特性

- ✨ **微服务架构**：采用 Spring Cloud 微服务架构，服务拆分清晰，易于扩展和维护
- 🔧 **自定义 Starter**：AI 能力和认证安全以独立 Starter 提供，实现高内聚、可复用
- 🔄 **响应式支持**：支持 WebMVC 同步模式和 WebFlux 响应式模式
- 🗄️ **多数据源**：MySQL（关系型数据）、MongoDB（文档存储）、Redis（缓存）、MinIO（对象存储）
- 🔐 **企业级安全**：JWT 认证、RBAC 权限、RSA 加密、验证码防护
- 📊 **可观测性**：集成 Spring Boot Actuator，提供健康检查和指标监控
- ⚡ **异步任务处理**：支持异步任务执行和状态查询，提升系统响应速度
- 🎨 **提示词模板**：支持自定义模板和动态参数渲染，灵活配置 AI 行为

---

## 🛠️ 技术栈

### 核心框架与平台

| 技术 | 版本 | 说明 |
|------|------|------|
| **Java** | 17+ | 项目基础语言版本 |
| **Spring Boot** | 3.5.6 | 应用开发框架 |
| **Spring Cloud** | 2025.0.0 | 微服务框架 |
| **Spring Cloud Alibaba** | 2025.0.0.0 | Nacos 服务注册与配置中心 |
| **Spring Security** | 6.x | 安全框架 |
| **Spring Data** | - | 数据访问抽象 |
| **Maven** | 3.9+ | 项目构建工具 |

### 数据存储

| 技术 | 版本 | 用途 |
|------|------|------|
| **MySQL** | 8.0+ | 关系型数据存储（用户、角色、权限、岗位、任务等） |
| **Redis** | 6.0+ | 缓存和会话存储（支持集群模式） |
| **MongoDB** | 6.0+ | 文档存储（简历详情、对话历史） |
| **MinIO** | 8.6.0 | 对象存储（简历文件） |

### 数据访问

| 技术 | 版本 | 说明 |
|------|------|------|
| **MyBatis Plus** | 3.5.12 | ORM 框架，简化数据库操作 |
| **Druid** | 1.2.23 | 数据库连接池 |
| **Spring Data MongoDB** | - | MongoDB 数据访问 |
| **Spring Data Redis** | - | Redis 数据访问 |

### AI 与文档处理（核心亮点）⭐

| 技术 | 版本 | 说明 |
|------|------|------|
| **Spring AI** | - | Spring 官方 AI 抽象框架，提供统一的 AI 能力接口 |
| **Spring AI Alibaba** | 1.0.0.3 | Spring AI 阿里云集成，对接阿里云 DashScope API |
| **阿里云 DashScope** | - | AI 服务提供商（通义千问大模型） |
| **阿里云 ICE** | - | 智能通信引擎，提供 AI Agent 智能体能力 |
| **阿里云 Tea OpenAPI** | 0.3.11 | 阿里云 OpenAPI SDK，用于调用 AI Agent 相关接口 |
| **Apache Tika** | 3.2.2 | 文档内容提取和元数据分析 |

> 💡 **技术亮点**：项目深度集成 **Spring AI** 和 **Spring AI Alibaba**，通过自定义 Starter 封装了完整的 AI 能力，包括 ChatClient、对话记忆、模板渲染、Advisor 机制等。同时集成阿里云 ICE 提供的 **AI Agent 智能体**能力，为实时音视频面试、语音面试等高级场景提供支持。

### 微服务组件

| 技术 | 版本 | 说明 |
|------|------|------|
| **Spring Cloud Gateway** | - | API 网关（基于 WebFlux） |
| **Spring Cloud OpenFeign** | - | 服务间 HTTP 调用 |
| **Spring Cloud LoadBalancer** | - | 客户端负载均衡 |
| **Nacos** | - | 服务注册与配置中心 |

### 安全认证

| 技术 | 版本 | 说明 |
|------|------|------|
| **JWT (java-jwt)** | 4.5.0 | JWT Token 生成与验证 |
| **Spring Security** | 6.x | 安全认证框架 |
| **Hutool Crypto** | 5.8.39 | 加密工具（RSA、验证码） |

### 工具库

| 技术 | 版本 | 说明 |
|------|------|------|
| **Lombok** | - | 简化 Java 代码 |
| **Hutool** | 5.8.39 | Java 工具类库 |
| **Apache Commons Lang3** | 3.18.0 | Apache 通用工具库 |

### 监控与运维

| 技术 | 版本 | 说明 |
|------|------|------|
| **Spring Boot Actuator** | - | 应用监控和管理 |
| **Docker** | - | 容器化部署 |
| **Docker Compose** | - | 容器编排 |

### 阿里云服务

| 服务 | 版本 | 说明 |
|------|------|------|
| **阿里云 SDK Core** | 4.7.8 | 阿里云核心 SDK |
| **阿里云 Live SDK** | 3.9.74 | 视频直播 SDK（预留） |
| **阿里云 VOD SDK** | 2.16.34 | 视频点播 SDK（预留） |

### 自研组件

| 组件 | 说明 |
|------|------|
| **ai-spring-boot-starter** | AI 能力 Starter，基于 Spring AI 封装，提供 ChatClient、模板渲染、对话记忆、Advisor 机制等 |
| **authentication-spring-boot-starter** | 认证安全 Starter，提供 JWT、RBAC、验证码、WebMVC/WebFlux 双栈支持等 |

---

## 🏗️ 项目架构

### 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                       客户端层 (Client)                        │
│              Web 前端 / 移动端 / API 调用方                    │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                     API 网关层 (Gateway)                      │
│              Spring Cloud Gateway (WebFlux)                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  路由规则：/api/user/** → user-service                │  │
│  │          /api/resume/** → resume-service              │  │
│  │          /api/ai/** → ai-service                      │  │
│  │          /api/candidate/** → candidate-service        │  │
│  │          /api/interview/** → interview-service        │  │
│  └──────────────────────────────────────────────────────┘  │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ 用户服务     │  │ 简历服务     │  │ AI 服务      │
│ User Service │  │ Resume       │  │ AI Service   │
│              │  │ Service      │  │              │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                  │                  │
       ▼                  ▼                  ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ 候选人服务   │  │ 面试服务     │  │              │
│ Candidate    │  │ Interview    │  │              │
│ Service      │  │ Service      │  │              │
└──────────────┘  └──────────────┘  └──────────────┘
```

### 详细目录结构

```
interview-app/                                    # 父项目（聚合模块，Maven POM 类型）
│
├── gateway/                                      # API 网关模块
│   ├── src/main/java/cn/minglg/gateway/
│   │   └── GateWay.java                         # 网关启动类
│   └── src/main/resources/
│       ├── banner/banner.txt                    # 启动 Banner
│       └── config/application.yml               # 网关配置（路由规则、Nacos 配置）
│
├── service/                                      # 微服务模块聚合（Maven POM 类型）
│   │
│   ├── user-service/                            # 用户服务
│   │   ├── src/main/java/cn/minglg/user/
│   │   │   ├── UserApplication.java             # 启动类
│   │   │   ├── controller/                      # 控制器层（REST API）
│   │   │   │   ├── UserController.java          # 用户接口（注册、获取公钥）
│   │   │   │   └── CaptchaController.java       # 验证码接口
│   │   │   ├── service/                         # 服务层（业务逻辑）
│   │   │   │   ├── UserService.java             # 用户服务接口
│   │   │   │   └── impl/
│   │   │   │       └── UserServiceImpl.java     # 用户服务实现
│   │   │   ├── mapper/                          # MyBatis Mapper 接口
│   │   │   │   ├── UserMapper.java              # 用户 Mapper
│   │   │   │   ├── RoleMapper.java              # 角色 Mapper
│   │   │   │   ├── PermissionMapper.java        # 权限 Mapper
│   │   │   │   ├── UserRoleMapper.java          # 用户角色关联 Mapper
│   │   │   │   ├── UserCompanyMapper.java       # 用户公司关联 Mapper
│   │   │   │   └── CompanyMapper.java           # 公司 Mapper
│   │   │   ├── properties/                      # 配置属性类
│   │   │   │   └── RegisterProperties.java      # 注册配置属性
│   │   │   ├── exception/                       # 异常类
│   │   │   │   └── RegisterException.java       # 注册异常
│   │   │   ├── handler/                         # 异常处理器
│   │   │   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   │   │   └── listener/                        # 监听器
│   │   │       └── ApplicationShutdownListener.java # 应用关闭监听器
│   │   └── src/main/resources/
│   │       ├── banner/banner.txt                # 启动 Banner
│   │       ├── config/application.yml           # 服务配置
│   │       └── mapper/                          # MyBatis XML 映射文件
│   │           ├── UserMapper.xml
│   │           ├── RoleMapper.xml
│   │           ├── PermissionMapper.xml
│   │           ├── UserRoleMapper.xml
│   │           ├── UserCompanyMapper.xml
│   │           └── CompanyMapper.xml
│   │
│   ├── resume-service/                          # 简历服务
│   │   ├── src/main/java/cn/minglg/resume/
│   │   │   ├── ResumeApplication.java           # 启动类
│   │   │   ├── controller/                      # 控制器层
│   │   │   │   └── ResumeController.java        # 简历接口（上传、下载、删除、预览、分析等）
│   │   │   ├── service/                         # 业务逻辑层
│   │   │   │   ├── ResumeService.java           # 简历服务主接口
│   │   │   │   ├── MinioService.java            # MinIO 服务接口
│   │   │   │   ├── AsyncService.java            # 异步服务接口
│   │   │   │   └── impl/                        # 服务实现
│   │   │   │       ├── ResumeServiceImpl.java   # 简历服务实现
│   │   │   │       ├── MinioServiceImpl.java    # MinIO 服务实现
│   │   │   │       └── AsyncServiceImpl.java    # 异步服务实现
│   │   │   ├── config/                          # 配置类
│   │   │   │   ├── MinioConfig.java             # MinIO 客户端配置
│   │   │   │   └── ResumeParserConfig.java      # 简历解析器配置（Apache Tika）
│   │   │   ├── repository/                      # 数据访问层（MongoDB）
│   │   │   │   └── ResumeDetailRepository.java  # 简历详情 Repository
│   │   │   ├── mapper/                          # MyBatis Mapper 接口
│   │   │   │   └── ResumeMetadataMapper.java    # 简历元信息 Mapper
│   │   │   ├── feign/                           # Feign 客户端
│   │   │   │   └── AiServiceFeignClient.java    # AI 服务 Feign 客户端
│   │   │   ├── pojo/                            # 数据模型
│   │   │   │   └── ResumeMetadata.java          # 简历元信息实体
│   │   │   ├── properties/                      # 配置属性类
│   │   │   │   ├── MinioProperties.java         # MinIO 配置属性
│   │   │   │   └── ResumeProperties.java        # 简历服务配置属性
│   │   │   ├── exception/                       # 异常类
│   │   │   │   ├── ResumeUploadException.java   # 简历上传异常
│   │   │   │   ├── ResumeDownloadException.java # 简历下载异常
│   │   │   │   ├── ResumeDeleteException.java   # 简历删除异常
│   │   │   │   ├── ResumePreviewException.java  # 简历预览异常
│   │   │   │   ├── ResumeAnalyzeAndSaveException.java # 简历分析保存异常
│   │   │   │   └── NoSuchResumeException.java   # 简历不存在异常
│   │   │   ├── handler/                         # 异常处理器
│   │   │   │   └── GlobalExceptionHandler.java  # 全局异常处理器
│   │   │   └── utils/                           # 工具类
│   │   │       └── FileUtils.java               # 文件工具类
│   │   └── src/main/resources/
│   │       ├── banner/banner.txt                # 启动 Banner
│   │       └── config/application.yml           # 服务配置
│   │
│   ├── ai-service/                              # AI 服务（核心服务）⭐
│   │   ├── src/main/java/cn/minglg/ai/
│   │   │   ├── AiApplication.java               # 启动类
│   │   │   │
│   │   │   ├── assistant/                       # AI 助手模块（基于 Spring AI）
│   │   │   │   ├── controller/
│   │   │   │   │   └── AssistantController.java # AI 助手接口（通用聊天、带记忆聊天、准备聊天）
│   │   │   │   ├── service/
│   │   │   │   │   └── AssistantService.java    # AI 助手服务（ChatClient 调用）
│   │   │   │   ├── config/
│   │   │   │   │   └── AssistantConfig.java     # AI 助手配置（提示词模板、Advisor 配置）
│   │   │   │   ├── advisor/
│   │   │   │   │   └── RoundAdvisorRepository.java # 轮次限制 Advisor 仓库
│   │   │   │   ├── repository/
│   │   │   │   │   └── RedisRoundLimitManger.java # Redis 轮次限制管理器
│   │   │   │   ├── properties/
│   │   │   │   │   └── RoundLimitProperties.java # 轮次限制配置属性
│   │   │   │   └── exception/
│   │   │   │       └── AssistantCallException.java # AI 助手调用异常
│   │   │   │
│   │   │   ├── agent/                           # AI Agent 智能体模块（亮点功能）⭐
│   │   │   │   ├── controller/
│   │   │   │   │   └── AgentController.java     # AI Agent 接口
│   │   │   │   │       ├── generateMessageChatToken() # 生成消息聊天 Token
│   │   │   │   │       ├── getRtcAuthToken()    # 获取 RTC 认证令牌
│   │   │   │   │       └── describeAIAgentInstance() # 描述 AI Agent 实例
│   │   │   │   ├── service/
│   │   │   │   │   ├── ImsService.java          # 智能通信服务接口（IMS = Intelligent Messaging Service）
│   │   │   │   │   ├── AiAgentService.java      # AI Agent 服务接口
│   │   │   │   │   └── impl/
│   │   │   │   │       ├── ImsServiceImpl.java  # IMS 服务实现（业务封装）
│   │   │   │   │       └── AiAgentServiceImpl.java # AI Agent 服务实现（阿里云 ICE API 调用）
│   │   │   │   ├── dto/
│   │   │   │   │   ├── req/                     # 请求 DTO
│   │   │   │   │   │   ├── GenerateMessageChatTokenRequestDto.java
│   │   │   │   │   │   ├── RtcAuthTokenRequestDto.java
│   │   │   │   │   │   └── AiAgentInstanceDescribeRequestDto.java
│   │   │   │   │   └── res/                     # 响应 DTO
│   │   │   │   │       ├── GenerateMessageChatTokenResponse.java
│   │   │   │   │       └── AiAgentInstanceDescribeResponse.java
│   │   │   │   └── properties/
│   │   │   │       └── AiAgentProperties.java   # AI Agent 配置属性（AccessKey、区域等）
│   │   │   │
│   │   │   └── handler/                         # 异常处理器
│   │   │       └── GlobalExceptionHandler.java  # 全局异常处理器
│   │   │
│   │   └── src/main/resources/
│   │       ├── banner/banner.txt                # 启动 Banner
│   │       ├── config/application.yml           # 服务配置
│   │       └── prompt/                          # 提示词模板（StringTemplate 格式）
│   │           ├── general/                     # 通用聊天模板
│   │           │   └── 通用聊天.st
│   │           ├── interview/                   # 面试相关模板
│   │           │   ├── 基于简历内容的模拟面试问答（开始）.st
│   │           │   ├── 基于简历内容的模拟面试问答（结束）.st
│   │           │   └── 面试官角色预设.st
│   │           └── resume/                      # 简历分析模板
│   │               ├── 简历关键信息提取.st
│   │               ├── 简历分析-求职者.st
│   │               └── 综合评估-HR.st
│   │
│   ├── interview-service/                       # 面试服务
│   │   ├── src/main/java/cn/minglg/interview/
│   │   │   ├── InterviewApplication.java        # 启动类
│   │   │   │
│   │   │   ├── text/                            # 文本面试模块
│   │   │   │   ├── controller/
│   │   │   │   │   └── TextChatController.java  # 文本面试接口
│   │   │   │   │       ├── prepareChat()        # 准备面试会话
│   │   │   │   │       └── textChatInterviewInProgress() # 面试进行中的问答
│   │   │   │   └── service/
│   │   │   │       └── TextChatService.java     # 文本面试服务（SSE 流式响应）
│   │   │   │
│   │   │   ├── video/                           # 视频面试模块（预留，未来扩展）
│   │   │   │
│   │   │   ├── voice/                           # 语音面试模块（预留，未来扩展）
│   │   │   │
│   │   │   └── feign/                           # Feign 客户端（服务间调用）
│   │   │       ├── AiServiceFeignClient.java    # AI 服务 Feign 客户端
│   │   │       ├── ResumeServiceFeignClient.java # 简历服务 Feign 客户端
│   │   │       └── CandidateServiceFeignClient.java # 候选人服务 Feign 客户端
│   │   │
│   │   └── src/main/resources/
│   │       ├── banner/banner.txt                # 启动 Banner
│   │       └── config/application.yml           # 服务配置
│   │
│   └── candidate-service/                       # 候选人服务
│       ├── src/main/java/cn/minglg/candidate/
│       │   ├── CandidateApplication.java        # 启动类
│       │   ├── controller/
│       │   │   └── CandidateController.java     # 候选人接口（获取岗位列表）
│       │   ├── service/
│       │   │   └── CandidateService.java        # 候选人服务
│       │   ├── mapper/
│       │   │   └── JobMapper.java               # 岗位 Mapper
│       │   └── dto/
│       │       └── JobDTO.java                  # 岗位数据传输对象
│       └── src/main/resources/
│           ├── banner/banner.txt                # 启动 Banner
│           └── config/application.yml           # 服务配置
│
├── commons/                                      # 公共模块（被各服务依赖）
│   ├── src/main/java/cn/minglg/commons/
│   │   │
│   │   ├── model/                               # 数据模型（实体类）
│   │   │   ├── user/                            # 用户相关模型
│   │   │   │   ├── User.java                    # 用户实体
│   │   │   │   ├── Role.java                    # 角色实体
│   │   │   │   ├── Permission.java              # 权限实体
│   │   │   │   └── Company.java                 # 公司实体
│   │   │   ├── resume/                          # 简历相关模型
│   │   │   │   ├── ResumeDetail.java            # 简历详情（MongoDB 文档）
│   │   │   │   └── ResumeStatus.java            # 简历状态枚举
│   │   │   ├── candidate/                       # 候选人相关模型
│   │   │   │   └── Job.java                     # 岗位实体
│   │   │   ├── task/                            # 任务相关模型
│   │   │   │   ├── Task.java                    # 任务实体
│   │   │   │   ├── TaskStatus.java              # 任务状态枚举
│   │   │   │   └── TaskType.java                # 任务类型枚举
│   │   │   ├── response/                        # 响应模型
│   │   │   │   ├── GenericResponse.java         # 通用响应包装类
│   │   │   │   └── ResponseCode.java            # 响应码枚举
│   │   │   ├── context/                         # 上下文模型
│   │   │   │   └── RequestScopedUserContext.java # 请求作用域用户上下文
│   │   │   └── constants/                       # 常量类
│   │   │       └── Constants.java               # 通用常量
│   │   │
│   │   ├── async/                               # 异步任务支持
│   │   │   ├── AsyncConfig.java                 # 异步任务配置（线程池配置）
│   │   │   ├── AsyncContext.java                # 异步上下文（任务上下文信息）
│   │   │   ├── AsyncContextHolder.java          # 异步上下文持有者（ThreadLocal）
│   │   │   └── AsyncProperties.java             # 异步任务配置属性
│   │   │
│   │   ├── feign/                               # Feign 拦截器
│   │   │   └── FeignRequestInterceptor.java     # Feign 请求拦截器（传递 Token 等）
│   │   │
│   │   ├── mapper/                              # 公共 Mapper
│   │   │   └── TaskMapper.java                  # 任务 Mapper（任务状态查询）
│   │   │
│   │   ├── aspects/                             # AOP 切面
│   │   │   └── TaskAspect.java                  # 任务切面（自动创建和更新任务状态）
│   │   │
│   │   ├── annotation/                          # 自定义注解
│   │   │   ├── AsyncTaskQuery.java              # 异步任务查询注解
│   │   │   └── TaskHandler.java                 # 任务处理器注解
│   │   │
│   │   ├── exception/                           # 异常类
│   │   │   └── NoSuchTaskException.java         # 任务不存在异常
│   │   │
│   │   └── utils/                               # 工具类
│   │       ├── JsonUtils.java                   # JSON 工具类
│   │       └── TaskUtils.java                   # 任务工具类
│   │
│   └── src/test/java/init/                      # 数据库初始化脚本（测试用）
│       ├── ddl/                                 # DDL 脚本（建表语句）
│       │   ├── create_t_user.sql                # 用户表
│       │   ├── create_t_role.sql                # 角色表
│       │   ├── create_t_permission.sql          # 权限表
│       │   ├── create_t_user_role.sql           # 用户角色关联表
│       │   ├── create_t_user_company.sql        # 用户公司关联表
│       │   ├── create_t_company.sql             # 公司表
│       │   ├── create_t_jobs.sql                # 岗位表
│       │   ├── create_t_task.sql                # 任务表
│       │   ├── create_table_t_resume_metadata.sql # 简历元信息表
│       │   └── ...
│       └── dml/                                 # DML 脚本（初始数据）
│           ├── insert_t_user.sql                # 初始用户数据
│           ├── insert_t_role.sql                # 初始角色数据
│           ├── insert_t_permission.sql          # 初始权限数据
│           └── ...
│
└── starters/                                     # 自定义 Starter 模块（独立构建，Maven POM 类型）
    │
    ├── ai-spring-boot-autoconfigure/            # AI Starter 自动配置模块
    │   ├── src/main/java/org/minglg/ai/
    │   │   ├── autoconfig/
    │   │   │   └── AiAutoConfiguration.java     # AI 自动配置类（核心）
    │   │   │
    │   │   ├── config/                          # 配置类
    │   │   │   ├── InMemoryChatMemoryRepositoryConfig.java # 内存对话记忆仓库配置
    │   │   │   ├── RedisChatMemoryRepositoryConfig.java    # Redis 对话记忆仓库配置
    │   │   │   └── MongoChatMemoryRepositoryConfig.java    # MongoDB 对话记忆仓库配置
    │   │   │
    │   │   ├── repository/                      # 对话记忆仓库实现
    │   │   │   ├── webmvc/                      # WebMVC 实现
    │   │   │   │   ├── MongoChatMemoryRepository.java
    │   │   │   │   └── RedisChatMemoryRepository.java
    │   │   │   └── webflux/                     # WebFlux 实现
    │   │   │       ├── ReactiveMongoChatMemoryRepository.java
    │   │   │       └── ReactiveRedisChatMemoryRepository.java
    │   │   │
    │   │   ├── advisors/                        # Advisor（增强器）
    │   │   │   ├── CommonAdvisor.java           # 通用 Advisor 接口
    │   │   │   ├── CommonAdvisorRepository.java # CommonAdvisor 仓库
    │   │   │   ├── RoundLimitManager.java       # 轮次限制管理器（WebMVC）
    │   │   │   └── ReactiveRoundLimitManager.java # 轮次限制管理器（WebFlux）
    │   │   │
    │   │   ├── render/                          # 模板渲染器
    │   │   │   └── CustomMultiCharTemplateRenderer.java # 自定义多字符分隔符模板渲染器
    │   │   │
    │   │   ├── context/                         # 上下文提供者
    │   │   │   └── UserContextProvider.java     # 用户上下文提供者接口
    │   │   │
    │   │   ├── deserializer/                    # 反序列化器
    │   │   │   └── MessageDeserializer.java     # 消息反序列化器
    │   │   │
    │   │   ├── pojo/                            # 数据模型
    │   │   │   └── ChatHistory.java             # 聊天历史实体
    │   │   │
    │   │   └── properties/                      # 配置属性
    │   │       └── AiProperties.java            # AI 配置属性（对话记忆类型、最大消息数等）
    │   │
    │   └── src/main/resources/
    │       ├── META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
    │       └── META-INF/spring-configuration-metadata.json
    │
    ├── ai-spring-boot-starter/                  # AI Starter（聚合依赖，仅 POM）
    │   └── pom.xml                              # 聚合 ai-spring-boot-autoconfigure
    │
    ├── authentication-spring-boot-autoconfigure/ # 认证 Starter 自动配置模块
    │   ├── src/main/java/org/minglg/authentication/
    │   │   ├── autoconfig/
    │   │   │   ├── WebMvcSecurityAutoConfiguration.java # WebMVC 安全自动配置
    │   │   │   └── WebFluxSecurityAutoConfiguration.java # WebFlux 安全自动配置
    │   │   │
    │   │   ├── config/                          # 配置类
    │   │   │   ├── webmvc/                      # WebMVC 配置
    │   │   │   │   ├── WebMvcFilterConfig.java  # WebMVC 过滤器配置
    │   │   │   │   ├── WebMvcHandlerConfig.java # WebMVC 处理器配置
    │   │   │   │   ├── WebMvcServiceConfig.java # WebMVC 服务配置
    │   │   │   │   ├── CaptchaGeneratorConfig.java # 验证码生成器配置
    │   │   │   │   └── RsaKeyGeneratorConfig.java # RSA 密钥生成器配置
    │   │   │   └── webflux/                     # WebFlux 配置
    │   │   │       ├── WebFluxFilterConfig.java # WebFlux 过滤器配置
    │   │   │       └── WebFluxHandlerConfig.java # WebFlux 处理器配置
    │   │   │
    │   │   ├── filter/                          # 过滤器
    │   │   │   ├── webmvc/                      # WebMVC 过滤器
    │   │   │   │   ├── WebMvcJwtTokenFilter.java # JWT Token 过滤器
    │   │   │   │   ├── WebMvcCaptchaFilter.java # 验证码过滤器
    │   │   │   │   ├── WebMvcCustomAuthenticationFilter.java # 自定义认证过滤器
    │   │   │   │   └── WebMvcRequestBodyCacheFilter.java # 请求体缓存过滤器
    │   │   │   └── webflux/                     # WebFlux 过滤器
    │   │   │       └── WebFluxJwtTokenFilter.java # JWT Token 过滤器（响应式）
    │   │   │
    │   │   ├── handler/                         # 处理器（Spring Security 处理器）
    │   │   │   ├── webmvc/                      # WebMVC 处理器
    │   │   │   │   ├── WebMvcCustomAuthenticationSuccessHandler.java # 认证成功处理器
    │   │   │   │   ├── WebMvcCustomAuthenticationFailureHandler.java # 认证失败处理器
    │   │   │   │   ├── WebMvcCustomAccessDeniedHandler.java # 权限拒绝处理器
    │   │   │   │   └── WebMvcCustomLogoutSuccessHandler.java # 登出成功处理器
    │   │   │   └── webflux/                     # WebFlux 处理器
    │   │   │       └── WebFluxCustomAccessDeniedHandler.java # 权限拒绝处理器（响应式）
    │   │   │
    │   │   ├── service/                         # 服务层
    │   │   │   ├── CaptchaService.java          # 验证码服务接口
    │   │   │   ├── RsaService.java              # RSA 加密服务接口
    │   │   │   └── impl/                        # 服务实现
    │   │   │       ├── CaptchaServiceImpl.java  # 验证码服务实现
    │   │   │       └── RsaServiceImpl.java      # RSA 加密服务实现
    │   │   │
    │   │   ├── properties/                      # 配置属性
    │   │   │   ├── WebMvcSecurityProperties.java # WebMVC 安全配置属性
    │   │   │   └── WebFluxSecurityProperties.java # WebFlux 安全配置属性
    │   │   │
    │   │   ├── pojo/                            # 数据模型
    │   │   │   └── SecurityUser.java            # 安全用户实体（Spring Security）
    │   │   │
    │   │   ├── exception/                       # 异常类
    │   │   │   ├── InvalidUsernameOrPasswordException.java # 用户名或密码错误异常
    │   │   │   └── UnKnowUserException.java     # 未知用户异常
    │   │   │
    │   │   ├── utils/                           # 工具类
    │   │   │   ├── JwtUtils.java                # JWT 工具类（生成、解析 Token）
    │   │   │   ├── RsaUtils.java                # RSA 工具类（加密、解密）
    │   │   │   ├── CaptchaUtils.java            # 验证码工具类（生成图形验证码）
    │   │   │   └── WebFluxResponseUtils.java    # WebFlux 响应工具类
    │   │   │
    │   │   └── wrapper/                         # 包装类
    │   │       ├── CachedBodyHttpServletRequestWrapper.java # 请求体缓存包装器
    │   │       └── CachedBodyServletInputStreamWrapper.java # 输入流包装器
    │   │
    │   └── src/main/resources/
    │       ├── META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
    │       └── META-INF/spring-configuration-metadata.json
    │
    └── authentication-spring-boot-starter/      # 认证 Starter（聚合依赖，仅 POM）
        └── pom.xml                              # 聚合 authentication-spring-boot-autoconfigure
```

### 技术架构说明

#### 1. **微服务架构**
- **服务拆分**：按照业务领域拆分为 5 个核心微服务（用户、简历、AI、面试、候选人）
- **服务注册与发现**：基于 Nacos 实现服务注册与发现
- **服务调用**：使用 OpenFeign 实现服务间 HTTP 调用
- **负载均衡**：Spring Cloud LoadBalancer 实现客户端负载均衡

#### 2. **API 网关**
- **统一入口**：所有外部请求通过 Spring Cloud Gateway 进入系统
- **路由转发**：根据路径规则转发到对应的微服务
- **响应式**：基于 WebFlux 实现非阻塞 I/O

#### 3. **数据存储架构**
- **MySQL**：存储结构化数据（用户、角色、权限、岗位、任务等）
- **MongoDB**：存储非结构化数据（简历详情、对话历史等）
- **Redis**：缓存和会话存储（验证码、Token、对话记忆等）
- **MinIO**：对象存储（简历文件）

#### 4. **安全架构**
- **认证层**：JWT Token 无状态认证
- **授权层**：基于 Spring Security 的 RBAC 权限控制
- **传输安全**：RSA 加密密码传输
- **防护措施**：图形验证码、异常处理器

#### 5. **AI 能力架构**（核心亮点）⭐
- **Spring AI 框架**：使用 Spring 官方 AI 抽象框架，提供统一的 AI 能力接口
- **Spring AI Alibaba**：通过 Spring AI Alibaba 集成阿里云 DashScope（通义千问大模型）
- **ChatClient**：基于 Spring AI 的聊天客户端，支持同步和响应式两种模式
- **对话记忆**：支持多种存储方式（Redis、MongoDB、内存），可配置切换
- **模板引擎**：自定义提示词模板渲染，支持 StringTemplate 格式和自定义分隔符
- **Advisor 机制**：可插拔的增强功能（轮次限制、日志记录、参数注入等）
- **AI Agent 智能体**：集成阿里云 ICE（智能通信引擎），提供：
  - 消息聊天 Token 生成（实时聊天场景）
  - RTC 认证令牌生成（音视频面试场景）
  - AI Agent 实例管理（多区域部署支持）

#### 6. **异步任务架构**
- **异步执行**：使用 Spring `@Async` 实现异步任务执行
- **任务追踪**：基于数据库的任务状态管理
- **上下文传递**：使用 RequestScope 解决异步任务中的上下文丢失问题

#### 7. **自定义 Starter 架构**
- **模块化设计**：AI 能力和认证安全封装为独立 Starter
- **自动配置**：基于 Spring Boot 自动配置机制
- **条件装配**：根据配置条件自动装配相关 Bean
- **双栈支持**：同时支持 WebMVC 和 WebFlux 两种模式

### 微服务功能划分

#### 1. **user-service（用户服务）**
- **职责**：用户认证、用户信息管理、RBAC 权限管理
- **核心功能**：
  - 用户注册、登录（JWT 认证）
  - RSA 公钥获取、密码加密传输
  - 图形验证码生成和验证
  - 用户信息管理（基础信息、公司关联、角色关联）
  - RBAC 权限管理（角色、权限的查询和管理）
- **数据存储**：MySQL（用户、角色、权限、公司等关系型数据）
- **依赖关系**：无依赖其他微服务

#### 2. **resume-service（简历服务）**
- **职责**：简历文件管理、简历解析、简历分析
- **核心功能**：
  - 简历文件上传（MinIO 对象存储）
  - 简历文件下载、删除、预览
  - 简历解析（Apache Tika 提取文本 + AI 提取结构化信息）
  - 简历智能分析（面向求职者优化建议 + 面向 HR 综合评估）
  - 异步任务管理（上传、解析、分析任务的异步执行和状态查询）
- **数据存储**：
  - MySQL（简历元信息）
  - MongoDB（简历结构化详情、分析报告）
  - MinIO（简历原始文件）
- **依赖关系**：
  - ai-service（通过 Feign 调用 AI 服务进行简历分析和信息提取）
  - user-service（通过认证获取用户信息，隐式依赖）

#### 3. **ai-service（AI 服务）**⭐ 核心服务
- **职责**：AI 能力提供（基于 Spring AI + 阿里云 DashScope + 阿里云 ICE）
- **核心功能**：
  - **AI 助手模块**（基于 Spring AI）：
    - 通用聊天（不带记忆的即时问答）
    - 带记忆的聊天（支持多轮对话，对话历史管理）
    - 聊天会话准备（模拟面试开始场景）
  - **AI Agent 智能体模块**（基于阿里云 ICE）：
    - 消息聊天 Token 生成（为实时聊天提供认证令牌）
    - RTC 认证令牌生成（为音视频面试提供认证令牌）
    - AI Agent 实例管理（查询智能体实例信息，支持多区域）
- **数据存储**：
  - Redis/MongoDB（对话历史，根据配置选择）
  - 内存（内存模式的对话记忆）
- **依赖关系**：无依赖其他微服务（独立 AI 服务）

#### 4. **interview-service（面试服务）**
- **职责**：面试会话管理、面试流程编排
- **核心功能**：
  - 文本面试（基于简历和岗位的个性化面试问答）
  - 面试会话准备（获取简历和岗位信息，准备面试）
  - 面试进行中（多轮问答交互，SSE 流式响应）
  - 面试记录（对话历史保存和管理）
  - 预留扩展：视频面试、语音面试模块
- **数据存储**：MongoDB/Redis（面试对话历史）
- **依赖关系**：
  - ai-service（通过 Feign 调用 AI 助手进行面试对话）
  - resume-service（通过 Feign 获取简历信息）
  - candidate-service（通过 Feign 获取岗位信息）

#### 5. **candidate-service（候选人服务）**
- **职责**：候选人投递管理、岗位信息查询
- **核心功能**：
  - 岗位列表查询（查询当前用户投递的所有岗位）
  - 岗位信息展示（岗位详情、公司信息、薪资范围等）
- **数据存储**：MySQL（岗位信息、投递记录）
- **依赖关系**：无依赖其他微服务

#### 6. **gateway（API 网关）**
- **职责**：统一入口、路由转发、负载均衡
- **核心功能**：
  - 路由转发（根据路径规则转发到对应的微服务）
  - 负载均衡（集成 Spring Cloud LoadBalancer）
  - 服务发现（基于 Nacos 的服务注册与发现）
- **路由规则**：
  - `/api/user/**` → `user-service`
  - `/api/resume/**` → `resume-service`
  - `/api/ai/**` → `ai-service`
  - `/api/candidate/**` → `candidate-service`
  - `/api/interview/**` → `interview-service`

### 服务间调用关系

```
┌─────────────┐
│   Gateway   │ 统一入口，路由转发
└──────┬──────┘
       │
       ├─────────────────────────────────────────┐
       │                                         │
       ▼                                         ▼
┌──────────────┐                        ┌──────────────┐
│ user-service │                        │ ai-service   │
│              │                        │              │
│ • 用户认证    │                        │ • AI 助手     │
│ • 权限管理    │                        │ • AI Agent   │
│ • 用户信息    │                        │ • 智能体      │
│              │                        │              │
│ 依赖：无      │                        │ 依赖：无      │
└──────────────┘                        └──────┬───────┘
                                               │
       ┌───────────────────────────────────────┘
       │
       ▼
┌──────────────┐                        ┌──────────────┐
│resume-service│                        │candidate-    │
│              │                        │service       │
│ • 简历管理    │                        │              │
│ • 简历解析    │                        │ • 岗位查询    │
│ • 简历分析    │                        │ • 投递管理    │
│              │                        │              │
│ 依赖：        │                        │ 依赖：无      │
│ • ai-service │                        └──────────────┘
└──────┬───────┘
       │
       └─────────────────────────────────────────┐
                                                 │
                                                 ▼
                                        ┌──────────────┐
                                        │interview-    │
                                        │service       │
                                        │              │
                                        │ • 文本面试    │
                                        │ • 面试流程    │
                                        │              │
                                        │ 依赖：        │
                                        │ • ai-service │
                                        │ • resume-    │
                                        │   service    │
                                        │ • candidate- │
                                        │   service    │
                                        └──────────────┘
```

### 数据流向

1. **简历处理流程**：
   ```
   用户上传简历 → resume-service → MinIO存储
                              ↓
                         异步解析任务
                              ↓
                         Tika提取文本
                              ↓
                         AI提取结构化信息
                              ↓
                         MongoDB存储结构化数据
                              ↓
                         AI生成分析报告
                              ↓
                         存储分析结果
   ```

2. **面试流程**：
   ```
   用户发起面试 → interview-service
                    ↓
               获取简历信息 (resume-service)
                    ↓
               获取岗位信息 (candidate-service)
                    ↓
               调用 AI 服务准备面试 (ai-service)
                    ↓
               开始多轮对话 (SSE流式响应)
                    ↓
               保存对话历史 (MongoDB/Redis)
   ```

---

## 📡 API 接口文档

### 1. 用户服务接口（user-service）

#### 1.1 用户认证相关

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| `GET` | `/api/user/publicKey` | 获取 RSA 公钥，用于加密登录密码 | 否 |
| `POST` | `/api/user/register` | 用户注册，支持用户名、密码、邮箱等信息 | 否 |
| `GET` | `/api/user/captcha` | 获取图形验证码，返回 JPEG 图片，Header 中包含 `captchaId` | 否 |

**接口说明**：
- 登录流程：先获取公钥 → 获取验证码 → 使用 RSA 公钥加密密码 → 发送登录请求（由认证 Starter 处理）
- 验证码有效期为配置的时间（默认 5 分钟）

---

### 2. 简历服务接口（resume-service）

#### 2.1 简历文件管理

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| `POST` | `/api/resume/upload` | 上传简历文件，支持 PDF、DOC、DOCX、TXT，表单字段名为 `resume` | 是 |
| `GET` | `/api/resume/download` | 批量下载简历文件，参数：`resumeIds`（多个简历ID） | 是 |
| `DELETE` | `/api/resume/delete` | 批量删除简历，请求体：`string[]` 简历ID数组 | 是 |
| `GET` | `/api/resume/preview/{resumeId}` | 获取简历预览地址（MinIO 临时访问链接） | 是 |

#### 2.2 简历信息查询

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| `GET` | `/api/resume/getMyResume` | 获取当前用户的简历元信息列表（需要 `JOB_SEEKER` 角色） | 是 |
| `GET` | `/api/resume/getResumeDetail/{resumeId}` | 获取简历详情（包含解析后的结构化信息和分析报告） | 是 |

#### 2.3 简历解析与分析

| 方法 | 路径 | 说明 | 认证 | 响应 |
|------|------|------|------|------|
| `GET` | `/api/resume/analyze/{resumeId}` | 触发简历分析，同步返回结果或异步返回 `taskId` | 是 | 同步：分析结果<br>异步：`taskId` |
| `GET` | `/api/resume/queryResumeAsyncUploadResult/{taskId}/{resumeId}` | 查询简历上传异步任务结果 | 是 | 任务状态和结果 |
| `GET` | `/api/resume/queryResumeAsyncAnalyzeResult/{taskId}/{resumeId}` | 查询简历分析异步任务结果 | 是 | 任务状态和结果 |

**接口说明**：
- 简历上传后自动触发异步解析任务，可通过查询接口获取解析进度
- 简历分析支持同步和异步两种模式，根据任务复杂度自动选择
- 分析结果包含：基本信息提取、结构化数据、面向求职者的优化建议、面向 HR 的综合评估

---

### 3. AI 服务接口（ai-service）

#### 3.1 AI 助手接口（基于 Spring AI）

| 方法 | 路径 | 说明 | 认证 | 请求参数 |
|------|------|------|------|----------|
| `POST` | `/api/ai/chat` | 带记忆的聊天，支持多轮对话，对话历史会自动保存 | 是 | `conversationId`（会话ID）<br>`userMessage`（用户消息）<br>`taskType`（任务类型）<br>`params`（额外参数） |
| `POST` | `/api/ai/assistant` | 不带记忆的即时问答助手，每次都是独立对话 | 是 | `userMessage`（用户消息）<br>`taskType`（任务类型） |
| `POST` | `/api/ai/prepareChat` | 准备聊天会话，用于模拟面试开始等场景 | 是 | `conversationId`（会话ID）<br>`params`（模板参数） |

**任务类型（TaskType）**：
- `GENERAL_CHAT`：通用聊天
- `RESUME_ANALYSIS`：简历分析
- `MOCK_INTERVIEW`：模拟面试
- `MOCK_INTERVIEW_START`：模拟面试开始
- `MOCK_INTERVIEW_END`：模拟面试结束

#### 3.2 AI Agent 智能体接口（基于阿里云 ICE）⭐

| 方法 | 路径 | 说明 | 认证 | 请求参数 |
|------|------|------|------|----------|
| `POST` | `/api/ai/agent/generateMessageChatToken` | 生成消息聊天 Token，用于实时聊天场景的认证 | 是 | `aiAgentId`（AI Agent ID）<br>`role`（用户角色）<br>`userId`（用户ID）<br>`expire`（过期时间，秒）<br>`region`（区域） |
| `POST` | `/api/ai/agent/getRtcAuthToken` | 获取 RTC 认证令牌，用于音视频面试场景的认证 | 是 | `channelId`（频道ID）<br>`userId`（用户ID）<br>`timestamp`（时间戳）等 |
| `POST` | `/api/ai/agent/describeAIAgentInstance` | 描述 AI Agent 实例信息，查询智能体的详细信息 | 是 | `aiAgentInstanceId`（实例ID）<br>`region`（区域） |

**接口说明**：
- AI Agent 智能体功能基于阿里云 ICE（智能通信引擎），为实时音视频面试等场景提供基础能力
- 支持多区域部署，可根据 `region` 参数访问不同区域的 AI Agent 实例

---

### 4. 面试服务接口（interview-service）

| 方法 | 路径 | 说明 | 认证 | 响应方式 | 请求参数 |
|------|------|------|------|----------|----------|
| `POST` | `/api/interview/textChat/prepare` | 准备面试会话，基于简历和岗位信息生成面试准备消息 | 是 | 同步/SSE | `conversationId`（会话ID）<br>`jobId`（岗位ID）<br>`resumeId`（简历ID） |
| `POST` | `/api/interview/textChat/progress` | 面试进行中的文本问答，支持多轮对话 | 是 | SSE 流式响应 | `conversationId`（会话ID）<br>`userMessage`（用户消息） |

**接口说明**：
- `/prepare` 接口会调用 `resume-service` 获取简历信息，调用 `candidate-service` 获取岗位信息，然后调用 `ai-service` 准备面试会话
- `/progress` 接口支持 SSE（Server-Sent Events）流式响应，提供实时的面试问答体验
- 对话历史自动保存到 MongoDB 或 Redis（根据配置）

---

### 5. 候选人服务接口（candidate-service）

| 方法 | 路径 | 说明 | 认证 | 说明 |
|------|------|------|------|------|
| `GET` | `/api/candidate/getJobList` | 获取当前用户投递的岗位列表 | 是 | 返回岗位列表，包含岗位基本信息 |
| `GET` | `/api/candidate/getJobDetails/{jobId}` | 获取岗位详细信息 | 是 | 返回完整的岗位信息，包括公司信息、薪资范围、岗位描述等 |

**接口说明**：
- 所有接口都会根据当前登录用户过滤数据，确保用户只能查看自己投递的岗位信息

---

### 6. 认证相关接口（由 authentication-starter 提供）

这些接口由认证 Starter 自动配置，不在 Controller 中显式定义：

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| `POST` | `/api/auth/login` | 用户登录，需要验证码，返回 JWT Token | 否 |
| `POST` | `/api/auth/logout` | 用户登出 | 是 |

**登录请求参数**：
```json
{
  "username": "用户名",
  "password": "RSA加密后的密码",
  "captcha": "验证码",
  "captchaId": "验证码ID"
}
```

---

### 接口调用示例

#### 1. 用户注册流程
```bash
# 1. 获取公钥
curl -X GET http://localhost:8080/api/user/publicKey

# 2. 获取验证码
curl -X GET http://localhost:8080/api/user/captcha

# 3. 用户注册
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "encrypted_password",
    "email": "test@example.com",
    "nickname": "测试用户",
    "roles": ["JOB_SEEKER"]
  }'
```

#### 2. 简历上传和分析
```bash
# 1. 上传简历
curl -X POST http://localhost:8080/api/resume/upload \
  -H "Authorization: Bearer <token>" \
  -F "resume=@resume.pdf"

# 2. 查询上传任务结果（如果返回了 taskId）
curl -X GET "http://localhost:8080/api/resume/queryResumeAsyncUploadResult/{taskId}/{resumeId}" \
  -H "Authorization: Bearer <token>"

# 3. 触发简历分析
curl -X GET "http://localhost:8080/api/resume/analyze/{resumeId}" \
  -H "Authorization: Bearer <token>"

# 4. 获取简历详情
curl -X GET "http://localhost:8080/api/resume/getResumeDetail/{resumeId}" \
  -H "Authorization: Bearer <token>"
```

#### 3. AI 聊天示例
```bash
# 带记忆的聊天
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "conv-001",
    "userMessage": "你好，请介绍一下自己",
    "taskType": "GENERAL_CHAT",
    "params": {}
  }'

# 不带记忆的即时问答
curl -X POST http://localhost:8080/api/ai/assistant \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "userMessage": "Java 中什么是线程池？",
    "taskType": "GENERAL_CHAT"
  }'
```

#### 4. 面试准备和进行
```bash
# 1. 准备面试会话
curl -X POST http://localhost:8080/api/interview/textChat/prepare \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "interview-001",
    "jobId": 123,
    "resumeId": "resume-456"
  }'

# 2. 面试进行中的问答（SSE 流式响应）
curl -X POST http://localhost:8080/api/interview/textChat/progress \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "interview-001",
    "userMessage": "我有多年的 Java 开发经验"
  }'
```

---

## 📝 项目总结

### 项目概述

**Interview App** 是一个基于 **Spring Cloud 微服务架构**的 **AI 智能模拟面试系统**，旨在通过 AI 技术为求职者和 HR 提供智能化的面试服务。项目采用现代化的技术栈，深度集成 **Spring AI** 和 **Spring AI Alibaba**，提供从简历解析、智能分析到模拟面试的完整解决方案。

### 核心亮点

1. **✨ 深度集成 Spring AI 生态**
   - 基于 Spring 官方 AI 抽象框架，提供统一的 AI 能力接口
   - 通过 Spring AI Alibaba 集成阿里云 DashScope（通义千问大模型）
   - 自定义 Starter 封装完整的 AI 能力，包括 ChatClient、对话记忆、模板渲染等

2. **🤖 AI Agent 智能体能力**
   - 集成阿里云 ICE（智能通信引擎），提供 AI Agent 智能体管理能力
   - 支持消息聊天 Token 生成、RTC 认证令牌生成
   - 为未来音视频面试、语音面试等实时通信场景提供基础能力

3. **🏗️ 微服务架构设计**
   - 服务拆分清晰，职责明确：用户、简历、AI、面试、候选人五大核心服务
   - 基于 Nacos 的服务注册与发现
   - Spring Cloud Gateway 统一入口，负载均衡自动分发

4. **🔧 自定义 Starter 架构**
   - AI 能力和认证安全封装为独立 Starter，实现高内聚、可复用
   - 支持 WebMVC 和 WebFlux 双栈，灵活适配不同场景
   - 开箱即用，配置简单

5. **💾 多数据源支持**
   - MySQL：关系型数据存储
   - MongoDB：非结构化数据存储（简历详情、对话历史）
   - Redis：缓存和会话存储
   - MinIO：对象存储（简历文件）

6. **🔐 企业级安全**
   - JWT 无状态认证
   - RBAC 权限控制
   - RSA 加密密码传输
   - 图形验证码防护

7. **⚡ 异步任务处理**
   - 简历上传、解析、分析任务异步执行
   - 任务状态实时查询
   - 基于 RequestScope 解决异步任务中的上下文传递问题

8. **📊 可观测性**
   - Spring Boot Actuator 健康检查和指标监控
   - 完善的异常处理和日志记录

### 技术栈亮点

- **Spring Boot 3.5.6** + **Spring Cloud 2025.0.0**：最新版本框架
- **Spring AI** + **Spring AI Alibaba 1.0.0.3**：AI 能力核心
- **Java 17**：现代 Java 特性
- **微服务架构**：Spring Cloud Gateway + Nacos + OpenFeign
- **多存储方案**：MySQL + MongoDB + Redis + MinIO

### 适用场景

- **求职者**：简历智能分析、模拟面试准备、面试技巧提升
- **HR/招聘方**：候选人简历快速评估、面试辅助工具
- **培训机构**：面试培训、技能评估、个性化指导

### 未来规划

- ✅ 视频面试模块（基于 AI Agent 的 RTC 能力）
- ✅ 语音面试模块（语音识别与合成）
- ✅ 面试数据分析和报告生成
- ✅ 更多 AI 模型的接入和支持

---

<div style="text-align: center">

**如果这个项目对你有帮助，请给个 ⭐ Star 支持一下！**

Made with ❤️ by kfzx-minglg

</div>
