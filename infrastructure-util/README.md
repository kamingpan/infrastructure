# infrastructure-util
该模块为工具库，统一集中一些常用工具类，提供其余模块使用。

## 目录
[Toc]
* 项目介绍
* 目录结构
* 使用说明

---

#### 项目介绍
无

---

#### 目录结构
* infrastructure-util - 工具库
* src - 资源目录
    * main - 主目录
        * java - java源码
            * calendar - 公历、农历转换
            * context - Spring应用上下文
            * conversion - 各类对象转换
            * date - 日期
            * export - 导出
            * file - 文件
            * generator - uuid等生成
            * http - http请求
            * id - id生成
            * instantiate - 实例化对象
            * ip - IP相关
            * location - 坐标相关
            * matcher - 正则校验
            * math - 数学计算
            * md5 - md5加密
            * mobile - 手机相关
            * sort - 排序
            * sql - sql预处理
            * string - 字符串操作
            * uri - uri处理
        * resources - 配置文件目录
            * template - 导出excel模板文件
    * test - 测试目录
        * java - java单元测试源码
            * calendar - 公历、农历单元测试

---

#### 使用说明
该工具模块大多都是静态方法，引用后可直接调用。
