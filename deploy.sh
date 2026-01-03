#!/bin/bash

# Docker 部署脚本
# 使用方法: ./deploy.sh [dev|prod]

set -e
WEB_DIR=~/project/interview/interview-web
APP_DIR=~/project/interview/interview-app

stopApp(){
  cd ${APP_DIR}
  docker-compose down
}

stopWeb(){
  cd ${WEB_DIR}
  docker-compose down
}

startApp(){
  cd ${APP_DIR}
  docker-compose up -d
}

startWeb(){
  cd ${WEB_DIR}
  docker-compose up -d
}

restartApp () {
  cd ${APP_DIR}
  stopApp && startApp
}

restartWeb () {
  cd ${WEB_DIR}
  stopWeb && startWeb
}

if [ $# -eq 0 ]; then
  restartApp
  restartWeb
elif [ $# -eq 1 ]; then
    case $1 in
       start)
         startApp &&  startWeb
         ;;
       stop)
         stopApp && stopWeb
         ;;
       *)
         echo "Usage: $0 [start|stop]"
         exit 1
         ;;
     esac
else
  local func=$1
  local options=$2
  $(${func}${options^})

fi
