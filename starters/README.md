# Interview App Starters

这是一个包含多个 Spring Boot Starter 的模块集合，用于快速配置和启动 AI 模拟面试系统。

## 模块说明

### 1. authentication-spring-boot-autoconfigure
认证模块的自动配置类，提供安全配置、JWT 认证、验证码等功能。

### 2. authentication-spring-boot-starter
认证模块的 Starter，引入后自动配置认证相关功能。

### 3. ai-spring-boot-autoconfigure
AI 模块的自动配置类，提供聊天客户端、模板渲染器、聊天记忆等 AI 相关功能。

### 4. ai-spring-boot-starter
AI 模块的 Starter，引入后自动配置 AI 相关功能。

## 使用方法

### 引入认证模块

```xml
<dependency>
    <groupId>cn.minglg.interview</groupId>
    <artifactId>authentication-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 引入 AI 模块

```xml
<dependency>
    <groupId>cn.minglg.interview</groupId>
    <artifactId>ai-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 配置说明

### AI 模块配置

在 `application.yml` 中添加以下配置：

```yaml
interview:
  ai:
    # 聊天记忆 Redis 键前缀
    chat-memory-redis-key-prefix: "chat:history"
    # 会话保存时间（天）
    chat-memory-redis-expire-days: 30
    # 单次对话上下文最长消息条数
    max-chat-messages: 50
    # 提示词模板渲染器使用的起始分隔符字符
    start-delimiter-character: "<"
    # 提示词模板渲染器使用的结束分隔符字符
    end-delimiter-character: ">"
    # 功能更强大的提示词模板渲染器使用的起始分隔符字符串
    start-delimiter-string: "#{"
    # 功能更强大的提示词模板渲染器使用的结束分隔符字符串
    end-delimiter-string: "}"
```

### 认证模块配置

在 `application.yml` 中添加以下配置：

```yaml
interview:
  authentication:
    # JWT 密钥
    jwt-secret: "your-jwt-secret"
    # JWT 过期时间（毫秒）
    jwt-expiration: 86400000
    # 验证码过期时间（秒）
    captcha-expiration: 300
```

## 功能特性

### AI 模块
- 自动配置聊天客户端（带记忆和不带记忆）
- 支持多种模板渲染器（StringTemplate、自定义多字符）
- 聊天记忆管理（支持 Redis 持久化和内存存储）
- 消息聊天记忆顾问

### 认证模块
- JWT 认证
- 验证码生成和验证
- RSA 加密
- 用户权限管理
- 支持 WebMvc 和 WebFlux

## 注意事项

1. AI 模块依赖 Redis，请确保 Redis 服务可用
2. 如果没有 Redis 配置，AI 模块会自动使用内存存储作为后备方案
3. 认证模块需要配置 JWT 密钥等敏感信息
4. 所有模块都基于 Spring Boot 3.x 和 Java 17

## 版本信息

- Spring Boot: 3.4.6
- Java: 17
- Spring AI: 通过阿里云 AI Starter 提供 