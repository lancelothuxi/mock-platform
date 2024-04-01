FROM maven:3.6.0-jdk-8-slim as build

# 指定构建过程中的工作目录
WORKDIR ../../../

# 将src目录下所有文件，拷贝到工作目录中src目录下（.gitignore/.dockerignore中文件除外）

# 将pom.xml文件，拷贝到工作目录下
COPY settings.xml pom.xml /tmp/

# 执行代码编译命令
# 自定义settings.xml, 选用国内镜像源以提高下载速度
RUN mvn -s -f /tmp/pom.xml clean package