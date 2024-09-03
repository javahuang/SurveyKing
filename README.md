# 卷王

<p align="center">
    <img src='https://gitee.com/surveyking/surveyking/badge/star.svg?theme=dark' alt='star'></img>
    <img src='https://gitee.com/surveyking/surveyking/badge/fork.svg?theme=dark' alt='fork'></img>
    <img src='https://img.shields.io/github/stars/javahuang/surveyking?style=social' alt='star'></img>
    <img src='https://img.shields.io/github/forks/javahuang/surveyking?style=social' alt='fork'></img>
    <br />
    <br />   
    <a href="https://surveyking.cn/" target="_blank">官方网站</a> 
    <a href="https://surveyking.cn/open-source/deploy.html"  target="_blank" >部署文档</a> 
    <a href="https://surveyking.cn/help/quickstart.html" target="_blank">操作手册</a> 
    <a href="https://pro.surveyking.cn/s/plus" target="_blank">演示问卷</a> 
</p>

简体中文 | [English](./README.en-us.md)

## 功能最强大的调查问卷系统和考试系统

### [点击了解](https://docs.qq.com/sheet/DZEVveUVMSHpVZkJw) 卷王都有哪些功能

### [点击演示](https://pro.surveyking.cn/s/survey) 体验调查问卷功能

### [点击演示](https://pro.surveyking.cn/s/exam) 体验考试功能

### [点击链接](https://wj.surveyking.cn/s/start) 卷王问卷考试系统-快速开始

需要您的 star ⭐️⭐️⭐️ 支持鼓励 🙏🙏🙏，**右上角点 Star 加 QQ 群(92878557)获取最新的数据库脚本**。

> Q1群: 338461197(2000人满) Q2群:1074277968(2000人满) Q3群: 770779467(2000人满) Q4群: 92878557(2000人满)  Q5群:980962382

## 快速开始(一键部署)

### 🚀 1 分钟快速体验调查问卷系统(无需安装数据库)

1. 下载卷王快速体验安装包(加群)
2. 解压，双击运行 start.bat
3. 打开浏览器访问 [http://localhost:1991](http://localhost:1991)，输入账号密码： _admin_/_123456_

### 宝塔一键部署

[使用宝塔快速一键部署，部署更方便，运维更简单](https://surveyking.cn/open-source/deploy/baota-simple-deploy)

### 一键 docker 部署

```bash
# 一键启动，默认使用的是内置的 h2 数据库
docker run -d -p 1991:1991 surveyking/surveyking
# 如果 surveyking/surveyking 无法拉取，可以使用阿里云的镜像库
docker run -d -p 1991:1991 registry.cn-hangzhou.aliyuncs.com/surveyking/surveyking:latest

# 挂载文件目录和日志文件
docker run -d -p 1991:1991 -v ${PWD}/files:/app/files -v ${PWD}/logs:/app/logs surveyking/surveyking

# 使用外置 mysql 数据库，系统启动时会自动导入初始 sql
docker run -e PROFILE=mysql \
           -v ${PWD}/logs:/app/logs \
           -v ${PWD}/files:/app/files \
           -e MYSQL_PASS=surveyking \
           -e MYSQL_USER=surveyking \
           -e DB_URL='jdbc:mysql://172.17.0.1:3306/surveyking?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8' \
           -p 1991:1991 \
           surveyking/surveyking
```

## 特性

- 🥇 支持 20 多种题型，如填空、选择、下拉、级联、矩阵、分页、签名、题组、上传、[横向填空](https://wj.surveyking.cn/s/EMqvs7)等
- 🎉 多种创建问卷方式，Excel 导入问卷、文本导入问卷、在线编辑器编辑问卷
- 💪 多种问卷设置，支持白名单答卷、公开查询、答卷限制等
- 🎇 数据，支持问卷数据新增、编辑、标记、导出、打印、预览和打包下载附件
- 🎨 报表，支持对问题实时统计分析并以图形（条形图、柱形图、扇形图）、表格的形式展示输出和导出
- 🚀 安装部署简单（**最快 1 分钟部署**），支持一键 windows 部署、一键 docker 部署、前后端分离部署、单 jar 部署、二级目录部署
- 🥊 响应式布局，所有页面完美适配电脑端和移动端(包含问卷编辑、设置、答卷)
- 👬 支持多人协作管理问卷
- 🎁 后端支持多种数据库，可支持所有带有 jdbc 驱动的关系型数据库
- 🐯 安全、可靠、稳定、高性能的后端 API 服务
- 🙆 支持完善的 RBAC 权限控制
- 🦋 支持可视化配置问卷跳转和显示逻辑，以及通过公式实现自定义逻辑(卷王的逻辑设置比目前主流商业调查问卷系统强大的多)
  - **显示隐藏逻辑**
  - **值计算逻辑** 动态计算问题答案，从最简单的根据身高体重计算 BMI，到复杂的根据多个问题答案组合逻辑和数值实现复杂的运算
  - **文本替换逻辑** 动态显示题目内容
  - **值校验逻辑** 可以根据其他问题答案来判断当前问题是否有效
  - **必填逻辑** 动态判断当前问题是否必填
  - **选项自动勾选逻辑** 根据其他问题和选项答案自动勾选
  - **选项显示隐藏逻辑** 动态的显示或者隐藏选项
  - **结束问卷逻辑**
  - **跳转逻辑** 动态跳转
  - **结束问卷自定义提示语逻辑** 答卷后，可以根据问卷答案或者考试分数来显示不同的提示语信息
  - **自定义跳转链接逻辑** 答卷后，可以根据问卷答案或者考试分数来跳转到不同的链接，且支持携带答案参数
- 🌈 支持选项唯一设置，多问卷数据关联查询、更新和删除，考试自动算分，自定义提示语，自定义跳转链接等等

## 问卷产品对比

|                 | 问卷网 | 腾讯问卷 | 问卷星 | 金数据 | 卷王 |
| --------------- | ------ | -------- | ------ | ------ | ---- |
| 问卷调查        | ✔️   | ✔️     | ✔️   | ✔️   | ✔️ |
| 在线考试        | ✔️   | ❌       | ✔️   | ✔️   | ✔️ |
| 投票            | ✔️   | ✔️     | ✔️   | ✔️   | ✔️ |
| 支持题型        | 🥇     | 🥉       | 🥇     | 🥈     | 🥈   |
| 题型设置        | 🥇     | 🥉       | 🥇     | 🥇     | 🥇   |
| 自动计算        | ❌     | ❌       | 🥉     | 🥈     | 🥇   |
| 逻辑设置        | 🥈     | 🥈       | 🥈     | 🥈     | 🥇   |
| 自定义校验      | ❌     | ❌       | ❌     | ❌     | ✔️ |
| 自定义导出      | 🥈     | ❌       | ❌     | 🥉     | 🥇   |
| 手机端编辑      | ✔️   | ✔️     | ✔️   | ✔️   | ✔️ |
| 公开查询（快查) | ✔️   | ❌       | ✔️   | ❌     | ✔️ |
| 私有部署        | 💰💰💰 | 💰💰💰   | 💰💰💰 | 💰💰💰 | 🆓   |

注: 上表与卷王对比的全部是商业问卷产品，他们有很多地方值得卷王学习，仅列出部分主要功能供大家参考，如果对结果有疑问，可以点击对应产品的链接自行对比体验。

🥇 强 🥈 中 🥉 弱

## 友情推荐

[专注于中台化架构的低代码生成工具](https://gitee.com/orangeform/orange-admin)

## 预览截图

- 考试系统预览

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

- 调查问卷预览

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
