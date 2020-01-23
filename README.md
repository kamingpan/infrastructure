# infrastructure
该系统是对开源框架做一个整合，没有包含业务逻辑，只实现基础的登录相关，可供于线上部署使用（需要进行二次业务开发）或者学习用途。<br>
使用该系统前需要事先熟悉当中用到的开源框架（springboot、spring、spring security、mybatis、mybatis plus、druid等）。<br>
_注：该系统尚未整合微服务架构，后期如果时间允许，将往微服务方向完善和补充。_

## 目录
[Toc]
* 项目介绍
* 系统架构
* 安装教程&使用说明
* 技术交流

---

#### 项目介绍
该系统包含的部署模块有：web管理端、微信公众号、小程序、终端接口四种，主要为快速开发提供解决方案。

---

#### 系统架构
* infrastructure - 基础框架
    * infrastructure-integration -  jar包集成
        * infrastructure-generate - 实体代码生成
        * infrastructure-util - 工具库
            * infrastructure-core - 核心库
                * infrastructure-entity - 实体服务
                    * infrastructure-management - 管理端（[文档](https://github.com/kamingpan/infrastructure/tree/master/infrastructure-management "infrastructure-management")）
                    * infrastructure-subscription - 公众号（[文档](https://github.com/kamingpan/infrastructure/tree/master/infrastructure-subscription "infrastructure-subscription")）
                    * infrastructure-mini-program - 小程序（[文档](https://github.com/kamingpan/infrastructure/tree/master/infrastructure-mini-program "infrastructure-mini-program")）
                    * infrastructure-terminal - 终端接口（[文档](https://github.com/kamingpan/infrastructure/tree/master/infrastructure-terminal "infrastructure-terminal")）

---

#### 安装教程&使用说明
安装教程和使用说明详见每个模块说明文档

#### 技术交流
如果您有任何疑问或者建议，可以通过邮箱联系本人：`kamingpan@qq.com`。
