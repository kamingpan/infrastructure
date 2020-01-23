# infrastructure-entity
该模块为实体服务，包含了实体对象、事务层、dao层和实体常量等。

## 目录
[Toc]
* 项目介绍
* 目录结构
* 使用说明

---

#### 项目介绍
该模块定义了数据库实体对象映射，没有别的具体逻辑代码，继承了core核心库，并由更下层模块继承使用。
　　

---

#### 目录结构
* infrastructure-entity - 实体服务
* src - 资源目录
    * main - 主目录
        * java - java源码
            * constant - 常量
            * dao - dao层
            * group - 入参规则校验分组
            * model - 模型层
                * dto - 请求入参组装对象
                * entity - 实体对象
                * vo - 响应出参组装对象
            * service - 事务层
                * impl - 事务接口实现类
        * resources - 配置文件目录
            * mapper - mapper配置文件
            * mybatis - mybatis配置文件

---

#### 使用说明
无
