package com.kamingpan.infrastructure.generate;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * Generator
 *
 * @author kamingpan
 * @since 2018-06-25
 */
@Slf4j
public class Generation {

    private static final String[] TABLE_PREFIX = {"system"};

    private static final String[] PUBLIC_FIELD = {"id", "creator_id", "creator", "create_time", "updater_id", "updater", "update_time", "deleted"};

    public static void codeGeneration() {
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/infrastructure-generate/src/main/java"); // 输出文件目录
        globalConfig.setFileOverride(true);
        globalConfig.setActiveRecord(true);
        globalConfig.setOpen(false);
        globalConfig.setEnableCache(false); // XML 二级缓存
        globalConfig.setBaseResultMap(true); // XML ResultMap
        globalConfig.setBaseColumnList(true); // XML ColumnList
        globalConfig.setAuthor("kamingpan");
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setMapperName("%sDao");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setControllerName("%sController");
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MARIADB);
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver"); // 数据库包路径
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1:3306/infrastructure?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8"); // 数据库链接路径
        dataSourceConfig.setUsername("username"); // 数据库用户名
        dataSourceConfig.setPassword("password"); // 数据库密码
        autoGenerator.setDataSource(dataSourceConfig);

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        // strategyConfig.setCapitalMode(true); // 全局大写命名 ORACLE 注意
        strategyConfig.setNaming(NamingStrategy.underline_to_camel); // 表名生成策略
        // strategyConfig.setInclude(new String[]{""}); // 需要生成的表
        // strategyConfig.setExclude(new String[]{""}); // 排除生成的表
        strategyConfig.setTablePrefix(Generation.TABLE_PREFIX); // 表前缀
        strategyConfig.setLogicDeleteFieldName("deleted"); // 逻辑删除字段名

        // 自定义Controller类型
        strategyConfig.setRestControllerStyle(true);
        // 自定义controller注解路径类型
        strategyConfig.setControllerMappingHyphenStyle(true);
        // 自定义实体基础类
        strategyConfig.setSuperEntityClass("com.kamingpan.infrastructure.core.base.model.entity.BaseEntity");
        // 自定义实体基础类公共字段
        strategyConfig.setSuperEntityColumns(Generation.PUBLIC_FIELD);
        // 自定义 service 基础类
        strategyConfig.setSuperServiceClass("com.kamingpan.infrastructure.core.base.service.BaseService");
        // 自定义 service 实现基础类
        strategyConfig.setSuperServiceImplClass("com.kamingpan.infrastructure.core.base.service.BaseServiceImpl");
        // 自定义 mapper 父类
        strategyConfig.setSuperMapperClass("com.kamingpan.infrastructure.core.base.dao.BaseDao");

        autoGenerator.setStrategy(strategyConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.kamingpan.infrastructure.generate");
        // packageConfig.setModuleName("module"); // 模块名称，单独生成模块时使用！！！！！！！！！！！
        packageConfig.setController("controller");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setEntity("model.entity");
        packageConfig.setMapper("dao");
        packageConfig.setXml("mapper");
        autoGenerator.setPackageInfo(packageConfig);

        // 执行生成
        autoGenerator.execute();
    }

    public static void main(String[] args) {
        Generation.codeGeneration();
    }

}
