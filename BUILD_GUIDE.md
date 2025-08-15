# 项目构建说明

## 项目概述

本项目采用Maven父子项目管理架构，将原有的单体项目重构为模块化结构，为后续扩展实时通信功能做准备。

## 项目结构

```
interview-app/                           # 父项目根目录（当前目录）
├── pom.xml                             # 父POM文件
├── build.sh                            # 父项目构建脚本
├── docker-compose.yml                  # 父项目Docker编排文件
├── Dockerfile                          # 父项目Docker构建文件
├── README.md                           # 项目说明文档
├── BUILD_GUIDE.md                      # 本构建说明文档
└── interview-app-mvc/                  # MVC架构子模块
    ├── pom.xml                        # MVC模块POM文件
    ├── src/                           # 源代码目录
    │   ├── main/                      # 主要源码
    │   │   ├── java/                  # Java源码
    │   │   └── resources/             # 配置文件
    │   └── test/                      # 测试代码
    ├── target/                        # 编译输出目录
    ├── Dockerfile                     # Docker构建文件
    ├── build.sh                       # MVC模块构建脚本
    └── docker-compose.yml             # MVC模块Docker编排文件
```

## 模块说明

### 父项目 (interview-app) - 当前目录
- **作用**：管理整个项目的依赖版本、构建配置和子模块
- **特点**：使用`<packaging>pom</packaging>`，不包含具体业务代码
- **职责**：统一管理依赖版本、配置全局构建参数、管理子模块

### MVC模块 (interview-app-mvc)
- **作用**：包含原有的所有业务逻辑和功能
- **特点**：基于Spring Boot的传统MVC架构
- **职责**：用户认证与权限管理、简历管理与智能解析、AI智能分析引擎、文件存储服务、异步任务管理

## 构建说明

### 1. 构建整个项目
```bash
# 在父项目根目录执行（当前目录）
mvn clean install -DskipTests
```

### 2. 构建MVC模块
```bash
# 在MVC模块目录执行
cd interview-app-mvc
mvn clean package -DskipTests
```

### 3. 使用父项目构建脚本
```bash
# 在父项目根目录执行（当前目录）
./build.sh
```

### 4. 构建Docker镜像
```bash
# 在MVC模块目录执行
cd interview-app-mvc
./build.sh
```

## 部署说明

### 1. 使用父项目Docker编排
```bash
# 在父项目根目录执行（当前目录）
docker-compose up -d
```

### 2. 单独部署MVC模块
```bash
# 在MVC模块目录执行
cd interview-app-mvc
docker-compose up -d
```

### 3. 直接运行jar包
```bash
# 在MVC模块目录执行
cd interview-app-mvc
java -jar target/*.jar
```

## 技术栈

- **后端框架**：Spring Boot 3.4.6
- **安全框架**：Spring Security + JWT
- **数据访问**：MyBatis + MySQL
- **缓存**：Redis Cluster
- **文档数据库**：MongoDB
- **文件存储**：MinIO
- **AI服务**：阿里云通义千问
- **构建工具**：Maven 3.9.9
- **Java版本**：17

## 环境要求

- **JDK**：17+
- **Maven**：3.9.9+
- **Docker**：20.10+
- **MySQL**：8.0+
- **Redis**：7.0+（集群模式）
- **MongoDB**：7.0+
- **MinIO**：8.5.17+

## 环境变量配置

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

## 扩展计划

### 1. 实时通信模块 (interview-app-realtime)
- **技术栈**：Spring WebFlux + WebSocket
- **功能**：在线聊天、实时通知、流式响应
- **通信方式**：与MVC模块通过消息队列通信

### 2. 微服务架构
未来可考虑将项目拆分为独立的微服务：
- 用户服务
- 简历服务
- AI服务
- 文件服务
- 通知服务

## 注意事项

1. **依赖管理**：所有依赖版本在父POM中统一管理
2. **构建顺序**：必须先构建父项目，再构建子模块
3. **配置文件**：各模块的配置文件保持独立
4. **Docker构建**：子模块的Dockerfile需要适配新的目录结构
5. **环境变量**：确保所有必需的环境变量都已正确配置

## 常见问题

### 1. Maven构建问题
- 确保JDK版本为17+
- 确保Maven版本为3.9.9+
- 检查网络连接，确保能访问Maven仓库

### 2. Docker构建问题
- 确保Docker服务正在运行
- 检查Dockerfile中的路径配置
- 确保有足够的磁盘空间

### 3. 应用启动问题
- 检查环境变量配置
- 检查数据库连接配置
- 检查端口是否被占用

## 联系支持

如有问题或建议，请参考项目根目录的README.md文档，或联系项目维护团队。 