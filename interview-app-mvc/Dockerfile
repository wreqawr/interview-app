# ----------- 构建阶段 -----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# 配置Maven镜像源，加速依赖下载
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

# 复制源代码和POM文件
COPY src ./src
COPY pom.xml ./pom.xml

# 直接构建当前模块（不依赖父项目）
RUN mvn clean package -DskipTests

# ----------- 运行阶段 -----------
FROM eclipse-temurin:17-jre
WORKDIR /app
# 只复制最终jar包，避免源码、测试等无关内容进入镜像
COPY --from=builder /app/target/*.jar app.jar

# 可选：暴露端口（如8081）
EXPOSE 8081

# JVM参数可根据实际需求调整
ENTRYPOINT ["java", "-jar", "app.jar"] 