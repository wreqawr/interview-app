#!/bin/bash

# Docker 部署脚本
# 使用方法: ./deploy.sh [dev|prod]

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_ROOT=$(cd "$(dirname "$0")" && pwd)
cd "$PROJECT_ROOT"

# 环境参数
ENV=${1:-dev}

echo -e "${GREEN}开始部署，环境: ${ENV}${NC}"

# 检查 Docker 和 Docker Compose
if ! command -v docker &> /dev/null; then
    echo -e "${RED}错误: 未找到 Docker，请先安装 Docker${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo -e "${RED}错误: 未找到 Docker Compose，请先安装 Docker Compose${NC}"
    exit 1
fi

# 确定使用的 compose 命令
if docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
else
    COMPOSE_CMD="docker-compose"
fi

# 根据环境选择配置文件
if [ "$ENV" == "prod" ]; then
    echo -e "${YELLOW}使用生产环境配置${NC}"
    COMPOSE_FILES="-f docker-compose.yml -f docker-compose.prod.yml"
else
    echo -e "${YELLOW}使用开发环境配置${NC}"
    COMPOSE_FILES="-f docker-compose.yml"
fi

# 停止旧容器
echo -e "${YELLOW}停止旧容器...${NC}"
$COMPOSE_CMD $COMPOSE_FILES down

# 构建镜像（如果需要）
read -p "是否需要重新构建镜像? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}构建镜像...${NC}"
    $COMPOSE_CMD $COMPOSE_FILES build --no-cache
fi

# 启动服务
echo -e "${GREEN}启动服务...${NC}"
$COMPOSE_CMD $COMPOSE_FILES up -d

# 等待服务启动
echo -e "${YELLOW}等待服务启动...${NC}"
sleep 10

# 显示服务状态
echo -e "${GREEN}服务状态:${NC}"
$COMPOSE_CMD $COMPOSE_FILES ps

echo -e "${GREEN}部署完成!${NC}"
echo -e "${YELLOW}查看日志: $COMPOSE_CMD $COMPOSE_FILES logs -f [service-name]${NC}"
echo -e "${YELLOW}停止服务: $COMPOSE_CMD $COMPOSE_FILES down${NC}"

