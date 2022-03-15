---
slug: docker-deploy
title: 使用 docker 构建部署卷王
author: 米粥
---
# 通过 Docker 部署

## 准备工作

### 新建配置文件

在根目录新建配置文件 application.properties，内容如下。其中我们将数据库的连接配置采用从环境变量中获取。

```ini
spring.application.name=SurveyKing
server.port=1991
spring.datasource.url=jdbc:mysql://${MYSQL_ADDRESS}/${MYSQL_DATABASE}
spring.datasource.username=${MYSQL_USERNAME}
spring.datasource.password=${MYSQL_PASSWORD}
```

### 新建 Dockerfile

在根目录新建 Dockerfile 文件，内容如下：

```dockerfile
FROM maven:3.6.0-jdk-8-slim as build

ENV WEB_PATH /home/surveyking

COPY ./server $WEB_PATH
WORKDIR $WEB_PATH

# 这步是构建镜像的时候编译代码
# 如果在构建时编译时间过长，也可以将此命令注释，在本机进行构建

RUN mvn clean package -DskipTests -Ppro

FROM alpine

RUN apk add --update --no-cache openjdk8 \
    && rm -f /var/cache/apk/*

ENV WEB_PATH /home/surveyking
WORKDIR $WEB_PATH
COPY . $WEB_PATH

# 此命令是将上一个静像编译好的目标文件复制到我们的工作目录
# 如果你编译是在本机进行，此命令也一同注释即可
COPY --from=build /home/surveyking/api/target/ $WEB_PATH/server/api/target

# 这里要注意的是，运行的 surveyking-v0.3.0-beta.7.jar 包，要根据当前编译后的版本号来修改启动命令

RUN echo '#!/bin/sh' >> start.sh \
    && echo "java -jar ./server/api/target/surveyking-v0.3.0-beta.7.jar" >> start.sh

CMD ["sh", "start.sh"]
```

文件准备好后，执行构建静像操作

```shell
sudo docker build -t surveyking/server .
```

## 导入数据

创建 mysql 数据库，然后执行初始化脚本，[下载脚本](https://gitee.com/surveyking/surveyking/raw/master/server/rdbms/src/main/resources/scripts/init-mysql.sql)。

## 启动容器

命令如下，其实中 80 端口为对外暴露的端口。

MYSQL_ADDRESS、MYSQL_DATABASE、MYSQL_USERNAME、MYSQL_PASSWORD 为刚才导入数据时数据的一些权限，这里采用通过环境变量的形式注入。

> 其中数据库 surveyking_app_table 数据库要提前创建好，并把上面的sql数据导入

```shell
sudo docker run -it -d --name surveyking-app -p 8877:1991 -e MYSQL_PASSWORD=123456 -e MYSQL_USERNAME=root -e MYSQL_DATABASE=surveyking_app_table -e MYSQL_ADDRESS=172.16.1.13:3306 surveyking/server
```

执行命令后，通过 http://127.0.0.1:8877 访问。