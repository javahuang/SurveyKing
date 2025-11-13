```bash
# One‑command Docker deploy (embedded H2)
docker run -d -p 1991:1991 surveyking/surveyking
```

# SurveyKing · AI‑Powered Open‑Source Survey & Exam Platform

<p align="center">
  <a href="https://github.com/javahuang/surveyking" target="_blank">
    <img src='https://img.shields.io/github/stars/javahuang/surveyking?style=social' alt='GitHub stars' />
  </a>
  <a href="https://github.com/javahuang/surveyking/forks" target="_blank">
    <img src='https://img.shields.io/github/forks/javahuang/surveyking?style=social' alt='GitHub forks' />
  </a>
  <br />
  <img src='https://img.shields.io/badge/AI-Powered-brightgreen' alt='AI Powered' />
  <img src='https://img.shields.io/badge/license-MIT-blue' alt='License' />
  <img src='https://img.shields.io/badge/platform-Web%20%7C%20Mobile-lightgrey' alt='Platform' />
  <br />
  <br />
  <a href="https://surveyking.cn/" target="_blank">Website</a> ·
  <a href="https://surveyking.cn/open-source/deploy.html" target="_blank">Deploy Docs</a> ·
  <a href="https://surveyking.cn/help/quickstart.html" target="_blank">User Guide</a> ·
  <a href="https://pro.surveyking.cn/s/plus" target="_blank">Live Demo</a> ·
  <a href="https://surveyking.cn/open-source/docs/ai" target="_blank">AI Docs</a>
</p>

[English](./README.md) | [简体中文](./README.zh-CN.md)

SurveyKing is an AI‑powered, enterprise‑grade survey and online exam system. Create professional surveys from natural language, run exams with item banks and auto‑grading, and publish across web and mobile — all open source.

> One command to deploy a more powerful, self‑hosted alternative to SurveyMonkey — with built‑in exams, item bank, and AI generation.

## Key Features

- AI survey generation from natural language prompts; supports multiple mainstream models
- 20+ question types: text, choice, dropdown, matrix, cascader, file upload, signature, pagination, question groups, and more
- Powerful logic engine: show/hide logic, required rules, skip/branching, calculations, randomization
- Survey and exam modes: item bank, question picker, randomized papers, automatic grading, import/export
- Real‑time analytics and exportable reports (CSV/Excel)
- Collaboration and roles: multi‑user management, role‑based permissions
- Responsive across devices: desktop, mobile, and WeChat Mini Program
- One‑click deploy via Docker; supports external MySQL or embedded H2
- Multi‑language (i18n): English and Simplified Chinese today; more languages coming

## Quick Start

1) Run the Docker image (embedded H2 database):

```bash
docker run -d -p 1991:1991 surveyking/surveyking
```

2) Open http://localhost:1991 and sign in:

- Username: `admin`
- Password: `123456`

That’s it. You’re ready to create your first survey or exam.

## Docker (advanced)

Use the Alibaba Cloud mirror if the default registry is slow:

```bash
docker run -d -p 1991:1991 registry.cn-hangzhou.aliyuncs.com/surveyking/surveyking:latest
```

Persist files and logs on the host:

```bash
docker run -d \
  -p 1991:1991 \
  -v ${PWD}/files:/app/files \
  -v ${PWD}/logs:/app/logs \
  surveyking/surveyking
```

Connect to an external MySQL (auto‑migrates schema on first run):

```bash
docker run -d \
  -p 1991:1991 \
  -e PROFILE=mysql \
  -e MYSQL_USER=surveyking \
  -e MYSQL_PASS=surveyking \
  -e DB_URL='jdbc:mysql://172.17.0.1:3306/surveyking?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8' \
  -v ${PWD}/files:/app/files \
  -v ${PWD}/logs:/app/logs \
  surveyking/surveyking
```

## Screenshots

<table>
  <tr>
    <td><img src="docs/images/survey-editor.jpg" alt="Survey editor" /></td>
    <td><img src="docs/images/survey-editor-preview.jpg" alt="Survey preview" /></td>
    <td><img src="docs/images/survey-report.jpg" alt="Survey report" /></td>
  </tr>
  <tr>
    <td><img src="docs/images/exam-editor.jpg" alt="Exam editor" /></td>
    <td><img src="docs/images/exam-pc-prev.jpg" alt="Exam preview" /></td>
    <td><img src="docs/images/survey-setting.jpg" alt="Project settings" /></td>
  </tr>
  <tr>
    <td colspan="3" align="center">More screenshots in the docs folder.</td>
  </tr>
</table>

## Internationalization

- Current languages: English, Simplified Chinese
- More languages are on the way; community contributions are welcome

## Links

- Website: https://surveyking.cn/
- Deploy Docs: https://surveyking.cn/open-source/deploy.html
- User Guide: https://surveyking.cn/help/quickstart.html
- AI Docs: https://surveyking.cn/open-source/docs/ai
- Live Demo (Survey/Exam): https://s.surveyking.cn/

## Contributing

Issues and pull requests are welcome. If you like SurveyKing, please give us a star — it really helps.

## License

MIT License © SurveyKing contributors
