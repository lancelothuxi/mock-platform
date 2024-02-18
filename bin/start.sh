#!/bin/bash
echo "compile and run in docker"
cd ..
mvn clean install && cd ./backend/ruoyi-admin && mvn docker:build && mvn docker:run