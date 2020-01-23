# infrastructure-mini-program
该模块为小程序基础，提供小程序的接口。

## 目录
[Toc]
* 项目介绍
* 目录结构
* 使用说明

---

#### 项目介绍
该模块主要是controller层对外接口和security相关的权限校验，包含了小程序相关的用户体系，引用了entity模块，支持开发者模式和普通模式开发，可单独部署或者打包成jar供其它项目引用。

---

#### 目录结构
* infrastructure-mini-program - 小程序基础
* src - 资源目录
    * main - 主目录
        * java - java源码
            * controller - 接口
            * exception - 小程序异常统一捕捉
            * security - 小程序安全校验
            * util - 小程序工具
            * wechat - 微信相关
                * configuration - 微信配置
                * entity - 微信实体
                * properties - 微信配置参数
            * MiniProgramApplication.java - 小程序启动类
        * resources - 配置文件目录
            * application.yml - spring boot配置文件
            * application-datasource.yml - 数据源配置文件
            * application-mini-program.yml - 小程序配置
            * application-redis.yml - redis配置文件
            * application-spring-security.yml - spring security配置文件
            * logback-spring.xml - logback配置文件

---

#### 使用说明
##### 一、创建数据库
​		1、创建新的数据库，数据库名称可以自定义（注意字符集设置为utf8mb4）。
![创建数据库](https://www.kamingpan.com/infrastructure-course/create-database.png "创建数据库")
​        2、安装redis数据库。
​        步骤略...

##### 二、修改配置文件
​		1、修改application.yml配置文件（列出来的是必须要修改的信息，剩余的可以按照实际情况选择是否修改，首次运行需要设置init-default-sql为true以初始化数据库）。

```yml
### 服务配置 ###
server:
  port: 8030
  servlet:
    context-path: /mini-program

### spring配置 ###
spring:
  # 数据源配置
  datasource:
    # 主数据源配置
    primary:
      # 数据库配置
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://127.0.0.1:3306/infrastructure?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
      username: username
      password: password

# 日志输出配置
logging:
  path: /usr/local/logs

### mybatis plus配置 ###
mybatis-plus:
  # 实体扫描，多个package用逗号或者分号分隔
  type-aliases-package: com.kamingpan.infrastructure.entity.entity
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapper-locations: classpath*:mapper/*Mapper.xml

### 系统配置 ###
system:
  # 上传文件存储方式（local：本地存储，fast_dfs：FastDFS服务器存储，后续可能持续增加）
  upload-file-storage-type: local
  # 上传文件存储目录
  upload-file-directory: /usr/local/upload/infrastructure/infrastructure-mini-program
  # 上传文件请求目录
  upload-file-url: /mini-program/upload-file/download/:id
```

​		2、修改application-redis.yml配置文件。
```yml
spring:
  redis:
    # 服务器地址
    host: 127.0.0.1
    # 服务器连接端口
    port: 6379
    # 密码
    # password: password
    # 数据库索引
    database: 0
    # 连接超时时间（单位：毫秒）
    timeout: 500
    # 是否开启事务管理控制
    # enable-transaction-support: false
```

​		3、修改application-mini-program.yml配置文件。
```yml
# 小程序配置
mini-program:
  # 小程序appID(应用ID)
  app-id: APP_ID
  # 小程序appSecret(应用密钥)
  app-secret: SECRET

  # 缓存类型（用于缓存微信access_token等，目前支持local和redis两种，local只支持单机部署）
  cache-type: redis
```

​		4、修改logback-spring.xml配置文件（该文件主要修改一些路径和包指定打印级别，在此不做赘述）。

##### 三、运行系统
​		运行“com.kamingpan.infrastructure.mini.program.MiniProgramApplication”的main方法。

##### 四、预览系统
​		预览系统前需要先启动对应搭配的前端页面，页面详见传送门：[小程序页面源码](https://github.com/kamingpan/mini-program-html "mini-program-html")

##### 五、打包运行
​		项目使用了spring boot搭建，因此打包成可运行jar包需要修改pom.xml文件，在`build -> plugins`节点内增加标签`plugin`（具体如下）：
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

然后使用maven命令`mvn package`进行打包。
