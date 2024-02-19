#!/bin/sh
cd ..
baseDir=`pwd`
# 要判断的端口号
port=8080
# 检查端口是否被占用
if netstat -an | grep LISTEN | grep $port > /dev/null; then
  echo "端口 $port 已被占用"
  exit 1
fi

echo "compile and run in docker"
mvn clean install
cd $baseDir/backend/ruoyi-admin
echo `pwd`
sh -c "(mvn docker:build && mvn docker:run) &"

# 等待端口被监听
while ! netstat -an | grep LISTEN | grep $port > /dev/null; do
  sleep 1
done

cd $baseDir/frontend
echo "pnpm install"
mvn com.github.eirslett:frontend-maven-plugin:1.12.1:npx@install-frontend-dependencies

echo "pnpm dev"
mvn com.github.eirslett:frontend-maven-plugin:1.12.1:npx@run-dev