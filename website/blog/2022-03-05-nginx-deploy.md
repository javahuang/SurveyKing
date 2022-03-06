---
slug: nginx-deploy
title: 使用 nginx 部署卷王
author: 大黄
---
卷王默认将前端文件集成到了 jar 包里面，直接启动 jar 就能访问。

同时提供了 nginx 前后端分离的方式部署。

:::tip

卷王的 [演示环境](https://s.surveyking.cn) 就是采用 nginx 前后端分离的方式部署。

:::


### 如何部署


```bash
# 1. 启动后端服务
nohup java -jar surveyking-v0.3.0.jar --server.port=1991 &

# 2. 将前端文件放入 nginx 对应的目录
# 前端文件的位置在 server/api/src/main/resources/static 目录下面


# 3. 配置 nginx 并启动(配置文件如下)
nginx -s reload

```



### nginx 配置

下面是卷王线上环境 nginx 配置

```nginx
   server {
        listen      443 ssl;
        server_name  www.surveyking.cn surveyking.cn;
        ssl_certificate /usr/local/nginx/conf/ssl/surveyking.cn.pem; # ssl 配置
        ssl_certificate_key /usr/local/nginx/conf/ssl/surveyking.cn.key;
        ssl_session_timeout 5m;
        ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_prefer_server_ciphers on;

        location / {
            root   /usr/local/nginx/html/website; # 将静态文件放到该目录下面
            try_files $uri $uri/ /index.html;
        }

        location /api { # 将 api 请求代理到后端服务
          proxy_pass http://localhost:1991;
          set_real_ip_from 0.0.0.0/0;
          real_ip_header  X-Forwarded-For;
          real_ip_recursive on;
          proxy_set_header Host      $host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          client_max_body_size 20m;
          proxy_http_version 1.1;
          proxy_set_header Connection close;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection "Upgrade";
          proxy_send_timeout 1800;
          proxy_read_timeout 1800;
          proxy_connect_timeout 1800;

        }
    }
```
