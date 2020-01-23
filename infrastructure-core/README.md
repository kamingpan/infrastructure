# infrastructure-core
该模块为核心库，是提供整个框架的核心内容（类似common）。

## 目录
[Toc]
* 项目介绍
* 目录结构
* 使用说明

---

#### 项目介绍
1、该模块中包含了各层级（controller、service、dao、entity等）的基础类、系统基础配置（数据库、redis、tomcat等）、异常定义、mybatis plus自定义方法、请求和响应拦截器、监听器、操作日志注解等，其它提供接口的模块可以直接传承使用来代码复用，部分配置和功能能够通过其它模块重写来覆盖。<br>
2、日志打印使用了logback，生成文件规则为：<br>
　　(1) 按日期每天生成一个普通日志文件（单文件最大容量为300M，超出会生成序号递增的日志文件，总容量最大为20G，保留90天，可自由修改配置）；<br>
　　(2) 按容量生成异常日志文件（单个文件最大为300M，超出会生成序号递增的日志文件，最大序号为12，可自由修改配置），普通日志也包含了打印异常日志，单独生成日志文件只是为了方便查看系统异常。<br>
3、接口的入参和出参打印都配置拦截器（详见interceptor包）统一打印。<br>
4、为了统一响应格式，系统中定义了响应对象，包含了分页对象，由```ResponseData.build```静态方法创建，内容分为状态码和描述，以及响应对象信息（详见response包，当然开发者可以另外再自行定义）。<br>
5、响应状态码和描述目前分三层规则：<br>
　　优先使用代码中设置的状态码，例如```ResponseData.build("0", "SUCCCESS")```，返回的是状态码为“0”，描述为“SUCCESS”；<br>
　　其次是使用枚举时，此时优先使用“response-status.properties”的配置，以状态码为匹配规则，描述为配置文件中的描述；<br>
　　如果上述都没有涉及，则使用ResponseStatus枚举类中的描述。<br>
6、自定义异常包含了一些常见异常，上级异常为```CustomStatusException```，但同时继承了```RuntimeException```，这样开发过程中抛出自定义不需要显式代码抛出，目前定义的异常都有一一对应的状态码枚举；当然，开发者可以在此基础上自由拓展更多异常。<br>
7、该模块中配置了运行系统时初始化sql脚本（详见SqlRunner和infrastructure.sql），可通过init-default-sql配置是否初始化；同时可以配置执行新的自定义sql文件，方便开发者做脚本拓展（详见application.yml）。
　　

---

#### 目录结构
* infrastructure-core - 核心库
* src - 资源目录
    * main - 主目录
        * java - java源码
            * base - 基础类
            * configuration - 基本配置
            * exception - 自定义异常
            * injector - mybatis plus基础接口注入
            * interceptor - 拦截器
            * listener - 监听器
            * log - 日志注解和逻辑
            * properties - 配置文件映射实体
            * response - 响应对象
            * runner - sql初始化
            * security - spring security相关
            * util - 工具
                * file - 文件存储类型定义
        * resources - 配置文件目录
            * cache - 缓存配置
            * response - 响应状态码配置
            * sql - sql初始化文件
            * application.yml - spring boot配置文件
            * application-datasource.yml - 数据源配置文件
            * application-fastdfs.yml - fastdfs文件系统配置
            * application-java-melody.yml - java melody配置文件
            * application-redis.yml - redis配置文件
            * banner.txt - spring boot启动横幅
            * logback-spring.xml - logback配置文件

---

#### 使用说明
1、创建数据库。<br>
2、分别修改application.yml、application-fastdfs.yml（上传文件存储方式配置为fastdfs模式时）、application-redis.yml、logback-spring.xml为合适自己本地系统的配置，但由于下层模块也包含了这些文件，启动时会覆盖，所以核心库中的配置文件不做改动也可以。<br>
3、id生成使用了snowflake(雪花)算法，为了避免集群部署时多台机器生成出一样的id，需要保证worker-id和data-center-id的唯一配置（详见application.yml）。
