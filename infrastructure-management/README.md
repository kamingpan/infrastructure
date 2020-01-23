# infrastructure-management
该模块为管理端基础，提供后台管理端的接口。<br>
如果您是初学者，且无法单凭该文档将项目本地运行起来，可以移步到[新手教程](https://github.com/kamingpan/infrastructure/blob/master/infrastructure-management/novice-tutorial.md "infrastructure-management")，那里将有详细步骤说明如何运行该系统。

## 目录
[Toc]
* 项目介绍
* 目录结构
* 使用说明

---

#### 项目介绍
该模块主要是controller层对外接口和security相关的权限校验，包含了完全的用户体系，引用了entity模块，可单独部署或者打包成jar供其它项目引用。<br>

---

#### 目录结构
* infrastructure-management - 管理端基础
* src - 资源目录
    * main - 主目录
        * java - java源码
            * constant - 示例常量
            * controller - 接口
            * dao - 示例dao
            * exception - 管理端异常统一捕捉
            * model - 示例模型
            * security - 管理端安全校验
            * service - 示例事务
            * util - 管理端工具
            * ManagementApplication.java - 管理端启动类
        * resources - 配置文件目录
            * excel-template - 导出excel模板文件
            * mapper - 示例mapper
            * sql - sql初始化文件
            * application.yml - spring boot配置文件
            * application-datasource.yml - 数据源配置文件
            * application-fastdfs.yml - fastdfs文件系统配置
            * application-java-melody.yml - java melody配置文件
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
  port: 8010
  servlet:
    context-path: /management

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
  upload-file-directory: /usr/local/upload/infrastructure/infrastructure-management
  # 上传文件请求目录
  upload-file-url: /management/upload-file/download/:id
```

​		2、修改application-fastdfs.yml配置文件（仅当文件存储类型为FastDFS存储时才需要修改，即system.upload-file-storage-type=fast_dfs）。
```yml
fdfs:
  # 读取时间
  so-timeout: 1500
  # 连接超时时间
  connect-timeout: 600
  # tracker服务配置地址列表
  tracker-list: 127.0.0.1:22122
  # 连接池配置
  pool:
    # 避免和Springboot框架自身注册的commons-pool2连接池重名，因此设置为false
    jmx-enabled: false
```

​		3、修改application-redis.yml配置文件。
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

​		4、修改logback-spring.xml配置文件（该文件主要修改一些路径和包指定打印级别，在此不做赘述）。

##### 三、运行系统
​		运行“com.kamingpan.infrastructure.management.ManagementApplication”的main方法。

##### 四、预览系统
​		预览系统前需要先启动对应搭配的前端页面，页面详见传送门：[管理端页面源码](https://github.com/kamingpan/management-html "management-html")

##### 五、打包运行
​		项目使用了spring boot搭建，因此打包成可运行jar包，需要修改pom.xml文件，在`build -> plugins`节点内增加标签`plugin`（具体如下）：
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
