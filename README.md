# 卷王

简体中文 | [English](./README.en-us.md)

功能更强大的调查问卷、考试系统，很多功能体验超过问卷网、问卷星。

需要您的 star ⭐️⭐️⭐️ 支持鼓励 🙏🙏🙏，**右上角点 Star (非强制)加QQ群([338461197](https://qm.qq.com/cgi-bin/qm/qr?k=yJ8K8BUR0XxDi-KX3r2CBjDpMEuRHbG9&authKey=a518l1HYfk2Nte1WVAMl97+H96zOW5iNveohr15Vj1av8OQewJOFRDbTjDpyHcdR&noverify=0))获取最新的数据库脚本**。

 [在线预览 点击试一试无需注册登录](https://s.surveyking.cn)

## 🚀 1 分钟快速体验调查问卷系统(无需安装数据库)

1. 下载 [卷王快速体验安装包](https://pan.baidu.com/s/1G6A1V2V4WjGJXopPXpRLNA?pwd=1234)
2. 解压，双击运行 start.bat
3. 打开浏览器访问 [http://localhost:1991](http://localhost:1991)，输入账号密码： *admin*/*123456*

## 特性

- 🥇 支持 20 多种题型，如填空、选择、下拉、级联、矩阵、分页、签名、题组、上传等
- 🎉 多种创建问卷方式，Excel导入问卷、文本导入问卷、在线编辑器编辑问卷
- 💪 多种问卷设置，支持白名单答卷、公开查询、答卷限制等
- 🎇 数据，支持问卷数据新增、编辑、标记、导出、预览和打包下载附件
- 🎨 报表，支持对问题实时统计分析并以图形（条形图、柱形图、扇形图）、表格的形式展示输出
- 🚀 安装部署简单（**最快 1 分钟部署**），支持 nginx 部署和一键启动的方式
- 🥊 响应式布局，所有页面在 PC 和手机端都有良好的操作体验，支持手机端编辑问卷
- 👬 支持多人协作管理问卷
- 🎁 后端支持多种数据库，可支持所有带有 jdbc 驱动的关系型数据库
- 🐯 安全、可靠、稳定、高性能的后端 API 服务
- 🙆 支持完善的 RBAC 权限控制
- 😊 支持**工作流**，工作流代码已全部开源
- 🦋 可计算，分为**逻辑计算**、**值计算**、**文本替换计算**、**必填计算**，[点击体验](https://s.surveyking.cn/s/logic8)
- ...


## 问卷产品对比

|            | [问卷网](https://www.wenjuan.com/) | [腾讯问卷](https://wj.qq.com) | [问卷星](https://www.wjx.cn/) | [金数据](https://jinshuju.net/) | [卷王](https://surveyking.cn/) |      | [卷王](https://surveyking.cn/) |
| ---------- | ------------------------------- | -------------------------- | -------------------------- | ---------------------------- | --------------------------- | ---- | --------------------------- |
| 问卷调查   | ✔️                            | ✔️                       | ✔️                       | ✔️                         | ✔️                        | ✔️ | ✔️                        |
| 在线考试   | ✔️                            | ❌                         | ✔️                       | ✔️                         | ✔️                        | ❌   | ✔️                        |
| 投票       | ✔️                            | ✔️                       | ✔️                       | ✔️                         | ✔️                        | ❌   | ✔️                        |
| 支持题型   | 🥇                              | 🥉                         | 🥇                         | 🥈                           | 🥈                          | 🥉   | 🥈                          |
| 题型设置   | 🥇                              | 🥉                         | 🥇                         | 🥇                           | 🥇                          | 🥉   | 🥇                          |
| 工作流     | ❌                              | ❌                         | ❌                         | ❌                           | ✔️                        | ❌   | ✔️                        |
| 自动计算   | ❌                              | ❌                         | 🥉                         | 🥈                           | 🥇                          | ❌   | 🥇                          |
| 逻辑设置   | 🥈                              | 🥈                         | 🥈                         | 🥈                           | 🥇                          | 🥉   | 🥇                          |
| 自定义校验 | ❌                              | ❌                         | ❌                         | ❌                           | ✔️                        | ❌   | ✔️                        |
| 自定义导出 | 🥈                              | ❌                         | ❌                         | 🥉                           | 🥇                          | ❌   | 🥇                          |
| 手机端编辑 | ✔️                            | ✔️                       | ✔️                       | ✔️                         | ✔️                        | ❌   | ✔️                        |
| 私有部署   | 💰💰💰                          | 💰💰💰                     | 💰💰💰                     | 💰💰💰                       | 💰                          | 💰💰 | 🆓                          |

注: 上表与卷王对比的全部是商业问卷产品，他们有很多地方值得卷王学习，仅列出部分主要功能供大家参考，如果对结果有疑问，可以点击对应产品的链接自行对比体验。

🥇强  🥈中 🥉弱

## 考试预览

* 考试系统预览

<table>
    <tr>
        <td><img src="docs/images/exam-editor.jpg"/></td>
        <td><img src="docs/images/exam-import.jpg"/></td>
    </tr>
     <tr>
        <td><img src="docs/images/exam-pc-prev.jpg"/></td>
        <td><img src="docs/images/exam-mb-preview.jpeg"/></td>
    </tr>
     <tr>
        <td><img src="docs/images/exam-repo-list.jpg"/></td>
        <td><img src="docs/images/exam-repo-pick.jpg"/></td>
    </tr>
     <tr>
        <td><img src="docs/images/exam-repo-qedit.jpg"/></td>
        <td><img src="docs/images/exam-repo.jpg"/></td>
    </tr>
</table>

* 调查问卷预览

<table>
    <tr>
        <td><img src="docs/images/survey-editor.jpg"/></td>
        <td><img src="docs/images/survey-editor-formula.jpg"/></td>
    </tr>
    <tr>
        <td><img src="docs/images/survey-editor-preview.jpg"/></td>
        <td><img src="docs/images/survey-imp.jpg"/></td>
    </tr>
    <tr>
        <td><img src="docs/images/survey-export.jpg"/></td>
        <td><img src="docs/images/survey-exp-preview.jpg"/></td>
    </tr>
    <tr>
        <td><img src="docs/images/survey-exp-formula.jpg"/></td>
        <td><img src="docs/images/survey-formula.jpg"/></td>
    </tr>
    <tr>
        <td><img src="docs/images/survey-editor-preview.jpg"/></td>
        <td><img src="docs/images/survey-prev-mbmi.jpeg"/></td>
    </tr>
    <tr>
        <td><img src="docs/images/survey-report.jpg"/></td>
        <td><img src="docs/images/survey-setting.jpg"/></td>
    </tr>
    <tr>
        <td><img src="docs/images/survey-sys.jpg"/></td>
        <td><img src="docs/images/survey-post.jpg"/></td>
    </tr>
</table>


## 安装

### 通过发行版安装

目前已适配 mysql 数据库

加 Q群(**[338461197](https://qm.qq.com/cgi-bin/qm/qr?k=yJ8K8BUR0XxDi-KX3r2CBjDpMEuRHbG9&authKey=a518l1HYfk2Nte1WVAMl97+H96zOW5iNveohr15Vj1av8OQewJOFRDbTjDpyHcdR&noverify=0)**)群下载最新版安装文件(`/正式版目录/surveyking-v0.x.x.jar`)到本地

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

### 使用 docker 快速启动(非最新版本)

启动 SurveyKing 镜像时，你可以指定 SurveyKing 挂载参数，将日志文件和内置数据库保存到你本地。(docker 版本目前还有点问题，待解决）

```bash
docker run -p 1991:1991 registry.cn-hangzhou.aliyuncs.com/surveyking/surveyking
# 挂载数据文件
docker run -p 1991:1991 -v /surveyking:/surveyking registry.cn-hangzhou.aliyuncs.com/surveyking/surveyking
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

1. 首先创建 mysql 数据库，然后执行初始化脚本，[下载脚本](https://gitee.com/surveyking/surveyking/raw/master/server/rdbms/src/main/resources/scripts/init-mysql.sql)。
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
 QQ群：338461197
</div>

## 致谢

[formily](https://github.com/alibaba/formily)

## 开源协议

SurveyKing is open source software licensed as
[MIT.](https://github.com/javahuang/SurveyKing/blob/master/LICENSE)
