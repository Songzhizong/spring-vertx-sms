# spring-vertx-sms

## 简介

spring-vertx-sms是一个由 **vert.x + kotlin协程 + spring boot** 构建的高性能短信网关服务。



## 特性

- 对外提供统一的短信发送接口
- 支持多家短信服务提供商（目前只对接了阿里云）
- 集中化管理短信模板



## 快速开始

> 在开始之前先将 [ideal-framework](https://github.com/Songzhizong/ideal-framework) clone到本地并打包到maven仓库

### 1. 创建数据库

使用`database_mysql.sql`脚本建表



### 2. 编辑配置文件 application.yml

```yaml
# 修改数据库和阿里云配置信息
sms:
  datasource:
    # mysql://{username}:{password}@{mysql_host}:{mysql_port}/{database_name}
    url: "mysql://username:password@127.0.0.1:3306/vertx-demo?useUnicode=true&characterEncoding=utf-8&useAffectedRows=true&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false"
  provider:
    # 启动的短信服务提供商
    enabled: ali_yun
    # 阿里云短信网关配置信息
    ali-yun:
      sign-name: 短信签名
      access-key-id: access_key
      access-secret: access_secret
```

### 3. 启动服务

**Linux & Unix**

```shell
./mvnw spring-boot:run
```

**Windows**

```powershell
mvnw.cmd spring-boot:run
```



### 4. 新建短信模板

依次运行`http_script`目录下`TemplateScript.http`中的两个http脚本

#### 新建模板

**请求**

```http
POST http://127.0.0.1:8888/template
Content-Type: application/json

{
  "code": "SMS_PHONE_CODE",
  "name": "短信验证码",
  "description": "短信验证码",
  "params": [
    {
      "param": "code",
      "description": "短信验证码"
    }
  ]
}
```

- code - 模板编码，后续发送短信需要
- name - 模板名称
- description - 模板描述
- params - 模板中的参数列表
  - param - 参数名称
  - description - 参数描述

**响应**

```json
{
  "traceId": null,
  "success": true,
  "code": 200,
  "message": "Success",
  "data": "269812346833076224"
}
```

- data - 模板id



#### 配置服务提供商模板信息

**请求**

```http
POST http://127.0.0.1:8888/template/provider
Content-Type: application/json

{
  "templateId": 269812346833076224,
  "providerCode": "ali_yun",
  "providerTemplate": "SMS_201125008",
  "content": "验证码${code}，您正在进行身份验证，打死不要告诉别人哦！"
}
```

- templateId - 新建模板是返回的模板id
- providerCode - 短信服务提供商编码
- providerTemplate - 短信服务提供商的模板id
- content - 短信模板内容，${code}对应模板参数中的占位符

**响应**

```json
{
  "traceId": null,
  "success": true,
  "code": 200,
  "message": "Success",
  "data": "269812373341077504"
}
```



### 5. 发送短信

见`http_script`目录下`SmsScript.http`脚本

#### 单发

适用于相同的短信内容发送给 1~n 个手机号

```http
POST http://127.0.0.1:8888/sms/send
Content-Type: application/json

{
  "templateCode": "SMS_PHONE_CODE",
  "mobiles": [
    "18888888888"
  ],
  "params": {
    "code": "8819"
  }
}
```

- templateCode - 短信模板编码
- mobiles - 手机号列表
- params - 参数列表

#### 批量发送

适用于向多个用户发送不同的短信内容

```http
POST http://127.0.0.1:8888/sms/send/batch
Content-Type: application/json

[
  {
    "templateCode": "SMS_PHONE_CODE",
    "mobiles": [
      "18866666666"
    ],
    "params": {
      "code": "1234"
    }
  },
  {
    "templateCode": "SMS_PHONE_CODE",
    "mobiles": [
      "18888888888"
    ],
    "params": {
      "code": "5678"
    }
  }
]
```

