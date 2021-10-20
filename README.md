
# 卷王

简体中文 | [English](./README.en-us.md)

![preview-surveyking](./docs/preview.gif)

[BMI 计算器问卷](https://surveyking.cn/s/q443is)

## 背景

基于阿里开源的 [formily](https://github.com/alibaba/formily) 表单解决方案自研问卷编辑器，使用最新的前后端技术（React+SpringBoot+AntDesignUI），构建出一套完整的调查问卷系统。

卷王是已知开源调查问卷系统中**功能最强大**、**安装最简单**、**综合体验最好**的一个，一些功能特性及体验可以对标甚至超过问卷网、问卷星、腾讯问卷这些国内主流的在线表单系统。

**简单**、**易用**、**专业**，是卷王的持续开发理念。大中小企业甚至个人都可以使用卷王快速搭建一个属于自己的在线问卷系统。

## 特性

- 🥇 支持多种题型，如填空、选择、下拉、级联、矩阵、分页、签名、题组等
- 🚀 可计算，分为**逻辑计算**和**字段计算**，如根据身份证号计算年龄和性别、根据选项值控制另外一个问题显示隐藏
- 🦋 问题、选项支持快速富文本编辑
- 🥊 响应式布局，所有页面在 PC 和手机端都有良好的操作体验
- 🥂 多种问卷设置，如支持暂存、问卷修改、设置密码等
- 🎇 数据，支持问卷数据新增、编辑、标记、导出、预览和打包下载附件
- 🎨 报表，支持对问题实时统计分析并以图形（条形图、柱形图、扇形图）、表格的形式展示输出
- 🎉 安装部署简单（**最快 1 分钟部署**），支持 nginx 部署和一键启动的方式
- 🎁 后端支持多种数据库（内嵌 h2），可支持所有带有 jdbc 驱动的关系型数据库，未来将支持 mongodb 这种文档数据库
- 🐯 安全、可靠、稳定、高性能的后端 API 服务
- 🙆 支持完善的 RBAC 权限控制（后续将推出工作流）
- 🌈 支持国际化（正在完善）
- ...

## 安装

### 通过发行版安装

目前已适配 h2 数据库和 mysql 数据库

点击[下载 surveyking-h2 版本](https://gitee.com/surveyking/surveyking/attach_files/857675/download/surveyking-h2-v0.1.0.jar)到本地

点击[下载 surveyking-mysql 版本](https://gitee.com/surveyking/surveyking/attach_files/857674/download/surveyking-v0.1.0.jar)到本地

### 使用源码编译安装

默认构建的是 h2 版本的安装包。

```bash
# 下载源码
git clone https://gitee.com/surveyking/surveyking.git

# 开始构建
gradle clean build -P pro -x test

# 开始运行
java -jar api/build/libs/surveyking-v0.1.0.jar
```

### 使用 docker 快速启动

启动 SurveyKing 镜像时，你可以指定 SurveyKing 挂载参数，将日志文件和内置数据库保存到你本地。(docker 版本目前还有点问题，待解决）

```bash
docker run -p 1991:1991 surveyking/surveyking
# 挂载数据文件
docker run -p 1991:1991 -v /surveyking:/surveyking surveyking/surveyking
```

## 使用

- **预安装 JRE 环境**，由于本系统是 Java 构建的，需要依赖 Java 运行环境，可以通过 [适用于所有操作系统的 Java 下载
](https://www.java.com/zh-CN/download/manual.jsp) 来预装 java 环境。
- **配置数据库**，按照下面的说明来配置不同的数据库，如果前端需要使用 nginx 部署，参考使用 nginx 部署前端。
- **运行**，支持所有平台部署，windows 和 mac 支持双击运行，或者打开命令行窗口执行如下命令

```bash
java -jar surveyking-v0.1.0.jar
```

打开浏览器，访问 <http://localhost:1991> 即可，系统首次启动之后会自动创建 admin 用户，账号/密码（*admin/surveyking*），登录系统之后可以通过用户管理界面来修改密码。

### h2 启动方式

无需任何配置，会自动创建数据库启动脚本，如需改变端口号，参考 mysql 启动方式的定义端口。

### mysql 启动方式

1. 首先创建 mysql 数据库，然后执行初始化脚本，[下载脚本](https://gitee.com/surveyking/surveyking/blob/master/rdbms/src/main/resources/scripts/init-mysql.sql)。
2. 然后在当前目录下面新建 `application.properties` 文件。

  ```properties
  server.port=1991 # 端口号（可选，默认 1991）
  spring.datasource.url=jdbc:mysql://<ip>:<port>/<dbname> # 数据库连接地址，替换为你的 mysql 数据库地址
  spring.datasource.username=username # 数据库账号（必填）
  spring.datasource.password=password # 数据库密码（必填）
  ```

### 使用 nginx 部署前端

下载 [该目录下面的静态资源文件](https://gitee.com/surveyking/surveyking/tree/master/api/src/main/resources/static)，直接部署到 nginx 即可。

然后配置 proxy 代理到后端 api 服务。

## 在线体验

演示地址： <https://surveyking.cn>

点击 *试一试*，无需注册登录（由于作者比较穷 -_-||，服务器带宽只有1M，首次加载稍慢）

## 联系作者

本项目是作者个人基于兴趣驱动，无任何利益驱使开发的，后端代码完全开源，前端代码待稳定之后也会有开源计划。如果觉得对您有帮助，可以点击右上角的 star。

如果遇到任何问题或者建议，欢迎加群讨论。

![contact-me](./docs/wechat.jpeg)

## 开源协议

SurveyKing is open source software licensed as
[MIT.](https://github.com/javahuang/SurveyKing/blob/master/LICENSE)