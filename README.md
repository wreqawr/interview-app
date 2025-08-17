# Interview App - AI 模拟面试系统

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-20.10+-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

> 🚀 **Interview App** 是一个基于 Spring Boot 3.x 的智能 AI 模拟面试系统，提供简历分析、AI 面试、用户管理等核心功能。项目采用模块化架构设计，通过自定义 Spring Boot Starter 实现功能模块的复用和共享。

## ✨ 核心特性

### 🤖 **AI 驱动功能**
- **智能简历解析**: 基于大语言模型的简历内容提取和结构化分析
- **AI 模拟面试**: 根据简历内容进行个性化技术面试问答
- **多模型支持**: 集成阿里云 DashScope 等多种 AI 服务
- **智能提示词**: 动态模板渲染，支持中英文混合提示词

### 🔧 **自定义启动器架构**
- **模块化设计**: 通过 `interview-app-starter` 实现功能模块的复用
- **自动配置**: Spring Boot 自动配置机制，零配置集成
- **安全框架**: 基于 RBAC 的用户权限管理系统
- **可插拔**: 支持在不同项目中独立使用各个模块

### 🏗️ **技术架构**
- **微服务就绪**: 支持单体部署和微服务拆分
- **多数据源**: MySQL、Redis Cluster、MongoDB、MinIO 集成
- **异步处理**: 基于 Spring 异步任务处理简历分析
- **容器化部署**: Docker + Docker Compose 支持

## 🏗️ 项目架构

### 模块结构
```
interview-app/                          # 父项目
├── interview-app-starter/             # 启动器模块
│   ├── authentication-spring-boot-autoconfigure/  # 自动配置模块
│   │   ├── config/                   # 安全配置
│   │   ├── filter/                   # 认证过滤器
│   │   ├── handler/                  # 认证处理器
│   │   ├── properties/               # 配置属性
│   │   ├── service/                  # 认证服务
│   │   └── utils/                    # 工具类
│   └── authentication-spring-boot-starter/        # 启动器入口
├── interview-app-mvc/                 # Web MVC 模块
│   ├── ai/                           # AI 功能模块
│   │   ├── config/                   # AI 配置
│   │   ├── controller/               # AI 控制器
│   │   ├── core/                     # AI 核心服务
│   │   │   ├── interview/            # 面试服务
│   │   │   └── resume/               # 简历服务
│   │   ├── repository/               # AI 存储
│   │   ├── render/                   # 模板渲染
│   │   └── service/                  # AI 服务
│   ├── resume/                       # 简历管理模块
│   ├── user/                         # 用户管理模块
│   ├── job/                          # 职位管理模块
│   ├── minio/                        # 文件存储模块
│   └── common/                       # 公共模块
└── 构建和部署文件
    ├── build-all.sh                  # 统一构建脚本
    ├── manage-modules.sh             # 模块管理脚本
    ├── docker-compose-all.yml        # 应用服务编排
    └── docker-compose-infrastructure.yml # 基础设施服务
```

### 技术栈
- **后端框架**: Spring Boot 3.4.6 + Spring Security + Spring AI
- **数据访问**: MyBatis + Druid + Spring Data Redis + Spring Data MongoDB
- **AI 服务**: 阿里云 DashScope + Spring AI
- **存储服务**: MySQL 8.0 + Redis Cluster + MongoDB 6.0 + MinIO
- **构建工具**: Maven 3.9+ + Docker + Docker Compose
- **开发语言**: Java 17

## 🚀 快速开始

### 环境要求
- **JDK**: 17 或更高版本
- **Maven**: 3.9+ 
- **Docker**: 20.10+ (推荐)
- **Docker Compose**: 2.0+

### 快速启动

#### 1. 克隆项目
```bash
git clone git@github.com:wreqawr/interview-app.git
cd interview-app
```

#### 2. 一键构建和部署
```bash
# 构建所有模块的 Docker 镜像
./build-all.sh

# 构建并运行所有服务
./build-all.sh -r

# 启动应用服务编排
./build-all.sh -s
```

#### 3. 访问系统
- **主应用**: http://localhost:8081
- **健康检查**: http://localhost:8081/actuator/health
- **MinIO 控制台**: http://localhost:9001 (用户名/密码: minioadmin/minioadmin)

### 开发环境

#### 本地构建
```bash
# 构建整个项目
mvn clean install -Dmaven.test.skip=true

# 运行 MVC 模块
cd interview-app-mvc
mvn spring-boot:run
```

#### 模块管理
```bash
# 查看所有模块
./manage-modules.sh list

# 创建新模块
./manage-modules.sh create interview-app-realtime spring-boot

# 构建特定模块
./manage-modules.sh build interview-app-mvc
```

## 🤖 AI 功能详解

### 智能简历解析
系统基于大语言模型，能够智能解析简历内容并提取结构化信息：

- **信息提取**: 自动识别姓名、联系方式、工作经历、教育背景、技能等
- **智能纠错**: 自动修正错别字和技术术语错误
- **内容优化**: 合并同类技术栈，保留量化指标
- **异常检测**: 标记时间冲突和关键信息缺失

**示例提示词模板**:
```st
你是一名专业的简历解析专家，请从简历文本中提取关键信息并生成JSON数据...
```

### AI 模拟面试
根据候选人简历内容，进行个性化的技术面试：

- **简历驱动**: 基于简历内容设计面试问题
- **难度自适应**: 根据回答质量动态调整问题难度
- **技术深挖**: 重点考察简历中的技术栈和项目经验
- **量化评估**: 鼓励候选人提供具体的量化指标

**面试流程**:
1. 简历分析 → 2. 问题设计 → 3. 实时问答 → 4. 综合评估

### 多模型支持
- **阿里云 DashScope**: 主要 AI 服务提供商
- **Spring AI**: 统一的 AI 抽象层
- **自定义渲染器**: 支持多种模板格式
- **记忆管理**: 支持带记忆和无记忆的对话模式

## 🔧 自定义启动器详解

### 架构设计
项目采用 Spring Boot Starter 模式，实现了功能模块的复用和共享：

```
authentication-spring-boot-autoconfigure  # 自动配置模块
├── 安全配置 (SecurityConfig)
├── 认证过滤器 (JwtTokenFilter, CaptchaFilter)
├── 认证处理器 (CustomAuthenticationHandler)
├── 配置属性 (WebMvcSecurityProperties)
└── 工具类 (JwtUtils, CaptchaUtils)

authentication-spring-boot-starter        # 启动器入口
└── 依赖管理 (引入 autoconfigure 模块)
```

### 核心特性
- **零配置集成**: 通过 `@EnableAutoConfiguration` 自动启用
- **条件装配**: 使用 `@ConditionalOnMissingBean` 避免冲突
- **配置外部化**: 支持通过 `application.yml` 自定义配置
- **模块复用**: 可在其他项目中独立使用认证模块

### 使用方式
在其他项目中引入启动器：

```xml
<dependency>
    <groupId>cn.minglg.interview</groupId>
    <artifactId>authentication-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 📊 核心业务模块

### 简历管理
- **简历上传**: 支持多种格式 (PDF, Word, TXT)
- **内容解析**: AI 驱动的智能内容提取
- **版本管理**: 简历历史版本追踪
- **分析报告**: 求职者视角的优劣势分析

### 用户管理
- **RBAC 权限**: 基于角色的访问控制
- **多租户**: 支持公司级别的用户管理
- **安全认证**: JWT + 验证码双重认证
- **会话管理**: Redis 集群存储用户会话

### 职位管理
- **职位发布**: 企业职位信息管理
- **智能匹配**: 基于简历内容的职位推荐
- **状态跟踪**: 简历投递进度管理

## 🐳 部署说明

### Docker 部署
项目提供了完整的 Docker 化支持：

```bash
# 构建所有镜像
./build-all.sh

# 启动应用服务
docker-compose -f docker-compose-all.yml up -d

# 启动基础设施服务（可选）
docker-compose -f docker-compose-infrastructure.yml up -d
```

### 环境配置
通过环境变量配置外部服务：

```bash
# 数据库配置
MYSQL_JDBC_URL=jdbc:mysql://localhost:3306/interview
MYSQL_USERNAME=your_username
MYSQL_PASSWORD=your_password

# Redis 配置
REDIS_NODE_1=localhost:6379
REDIS_PASSWORD=your_redis_password

# AI 服务配置
ALIYUN_API_KEY=your_api_key
```

### 生产环境
- **高可用**: 支持 Redis Cluster 和 MySQL 主从
- **监控**: 集成 Spring Boot Actuator
- **日志**: 结构化日志输出
- **健康检查**: 容器级别的健康状态监控

## 🔄 开发指南

### 添加新模块
```bash
# 创建新的 Spring Boot 模块
./manage-modules.sh create interview-app-realtime spring-boot

# 创建通用工具模块
./manage-modules.sh create interview-app-common common
```

### 代码规范
- **包命名**: 遵循 `cn.minglg.interview.{module}` 规范
- **异常处理**: 使用统一的异常处理机制
- **日志记录**: 使用 SLF4J + Logback
- **测试覆盖**: 单元测试覆盖率 > 80%

### API 设计
- **RESTful**: 遵循 REST 设计原则
- **版本控制**: 通过 URL 路径进行版本管理
- **统一响应**: 使用 `R<T>` 包装响应结果
- **错误处理**: 标准化的错误码和错误信息

## 📈 性能优化

### 缓存策略
- **Redis Cluster**: 分布式缓存，支持高并发
- **本地缓存**: Caffeine 本地缓存，减少网络开销
- **缓存预热**: 系统启动时预加载热点数据

### 异步处理
- **简历解析**: 异步处理大文件解析任务
- **AI 调用**: 异步调用 AI 服务，提升响应速度
- **任务队列**: 基于 Redis 的任务队列管理

### 数据库优化
- **连接池**: Druid 连接池，支持监控和防 SQL 注入
- **索引优化**: 针对查询场景优化数据库索引
- **读写分离**: 支持主从数据库配置

## 🧪 测试

### 测试策略
- **单元测试**: 使用 JUnit 5 + Mockito
- **集成测试**: Spring Boot Test 集成测试
- **端到端测试**: 完整的业务流程测试

### 测试命令
```bash
# 运行所有测试
mvn test

# 运行特定模块测试
cd interview-app-mvc
mvn test

# 生成测试报告
mvn surefire-report:report
```

## 📚 相关文档

- [构建指南](BUILD_GUIDE_ALL.md) - 完整的构建和部署说明
- [MVC 模块指南](interview-app-mvc/BUILD_GUIDE.md) - MVC 模块详细说明
- [API 文档](docs/api.md) - 接口文档和示例
- [部署指南](docs/deployment.md) - 生产环境部署指南

## 🤝 贡献指南

我们欢迎所有形式的贡献！

### 贡献方式
1. **报告 Bug**: 在 [Issues](../../issues) 中报告问题
2. **功能建议**: 提出新功能或改进建议
3. **代码贡献**: 提交 Pull Request
4. **文档改进**: 完善项目文档

### 开发流程
1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)。

## 🙏 致谢

- **Spring Boot**: 优秀的应用框架
- **Spring AI**: 统一的 AI 抽象层
- **阿里云 DashScope**: 强大的 AI 服务
- **开源社区**: 所有贡献者和用户

## 📞 联系我们

- **项目维护者**: kfzx-minglg
- **项目地址**: [GitHub Repository](../../)
- **问题反馈**: [Issues](../../issues)
- **功能建议**: [Discussions](../../discussions)

---

**Interview App** - 让 AI 为你的面试保驾护航！ 🚀

