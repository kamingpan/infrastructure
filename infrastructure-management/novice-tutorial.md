# infrastructure-management（管理端新手教程）
考虑到初入门的码农们可能无法完全看懂`README`说明文档，因此单独提供一份新手教程来说明从零到运行该项目（不包括各类软件安装），仅供学习。

## 目录
[Toc]
* 目录结构
* 预先准备
* 配置系统
* 运行并预览系统

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

#### 预先准备
##### 一、安装java JDK（[java官网](https://www.oracle.com/technetwork/java/javase/downloads/index.html "java下载地址")），安装教程请自行Google。

##### 二、安装git（[git官网](https://git-scm.com/downloads "git下载地址")），安装教程请自行Google。

##### 三、安装mysql数据库（[mysql官网](https://dev.mysql.com/downloads/mysql/ "mysql下载地址")），安装教程请自行Google。
1、创建新的数据库，数据库名为`infrastructure`（注意字符集设置为utf8mb4）。
![创建数据库](https://www.kamingpan.com/infrastructure-course/create-database.png "创建数据库")

##### 四、安装redis数据库（[redis官网](https://redis.io/ "redis下载地址")），安装教程请自行Google。<br>
1、启动redis数据库。
  ```
  # 跳转redis安装目录
  cd redis安装目录/bin
  
  # 启动redis
  ./redis-server redis配置文件目录/redis.conf
  ```
  
##### 五、创建目录保存项目日志，如：`/usr/local/logs`
  
##### 六、创建目录保存项目文件，如：`/usr/local/upload/infrastructure/infrastructure-management`

---

#### 配置系统
##### 一、通过git将项目clone到本地。
```git
git clone https://github.com/kamingpan/infrastructure.git
```

##### 二、导入项目到开发工具中（开发工具选择[idea](https://www.eclipse.org/downloads/ "idea下载地址")还是[eclipse](http://www.jetbrains.com/idea/download/ "eclipse下载地址")看个人选择，笔者个人推荐idea）。

##### 三、进入后台管理端项目配置目录。
```
cd 项目保存目录/infrastructure/infrastructure-management/src/main/resources
```

1、打开`application.yml`文件，并修改以下配置信息（没有单独指出的可以暂时不修改）。
```yml
spring:
  datasource:
    primary:
      url: jdbc:mysql://mysql数据库机器ip:mysql数据库开放端口/infrastructure?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
      username: mysql数据库用户名
      password: mysql数据库密码

logging:
  path: 保存日志目录，和“预先准备”中的“五”一致

system:
  init-default-sql: true # 设置为true，在初次启动系统时执行数据库脚本初始化数据库
  upload-file-directory: 文件保存目录，和“预先准备”中的“六”一致
```

2、打开`application-redis.yml`文件，并修改以下配置信息（没有单独指出的可以暂时不修改）。
```yml
spring:
  redis:
    host: redis数据库机器ip
    port: redis数据库开放端口
    password: redis数据库密码 # 没有设置密码的可以注释掉该项
    database: redis数据库下标
```

---

#### 运行并预览系统
##### 一、进入项目目录`项目保存目录/infrastructure/infrastructure-management/src/main/java/com/kamingpan/infrastructure/management`，运行`ManagementApplication`的`main`方法。
##### 二、因项目需要搭配前端页面才能完整地预览到功能，因此需要部署前端对应页面，页面的相关安装方法请移步：[管理端页面源码](https://github.com/kamingpan/management-html "management-html")
##### 三、打开浏览器，输入域名`http://127.0.0.1:8081/`预览系统
