# Interview App Starters

这是AI模拟面试系统的Starter模块集合，包含各种可复用的Spring Boot Starter。

## 项目结构

```
interview-app-starters/
├── authentication-spring-boot-autoconfigure/    # 认证自动配置模块
└── authentication-spring-boot-starter/          # 认证Starter入口模块
```

## 使用方法

### 1. 构建Starter模块

```bash
# 在项目根目录执行
./build-starters.sh
```

### 2. 在项目中使用

在Spring Boot项目的`pom.xml`中添加依赖：

```xml
<dependency>
    <groupId>cn.minglg.interview</groupId>
    <artifactId>authentication-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 3. 配置属性

在`application.yml`中配置相关属性：

```yaml
authentication:
  jwt:
    secret-key: your-secret-key
    expiration: 86400000
  redis:
    auth-key-prefix: "auth:"
  white-list-patterns:
    - "/api/public/**"
    - "/api/auth/login"
```

## 特性

- 支持WebMVC和WebFlux两种模式
- JWT Token认证
- Redis缓存支持
- 可配置的白名单路径
- 自动配置，开箱即用

## 注意事项

- 此模块已从主项目中独立出来，不再继承主项目的依赖
- 只包含必要的Spring Boot核心依赖
- 可以独立发布和复用 