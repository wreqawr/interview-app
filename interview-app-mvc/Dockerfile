# ----------- 构建阶段 -----------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# 配置Maven镜像源
RUN mkdir -p /root/.m2 && \
    echo '<?xml version="1.0" encoding="UTF-8"?>' > /root/.m2/settings.xml && \
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' >> /root/.m2/settings.xml && \
    echo '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' >> /root/.m2/settings.xml && \
    echo '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0' >> /root/.m2/settings.xml && \
    echo '                          http://maven.apache.org/xsd/settings-1.0.0.xsd">' >> /root/.m2/settings.xml && \
    echo '    <mirrors>' >> /root/.m2/settings.xml && \
    echo '        <mirror>' >> /root/.m2/settings.xml && \
    echo '            <id>aliyunmaven</id>' >> /root/.m2/settings.xml && \
    echo '            <mirrorOf>*</mirrorOf>' >> /root/.m2/settings.xml && \
    echo '            <name>阿里云公共仓库</name>' >> /root/.m2/settings.xml && \
    echo '            <url>https://maven.aliyun.com/repository/public</url>' >> /root/.m2/settings.xml && \
    echo '        </mirror>' >> /root/.m2/settings.xml && \
    echo '    </mirrors>' >> /root/.m2/settings.xml && \
    echo '</settings>' >> /root/.m2/settings.xml

# 复制整个项目结构
COPY . .

# 构建项目
RUN mvn clean install -N -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Dmaven.dependency.skip=true && \
    mvn clean install -pl interview-app-starter -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Dmaven.dependency.skip=true && \
    mvn clean install -pl interview-app-starter/authentication-spring-boot-autoconfigure -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Dmaven.dependency.skip=true && \
    mvn clean install -pl interview-app-starter/authentication-spring-boot-starter -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Dmaven.dependency.skip=true && \
    mvn clean package -pl interview-app-mvc -Dmaven.test.skip=true \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Dmaven.dependency.skip=true

# ----------- JRE 构建阶段 -----------
FROM eclipse-temurin:17-jdk-alpine AS jre-builder
WORKDIR /app

# 创建自定义 JRE，只包含必要的模块
RUN jlink --module-path $JAVA_HOME/jmods \
    --add-modules java.base,java.logging,java.xml,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --output /opt/jre \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2

# ----------- 运行阶段 -----------
FROM alpine:latest
WORKDIR /app

# 设置时区（使用环境变量，避免网络依赖）
ENV TZ=Asia/Shanghai
RUN echo $TZ > /etc/timezone

# 复制自定义 JRE
COPY --from=jre-builder /opt/jre /opt/jre

# 创建非root用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 只复制最终jar包
COPY --from=builder /app/interview-app-mvc/target/*.jar app.jar

# 设置文件权限
RUN chown -R appuser:appgroup /app

# 切换到非root用户
USER appuser

# 暴露端口
EXPOSE 8081

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# 使用自定义 JRE 启动
ENTRYPOINT ["/opt/jre/bin/java", \
    "-server", \
    "-Xms256m", \
    "-Xmx512m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+UnlockExperimentalVMOptions", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=50.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=docker", \
    "-jar", "app.jar"] 