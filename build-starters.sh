#!/bin/bash

echo "开始构建 interview-app-starters 模块..."

# 进入starter项目目录
cd interview-app-starters

# 清理并安装到本地仓库
echo "清理并安装 authentication-spring-boot-autoconfigure..."
cd authentication-spring-boot-autoconfigure
mvn clean install -DskipTests
cd ..

echo "清理并安装 authentication-spring-boot-starter..."
cd authentication-spring-boot-starter
mvn clean install -DskipTests
cd ..

echo "清理并安装父模块..."
mvn clean install -DskipTests

echo "interview-app-starters 模块构建完成！"
echo "现在可以在其他项目中使用这些starter了。" 