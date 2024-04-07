FROM maven:3.6.0-jdk-8-slim as build

# 指定构建过程中的工作目录

# 将src目录下所有文件，拷贝到工作目录中src目录下（.gitignore/.dockerignore中文件除外）
# 将pom.xml文件，拷贝到工作目录下
COPY docker/compose/backend/settings.xml /build/

COPY . /build/backend


RUN mvn -s /build/settings.xml -f /build/backend/pom.xml clean package



# 执行代码编译命令
# 自定义settings.xml, 选用国内镜像源以提高下载速度aaaas