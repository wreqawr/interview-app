# AI模拟面试系统（后端）

> 本项目遵循 [GNU GPL v3.0](LICENSE) 开源协议。你可以自由使用、修改、分发本项目，但必须保留原作者版权声明和本许可证，且衍生项目需同样以GPL-3.0协议开源。

---

## 一、项目简介

本项目为"AI模拟面试系统"后端，采用Maven父子项目管理架构，基于Spring Boot 3.4.6，集成Spring Security、JWT、Mybatis、MinIO、Apache Tika、阿里云AI等，提供认证授权、简历解析、文件存储、AI智能分析、RBAC权限、数据存储等核心服务。支持Docker容器化部署，敏感信息通过环境变量注入，适合企业级生产环境。

### 项目架构

```
interview-app/                           # 父项目
├── pom.xml                             # 父POM文件
├── build.sh                            # 父项目构建脚本
├── docker-compose.yml                  # 父项目Docker编排
├── Dockerfile                          # 父项目Docker构建
├── README.md                           # 项目说明文档
└── interview-app-mvc/                  # MVC架构子模块
    ├── pom.xml                        # MVC模块POM
    ├── src/                           # 源代码
    │   ├── main/
    │   │   ├── java/                  # Java源码
    │   │   └── resources/             # 配置文件
    │   └── test/                      # 测试代码
    ├── target/                        # 编译输出
    ├── BUILD_GUIDE.md                 # 构建说明文档
    └── 其他配置文件...
```

> 📖 **构建说明**：详细的构建和部署说明请参考 [BUILD_GUIDE.md](BUILD_GUIDE.md)

### 核心特性

- **🔐 安全认证**：基于JWT + RSA加密的现代化认证体系
- **📄 智能简历解析**：支持PDF/Word/TXT格式，AI智能提取结构化数据
- **☁️ 云存储集成**：基于MinIO的对象存储，支持分布式部署
- **🤖 AI智能分析**：集成阿里云通义千问，智能解析简历内容并提供专业优化建议
- **⚡ 异步任务处理**：基于AOP的异步任务管理，支持任务状态跟踪
- **🛡️ 权限控制**：基于RBAC模型的细粒度权限管理
- **📊 统一异常处理**：自定义AOP切面，统一API响应格式
- **🔄 异步简历处理**：简历上传后异步AI解析，提升用户体验
- **📈 简历分析引擎**：面向求职者的专业简历分析，提供核心竞争力评估和优化建议
- **💬 AI智能对话**：支持上下文记忆的智能客服和面试官角色
- **🧠 智能记忆管理**：基于Redis的分布式对话历史记忆，支持多用户并发

---

## 二、AI功能亮点

### 🤖 智能简历解析与分析
- **多格式支持**：支持PDF、Word、TXT等主流文档格式
- **AI智能提取**：基于通义千问的深度学习模型，自动提取简历关键信息
- **结构化输出**：生成标准化的JSON数据结构，包含基本信息、工作经历、教育背景、技能等
- **智能纠错**：自动识别并修正简历中的错别字、技术术语错误
- **专业分析报告**：面向求职者提供核心竞争力评估、短板识别、优化建议

### 💬 AI智能对话系统
- **上下文记忆**：基于Redis的分布式对话历史记忆，支持多轮对话
- **角色扮演**：可配置的AI角色，包括智能客服、技术面试官等
- **流式响应**：支持实时流式对话输出，提升用户体验
- **智能理解**：基于简历内容的个性化面试问答生成
- **多用户并发**：支持多用户同时进行AI对话，互不干扰

### 🧠 智能记忆管理
- **分布式存储**：基于Redis集群的分布式对话记忆存储
- **自动过期**：可配置的记忆过期时间，自动清理过期对话
- **用户隔离**：每个用户独立的对话记忆空间
- **记忆优化**：智能清理超长对话历史，保持对话质量

### 📝 专业提示词工程
- **模板系统**：支持动态占位符的提示词模板系统
- **多格式渲染**：支持多种占位符格式的模板渲染器
- **任务驱动**：针对不同任务类型的专业提示词模板
- **可扩展性**：易于添加新的AI任务和提示词模板

---

## 三、主要功能

### 3.1 用户认证与权限管理
- **用户注册/登录**：支持RSA加密密码、验证码校验、JWT令牌签发
- **权限控制**：基于RBAC模型，支持用户-角色-权限-公司多维绑定
- **安全过滤器**：JWT验证、验证码校验、请求体缓存等
- **异常处理**：登录失败、权限不足、JWT过期等异常处理

### 3.2 简历管理与智能解析
- **文件上传**：支持PDF、Word、TXT格式，文件大小限制5MB
- **异步AI解析**：简历上传后立即返回taskId，后台异步进行AI解析
- **智能解析**：基于Apache Tika的文档内容提取
- **AI分析**：集成阿里云通义千问，智能解析简历内容，提取结构化数据
- **文件存储**：集成MinIO对象存储，支持分布式部署
- **下载管理**：文件下载、删除、权限控制
- **简历元信息**：支持简历标题、查看次数、下载次数、综合评分等扩展字段
- **简历预览**：支持文件预览功能，自动统计查看次数

### 3.3 AI智能分析引擎
- **简历智能解析**：基于通义千问的简历内容分析
- **结构化数据提取**：自动提取个人信息、教育背景、工作经验、项目经历等
- **智能纠错**：自动修正错别字和技术术语错误
- **格式标准化**：统一日期、技能名称等字段格式
- **异步处理**：支持长时间AI解析任务，避免接口超时
- **专业分析报告**：面向求职者的简历分析，提供核心竞争力评估、短板识别、优化建议
- **HTML格式输出**：生成精美的HTML分析报告，支持样式定制

### 3.4 AI智能对话系统
- **智能客服**：支持上下文记忆的通用对话服务
- **模拟面试官**：基于简历内容的专业技术面试问答
- **角色扮演**：可配置的AI角色和对话风格
- **记忆管理**：基于Redis的分布式对话历史记忆
- **流式响应**：支持实时流式对话输出
- **多轮对话**：智能上下文理解和连续对话支持

### 3.5 异步任务管理
- **任务状态跟踪**：PENDING、RUNNING、FINISHED、FAILED四种状态
- **AOP切面管理**：基于`@TaskHandler`注解的异步任务处理
- **任务类型支持**：简历解析、简历分析、能力评估、岗位匹配度分析等
- **异常处理**：任务失败时的错误信息记录和重试机制
- **查询切面**：基于`@AsyncTaskQuery`注解的异步任务结果查询

### 3.6 统一响应与异常处理
- **标准化API响应**：统一的`R<T>`响应结构
- **AOP异常处理**：基于`@ResponseEntityExceptionHandler`注解的异常处理
- **全局异常处理器**：`@RestControllerAdvice`统一异常处理
- **响应码管理**：标准化的业务响应码定义

### 3.7 缓存策略
- **三级缓存机制**：Redis-MongoDB-AI三级缓存，提升查询效率
- **智能缓存更新**：支持缓存自动更新和失效机制
- **分布式缓存**：基于Redis集群的分布式缓存支持

---

## 四、技术栈

| 模块 | 技术实现 | 版本 | 说明 |
|------|----------|------|------|
| **后端框架** | Spring Boot | 3.4.6 | Java 17基础 |
| **安全框架** | Spring Security + JWT | 6.x + 4.5.0 | OAuth2.0兼容 |
| **数据访问** | MyBatis + MySQL | 3.0.4 + 8.x | 关系型数据库 |
| **缓存** | Redis Cluster | 8.2 | 分布式缓存 |
| **文档数据库** | MongoDB | 7.x | 简历详情存储 |
| **文件存储** | MinIO | 8.5.17 | 对象存储服务 |
| **文档解析** | Apache Tika | 3.1.0 | PDF/Word/TXT解析 |
| **AI服务** | 阿里云通义千问 | 1.0.0.2 | 智能简历分析、对话系统 |
| **AI框架** | Spring AI | 1.0.0.2 | AI应用开发框架 |
| **AI记忆** | Redis + Spring AI Memory | 8.2 + 1.0.0.2 | 分布式对话记忆管理 |
| **工具库** | Hutool | 5.8.39 | 工具类集合 |
| **构建工具** | Maven | 3.9.9 | 依赖管理 |
| **容器化** | Docker | - | 容器化部署 |

---

## 五、目录结构与模块剖析

```
interview-app/                           # 父项目
├── pom.xml                             # 父POM文件
├── interview-app-mvc/                  # MVC架构子模块
│   ├── pom.xml                        # MVC模块POM
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/cn/minglg/interview/
│   │   │   │   ├── Application.java                    # 启动类
│   │   │   │   ├── auth/                               # 认证与权限模块
│   │   │   │   │   ├── controller/                     # 登录、验证码等接口
│   │   │   │   │   ├── handler/                        # 认证、鉴权、登出等处理器
│   │   │   │   │   ├── filter/                         # JWT、验证码等安全过滤器
│   │   │   │   │   ├── service/                        # 用户、验证码等服务接口与实现
│   │   │   │   │   ├── mapper/                         # MyBatis数据访问层
│   │   │   │   │   ├── pojo/                           # 用户、角色、权限等实体
│   │   │   │   │   ├── config/                         # Spring Security、验证码等配置
│   │   │   │   │   ├── event/                          # 登录成功等事件发布
│   │   │   │   │   ├── exception/                      # 自定义异常
│   │   │   │   │   └── wrapper/                        # 请求体缓存包装器
│   │   │   │   ├── common/                             # 通用工具与全局配置
│   │   │   │   │   ├── annotation/                     # 自定义注解
│   │   │   │   │   ├── aspects/                        # AOP切面类
│   │   │   │   │   ├── config/                         # 异步配置
│   │   │   │   │   ├── constant/                       # 常量定义
│   │   │   │   │   ├── exception/                      # 全局异常
│   │   │   │   │   ├── listener/                       # 应用监听器
│   │   │   │   │   ├── mapper/                         # 任务管理Mapper
│   │   │   │   │   ├── pojo/                           # 任务实体
│   │   │   │   │   ├── properties/                     # 全局配置属性
│   │   │   │   │   ├── response/                       # 统一响应结构
│   │   │   │   │   └── utils/                          # 工具类（JWT、RSA、验证码等）
│   │   │   │   ├── resume/                             # 简历模块
│   │   │   │   │   ├── controller/                     # 简历相关接口
│   │   │   │   │   ├── service/                        # 简历服务接口与实现
│   │   │   │   │   ├── config/                         # 简历解析器配置
│   │   │   │   │   ├── exception/                      # 简历相关异常
│   │   │   │   │   ├── mapper/                         # 简历元数据Mapper
│   │   │   │   │   ├── pojo/                           # 简历实体类
│   │   │   │   │   └── repository/                     # MongoDB数据访问
│   │   │   │   ├── minio/                              # 文件存储模块
│   │   │   │   │   ├── service/                        # MinIO服务接口与实现
│   │   │   │   │   └── config/                         # MinIO配置
│   │   │   │   └── ai/                                 # AI模块
│   │   │   │       ├── config/                         # AI配置（通义千问）
│   │   │   │       └── core/                           # AI核心服务
│   │   │   └── resources/
│   │   │       ├── config/application.yml              # 配置文件，端口8081，敏感信息用环境变量
│   │   │       ├── mapper/                             # MyBatis XML映射
│   │   │       ├── prompt/                             # AI提示词模板
│   │   │       └── banner/                             # 启动Banner
│   │   └── test/java/cn/minglg/interview/              # 单元测试
│   │       ├── CommonTest.java, UuidTest.java, utils/
│   └── target/                                         # 编译输出
├── Dockerfile                                           # 多阶段构建，JDK17，8081端口
├── docker-compose.yml                                   # 一键部署示例，环境变量注入
├── build.sh                                             # 构建脚本
└── README.md                                            # 项目说明
```

---

## 六、构建与部署

> 📖 **详细说明**：完整的构建和部署说明请参考 [BUILD_GUIDE.md](BUILD_GUIDE.md)

### 6.1 环境要求

- **JDK**：17+
- **Maven**：3.9.9+
- **Docker**：20.10+
- **MySQL**：8.0+
- **Redis**：7.0+（集群模式）
- **MongoDB**：7.0+
- **MinIO**：8.5.17+
- **阿里云API**：通义千问API密钥

### 6.2 本地构建

```bash
# 1. 克隆项目
git clone git@github.com:wreqawr/interview-app.git
cd interview-app

# 2. 设置环境变量
export MYSQL_JDBC_URL=jdbc:mysql://localhost:3306/interview_db
export MYSQL_USERNAME=your_username
export MYSQL_PASSWORD=your_password
export REDIS_HOST=localhost
export REDIS_PASSWORD=your_redis_password
export MONGODB_HOST=localhost
export MONGODB_PORT=27017
export MONGODB_USERNAME=your_mongodb_username
export MONGODB_PASSWORD=your_mongodb_password
export MINIO_ENDPOINT=http://localhost:9000
export MINIO_ACCESS_KEY=admin
export MINIO_SECRET_KEY=your_minio_password
export ALIYUN_API_KEY=your_aliyun_api_key

# 3. 构建整个项目
mvn clean install

# 4. 构建MVC模块
cd interview-app-mvc
mvn clean package -DskipTests
```

### 6.3 Docker镜像构建与运行

```bash
# 1. 构建镜像（在MVC模块目录下）
cd interview-app-mvc
./build.sh

# 2. 运行容器
docker run -d \
  -p 8081:8081 \
  -e MYSQL_JDBC_URL=jdbc:mysql://your-mysql-host:3306/yourdb \
  -e MYSQL_USERNAME=your_db_user \
  -e MYSQL_PASSWORD=your_db_password \
  -e REDIS_HOST=your-redis-host \
  -e REDIS_PASSWORD=your_redis_password \
  -e MONGODB_HOST=your-mongodb-host \
  -e MONGODB_PORT=27017 \
  -e MONGODB_USERNAME=your_mongodb_user \
  -e MONGODB_PASSWORD=your_mongodb_password \
  -e MINIO_ENDPOINT=http://your-minio-host:9000 \
  -e MINIO_ACCESS_KEY=admin \
  -e MINIO_SECRET_KEY=your_minio_password \
  -e ALIYUN_API_KEY=your_aliyun_api_key \
  --name interview-app-mvc interview-app-mvc:latest
```

### 6.4 docker-compose一键部署

```yaml
# docker-compose.yml
version: '3.8'
services:
  interview-app-mvc:
    image: interview-app-mvc:latest
    container_name: interview-app-mvc
    ports:
      - "8081:8081"
    environment:
      - MYSQL_JDBC_URL=jdbc:mysql://mysql:3306/interview_db
      - MYSQL_USERNAME=root
      - MYSQL_PASSWORD=password
      - REDIS_HOST=redis
      - REDIS_PASSWORD=redis_password
      - MONGODB_HOST=mongodb
      - MONGODB_PORT=27017
      - MONGODB_USERNAME=admin
      - MONGODB_PASSWORD=password
      - MINIO_ENDPOINT=http://minio:9000
      - MINIO_ACCESS_KEY=admin
      - MINIO_SECRET_KEY=minio_password
      - ALIYUN_API_KEY=your_aliyun_api_key
    restart: unless-stopped
    networks:
      - interview-net

networks:
  interview-net:
    external: true
```

### 6.5 数据库初始化

```bash
# 1. 创建数据库
CREATE DATABASE interview_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. 执行DDL脚本
mysql -u root -p interview_db < interview-app-mvc/src/main/resources/init/ddl/*.sql

# 3. 执行DML脚本
mysql -u root -p interview_db < interview-app-mvc/src/main/resources/init/dml/*.sql
```

---

## 七、配置说明

### 7.1 核心配置

- **端口**：默认8081（见`interview-app-mvc/src/main/resources/config/application.yml`）
- **敏感信息**：数据库、Redis、MongoDB、MinIO、阿里云API等均通过环境变量注入
- **文件上传**：单文件最大5MB，总请求最大20MB
- **JWT过期时间**：默认30分钟，可配置
- **AI模型**：默认使用通义千问qwen-turbo模型

### 7.2 环境变量

| 变量名 | 说明 | 示例 |
|--------|------|------|
| `MYSQL_JDBC_URL` | MySQL连接URL | `jdbc:mysql://localhost:3306/interview_db` |
| `MYSQL_USERNAME` | MySQL用户名 | `root` |
| `MYSQL_PASSWORD` | MySQL密码 | `password` |
| `REDIS_HOST` | Redis主机地址 | `localhost` |
| `REDIS_PASSWORD` | Redis密码 | `redis_password` |
| `MONGODB_HOST` | MongoDB主机地址 | `localhost` |
| `MONGODB_PORT` | MongoDB端口 | `27017` |
| `MONGODB_USERNAME` | MongoDB用户名 | `admin` |
| `MONGODB_PASSWORD` | MongoDB密码 | `password` |
| `MINIO_ENDPOINT` | MinIO服务地址 | `http://localhost:9000` |
| `MINIO_ACCESS_KEY` | MinIO访问密钥 | `admin` |
| `MINIO_SECRET_KEY` | MinIO秘密密钥 | `minio_password` |
| `ALIYUN_API_KEY` | 阿里云API密钥 | `your_aliyun_api_key` |

---

## 八、API接口

### 8.1 认证接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户注册 | POST | `/api/auth/register` | 用户注册 |
| 用户登录 | POST | `/api/auth/login` | 用户登录 |
| 用户登出 | POST | `/api/auth/logout` | 用户登出 |
| 获取公钥 | GET | `/api/auth/publicKey` | 获取RSA公钥 |
| 获取验证码 | GET | `/api/auth/captcha` | 获取验证码 |

### 8.2 简历接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 简历上传 | POST | `/api/resume/upload` | 上传简历文件（异步解析） |
| 简历下载 | GET | `/api/resume/download` | 下载简历文件 |
| 简历删除 | DELETE | `/api/resume/delete` | 删除简历文件 |
| 简历列表 | GET | `/api/resume/getMyResume` | 获取简历元信息列表 |
| 异步解析结果 | GET | `/api/resume/queryResumeAsyncUploadResult/{taskId}/{resumeId}` | 查询简历异步解析结果 |
| 简历预览 | GET | `/api/resume/preview/{resumeId}` | 获取简历预览URL |
| 简历分析 | GET | `/api/resume/analyze/{resumeId}` | 触发简历分析（面向求职者） |
| 异步分析结果 | GET | `/api/resume/queryResumeAsyncAnalyzeResult/{taskId}/{resumeId}` | 查询简历分析结果 |

### 8.3 AI接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| AI对话 | POST | `/api/ai/chat` | 通用AI对话服务（支持上下文记忆） |

### 8.4 权限说明

- **JOB_SEEKER**：求职者角色，可以上传简历、查看分析结果、使用AI对话
- **HR**：人力资源角色，可以查看和管理简历、使用AI客服
- **ADMIN**：管理员角色，拥有所有权限

### 8.5 响应格式

#### 8.5.1 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
  }
}
```

#### 8.5.2 错误响应
```json
{
  "code": 210,
  "message": "简历分析失败，原因为：无效用户！"
}
```

#### 8.5.3 异步任务响应
```json
{
  "code": 900,
  "message": "异步任务执行中",
  "data": {
    "taskId": "task_123456",
    "status": "RUNNING"
  }
}
```

---

## 九、测试

### 9.1 单元测试

```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=JWTUtilsTest
```

### 9.2 接口测试

```bash
# 1. 获取验证码
curl -X GET http://localhost:8081/api/auth/captcha

# 2. 获取RSA公钥
curl -X GET http://localhost:8081/api/auth/publicKey

# 3. 用户登录
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"encrypted_password","captcha":"1234"}'

# 4. 上传简历（需要JWT token）
curl -X POST http://localhost:8081/api/resume/upload \
  -H "Authorization: your_jwt_token" \
  -F "resume=@/path/to/resume.pdf"

# 5. 查询简历异步解析结果
curl -X GET "http://localhost:8081/api/resume/queryResumeAsyncUploadResult/xxx/xxx" \
  -H "Authorization: your_jwt_token"

# 6. 触发简历分析
curl -X GET "http://localhost:8081/api/resume/analyze/xxx" \
  -H "Authorization: your_jwt_token"

# 7. 查询简历分析结果
curl -X GET "http://localhost:8081/api/resume/queryResumeAsyncAnalyzeResult/xxx/xxx" \
  -H "Authorization: your_jwt_token"

# 8. AI对话服务
curl -X POST http://localhost:8081/api/ai/chat \
  -H "Authorization: your_jwt_token" \
  -H "Content-Type: application/json" \
  -d '{"conversationId":"conv_123","userMessage":"你好，请介绍一下自己"}'
```

---

## 十、常见问题与解决方案

### 10.1 启动问题

- **端口冲突**：修改`interview-app-mvc/src/main/resources/config/application.yml`中的`server.port`
- **数据库连接失败**：检查环境变量`MYSQL_JDBC_URL`等配置
- **Redis连接失败**：检查Redis集群配置和环境变量
- **MongoDB连接失败**：检查MongoDB服务状态和环境变量

### 10.2 文件上传问题

- **文件大小超限**：检查`spring.servlet.multipart`配置
- **文件类型不支持**：检查`global.resume.allow-file-types`配置
- **MinIO连接失败**：检查MinIO服务状态和环境变量

### 10.3 认证问题

- **JWT token过大**：增加`server.tomcat.max-http-header-size`配置
- **验证码错误**：检查验证码生成和校验逻辑
- **权限不足**：检查用户角色和权限配置

### 10.4 AI服务问题

- **阿里云API调用失败**：检查`ALIYUN_API_KEY`环境变量配置
- **AI模型响应慢**：检查网络连接和API配额
- **简历解析失败**：检查文件格式和内容质量
- **简历分析失败**：检查AI提示词模板和模型配置
- **AI对话记忆丢失**：检查Redis集群配置和对话记忆配置
- **AI角色响应异常**：检查提示词模板文件格式和内容
- **流式对话中断**：检查网络连接和流式响应配置
- **多用户对话混乱**：检查用户隔离和对话ID生成逻辑

### 10.5 异步任务问题

- **任务状态异常**：检查任务表数据完整性
- **异步执行失败**：检查线程池配置和任务执行日志
- **任务超时**：调整`@TaskHandler`的timeout配置
- **类型转换异常**：检查`@AsyncTaskQuery`切面与业务方法返回类型一致性

### 10.6 部署问题

- **环境变量未配置**：确保所有必需的环境变量都已设置
- **容器启动失败**：查看Docker日志`docker logs interview-app-mvc`
- **网络连接问题**：检查容器网络配置和端口映射

---

## 十一、开发指南

### 11.1 添加新功能

1. **创建Controller**：在对应模块下创建控制器
2. **创建Service**：实现业务逻辑
3. **创建Mapper**：数据访问层
4. **添加配置**：在`application.yml`中添加配置项
5. **编写测试**：添加单元测试

### 11.2 代码规范

- **命名规范**：使用驼峰命名法
- **异常处理**：统一使用全局异常处理
- **日志记录**：使用SLF4J记录关键操作
- **注释规范**：类和方法必须有注释

### 11.3 AI功能扩展

- **添加新的AI模型**：在`AiConfig`中配置新的ChatClient
- **自定义提示词**：修改AI提示词模板文件
- **扩展AI服务**：在`ai.service`包下添加新的服务类
- **新增任务类型**：在`TaskType`枚举中添加新类型
- **AI角色配置**：在提示词模板中配置不同的AI角色和对话风格
- **记忆策略优化**：自定义`ChatMemoryRepository`实现不同的记忆策略
- **流式响应处理**：扩展`AiInterviewCoreService`支持更多流式对话场景
- **提示词模板系统**：利用`CustomMultiCharTemplateRenderer`创建复杂的动态提示词

### 11.4 异步任务扩展

- **添加新任务类型**：在`TaskType`枚举中添加新类型
- **创建任务处理器**：使用`@TaskHandler`注解
- **任务状态管理**：通过AOP切面自动管理任务状态
- **查询切面**：使用`@AsyncTaskQuery`注解实现异步查询

### 11.5 异常处理扩展

- **添加自定义异常**：继承`RuntimeException`
- **配置异常处理**：使用`@ResponseEntityExceptionHandler`注解
- **全局异常处理**：在`GlobalExceptionHandler`中添加处理方法

### 11.6 缓存策略优化

- **Redis缓存**：合理使用Redis缓存热点数据
- **MongoDB存储**：存储结构化文档数据
- **AI缓存**：缓存AI分析结果，避免重复计算

---

## 十二、项目扩展计划

### 12.1 实时通信模块

计划新增`interview-app-realtime`子模块，用于处理实时通信功能：

- **WebSocket支持**：实时双向通信
- **Server-Sent Events**：服务器推送事件
- **响应式编程**：基于WebFlux的非阻塞架构
- **消息队列集成**：与MVC模块通过消息队列通信

### 12.2 微服务架构

未来可考虑将项目拆分为独立的微服务：

- **用户服务**：认证授权、用户管理
- **简历服务**：简历解析、分析、存储
- **AI服务**：智能分析、对话系统
- **文件服务**：文件存储、管理
- **通知服务**：消息推送、邮件通知

---

## 十三、贡献指南

1. **Fork项目**
2. **创建功能分支**：`git checkout -b feature/new-feature`
3. **提交更改**：`git commit -am 'Add new feature'`
4. **推送分支**：`git push origin feature/new-feature`
5. **创建Pull Request**

---

## 十四、许可证

本项目采用 [GNU GPL v3.0](LICENSE) 开源协议。

---

## 十五、联系方式

- **作者**：kfzx-minglg
- **邮箱**：2820996063@qq.com
- **项目地址**：[GitHub Repository]

如有问题或建议，欢迎提交Issue或Pull Request！

