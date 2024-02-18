#!/bin/bash
dir=`pwd`

echo "compile and run in docker"
cd $dir/..
mvn clean install && cd $dir/../backend/ruoyi-admin && mvn docker:build && mvn docker:run


echo "pnpm install"
mvn com.github.eirslett:frontend-maven-plugin:1.12.1:npx@install-frontend-dependencies

echo "pnpm dev"
mvn com.github.eirslett:frontend-maven-plugin:1.12.1:npx@run-dev