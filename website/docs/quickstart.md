---
id: quickstart
title: 快速开始
sidebar_label: 快速开始
---

## 安装

### 通过发行版安装

目前已适配 mysql 数据库，理论上支持所有的关系型数据库


点击[下载 surveyking-mysql 版本](https://gitee.com/surveyking/surveyking)到本地

### 使用源码编译安装

默认构建的是 h2 版本的安装包。

使用 gradle 构建：

```bash
# 下载源码
git clone https://gitee.com/surveyking/surveyking.git

# 设置 profile，修改 api/src/main/resources/application.yml
# 打开 active: ${activeProfile} # gradle 配置

# 开始构建
gradle clean :api:build -P pro -x test
# 生成的 jar 包位于 ./api/build/libs/surveyking-v0.x.x.jar
```

使用 maven 构建：

```bash
# 下载源码
git clone https://gitee.com/surveyking/surveyking.git

# 开始构建
mvn clean package -DskipTests -Ppro
# 生成的 jar 包位于 ./api/target/surveyking-v0.x.x.jar
```

### 使用 docker 快速启动

启动 SurveyKing 镜像时，你可以指定 SurveyKing 挂载参数，将日志文件和内置数据库保存到你本地。(docker 版本目前还有点问题，待解决）

```bash
docker run -p 1991:1991 surveyking/surveyking
# 挂载数据文件
docker run -d -p 1991:1991 -v /my/logs:/files -v /my/logs:/logs
```

## 使用

- **预安装 JRE 环境**，由于本系统是 Java 构建的，需要依赖 Java 运行环境，可以通过 [适用于所有操作系统的 Java 下载
  ](https://www.java.com/zh-CN/download/manual.jsp) 来预装 java 环境。
- **配置数据库**，按照下面的说明来配置不同的数据库，如果前端需要使用 nginx 部署，参考使用 nginx 部署前端。
- **运行**，支持所有平台部署，windows 和 mac 支持双击运行，或者打开命令行窗口执行如下命令

```bash
java -jar surveyking-v0.x.x.jar
```

打开浏览器，访问 [http://localhost:1991](http://localhost:1991) 即可，系统首次启动之后会自动创建 admin 用户，账号/密码（*admin/123456*），登录系统之后可以通过用户管理界面来修改密码。

<!-- ### h2 启动方式

无需任何配置，会自动创建数据库启动脚本，如需改变端口号，参考 mysql 启动方式的定义端口。 -->

### mysql 启动方式

使用参数启动

1. 首先创建 mysql 数据库，然后执行初始化脚本，点击 [快速开始-获取最新数据库脚本](https://wj.surveyking.cn/s/start))。
2. 执行 `java -jar surveyking-v0.x.x.jar --server.port=1991 --spring.datasource.url=jdbc:mysql://localhost:3306/surveyking --spring.datasource.username=root --spring.datasource.password=123456`（只有首次启动系统需要添加后面的参数）

参数说明(按照实际需要自行修改)：

- `--server.port=1991` 系统端口
- `--spring.datasource.url=jdbc:mysql://localhost:3306/surveyking` 数据库连接的 url
- `--spring.datasource.username=root` 数据库账号
- `--spring.datasource.password=123456` 数据库密码

也可以尝试使用命令行的方式初始化数据库（会自动执行数据库初始脚本）

```bash
# 按照提示初始化数据库
java -jar surveyking-v0.x.x.jar i
# 初始化完成之后运行即可
java -jar surveyking-v0.x.x.jar 
```

### 使用 nginx 部署前端

下载 [该目录下面的静态资源文件](https://gitee.com/surveyking/surveyking/tree/master/server/api/src/main/resources/static)，直接部署到 nginx 即可。

然后配置 proxy 代理到后端 api 服务。

## 在线体验

演示地址： [https://surveyking.cn](https://surveyking.cn)

点击 *试一试*，无需注册登录

## 联系作者

本项目后端代码完全开源，前端代码开源版本正在开发，即将发布。如果觉得对您有帮助，可以点击右上角的 star。

如果遇到任何问题或者建议，欢迎加群讨论。

<div>
 QQ群：1074277968
</div>