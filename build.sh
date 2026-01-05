#!/bin/bash

# Docker 构建脚本
# 使用方法: ./build.sh [app|web]

set -e
WEB_DIR=~/project/interview/interview-web
APP_DIR=~/project/interview/interview-app

buildApp(){
  cd ${APP_DIR}
  bash build-app.sh
}

buildWeb(){
  cd ${WEB_DIR}
  bash build-web.sh
}

if [ $# -eq 0 ]; then
  sh deploy.sh stop
  buildApp
  buildWeb
else [ $# -eq 1 ]
    case $1 in
       app)
         bash deploy.sh stop app
         buildApp
         ;;
       web)
         bash deploy.sh stop web
         buildWeb
         ;;
       *)
         echo "Usage: $0 [app|web]"
         exit 1
         ;;
     esac
fi
