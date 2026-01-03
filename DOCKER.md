# Docker 部署指南

本文档介绍如何使用 Docker 和 Docker Compose 部署 Interview App 微服务系统。

## 📋 目录

- [前置要求](#前置要求)
- [快速开始](#快速开始)
- [构建镜像](#构建镜像)
- [部署服务](#部署服务)
- [服务说明](#服务说明)
- [环境变量配置](#环境变量配置)
- [生产环境部署](#生产环境部署)
- [常见问题](#常见问题)

## 前置要求

1. **Docker**: 版本 20.10 或更高
2. **Docker Compose**: 版本 2.0 或更高（或 Docker Desktop 内置的 compose）
3. **外部服务**: 
   - Nacos（服务注册与配置中心）
   - MySQL（关系型数据库）
   - Redis（缓存）
   - MongoDB（文档数据库，可选）
   - MinIO（对象存储，可选）

## 快速开始

### 1. 配置环境变量

创建 `.env` 文件（可选），或使用环境变量：

```bash
export NACOS_SERVER_ADDR=your-nacos-host:8848
```

### 2. 构建所有服务镜像

```bash
# 使用构建脚本（推荐）
./build-docker.sh

# 或使用 docker-compose 构建
docker-compose build
```

**注意**: 构建时使用阿里云 Maven 镜像加速，配置文件位于 `.m2/settings.xml`

### 3. 启动所有服务

```bash
# 使用部署脚本（推荐）
./deploy.sh dev

# 或直接使用 docker-compose
docker-compose up -d
```

### 4. 查看服务状态

```bash
docker-compose ps
```

### 5. 查看服务日志

```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f gateway
docker-compose logs -f user-service
```

### 6. 停止服务

```bash
docker-compose down
```

## 构建镜像

### 构建单个服务

```bash
# 使用构建脚本
./build-docker.sh gateway
./build-docker.sh user-service
./build-docker.sh resume-service
./build-docker.sh ai-service
./build-docker.sh interview-service
./build-docker.sh candidate-service

# 或使用 Docker 命令
docker build -f gateway/Dockerfile -t interview-gateway:latest .
docker build -f service/user-service/Dockerfile -t interview-user-service:latest .
```

### 构建所有服务

```bash
./build-docker.sh
```

### Maven 镜像加速

项目已配置使用阿里云 Maven 镜像加速依赖下载，配置文件位于 `.m2/settings.xml`。

如需使用其他镜像源，可以修改 `.m2/settings.xml` 文件。

## 部署服务

### 开发环境部署

```bash
./deploy.sh dev
# 或
docker-compose up -d
```

### 生产环境部署

```bash
./deploy.sh prod
# 或
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

## 服务说明

### 微服务列表

| 服务名称 | 容器名 | 端口 | 说明 |
|---------|--------|------|------|
| gateway | interview-gateway | 80 | API 网关 |
| user-service | interview-user-service | 10000 | 用户服务 |
| resume-service | interview-resume-service | 20000 | 简历服务 |
| ai-service | interview-ai-service | 50000 | AI 服务 |
| interview-service | interview-interview-service | 30000 | 面试服务 |
| candidate-service | interview-candidate-service | 40000 | 候选人服务 |

### 访问地址

- **API 网关**: http://localhost
- **服务健康检查**: http://localhost:{服务端口}/actuator/health

## 环境变量配置

### 必需环境变量

所有微服务都需要以下环境变量：

- `NACOS_SERVER_ADDR`: Nacos 服务器地址（格式: host:port，例如: 192.168.1.100:8848）

### 配置方式

#### 方式一：使用环境变量

```bash
export NACOS_SERVER_ADDR=your-nacos-host:8848
docker-compose up -d
```

#### 方式二：使用 .env 文件

在项目根目录创建 `.env` 文件：

```bash
NACOS_SERVER_ADDR=your-nacos-host:8848
```

#### 方式三：修改 docker-compose.yml

直接修改 `docker-compose.yml` 中的 `NACOS_SERVER_ADDR` 环境变量。

### Java 运行时参数

可以通过 `JAVA_OPTS` 环境变量配置 JVM 参数：

```yaml
environment:
  - JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC
```

### 数据库连接配置

数据库连接配置应在 Nacos 配置中心配置，服务启动时会自动从 Nacos 读取。

### 修改配置

1. **修改 docker-compose.yml**: 直接编辑文件，修改环境变量、端口等
2. **修改 Nacos 配置**: 访问 Nacos 控制台，修改配置后服务会自动刷新（如果配置了 refreshEnabled=true）

## 生产环境部署

### 重要提示

⚠️ **生产环境建议**：

1. **使用外部服务**: 确保 MySQL、Redis、MongoDB、Nacos、MinIO 等中间件服务已部署并可访问
2. **网络配置**: 确保微服务容器可以访问外部服务（MySQL、Redis、Nacos 等）
3. **安全配置**: 使用强密码，配置防火墙规则
4. **资源限制**: 根据实际负载调整 JVM 内存参数
5. **监控告警**: 配置监控和日志收集系统
6. **备份策略**: 制定数据库和文件备份策略

### 生产环境配置

使用 `docker-compose.prod.yml` 覆盖默认配置：

```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

生产环境配置包含：
- 更大的 JVM 堆内存
- 容器资源限制
- G1GC 垃圾回收器配置

### 网络配置

如果外部服务与 Docker 容器不在同一网络，需要确保：

1. **DNS 解析**: 确保容器可以通过主机名或 IP 访问外部服务
2. **端口开放**: 确保防火墙允许访问外部服务端口
3. **Nacos 配置**: 使用外部服务的实际 IP 或域名

示例配置：

```yaml
environment:
  - NACOS_SERVER_ADDR=192.168.1.100:8848  # 使用实际 IP
```

## 常见问题

### 1. 服务启动失败

**问题**: 服务启动后立即退出

**解决方案**:
- 查看日志: `docker-compose logs <service-name>`
- 检查 Nacos 是否可访问: `docker-compose exec gateway ping your-nacos-host`
- 检查环境变量 `NACOS_SERVER_ADDR` 是否正确
- 确认外部服务（MySQL、Redis、Nacos 等）正常运行

### 2. 服务无法连接到 Nacos

**问题**: 服务日志显示无法连接到 Nacos

**解决方案**:
- 检查 `NACOS_SERVER_ADDR` 环境变量是否正确
- 确认 Nacos 服务正常运行且网络可达
- 检查防火墙规则，确保端口 8848 可访问
- 如果 Nacos 在宿主机上，可以使用 `host.docker.internal` (Mac/Windows) 或宿主机 IP (Linux)

### 3. 端口冲突

**问题**: 端口已被占用

**解决方案**:
- 修改 `docker-compose.yml` 中的端口映射
- 检查并停止占用端口的其他服务

### 4. 内存不足

**问题**: 容器因内存不足被杀死

**解决方案**:
- 增加 Docker 可用内存
- 减少服务的 JVM 堆内存配置
- 使用生产环境配置，合理分配资源

### 5. Maven 构建速度慢

**问题**: Docker 镜像构建时 Maven 下载依赖慢

**解决方案**:
- 项目已配置阿里云 Maven 镜像，构建时会自动使用
- 如果仍然慢，可以检查网络连接
- 可以考虑使用本地 Maven 仓库缓存

### 6. 服务无法访问外部数据库

**问题**: 容器内无法连接到宿主机上的 MySQL/Redis 等服务

**解决方案**:
- **Mac/Windows**: 使用 `host.docker.internal` 作为主机名
  ```yaml
  environment:
    - NACOS_SERVER_ADDR=host.docker.internal:8848
  ```
- **Linux**: 使用宿主机 IP 地址，或使用 `host` 网络模式
  ```yaml
  environment:
    - NACOS_SERVER_ADDR=192.168.1.100:8848
  ```

### 7. 清理资源

```bash
# 停止并删除容器
docker-compose down

# 停止并删除容器和网络
docker-compose down --remove-orphans

# 删除所有未使用的镜像
docker image prune -a
```

## 性能优化建议

1. **JVM 调优**: 根据服务负载调整 `-Xms` 和 `-Xmx` 参数
2. **垃圾回收**: 生产环境使用 G1GC: `-XX:+UseG1GC`
3. **容器资源**: 合理设置 CPU 和内存限制
4. **数据库连接池**: 在 Nacos 配置中优化数据库连接池参数
5. **缓存策略**: 合理使用 Redis 缓存，减少数据库压力

## 监控和日志

### 查看实时日志

```bash
# 所有服务
docker-compose logs -f

# 特定服务
docker-compose logs -f gateway user-service
```

### 健康检查

所有服务都配置了健康检查，可以通过以下方式查看：

```bash
# 查看容器健康状态
docker-compose ps

# 直接访问健康检查端点
curl http://localhost:10000/actuator/health
```

### 监控指标

所有服务都集成了 Spring Boot Actuator，可以通过以下端点获取监控指标：

- `/actuator/health`: 健康检查
- `/actuator/info`: 应用信息
- `/actuator/metrics`: 指标列表
- `/actuator/prometheus`: Prometheus 格式指标（如果集成了）

## 更新服务

### 更新单个服务

```bash
# 1. 重新构建镜像
./build-docker.sh user-service

# 2. 重启服务
docker-compose up -d --no-deps user-service
```

### 更新所有服务

```bash
# 1. 重新构建所有镜像
./build-docker.sh

# 2. 重启所有服务
docker-compose up -d --force-recreate
```

## 故障排查

### 查看容器状态

```bash
docker-compose ps
docker-compose top
```

### 进入容器调试

```bash
docker-compose exec gateway sh
docker-compose exec user-service sh
```

### 查看容器资源使用

```bash
docker stats
```

---

**提示**: 如有问题，请查看服务日志或联系开发团队。
