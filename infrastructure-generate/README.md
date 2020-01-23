# infrastructure-generate
该模块为实体代码生成，藉由mybatis plus工具生成，包括生成controller、service、entity、dao、mapper等代码。

## 目录
[Toc]
* 项目介绍
* 目录结构
* 使用说明

---

#### 项目介绍
因mybatis plus的生成会覆盖原有代码，因此将该模块单独抽离出来，避免后期修改数据库字段并重新生成代码时覆盖原有的代码造成不可挽回的损失，如果需要二次以上的生成，可生成代码后手动拷贝至对应目录。

---

#### 目录结构
* infrastructure-generate - 实体代码生成
* src - 资源目录
    * main - 主目录
        * java - java源码
        * resources - 配置文件目录
            * template - 生成代码的模板文件

---

#### 使用说明
修改Generation文件中的“输出文件目录”、“数据库连接配置”，然后运行main方法即可。
