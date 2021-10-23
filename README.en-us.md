# SurveyKing

English | [简体中文](./README.md)

![preview-surveyking](./docs/preview.gif)

[BMI calculator survey](https://surveyking.cn/s/q12345)

## Background

Based on Ali's open source [formily](https://github.com/alibaba/formily) form solution self-developed survey editor, using the latest front-end and back-end technology (React+SpringBoot+AntDesignUI) to build a complete survey system.

SurveyKing is one of the known open source survey systems that has **the most powerful functions**, **the simplest installation**, and **the best comprehensive experience**.

**Simple**, **Easy to use**, and **Professional** are the continuous development concepts of SurveyKing. Large and medium-sized enterprises and even individuals can use SurveyKing to quickly build their own online survey system.

## Features

- 🥇 Support a variety of question types, such as fill-blank, selection, dropdown, cascade, matrix, pagination, signature, question-group, etc.
- 🚀 It can be calculated, divided into **logical calculation** and **field calculation**, such as calculating age and gender according to the ID number, and controlling another question according to the option value. Show hide
- 🦋 Questions and options support fast rich text editing
- 🥊 Responsive layout, all pages have a good operating experience on PC and mobile
- 🥂 Various survey settings, such as support for temporary storage, survey modification, password setting, etc.
- 🎇 Data, support survey data adding, editing, marking, exporting, previewing and downloading attachments
- 🎨 Reports, support real-time statistical analysis of problems and display the output in the form of graphs (bar graphs, column graphs, fan graphs) and tables
- 🎉 Simple installation and deployment (**The fastest deployment in 1 minute**), supports nginx deployment and one-click startup
- 🎁 The backend supports a variety of databases (embedded h2), which can support all relational databases with jdbc driver, and mongodb will be supported in the future.
- 🐯 Safe, reliable, stable and high-performance back-end API service
- 🙆 Support complete RBAC permission control (workflow will be introduced later)
- 🌈 Support internationalization (under improvement)
-...

## Install

### Install via distribution

Currently adapted to h2 database and mysql database

Click [Download surveyking-h2 version](https://github.com/javahuang/SurveyKing/releases/download/v0.1.0/surveyking-h2-v0.1.0.jar) to the local

Click [Download surveyking-mysql version](https://github.com/javahuang/SurveyKing/releases/download/v0.1.0/surveyking-mysql-v0.1.0.jar) to the local

### Use source code to compile and install

The h2 version of the installation package is built by default.

```bash
# Download source code
git clone https://github.com/javahuang/SurveyKing.git

# Start building
gradle clean :api:build -P pro -x test

# start operation
java -jar api/build/libs/surveyking-v0.1.0.jar
```

### Quick start with docker

When you start the SurveyKing mirroring, you can specify the SurveyKing mount parameters and save the log files and built-in database to your local. (There are still some problems with the docker version, to be resolved)

```bash
docker run -p 1991:1991 surveyking/surveyking
# Mount data file
docker run -p 1991:1991 -v /surveyking:/surveyking surveyking/surveyking
```

## Usage

Since this system is built in Java, it needs to rely on the Java runtime environment, which can be downloaded via [Java download for all operating systems
](https://www.java.com/en/download/manual.jsp) to pre-install the java environment.

Follow the instructions below to configure different databases. If the front-end needs to be deployed with nginx, refer to using nginx to deploy the front-end.

Windows and mac support double-click to run, or open a command line window to execute the following commands

```bash
java -jar surveyking-v0.1.0.jar
```

Open the browser and visit <http://localhost:1991>. After the system is started for the first time, the admin user, account/password (*admin/surveyking*) will be created automatically, and the password can be changed through the user management interface after logging in to the system.

### h2 startup method

Without any configuration, the database startup script will be created automatically. If you need to change the port number, refer to the defined port of mysql startup mode.

### mysql startup mode

1. First create the mysql database, and then execute the initialization script, [download script](https://raw.githubusercontent.com/javahuang/SurveyKing/master/rdbms/src/main/resources/scripts/init-mysql.sql).
2. Then create a new `application.properties` file under the current directory.

  ```properties
  server.port=1991 # Port number (optional, default 1991)
  spring.datasource.url=jdbc:mysql://<ip>:<port>/<dbname> # Database connection address, replace with your mysql database address
  spring.datasource.username=username # Database account (required)
  spring.datasource.password=password # database password (required)
  ```

### Deploy the front-end with nginx

Download [static resource files under this directory](https://github.com/javahuang/SurveyKing/tree/master/api/src/main/resources/static) and deploy directly to nginx.

Then configure the proxy to proxy to the back-end api service.

## Online experience

Demo address: <https://surveyking.cn>

Click *Try it*, no need to register and log in (-_-||, the server bandwidth is only 1M, maybe the first load is slightly slower)

## Contact the author

This project was developed by the author personally based on interest. The back-end code is completely open source, and the front-end code will have an open source plan after it is stabilized. If you find it helpful, you can click the star in the upper right corner.

If you encounter any problems or suggestions, Please send me an issue.

## LICENSE

SurveyKing is open source software licensed as
[MIT.](https://github.com/javahuang/SurveyKing/blob/master/LICENSE)