# Interview App MVC 模块构建指南

## 📋 项目概述

**Interview App MVC** 是一个基于 Spring Boot 的 AI 模拟面试系统，提供简历分析、AI 面试、用户管理等功能。

### 主要特性
- 🤖 AI 驱动的简历分析和面试模拟
- 🔐 基于 RBAC 的用户权限管理
- 📊 简历管理和进度跟踪
- 🗄️ 多数据源支持（MySQL、Redis、MongoDB、MinIO）
- 🐳 Docker 容器化部署（JLink 优化，镜像大小仅 291MB）
- 🔄 异步任务处理

### 技术栈
- **后端框架**: Spring Boot 3.x
- **安全框架**: Spring Security + JWT
- **数据访问**: MyBatis + Druid
- **缓存**: Redis Cluster
- **文档存储**: MongoDB
- **对象存储**: MinIO
- **AI 服务**: Alibaba DashScope
- **构建工具**: Maven 3.9+
- **容器化**: Docker + Docker Compose

## 🚀 快速开始

### 环境要求

#### 必需环境
- **JDK**: 17 或更高版本
- **Maven**: 3.9+ (仅用于本地开发)
- **Docker**: 20.10+ (用于容器化部署)
- **Docker Compose**: 2.0+

#### 可选环境
- **IDE**: IntelliJ IDEA, Eclipse, VS Code
- **数据库客户端**: MySQL Workbench, Redis Desktop Manager

### 系统要求
- **内存**: 最少 4GB RAM
- **磁盘**: 最少 10GB 可用空间
- **网络**: 可访问 Maven 中央仓库和 Docker Hub

## 🏗️ 构建方式

### 方式一：本地构建（开发环境）

#### 1. 克隆项目
```bash
git clone <repository-url>
cd interview-app
```

#### 2. 构建整个项目
```bash
# 从根目录构建
mvn clean install -Dmaven.test.skip=true
```

#### 3. 构建 MVC 模块
```bash
cd interview-app-mvc
mvn clean package -Dmaven.test.skip=true
```

#### 4. 运行应用
```bash
# 方式1：直接运行 jar 包
java -jar target/*.jar

# 方式2：使用 Spring Boot Maven 插件
mvn spring-boot:run
```

### 方式二：Docker 构建（推荐生产环境）

#### 1. 从根目录构建（推荐）
```bash
cd interview-app
./build.sh
```

#### 2. 构建并运行
```bash
./build.sh
# 选择 'y' 构建 Docker 镜像
```

#### 3. 启动应用服务
```bash
# 使用 Docker Compose 启动应用服务
docker-compose -f docker-compose-all.yml up -d

# 或启动基础设施服务（可选）
docker-compose -f docker-compose-infrastructure.yml up -d
```

## 📦 构建脚本详解

### 根目录 build.sh 脚本

```bash
# 在项目根目录执行
./build.sh

# 脚本功能：
# 1. 构建整个项目
# 2. 构建 MVC 模块
# 3. 构建 JLink 优化的 Docker 镜像
# 4. 显示优化效果（404MB → 291MB，减少 28%）
```

### 常用构建命令

```bash
# 基础构建（包含 Docker 镜像）
./build.sh

# 仅 Maven 构建（不构建 Docker 镜像）
./build.sh
# 选择 'n' 跳过 Docker 镜像构建
```

## 🐳 Docker 部署

### 单容器部署

```bash
# 从项目根目录构建镜像
cd interview-app
docker build -t interview-app-mvc:latest -f interview-app-mvc/Dockerfile .

# 运行容器
docker run -d \
  --name interview-app-mvc \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e TZ=Asia/Shanghai \
  interview-app-mvc:latest
```

### 使用 Docker Compose

#### 应用服务编排
```bash
# 启动所有应用服务
docker-compose -f docker-compose-all.yml up -d

# 查看服务状态
docker-compose -f docker-compose-all.yml ps

# 查看日志
docker-compose -f docker-compose-all.yml logs -f

# 停止服务
docker-compose -f docker-compose-all.yml down
```

#### 基础设施服务（可选）
```bash
# 启动基础设施服务
docker-compose -f docker-compose-infrastructure.yml up -d

# 初始化 Redis 集群
docker-compose -f docker-compose-infrastructure.yml --profile init up -d
```

## 🎉 Docker 镜像优化效果

### 优化前后对比
| 版本 | 镜像大小 | 优化技术 | 减少幅度 |
|------|----------|----------|----------|
| **原始版本** | 404MB | 标准 JRE | - |
| **JLink 优化版** | **291MB** | **JLink 自定义 JRE + Alpine** | **28%** |

### 优化技术说明
- **JLink 自定义 JRE**: 只包含应用必需的 Java 模块
- **Alpine Linux**: 使用最轻量的 Linux 发行版
- **模块裁剪**: 移除不必要的 Java 模块
- **分层优化**: 优化 Docker 镜像层结构

## 🔧 配置说明

### 配置文件结构
```
src/main/resources/
├── config/
│   ├── application.yml              # 主配置文件
│   └── application-docker.yml      # Docker 环境配置
├── mapper/                          # MyBatis 映射文件
├── prompt/                          # AI 提示词模板
└── banner/                          # 启动横幅
```

### 主要配置项

#### 服务器配置
```yaml
server:
  port: 8081
```

#### 数据源配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${MYSQL_JDBC_URL}
    username: ${MYSQL_USERNAME}
    password: ${MYSQL_PASSWORD}
    type: com.alibaba.druid.pool.DruidDataSource
```

#### Redis 集群配置
```yaml
spring:
  data:
    redis:
      cluster:
        nodes:
          - ${REDIS_NODE_1}
          - ${REDIS_NODE_2}
          - ${REDIS_NODE_3}
          - ${REDIS_NODE_4}
          - ${REDIS_NODE_5}
          - ${REDIS_NODE_6}
        max-redirects: 3
      password: ${REDIS_PASSWORD}
```

## 🚨 常见问题

### 构建问题

#### 1. Maven 依赖下载失败
```bash
# 清理本地仓库缓存
mvn dependency:purge-local-repository

# 使用阿里云镜像
# 已在 Dockerfile 中配置
```

#### 2. 父 POM 找不到
```bash
# 确保从项目根目录构建
cd interview-app
./build.sh
```

#### 3. 测试编译失败
```bash
# 使用 -Dmaven.test.skip=true 跳过测试
# 已在 Dockerfile 中配置
```

### 运行时问题

#### 1. 数据库连接失败
- 检查数据库服务是否启动
- 验证连接字符串和凭据
- 确认网络连通性

#### 2. Redis 连接失败
- 检查 Redis 集群状态
- 验证节点地址和密码
- 确认防火墙设置

#### 3. 端口被占用
```bash
# 查看端口占用
lsof -i :8081

# 停止占用端口的进程
kill -9 <PID>
```

### Docker 相关问题

#### 1. 镜像构建失败
```bash
# 确保在项目根目录构建
cd interview-app
docker build -t interview-app-mvc:latest -f interview-app-mvc/Dockerfile .

# 清理 Docker 缓存
docker system prune -a
```

#### 2. 容器启动失败
```bash
# 查看容器日志
docker logs interview-app-mvc

# 检查环境变量配置
docker exec -it interview-app-mvc env
```

#### 3. 镜像大小异常
```bash
# 验证是否使用了 JLink 优化版本
docker images | grep interview-app-mvc
# 应该显示约 291MB
```

## 📚 相关文档

- [项目根目录 README.md](../README.md) - 项目整体说明
- [构建指南](../BUILD_GUIDE_ALL.md) - 完整构建说明
- [模块管理脚本](../manage-modules.sh) - 模块管理工具

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 [MIT 许可证](../LICENSE)。

---

**Interview App MVC** - 让 AI 为你的面试保驾护航！ 🚀 