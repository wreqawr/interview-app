# syntax=docker/dockerfile:1.4
# 多阶段构建 - 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 配置 Maven 使用阿里云镜像加速
COPY .m2/settings.xml /root/.m2/settings.xml

# 复制 pom 文件（利用 Docker 层缓存）
COPY pom.xml .
COPY starters/pom.xml starters/
COPY starters/ai-spring-boot-autoconfigure/pom.xml starters/ai-spring-boot-autoconfigure/
COPY starters/ai-spring-boot-starter/pom.xml starters/ai-spring-boot-starter/
COPY starters/authentication-spring-boot-autoconfigure/pom.xml starters/authentication-spring-boot-autoconfigure/
COPY starters/authentication-spring-boot-starter/pom.xml starters/authentication-spring-boot-starter/
COPY commons/pom.xml commons/
COPY service/pom.xml service/
COPY service/user-service/pom.xml service/user-service/
COPY service/resume-service/pom.xml service/resume-service/
COPY service/candidate-service/pom.xml service/candidate-service/
COPY service/interview-service/pom.xml service/interview-service/
COPY service/ai-service/pom.xml service/ai-service/
COPY gateway/pom.xml gateway/

# 使用 BuildKit 缓存挂载 Maven 本地仓库，避免重复下载依赖
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn dependency:go-offline -T 1C -pl gateway -am || true

# 复制源代码
COPY starters/ starters/
COPY commons/src commons/src
COPY gateway/src gateway/src

# 构建应用（gateway 不依赖 starters 和 commons，所以直接构建即可）
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn clean package -DskipTests -T 1C -pl gateway -am

# 将可执行的 fat JAR 复制为固定名称（排除 -original.jar）
RUN cd /build/gateway/target && \
    ls -la *.jar 2>/dev/null && \
    JAR_FILE=$(ls gateway-*.jar 2>/dev/null | grep -v original | head -1) && \
    if [ -z "$JAR_FILE" ]; then \
        echo "ERROR: No valid JAR file found!" && ls -la && exit 1; \
    fi && \
    cp "$JAR_FILE" app.jar && \
    echo "Copied $JAR_FILE to app.jar" && \
    ls -lh app.jar

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 安装 curl 用于健康检查
RUN apk add --no-cache curl

# 创建非 root 用户
RUN addgroup -S spring && adduser -S spring -G spring

# 从构建阶段复制 JAR 文件（已在构建阶段重命名为 app.jar）
COPY --from=builder /build/gateway/target/app.jar app.jar

# 设置文件所有者
RUN chown spring:spring app.jar

USER spring:spring

# 暴露端口
EXPOSE 80

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:80/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]

