#!/bin/bash

# Interview App 模块管理脚本
# 用于管理项目的各个模块，包括创建、删除、配置等

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_step() {
    echo -e "${PURPLE}[STEP]${NC} $1"
}

print_header() {
    echo -e "${CYAN}================================${NC}"
    echo -e "${CYAN}  $1${NC}"
    echo -e "${CYAN}================================${NC}"
}

# 项目配置
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# shellcheck disable=SC2034
PROJECT_NAME="interview-app"

# 模块模板配置
declare -A MODULE_TEMPLATES=(
    ["spring-boot"]="Spring Boot 应用模块"
    ["spring-boot-starter"]="Spring Boot Starter 模块"
    ["spring-boot-autoconfigure"]="Spring Boot 自动配置模块"
    ["common"]="通用工具模块"
    ["api"]="API 模块"
    ["web"]="Web 模块"
)

# 显示帮助信息
show_help() {
    echo "用法: $0 [命令] [选项]"
    echo ""
    echo "命令:"
    echo "  create <模块名> <模板>    创建新模块"
    echo "  delete <模块名>           删除模块"
    echo "  list                     列出所有模块"
    echo "  info <模块名>            显示模块信息"
    echo "  build <模块名>           构建指定模块"
    echo "  run <模块名>             运行指定模块"
    echo "  test <模块名>            测试指定模块"
    echo "  clean <模块名>           清理指定模块"
    echo "  docker <模块名>          构建模块的 Docker 镜像"
    echo "  help                     显示此帮助信息"
    echo ""
    echo "模板类型:"
    for template in "${!MODULE_TEMPLATES[@]}"; do
        echo "  $template - ${MODULE_TEMPLATES[$template]}"
    done
    echo ""
    echo "示例:"
    echo "  $0 create interview-app-realtime spring-boot"
    echo "  $0 list"
    echo "  $0 build interview-app-mvc"
    echo "  $0 docker interview-app-mvc"
}

# 创建新模块
create_module() {
    local module_name=$1
    local template=$2
    
    if [[ -z "$module_name" || -z "$template" ]]; then
        print_error "请提供模块名和模板类型"
        echo "用法: $0 create <模块名> <模板>"
        exit 1
    fi
    
    if [[ ! -d "$PROJECT_ROOT" ]]; then
        print_error "项目根目录不存在: $PROJECT_ROOT"
        exit 1
    fi
    
    if [[ -d "$PROJECT_ROOT/$module_name" ]]; then
        print_error "模块 $module_name 已存在"
        exit 1
    fi
    
    if [[ -z "${MODULE_TEMPLATES[$template]}" ]]; then
        print_error "未知的模板类型: $template"
        echo "可用模板: ${!MODULE_TEMPLATES[*]}"
        exit 1
    fi
    
    print_header "创建新模块: $module_name"
    print_info "模板类型: $template (${MODULE_TEMPLATES[$template]})"
    
    # 创建模块目录
    print_step "创建模块目录结构..."
    mkdir -p "$PROJECT_ROOT/$module_name/src/main/java"
    mkdir -p "$PROJECT_ROOT/$module_name/src/main/resources"
    mkdir -p "$PROJECT_ROOT/$module_name/src/test/java"
    mkdir -p "$PROJECT_ROOT/$module_name/src/test/resources"
    
    # 创建 POM 文件
    print_step "创建 Maven POM 文件..."
    create_module_pom "$module_name" "$template"
    
    # 创建主类
    print_step "创建主类..."
    create_main_class "$module_name" "$template"
    
    # 创建配置文件
    print_step "创建配置文件..."
    create_config_files "$module_name" "$template"
    
    # 创建 Dockerfile（如果适用）
    if [[ "$template" == "spring-boot" ]]; then
        print_step "创建 Dockerfile..."
        create_dockerfile "$module_name"
    fi
    
    # 创建 README
    print_step "创建 README 文件..."
    create_readme "$module_name" "$template"
    
    # 更新父项目 POM
    print_step "更新父项目 POM..."
    update_parent_pom "$module_name"
    
    print_success "模块 $module_name 创建完成！"
    echo ""
    echo "下一步操作:"
    echo "  1. 进入模块目录: cd $module_name"
    echo "  2. 编辑代码和配置"
    echo "  3. 构建模块: $0 build $module_name"
    echo "  4. 运行模块: $0 run $module_name"
}

# 创建模块的 POM 文件
create_module_pom() {
    local module_name=$1
    local template=$2
    
    local pom_content=""
    
    case "$template" in
        "spring-boot")
            pom_content=$(cat <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>cn.minglg.interview</groupId>
        <artifactId>interview-app</artifactId>
        <version>1.0.0</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>$module_name</artifactId>
    <name>$module_name</name>
    <description>AI模拟面试系统 - ${template^} 模块</description>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
EOF
)
            ;;
        "spring-boot-starter")
            pom_content=$(cat <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>cn.minglg.interview</groupId>
        <artifactId>interview-app-starter</artifactId>
        <version>1.0.0</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>$module_name</artifactId>
    <name>$module_name</name>
    <description>AI模拟面试系统 - ${template^} 模块</description>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>cn.minglg.interview</groupId>
            <artifactId>${module_name}-autoconfigure</artifactId>
            <version>\${project.version}</version>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
EOF
)
            ;;
        *)
            pom_content=$(cat <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>cn.minglg.interview</groupId>
        <artifactId>interview-app</artifactId>
        <version>1.0.0</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>$module_name</artifactId>
    <name>$module_name</name>
    <description>AI模拟面试系统 - ${template^} 模块</description>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
EOF
)
            ;;
    esac
    
    echo "$pom_content" > "$PROJECT_ROOT/$module_name/pom.xml"
}

# 创建主类
create_main_class() {
    local module_name=$1
    local template=$2
    
    local package_path="cn/minglg/interview"
    local class_name=""
    local class_content=""
    
    case "$template" in
        "spring-boot")
            class_name="${module_name^}"
            class_content=$(cat <<EOF
package cn.minglg.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ${class_name} 应用主类
 * 
 * @author Interview App Team
 * @since 1.0.0
 */
@SpringBootApplication
public class ${class_name}Application {

    public static void main(String[] args) {
        SpringApplication.run(${class_name}Application.class, args);
    }
}
EOF
)
            ;;
        *)
            class_name="${module_name^}"
            class_content=$(cat <<EOF
package cn.minglg.interview;

/**
 * ${class_name} 模块主类
 * 
 * @author Interview App Team
 * @since 1.0.0
 */
public class ${class_name} {

    public static void main(String[] args) {
        System.out.println("${class_name} 模块启动成功！");
    }
}
EOF
)
            ;;
    esac
    
    mkdir -p "$PROJECT_ROOT/$module_name/src/main/java/$package_path"
    echo "$class_content" > "$PROJECT_ROOT/$module_name/src/main/java/$package_path/${class_name}Application.java"
}

# 创建配置文件
create_config_files() {
    local module_name=$1
    local template=$2
    
    case "$template" in
        "spring-boot")
            # 创建 application.yml
            # shellcheck disable=SC2155
            local app_yml=$(cat <<EOF
# ${module_name} 模块配置
server:
  port: 8080

spring:
  application:
    name: ${module_name}
  
  profiles:
    active: dev

logging:
  level:
    cn.minglg.interview: DEBUG
EOF
)
            echo "$app_yml" > "$PROJECT_ROOT/$module_name/src/main/resources/application.yml"
            
            # 创建 application-dev.yml
            # shellcheck disable=SC2155
            local dev_yml=$(cat <<EOF
# 开发环境配置
spring:
  config:
    activate:
      on-profile: dev

logging:
  level:
    root: INFO
    cn.minglg.interview: DEBUG
EOF
)
            echo "$dev_yml" > "$PROJECT_ROOT/$module_name/src/main/resources/application-dev.yml"
            ;;
    esac
}

# 创建 Dockerfile
create_dockerfile() {
    local module_name=$1
    
    # shellcheck disable=SC2155
    local dockerfile_content=$(cat <<EOF
# ----------- 构建阶段 -----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app

# 配置Maven镜像源，加速依赖下载
RUN mkdir -p /root/.m2 && \\
    echo '<?xml version="1.0" encoding="UTF-8"?>' > /root/.m2/settings.xml && \\
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' >> /root/.m2/settings.xml && \\
    echo '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' >> /root/.m2/settings.xml && \\
    echo '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0' >> /root/.m2/settings.xml && \\
    echo '                          http://maven.apache.org/xsd/settings-1.0.0.xsd">' >> /root/.m2/settings.xml && \\
    echo '    <mirrors>' >> /root/.m2/settings.xml && \\
    echo '        <mirror>' >> /root/.m2/settings.xml && \\
    echo '            <id>aliyunmaven</id>' >> /root/.m2/settings.xml && \\
    echo '            <mirrorOf>*</mirrorOf>' >> /root/.m2/settings.xml && \\
    echo '            <name>阿里云公共仓库</name>' >> /root/.m2/settings.xml && \\
    echo '            <url>https://maven.aliyun.com/repository/public</url>' >> /root/.m2/settings.xml && \\
    echo '        </mirror>' >> /root/.m2/settings.xml && \\
    echo '    </mirrors>' >> /root/.m2/settings.xml && \\
    echo '</settings>' >> /root/.m2/settings.xml

# 复制整个项目结构
COPY . .

# 构建整个项目
RUN mvn clean install -Dmaven.test.skip=true

# ----------- 运行阶段 -----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 安装必要的运行时依赖
RUN apk add --no-cache tzdata && \\
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \\
    echo "Asia/Shanghai" > /etc/timezone && \\
    apk del tzdata

# 创建非root用户
RUN addgroup -g 1001 -S appgroup && \\
    adduser -u 1001 -S appuser -G appgroup

# 只复制最终jar包
COPY --from=builder /app/${module_name}/target/*.jar app.jar

# 设置文件权限
RUN chown -R appuser:appgroup /app

# 切换到非root用户
USER appuser

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \\
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM参数优化
ENTRYPOINT ["java", \\
    "-server", \\
    "-Xms512m", \\
    "-Xmx1024m", \\
    "-XX:+UseG1GC", \\
    "-XX:MaxGCPauseMillis=200", \\
    "-XX:+UnlockExperimentalVMOptions", \\
    "-XX:+UseContainerSupport", \\
    "-XX:MaxRAMPercentage=75.0", \\
    "-Djava.security.egd=file:/dev/./urandom", \\
    "-Dspring.profiles.active=docker", \\
    "-jar", "app.jar"]
EOF
)
    
    echo "$dockerfile_content" > "$PROJECT_ROOT/$module_name/Dockerfile"
}

# 创建 README
create_readme() {
    local module_name=$1
    local template=$2
    
    # shellcheck disable=SC2155
    local readme_content=$(cat <<EOF
# ${module_name}

AI模拟面试系统 - ${template^} 模块

## 功能描述

[在此描述模块的主要功能]

## 技术栈

- Spring Boot 3.x
- Java 17
- Maven

## 快速开始

### 构建模块

\`\`\`bash
mvn clean install
\`\`\`

### 运行模块

\`\`\`bash
mvn spring-boot:run
\`\`\`

### Docker 构建

\`\`\`bash
docker build -t ${module_name}:latest .
\`\`\`

## 配置说明

[在此描述模块的配置项]

## API 文档

[在此描述模块的 API 接口]

## 开发指南

[在此描述开发相关的信息]

## 许可证

本项目采用 [LICENSE](../LICENSE) 许可证。
EOF
)
    
    echo "$readme_content" > "$PROJECT_ROOT/$module_name/README.md"
}

# 更新父项目 POM
update_parent_pom() {
    local module_name=$1
    
    local parent_pom="$PROJECT_ROOT/pom.xml"
    if [[ ! -f "$parent_pom" ]]; then
        print_warning "父项目 POM 文件不存在，跳过更新"
        return 0
    fi
    
    # 检查模块是否已经在 modules 中
    if grep -q "<module>$module_name</module>" "$parent_pom"; then
        print_info "模块 $module_name 已在父项目 POM 中"
        return 0
    fi
    
    # 在 modules 标签中添加新模块
    # shellcheck disable=SC2155
    local temp_file=$(mktemp)
    sed "/<modules>/a\        <module>$module_name</module>" "$parent_pom" > "$temp_file"
    mv "$temp_file" "$parent_pom"
    
    print_success "已更新父项目 POM，添加模块 $module_name"
}

# 列出所有模块
list_modules() {
    print_header "项目模块列表"
    echo ""
    
    local found_modules=false
    
    for item in "$PROJECT_ROOT"/*; do
        if [[ -d "$item" ]] && [[ -f "$item/pom.xml" ]]; then
            # shellcheck disable=SC2155
            local module_name=$(basename "$item")
            local module_type=""
            
            # 判断模块类型
            if [[ -f "$item/Dockerfile" ]]; then
                module_type="Docker 模块"
            elif [[ "$module_name" == *"starter"* ]]; then
                module_type="Starter 模块"
            elif [[ "$module_name" == *"autoconfigure"* ]]; then
                module_type="AutoConfigure 模块"
            else
                module_type="应用模块"
            fi
            
            echo -e "${GREEN}✓${NC} $module_name - $module_type"
            found_modules=true
        fi
    done
    
    if [[ "$found_modules" == "false" ]]; then
        echo "未找到任何模块"
    fi
    
    echo ""
}

# 显示模块信息
show_module_info() {
    local module_name=$1
    
    if [[ -z "$module_name" ]]; then
        print_error "请提供模块名"
        echo "用法: $0 info <模块名>"
        exit 1
    fi
    
    local module_dir="$PROJECT_ROOT/$module_name"
    if [[ ! -d "$module_dir" ]]; then
        print_error "模块 $module_name 不存在"
        exit 1
    fi
    
    print_header "模块信息: $module_name"
    echo ""
    
    # 基本信息
    echo "模块路径: $module_dir"
    echo "模块类型: $(get_module_type "$module_name")"
    echo ""
    
    # 检查文件
    echo "文件结构:"
    if [[ -f "$module_dir/pom.xml" ]]; then
        echo -e "  ${GREEN}✓${NC} pom.xml"
    else
        echo -e "  ${RED}✗${NC} pom.xml"
    fi
    
    if [[ -f "$module_dir/Dockerfile" ]]; then
        echo -e "  ${GREEN}✓${NC} Dockerfile"
    else
        echo -e "  ${YELLOW}○${NC} Dockerfile"
    fi
    
    if [[ -f "$module_dir/README.md" ]]; then
        echo -e "  ${GREEN}✓${NC} README.md"
    else
        echo -e "  ${YELLOW}○${NC} README.md"
    fi
    
    echo ""
    
    # 构建状态
    if [[ -d "$module_dir/target" ]]; then
        echo "构建状态: ${GREEN}已构建${NC}"
    else
        echo "构建状态: ${YELLOW}未构建${NC}"
    fi
}

# 获取模块类型
get_module_type() {
    local module_name=$1
    local module_dir="$PROJECT_ROOT/$module_name"
    
    if [[ -f "$module_dir/Dockerfile" ]]; then
        echo "Docker 模块"
    elif [[ "$module_name" == *"starter"* ]]; then
        echo "Starter 模块"
    elif [[ "$module_name" == *"autoconfigure"* ]]; then
        echo "AutoConfigure 模块"
    else
        echo "应用模块"
    fi
}

# 构建指定模块
build_module() {
    local module_name=$1
    
    if [[ -z "$module_name" ]]; then
        print_error "请提供模块名"
        echo "用法: $0 build <模块名>"
        exit 1
    fi
    
    local module_dir="$PROJECT_ROOT/$module_name"
    if [[ ! -d "$module_dir" ]]; then
        print_error "模块 $module_name 不存在"
        exit 1
    fi
    
    print_header "构建模块: $module_name"
    
    cd "$module_dir"
    if mvn clean install -Dmaven.test.skip=true; then
        print_success "模块 $module_name 构建成功"
    else
        print_error "模块 $module_name 构建失败"
        exit 1
    fi
    cd "$PROJECT_ROOT"
}

# 运行指定模块
run_module() {
    local module_name=$1
    
    if [[ -z "$module_name" ]]; then
        print_error "请提供模块名"
        echo "用法: $0 run <模块名>"
        exit 1
    fi
    
    local module_dir="$PROJECT_ROOT/$module_name"
    if [[ ! -d "$module_dir" ]]; then
        print_error "模块 $module_name 不存在"
        exit 1
    fi
    
    print_header "运行模块: $module_name"
    
    cd "$module_dir"
    print_info "使用 Spring Boot Maven 插件运行模块..."
    mvn spring-boot:run
}

# 测试指定模块
test_module() {
    local module_name=$1
    
    if [[ -z "$module_name" ]]; then
        print_error "请提供模块名"
        echo "用法: $0 test <模块名>"
        exit 1
    fi
    
    local module_dir="$PROJECT_ROOT/$module_name"
    if [[ ! -d "$module_dir" ]]; then
        print_error "模块 $module_name 不存在"
        exit 1
    fi
    
    print_header "测试模块: $module_name"
    
    cd "$module_dir"
    if mvn test; then
        print_success "模块 $module_name 测试通过"
    else
        print_error "模块 $module_name 测试失败"
        exit 1
    fi
    cd "$PROJECT_ROOT"
}

# 清理指定模块
clean_module() {
    local module_name=$1
    
    if [[ -z "$module_name" ]]; then
        print_error "请提供模块名"
        echo "用法: $0 clean <模块名>"
        exit 1
    fi
    
    local module_dir="$PROJECT_ROOT/$module_name"
    if [[ ! -d "$module_dir" ]]; then
        print_error "模块 $module_name 不存在"
        exit 1
    fi
    
    print_header "清理模块: $module_name"
    
    cd "$module_dir"
    if mvn clean; then
        print_success "模块 $module_name 清理完成"
    else
        print_error "模块 $module_name 清理失败"
        exit 1
    fi
    cd "$PROJECT_ROOT"
}

# 构建模块的 Docker 镜像
docker_module() {
    local module_name=$1
    
    if [[ -z "$module_name" ]]; then
        print_error "请提供模块名"
        echo "用法: $0 docker <模块名>"
        exit 1
    fi
    
    local module_dir="$PROJECT_ROOT/$module_name"
    if [[ ! -d "$module_dir" ]]; then
        print_error "模块 $module_name 不存在"
        exit 1
    fi
    
    if [[ ! -f "$module_dir/Dockerfile" ]]; then
        print_error "模块 $module_name 没有 Dockerfile"
        exit 1
    fi
    
    print_header "构建 Docker 镜像: $module_name"
    
    # 使用根目录作为构建上下文
    if docker build -t "${module_name}:latest" -f "$module_dir/Dockerfile" .; then
        print_success "Docker 镜像 $module_name 构建成功"
    else
        print_error "Docker 镜像 $module_name 构建失败"
        exit 1
    fi
}

# 主函数
main() {
    local command=$1
    shift
    
    case "$command" in
        "create")
            create_module "$@"
            ;;
        "delete")
            print_warning "删除模块功能暂未实现"
            ;;
        "list")
            list_modules
            ;;
        "info")
            show_module_info "$@"
            ;;
        "build")
            build_module "$@"
            ;;
        "run")
            run_module "$@"
            ;;
        "test")
            test_module "$@"
            ;;
        "clean")
            clean_module "$@"
            ;;
        "docker")
            docker_module "$@"
            ;;
        "help"|"-h"|"--help"|"")
            show_help
            ;;
        *)
            print_error "未知命令: $command"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@" 