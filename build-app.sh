#!/bin/bash

# Docker 构建脚本
# 使用方法: ./build-app.sh [service-name]
# 不传参数则构建所有服务（并行执行）

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 项目根目录
PROJECT_ROOT=$(cd "$(dirname "$0")" && pwd)
cd "$PROJECT_ROOT"
LOG_PATH="$PROJECT_ROOT/logs"
rm -rf "$LOG_PATH" && mkdir -p "$LOG_PATH"

# 服务列表
SERVICES=("gateway" "user-service" "resume-service" "ai-service" "interview-service" "candidate-service")

# 构建单个服务
build_service() {
    local service=$1
    local dockerfile_path=""
    local image_name="interview-${service}"
    
    echo -e "${GREEN}[$(date '+%H:%M:%S')] 开始构建服务: ${service}${NC}"
    
    case $service in
        gateway)
            dockerfile_path="gateway/Dockerfile"
            ;;
        user-service|resume-service|ai-service|interview-service|candidate-service)
            dockerfile_path="service/${service}/Dockerfile"
            ;;
        *)
            echo -e "${RED}未知的服务: ${service}${NC}"
            return 1
            ;;
    esac
    
    if [ ! -f "$dockerfile_path" ]; then
        echo -e "${RED}[${service}] Dockerfile 不存在: ${dockerfile_path}${NC}"
        return 1
    fi
    
    echo -e "${YELLOW}[${service}] 使用 Dockerfile: ${dockerfile_path}${NC}"
    if [ -n "$(docker images -q "${image_name}:latest" 2>/dev/null)" ]; then
        echo -e "${YELLOW}[${service}] 删除旧镜像: ${image_name}:latest${NC}"
        docker rmi "${image_name}:latest" 2>/dev/null || true
    fi
    echo -e "${YELLOW}[${service}] 构建镜像: ${image_name}:latest${NC}"
    
    # 并行构建时使用临时文件存储输出，避免输出混乱
    # 构建完成后显示关键信息
    local log_file="${LOG_PATH}/docker-build-${service}-$$.log"
    local exit_code=0
    
    # 使用 BuildKit 启用缓存挂载等高级特性
    DOCKER_BUILDKIT=1 docker build --progress=plain -f "$dockerfile_path" -t "${image_name}:latest" . > "$log_file" 2>&1 || exit_code=$?
    
    if [ $exit_code -eq 0 ]; then
        # 构建成功，显示最后的关键信息
        echo -e "${GREEN}[${service}] 构建成功，最后步骤:${NC}"
        tail -5 "$log_file" | grep -E "(Step|Successfully|built|tagged)" | sed "s/^/[${service}] /" || tail -3 "$log_file" | sed "s/^/[${service}] /"
#        rm -f "$log_file"
        echo -e "${GREEN}[$(date '+%H:%M:%S')] ✓ 服务 ${service} 构建成功${NC}"
        return 0
    else
        # 构建失败，显示错误信息
        echo -e "${RED}[${service}] 构建失败，错误信息:${NC}"
        tail -30 "$log_file" | sed "s/^/[${service}] /"
#        rm -f "$log_file"
        echo -e "${RED}[$(date '+%H:%M:%S')] ✗ 服务 ${service} 构建失败${NC}"
        return 1
    fi
}

# 构建所有服务（并行执行）
build_all() {
    echo -e "${GREEN}开始并行构建所有服务...${NC}"
    echo -e "${YELLOW}服务列表: ${SERVICES[*]}${NC}"
    echo ""
    
    # 存储所有后台任务的 PID
    local pids=()
    local service_names=()
    local failed_services=()
    
    # 启动所有服务的并行构建
    for service in "${SERVICES[@]}"; do
        build_service "$service" &
        local pid=$!
        pids+=($pid)
        service_names+=("$service")
        echo -e "${YELLOW}启动后台构建任务 [PID: $pid] - ${service}${NC}"
    done
    
    echo ""
    echo -e "${GREEN}等待所有构建任务完成...${NC}"
    echo ""
    
    # 等待所有后台任务完成，并收集退出状态
    local index=0
    for pid in "${pids[@]}"; do
        local service="${service_names[$index]}"
        if wait $pid; then
            echo -e "${GREEN}✓ [PID: $pid] ${service} 构建成功${NC}"
        else
            echo -e "${RED}✗ [PID: $pid] ${service} 构建失败${NC}"
            failed_services+=("$service")
        fi
        ((index++))
    done
    
    echo ""
    # 汇总结果
    if [ ${#failed_services[@]} -eq 0 ]; then
        echo -e "${GREEN}========================================${NC}"
        echo -e "${GREEN}所有服务构建完成! (共 ${#SERVICES[@]} 个服务)${NC}"
        echo -e "${GREEN}========================================${NC}"
        return 0
    else
        echo -e "${RED}========================================${NC}"
        echo -e "${RED}构建失败的服务: ${failed_services[*]}${NC}"
        echo -e "${RED}成功: $((${#SERVICES[@]} - ${#failed_services[@]})) 个, 失败: ${#failed_services[@]} 个${NC}"
        echo -e "${RED}========================================${NC}"
        return 1
    fi
}

# 主逻辑
if [ $# -eq 0 ]; then
    # 没有参数，并行构建所有服务
    if build_all; then
        exit 0
    else
        exit 1
    fi
else
    # 构建指定服务（串行执行）
    set -e
    build_service "$1"
fi

