#!/bin/bash

# Interview App 构建脚本
# 只负责 Docker 镜像构建和容器管理，不包含 Maven 构建步骤
set -e

echo "=========================================="
echo "Interview App 构建脚本"
echo "=========================================="

# 检查 Docker 是否运行
if ! docker info > /dev/null 2>&1; then
    echo "❌ 错误: Docker 服务未运行，请先启动 Docker"
    exit 1
fi

# 检查是否在项目根目录
if [ ! -f "pom.xml" ] || [ ! -d "interview-app-mvc" ]; then
    echo "❌ 错误: 请在项目根目录下执行此脚本"
    exit 1
fi

echo "✅ 环境检查通过"
echo ""

# 询问是否构建Docker镜像
read -p "是否构建 Docker 镜像？(y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "🚀 开始构建 Docker 镜像..."
    echo "注意：使用 JLink 优化的 Dockerfile，镜像大小约 291MB"
    
    # 构建Docker镜像
    docker build --no-cache -t interview-app-mvc:latest -f interview-app-mvc/Dockerfile .
    
    if [ $? -eq 0 ]; then
        echo "✅ Docker镜像构建成功！"
        
        # 显示镜像大小信息
        echo ""
        echo "📊 镜像构建结果："
        docker images | grep interview-app-mvc
        
        # 显示优化效果
        echo ""
        echo "🎉 JLink 优化效果："
        echo "- 原始版本大小：约 404MB"
        echo "- JLink 优化版本：约 291MB"
        echo "- 减少幅度：约 28%"
        echo "- 减少大小：约 113MB"
        
        # 询问是否运行容器
        echo ""
        read -p "是否立即运行容器？(y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            echo ""
            echo "🐳 启动容器..."
            
            # 停止并删除同名容器（如果存在）
            docker stop interview-app-mvc 2>/dev/null || true
            docker rm interview-app-mvc 2>/dev/null || true
            
            # 运行新容器
            docker run -d \
                --name interview-app-mvc \
                -p 8081:8081 \
                -e SPRING_PROFILES_ACTIVE=docker \
                -e TZ=Asia/Shanghai \
                interview-app-mvc:latest
            
            if [ $? -eq 0 ]; then
                echo "✅ 容器启动成功！"
                echo ""
                echo "📋 容器信息："
                docker ps | grep interview-app-mvc
                echo ""
                echo "🌐 访问地址：http://localhost:8081"
                echo "📊 健康检查：http://localhost:8081/actuator/health"
            else
                echo "❌ 容器启动失败"
            fi
        fi
    else
        echo "❌ Docker镜像构建失败"
        exit 1
    fi
else
    echo "跳过Docker镜像构建"
fi

echo ""
echo "=========================================="
echo "构建脚本执行完成！"
echo "=========================================="
echo ""
echo "📚 可用的命令："
echo ""
echo "1. 启动应用服务："
echo "   docker-compose -f docker-compose-all.yml up -d"
echo ""
echo "2. 启动基础设施服务（可选）："
echo "   docker-compose -f docker-compose-infrastructure.yml up -d"
echo ""
echo "3. 管理容器："
echo "   docker ps | grep interview-app-mvc          # 查看容器状态"
echo "   docker logs interview-app-mvc               # 查看容器日志"
echo "   docker stop interview-app-mvc               # 停止容器"
echo "   docker start interview-app-mvc              # 启动容器"
echo "   docker restart interview-app-mvc            # 重启容器"
echo ""
echo "4. 管理镜像："
echo "   docker images | grep interview-app-mvc      # 查看镜像"
echo "   docker rmi interview-app-mvc:latest         # 删除镜像"
echo ""
echo "5. 进入容器："
echo "   docker exec -it interview-app-mvc /bin/sh  # 进入容器shell"
echo ""
echo "💡 提示：所有 Maven 构建步骤都在 Dockerfile 中完成，"
echo "    确保在项目根目录下执行此脚本。" 