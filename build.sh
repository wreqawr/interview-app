#!/bin/bash

# 构建脚本
set -e

# 定义镜像名称
# shellcheck disable=SC2269
IMAGE_NAME="interview-app:latest"

# 检查镜像是否存在
if docker image inspect "$IMAGE_NAME" &> /dev/null; then
    echo "镜像 $IMAGE_NAME 存在，开始删除..."
    # 强制删除镜像（同时删除所有依赖容器）
    docker rmi -f "$IMAGE_NAME"
    # 检查删除结果
    # shellcheck disable=SC2181
    if [ $? -eq 0 ]; then
        echo "镜像 $IMAGE_NAME 已成功删除"
    else
        echo "删除镜像失败，请检查错误信息"
    fi
else
    echo "镜像 $IMAGE_NAME 不存在，无需删除"
fi

echo "开始构建 Docker 镜像..."

docker build --no-cache -t $IMAGE_NAME .

echo "构建完成！"

# 可选：推送到镜像仓库
# docker tag $IMAGE_NAME your-registry/$IMAGE_NAME
# docker push your-registry/$IMAGE_NAME

echo "镜像构建完成，可以使用以下命令运行："
echo "docker run -d -p 8081:8081 --name interview-app \
  -e MYSQL_JDBC_URL=jdbc:mysql://your-mysql-host:3306/yourdb?useSSL=false \
  -e MYSQL_USERNAME=your_db_user \
  -e MYSQL_PASSWORD=your_db_password \
  -e REDIS_HOST=your-redis-host \
  -e REDIS_PASSWORD=your_redis_password \
  $IMAGE_NAME"
echo "或者使用 docker-compose:"
echo "docker-compose up -d" 