
## 什么是卷王

[卷王](https://surveyking.cn/)是一款安装简单、界面美观、易于使用、功能强大的企业级问卷数据采集系统。

[问卷作答演示](https://surveyking.cn/s/q443is)

<details>
  <summary>演示 1 分钟快速创建、发布、作答 BMI 计算器</summary>

  ![logic-hideshow](https://surveyking.gitee.io/doc/static/BMI.47013baa.gif)
</details>

## 特性概览

- 安装简单（最快 1 分钟部署）
- 界面美观
- 易于使用（可以快速拖拽创建问卷）
- 响应式布局（所有功能跨终端跨平台自适应）
- 支持多种题型（持续更新）
- 强大的自定义逻辑功能（略胜问卷星、腾讯问卷等）
- 优秀的数据展示编辑功能
- 丰富的报表输出功能（支持多种报表）
- 安全、可靠、高性能的后端 API 服务
- ...

## 快速开始

### 通过发行版安装

点击[下载安装包](https://gitee.com/surveyking/surveyking/attach_files/817663/download/surveyking-0.0.1-release.jar)到本地

```bash
java -jar surveyking-0.0.1-release.jar
```

### 使用源码编译安装

```bash
git clone https://github.com/javahuang/SurveyKing
cd api
gradle clean format build -P pro -x test
java -jar build/libs/surveyking-0.0.1-release.jar
```

打开浏览器，访问 <https://loclhost:1991>

### 使用 docker 快速启动

启动 SurveyKing 镜像时，你可以指定 SurveyKing 挂载参数，将日志文件和内置数据库保存到你本地。

```bash
docker run -p 1991:1991 surveyking/surveyking
# 挂载数据文件
docker run -p 1991:1991 -v /surveyking:/surveyking surveyking/surveyking
```

## 在线体验

演示地址：<https://surveyking.cn>

> 网站部署在单核1G内存1M带宽的丐版服务器，访问稍慢

点击*试一试*，无需注册登录。

## 开源地址

目前后端代码已开源，前端代码后续也会有开源计划。

[SurveyKing](https://github.com/javahuang/SurveyKing)

[文档地址](https://surveyking.gitee.io/doc/)
