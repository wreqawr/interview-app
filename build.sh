#!/bin/bash

# 父项目构建脚本
set -e

echo "开始构建 interview-app 项目..."

# 构建整个项目
echo "1. 构建整个项目..."
mvn clean install -DskipTests

# 构建MVC模块
echo "2. 构建MVC模块..."
cd interview-app-mvc
mvn clean package -DskipTests

# 询问是否构建Docker镜像
echo ""
read -p "是否构建Docker镜像？(y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "3. 构建Docker镜像..."
    # 使用父项目构建Docker镜像
    docker build --no-cache -t interview-app-mvc:latest -f Dockerfile .
    echo "Docker镜像构建完成！"
else
    echo "跳过Docker镜像构建"
fi

echo ""
echo "构建完成！"
echo ""
echo "可以使用以下命令启动服务："
echo "1. 使用docker-compose启动整个项目："
echo "   docker-compose up -d"
echo ""
echo "2. 单独启动MVC模块："
echo "   cd interview-app-mvc"
echo "   docker-compose up -d"
echo ""
echo "3. 直接运行jar包："
echo "   cd interview-app-mvc"
echo "   java -jar target/*.jar"
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "4. 运行Docker容器："
    echo "   docker run -d -p 8081:8081 --name interview-app-mvc interview-app-mvc:latest"
fi 