# AI模拟面试系统（后端）

> 本项目遵循 [GNU GPL v3.0](LICENSE) 开源协议。你可以自由使用、修改、分发本项目，但必须保留原作者版权声明和本许可证，且衍生项目需同样以GPL-3.0协议开源。

---

## 一、项目简介

本项目为"AI模拟面试系统"后端，基于Spring Boot 3.4.6，集成Spring Security、JWT、Mybatis、MinIO、Apache Tika、阿里云AI等，提供认证授权、简历解析、文件存储、AI智能分析、RBAC权限、数据存储等核心服务。支持Docker容器化部署，敏感信息通过环境变量注入，适合企业级生产环境。

---

## 二、主要功能

- **用户认证与权限管理**：基于RBAC模型，支持JWT、RSA加密、验证码校验、权限细粒度控制
- **简历管理与智能解析**：简历上传、解析（PDF/Word/TXT）、关键信息提取、AI智能分析、结构化数据输出
- **文件存储服务**：基于MinIO的对象存储，支持文件上传、下载、删除、URL生成
- **AI智能分析引擎**：集成阿里云通义千问，支持简历智能解析、结构化数据提取、智能纠错
- **企业与岗位管理**：企业、岗位、用户、角色、权限等基础数据管理
- **统一响应与异常处理**：标准化API响应结构，丰富的异常处理机制
- **安全加固**：RSA加密、BCrypt密码、敏感数据脱敏、全链路HTTPS
- **数据库初始化与测试**：提供完整的DDL/DML脚本及单元测试用例

---

## 三、技术栈

| 模块 | 技术实现 | 版本          | 说明 |
|------|----------|-------------|------|
| **后端框架** | Spring Boot | 3.4.6       | Java 17基础 |
| **安全框架** | Spring Security + JWT | 6.x + 4.5.0 | OAuth2.0兼容 |
| **数据访问** | MyBatis + MySQL | 3.x + 8.x   | 关系型数据库 |
| **缓存** | Redis Cluster | 8.2         | 分布式缓存 |
| **文件存储** | MinIO | 8.5.17      | 对象存储服务 |
| **文档解析** | Apache Tika | 3.1.0       | PDF/Word/TXT解析 |
| **AI服务** | 阿里云通义千问 | 1.0.0.2     | 智能简历分析 |
| **工具库** | Hutool | 5.8.39      | 工具类集合 |
| **构建工具** | Maven | 3.9.9       | 依赖管理 |
| **容器化** | Docker | -           | 容器化部署 |

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
│   │   │   │   ├── constant/                       # 常量定义
│   │   │   │   ├── listener/                       # 应用监听器
│   │   │   │   ├── properties/                     # 全局配置属性
│   │   │   │   ├── response/                       # 统一响应结构
│   │   │   │   ├── utils/                          # 工具类（JWT、RSA、验证码等）
│   │   │   │   └── exception/                      # 全局异常处理
│   │   │   ├── resume/                             # 简历模块
│   │   │   │   ├── controller/                     # 简历相关接口
│   │   │   │   ├── service/                        # 简历服务接口与实现
│   │   │   │   ├── config/                         # 简历解析器配置
│   │   │   │   └── exception/                      # 简历相关异常
│   │   │   ├── minio/                              # 文件存储模块
│   │   │   │   ├── service/                        # MinIO服务接口与实现
│   │   │   │   └── config/                         # MinIO配置
│   │   │   └── ai/                                 # AI模块
│   │   │       ├── config/                         # AI配置（通义千问）
│   │   │       └── service/                        # AI服务（简历分析）
│   │   ├── resources/
│   │   │   ├── config/application.yml              # 配置文件，端口8081，敏感信息用环境变量
│   │   │   ├── mapper/                             # MyBatis XML映射
│   │   │   ├── init/ddl/                           # 数据库表结构SQL
│   │   │   ├── init/dml/                           # 数据库初始化数据SQL
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
- **用户注册/登录**：支持RSA加密密码、验证码校验、JWT令牌签发
- **权限控制**：基于RBAC模型，支持用户-角色-权限-公司多维绑定
- **安全过滤器**：JWT验证、验证码校验、请求体缓存等
- **异常处理**：登录失败、权限不足、JWT过期等异常处理

### 5.2 简历管理（resume）
- **文件上传**：支持PDF、Word、TXT格式，文件大小限制5MB
- **智能解析**：基于Apache Tika的文档内容提取
- **AI分析**：集成阿里云通义千问，智能解析简历内容，提取结构化数据
- **文件存储**：集成MinIO对象存储，支持分布式部署
- **下载管理**：文件下载、删除、权限控制

### 5.3 AI智能分析（ai）
- **简历智能解析**：基于通义千问的简历内容分析
- **结构化数据提取**：自动提取个人信息、教育背景、工作经验、项目经历等
- **智能纠错**：自动修正文本中的错别字和技术术语错误
- **格式标准化**：统一日期、技能名称等字段格式

### 5.4 文件存储（minio）
- **对象存储**：基于MinIO的文件上传、下载、删除
- **URL生成**：预签名URL，支持临时访问
- **桶管理**：自动创建、删除存储桶
- **文件类型**：支持多种文件类型识别

### 5.5 通用与全局（common）
- **统一响应**：标准化API响应结构
- **异常处理**：全局异常捕获与处理
- **工具类**：JWT、RSA、验证码、用户工具等
- **配置管理**：全局配置属性管理

---

## 六、构建与部署

### 6.1 环境要求

- **JDK**：17+
- **Maven**：3.9.9+
- **Docker**：20.10+
- **MySQL**：8.0+
- **Redis**：7.0+（集群模式）
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

### 6.5 MinIO部署

```bash
# 1. 创建MinIO docker-compose.yml
version: '3.8'
services:
  minio:
    image: quay.io/minio/minio:latest
    container_name: minio
    ports:
      - "9000:9000"
      - "9001:9001"
    environment:
      MINIO_ROOT_USER: admin
      MINIO_ROOT_PASSWORD: your_minio_password
    volumes:
      - ./minio/data:/data
      - ./minio/config:/root/.minio
    command: server /data --console-address ":9001"
    restart: unless-stopped

# 2. 启动MinIO
docker-compose up -d

# 3. 创建存储桶
docker exec -it minio mc alias set myminio http://localhost:9000 admin your_minio_password
docker exec -it minio mc mb myminio/resume-upload
```

---

## 七、配置说明

### 7.1 核心配置

- **端口**：默认8081（见`application.yml`）
- **敏感信息**：数据库、Redis、MinIO、阿里云API等均通过环境变量注入
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
| `MINIO_ENDPOINT` | MinIO服务地址 | `http://localhost:9000` |
| `MINIO_ACCESS_KEY` | MinIO访问密钥 | `admin` |
| `MINIO_SECRET_KEY` | MinIO秘密密钥 | `minio_password` |
| `ALIYUN_API_KEY` | 阿里云API密钥 | `your_aliyun_api_key` |

### 7.3 数据库初始化

```bash
# 1. 创建数据库
CREATE DATABASE interview_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. 执行DDL脚本
mysql -u root -p interview_db < src/main/init/ddl/*.sql

# 3. 执行DML脚本
mysql -u root -p interview_db < src/main/init/dml/*.sql
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
| 简历上传 | POST | `/api/resume/upload` | 上传简历文件（需要JOB_SEEKER角色） |
| 简历下载 | GET | `/api/resume/download` | 下载简历文件 |
| 简历删除 | DELETE | `/api/resume/delete` | 删除简历文件 |

### 8.3 权限说明

- **JOB_SEEKER**：求职者角色，可以上传简历
- **HR**：人力资源角色，可以查看和管理简历
- **ADMIN**：管理员角色，拥有所有权限

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
```

---

## 十、常见问题与解决方案

### 10.1 启动问题

- **端口冲突**：修改`application.yml`中的`server.port`
- **数据库连接失败**：检查环境变量`MYSQL_JDBC_URL`等配置
- **Redis连接失败**：检查Redis集群配置和环境变量

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

### 10.5 部署问题

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

