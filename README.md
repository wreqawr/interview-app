# AI模拟面试系统（后端）

> 本项目遵循 [GNU GPL v3.0](LICENSE) 开源协议。你可以自由使用、修改、分发本项目，但必须保留原作者版权声明和本许可证，且衍生项目需同样以GPL-3.0协议开源。

---

## 一、项目简介

本项目为"AI模拟面试系统"后端，基于Spring Boot 3.4.6，集成Spring Security、JWT、Mybatis、MinIO、Apache Tika、阿里云AI等，提供认证授权、简历解析、文件存储、AI智能分析、RBAC权限、数据存储等核心服务。支持Docker容器化部署，敏感信息通过环境变量注入，适合企业级生产环境。

### 核心特性

- **🔐 安全认证**：基于JWT + RSA加密的现代化认证体系
- **📄 智能简历解析**：支持PDF/Word/TXT格式，AI智能提取结构化数据
- **☁️ 云存储集成**：基于MinIO的对象存储，支持分布式部署
- **🤖 AI智能分析**：集成阿里云通义千问，智能解析简历内容
- **⚡ 异步任务处理**：基于AOP的异步任务管理，支持任务状态跟踪
- **🛡️ 权限控制**：基于RBAC模型的细粒度权限管理
- **📊 统一异常处理**：自定义AOP切面，统一API响应格式
- **🔄 异步简历处理**：简历上传后异步AI解析，提升用户体验

---

## 二、主要功能

### 2.1 用户认证与权限管理
- **用户注册/登录**：支持RSA加密密码、验证码校验、JWT令牌签发
- **权限控制**：基于RBAC模型，支持用户-角色-权限-公司多维绑定
- **安全过滤器**：JWT验证、验证码校验、请求体缓存等
- **异常处理**：登录失败、权限不足、JWT过期等异常处理

### 2.2 简历管理与智能解析
- **文件上传**：支持PDF、Word、TXT格式，文件大小限制5MB
- **异步AI解析**：简历上传后立即返回taskId，后台异步进行AI解析
- **智能解析**：基于Apache Tika的文档内容提取
- **AI分析**：集成阿里云通义千问，智能解析简历内容，提取结构化数据
- **文件存储**：集成MinIO对象存储，支持分布式部署
- **下载管理**：文件下载、删除、权限控制
- **简历元信息**：支持简历标题、查看次数、下载次数、综合评分等扩展字段

### 2.3 AI智能分析引擎
- **简历智能解析**：基于通义千问的简历内容分析
- **结构化数据提取**：自动提取个人信息、教育背景、工作经验、项目经历等
- **智能纠错**：自动修正文本中的错别字和技术术语错误
- **格式标准化**：统一日期、技能名称等字段格式
- **异步处理**：支持长时间AI解析任务，避免接口超时

### 2.4 异步任务管理
- **任务状态跟踪**：PENDING、RUNNING、FINISHED、FAILED四种状态
- **AOP切面管理**：基于`@TaskHandler`注解的异步任务处理
- **任务类型支持**：简历解析、能力评估、岗位匹配度分析等
- **异常处理**：任务失败时的错误信息记录和重试机制

### 2.5 统一响应与异常处理
- **标准化API响应**：统一的`R<T>`响应结构
- **AOP异常处理**：基于`@ResponseEntityExceptionHandler`注解的异常处理
- **全局异常处理器**：`@RestControllerAdvice`统一异常处理
- **响应码管理**：标准化的业务响应码定义

---

## 三、技术栈

| 模块 | 技术实现 | 版本 | 说明 |
|------|----------|------|------|
| **后端框架** | Spring Boot | 3.4.6 | Java 17基础 |
| **安全框架** | Spring Security + JWT | 6.x + 4.5.0 | OAuth2.0兼容 |
| **数据访问** | MyBatis + MySQL | 3.0.4 + 8.x | 关系型数据库 |
| **缓存** | Redis Cluster | 8.2 | 分布式缓存 |
| **文档数据库** | MongoDB | 7.x | 简历详情存储 |
| **文件存储** | MinIO | 8.5.17 | 对象存储服务 |
| **文档解析** | Apache Tika | 3.1.0 | PDF/Word/TXT解析 |
| **AI服务** | 阿里云通义千问 | 1.0.0.2 | 智能简历分析 |
| **工具库** | Hutool | 5.8.39 | 工具类集合 |
| **构建工具** | Maven | 3.9.9 | 依赖管理 |
| **容器化** | Docker | - | 容器化部署 |

---

## 四、目录结构与模块剖析

```
interview-app/
├── src/
│   ├── main/
│   │   ├── java/cn/minglg/interview/
│   │   │   ├── Application.java                    # 启动类
│   │   │   ├── auth/                               # 认证与权限模块
│   │   │   │   ├── controller/                     # 登录、验证码等接口
│   │   │   │   ├── handler/                        # 认证、鉴权、登出等处理器
│   │   │   │   ├── filter/                         # JWT、验证码等安全过滤器
│   │   │   │   ├── service/                        # 用户、验证码等服务接口与实现
│   │   │   │   ├── mapper/                         # MyBatis数据访问层
│   │   │   │   ├── pojo/                           # 用户、角色、权限等实体
│   │   │   │   ├── config/                         # Spring Security、验证码等配置
│   │   │   │   ├── event/                          # 登录成功等事件发布
│   │   │   │   ├── exception/                      # 自定义异常
│   │   │   │   └── wrapper/                        # 请求体缓存包装器
│   │   │   ├── common/                             # 通用工具与全局配置
│   │   │   │   ├── annotation/                     # 自定义注解
│   │   │   │   ├── aspects/                        # AOP切面类
│   │   │   │   ├── config/                         # 异步配置
│   │   │   │   ├── constant/                       # 常量定义
│   │   │   │   ├── exception/                      # 全局异常
│   │   │   │   ├── listener/                       # 应用监听器
│   │   │   │   ├── mapper/                         # 任务管理Mapper
│   │   │   │   ├── pojo/                           # 任务实体
│   │   │   │   ├── properties/                     # 全局配置属性
│   │   │   │   ├── response/                       # 统一响应结构
│   │   │   │   └── utils/                          # 工具类（JWT、RSA、验证码等）
│   │   │   ├── resume/                             # 简历模块
│   │   │   │   ├── controller/                     # 简历相关接口
│   │   │   │   ├── service/                        # 简历服务接口与实现
│   │   │   │   ├── config/                         # 简历解析器配置
│   │   │   │   ├── exception/                      # 简历相关异常
│   │   │   │   ├── mapper/                         # 简历元数据Mapper
│   │   │   │   ├── pojo/                           # 简历实体类
│   │   │   │   └── repository/                     # MongoDB数据访问
│   │   │   ├── minio/                              # 文件存储模块
│   │   │   │   ├── service/                        # MinIO服务接口与实现
│   │   │   │   └── config/                         # MinIO配置
│   │   │   └── ai/                                 # AI模块
│   │   │       ├── config/                         # AI配置（通义千问）
│   │   │       └── core/                           # AI核心服务
│   │   ├── resources/
│   │   │   ├── config/application.yml              # 配置文件，端口8081，敏感信息用环境变量
│   │   │   ├── mapper/                             # MyBatis XML映射
│   │   │   ├── prompt/                             # AI提示词模板
│   │   │   └── banner/                             # 启动Banner
│   ├── test/java/cn/minglg/interview/              # 单元测试
│   │   ├── CommonTest.java, UuidTest.java, utils/
├── Dockerfile                                       # 多阶段构建，JDK17，8081端口
├── docker-compose.yml                               # 一键部署示例，环境变量注入
├── build.sh                                         # 构建脚本
├── pom.xml                                          # Maven依赖
└── README.md                                        # 项目说明
```

---

## 五、核心功能模块详细说明

### 5.1 认证与权限（auth）

#### 5.1.1 用户管理
- **用户注册**：支持RSA加密密码、角色分配、公司关联
- **用户登录**：JWT令牌签发、Redis会话管理
- **用户登出**：令牌失效、Redis清理

#### 5.1.2 安全机制
- **JWT认证**：基于RSA256算法的JWT令牌
- **RSA加密**：前端密码RSA加密传输
- **验证码校验**：支持数学运算和随机字符验证码
- **请求体缓存**：支持多次读取请求体内容

#### 5.1.3 权限控制
- **RBAC模型**：用户-角色-权限-公司四维关联
- **角色定义**：ROLE_ADMIN（管理员）、ROLE_JOB_SEEKER（求职者）、ROLE_HR（企业招聘方）
- **权限细粒度**：基于注解的方法级权限控制

### 5.2 简历管理（resume）

#### 5.2.1 文件处理
- **文件上传**：支持PDF、Word、TXT格式，最大5MB
- **文件存储**：MinIO对象存储，按用户分桶
- **文件下载**：支持流式下载，权限验证
- **文件删除**：级联删除元数据和存储文件

#### 5.2.2 智能解析
- **文档解析**：基于Apache Tika的多格式文档解析
- **内容清理**：自动清理格式、空白行、重复空格
- **元数据提取**：文件大小、类型、哈希值等

#### 5.2.3 数据结构
- **ResumeMetadata**：简历元数据（MySQL存储）
  - 基本信息：resumeId、userId、bucketName、objectName等
  - 扩展信息：resumeTitle、viewCount、downloadCount、rate等
- **ResumeDetail**：简历详细信息（MongoDB存储）
- **结构化数据**：基本信息、工作经历、教育背景、技能、项目经历

#### 5.2.4 异步处理流程
1. **文件上传**：用户上传简历文件
2. **立即响应**：返回taskId和resumeId，提示用户等待后台解析
3. **异步解析**：后台使用Tika提取文本内容
4. **AI分析**：调用通义千问进行智能解析
5. **数据持久化**：保存解析结果到MongoDB和MySQL
6. **状态查询**：用户可通过taskId查询解析进度

### 5.3 AI智能分析（ai）

#### 5.3.1 核心服务
- **AiResumeCoreService**：简历分析核心服务
- **异步处理**：基于`@Async`注解的异步任务执行

#### 5.3.2 智能解析
- **提示词工程**：专业的简历解析提示词模板
- **结构化输出**：JSON格式的结构化数据输出
- **智能纠错**：自动修正错别字和技术术语
- **格式标准化**：统一日期、技能名称等格式

#### 5.3.3 数据模型
```json
{
  "basic_info": {
    "name": "张三",
    "phone": "13800138000",
    "email": "zhangsan@example.com",
    "location": "北京",
    "years_exp": 5.5,
    "target_title": "高级Java开发工程师"
  },
  "work_experience": [],
  "education": [],
  "skills": {},
  "projects": []
}
```

### 5.4 异步任务管理（common）

#### 5.4.1 任务状态
- **PENDING**：等待执行
- **RUNNING**：执行中
- **FINISHED**：执行结束
- **FAILED**：执行失败

#### 5.4.2 任务类型
- **RESUME_SUMMARIZE**：简历解析
- **SKILL_EVALUATION**：能力评估
- **JOB_MATCH_ANALYSIS**：岗位匹配度分析

#### 5.4.3 AOP切面
- **AsyncTaskAspect**：异步任务状态管理切面
- **ResponseEntityAspect**：统一响应处理切面
- **注解驱动**：`@TaskHandler`、`@ResponseEntityExceptionHandler`

### 5.5 文件存储（minio）

#### 5.5.1 核心功能
- **桶管理**：自动创建、删除存储桶
- **文件操作**：上传、下载、删除、URL生成
- **权限控制**：基于用户的桶隔离
- **元数据管理**：文件类型、大小、哈希值等

#### 5.5.2 存储策略
- **用户隔离**：每个用户独立的存储桶
- **文件命名**：时间戳+随机字符串，避免冲突
- **预签名URL**：支持临时访问链接

### 5.6 通用与全局（common）

#### 5.6.1 统一响应
```java
@Data
@Builder
public class R<T> {
    private Integer code;    // 响应码
    private String message;  // 响应消息
    private T data;         // 响应数据
}
```

#### 5.6.2 异常处理
- **全局异常处理器**：`@RestControllerAdvice`
- **自定义异常**：业务异常、认证异常、文件异常等
- **AOP切面**：基于注解的异常处理

#### 5.6.3 工具类
- **JwtUtils**：JWT令牌生成和验证
- **RsaUtils**：RSA加密解密
- **CaptchaUtils**：验证码生成和验证
- **UserUtils**：当前用户获取
- **TaskUtils**：任务管理工具

---

## 六、构建与部署

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
git clone <repository-url>
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

# 3. 构建jar包
mvn clean package -DskipTests
```

### 6.3 Docker镜像构建与运行

```bash
# 1. 构建镜像
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
  --name interview-app interview-app:latest
```

### 6.4 docker-compose一键部署

```yaml
# docker-compose.yml
version: '3.8'
services:
  interview-app:
    image: interview-app:latest
    container_name: interview-app
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
mysql -u root -p interview_db < src/main/resources/init/ddl/*.sql

# 3. 执行DML脚本
mysql -u root -p interview_db < src/main/resources/init/dml/*.sql
```

---

## 七、配置说明

### 7.1 核心配置

- **端口**：默认8081（见`application.yml`）
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

### 7.3 全局配置

#### 7.3.1 认证配置
```yaml
global:
  auth:
    request-timeout-seconds: 5
    jwt-expiration-minutes: 30
    auth-key-prefix: user:security:login
    login-uri: /api/auth/login
    logout-uri: /api/auth/logout
```

#### 7.3.2 验证码配置
```yaml
global:
  captcha:
    width: 120
    height: 40
    code-count: 4
    thickness: 10
    code-generator: cn.hutool.captcha.generator.MathGenerator
    redis-key-prefix: user:captcha
    redis-key-expire-minutes: 5
```

#### 7.3.3 异步任务配置
```yaml
global:
  async:
    core-pool-size: 5
    max-pool-size: 10
    queue-capacity: 100
    thread-name-prefix: AsyncTask-
```

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
| 简历下载 | GET | `/api/resume/download/{resumeName}` | 下载简历文件 |
| 简历删除 | DELETE | `/api/resume/delete` | 删除简历文件 |
| 简历列表 | GET | `/api/resume/getMyResume` | 获取简历元信息列表 |
| 异步解析结果 | GET | `/api/resume/queryResumeAsyncUploadResult/{taskId}/{resumeId}` | 查询简历异步解析结果 |
| 简历预览 | GET | `/api/resume/preview/{resumeId}` | 获取简历预览URL |

### 8.3 权限说明

- **JOB_SEEKER**：求职者角色，可以上传简历
- **HR**：人力资源角色，可以查看和管理简历
- **ADMIN**：管理员角色，拥有所有权限

### 8.4 响应格式

#### 8.4.1 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "xxx": "yyy"
  }
}
```

#### 8.4.2 错误响应
```json
{
  "code": 208,
  "message": "简历分析失败，原因为：无效用户！"
}
```

#### 8.4.3 异步任务响应
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
```

---

## 十、常见问题与解决方案

### 10.1 启动问题

- **端口冲突**：修改`application.yml`中的`server.port`
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

### 10.5 异步任务问题

- **任务状态异常**：检查任务表数据完整性
- **异步执行失败**：检查线程池配置和任务执行日志
- **任务超时**：调整`@AsyncTaskHandler`的timeout配置

### 10.6 部署问题

- **环境变量未配置**：确保所有必需的环境变量都已设置
- **容器启动失败**：查看Docker日志`docker logs interview-app`
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
- **自定义提示词**：修改`RESUME_SUMMARIZE_PROMPT`常量
- **扩展AI服务**：在`ai.service`包下添加新的服务类

### 11.4 异步任务扩展

- **添加新任务类型**：在`TaskType`枚举中添加新类型
- **创建任务处理器**：使用`@AsyncTaskHandler`注解
- **任务状态管理**：通过AOP切面自动管理任务状态

### 11.5 异常处理扩展

- **添加自定义异常**：继承`RuntimeException`
- **配置异常处理**：使用`@ResponseEntityExceptionHandler`注解
- **全局异常处理**：在`GlobalExceptionHandler`中添加处理方法

---

## 十二、贡献指南

1. **Fork项目**
2. **创建功能分支**：`git checkout -b feature/new-feature`
3. **提交更改**：`git commit -am 'Add new feature'`
4. **推送分支**：`git push origin feature/new-feature`
5. **创建Pull Request**

---

## 十三、许可证

本项目采用 [GNU GPL v3.0](LICENSE) 开源协议。

---

## 十四、联系方式

- **作者**：kfzx-minglg
- **邮箱**：2820996063@qq.com
- **项目地址**：[GitHub Repository]

如有问题或建议，欢迎提交Issue或Pull Request！

