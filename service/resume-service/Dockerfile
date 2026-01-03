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
COPY gateway/pom.xml gateway/
COPY service/pom.xml service/
COPY service/user-service/pom.xml service/user-service/
COPY service/resume-service/pom.xml service/resume-service/
COPY service/candidate-service/pom.xml service/candidate-service/
COPY service/interview-service/pom.xml service/interview-service/
COPY service/ai-service/pom.xml service/ai-service/

# 使用 BuildKit 缓存挂载 Maven 本地仓库，避免重复下载依赖
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn dependency:go-offline -T 1C -pl commons -am || true

# 复制源代码
COPY starters/ starters/
COPY commons/src commons/src
COPY gateway/src gateway/src
COPY service/resume-service/src service/resume-service/src

# 先构建并安装 commons，然后构建 starters（resume-service 依赖它们）
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn clean install -DskipTests -T 1C -pl commons -am && \
    cd starters && mvn clean install -DskipTests -T 1C && cd ..

# 构建应用
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn clean package -DskipTests -T 1C -pl service/resume-service -am

# 将可执行的 fat JAR 复制为固定名称（排除 -original.jar）
RUN cd /build/service/resume-service/target && \
    ls -la *.jar 2>/dev/null && \
    JAR_FILE=$(ls resume-service-*.jar 2>/dev/null | grep -v original | head -1) && \
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
COPY --from=builder /build/service/resume-service/target/app.jar app.jar

# 设置文件所有者
RUN chown spring:spring app.jar

USER spring:spring

# 暴露端口
EXPOSE 20000

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:20000/actuator/health || exit 1

# 启动应用（使用环境变量 JAVA_OPTS）
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

