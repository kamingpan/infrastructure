package com.kamingpan.infrastructure.core.runner;

import com.kamingpan.infrastructure.core.properties.DruidProperties;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * SqlRunner sql脚本初始化
 *
 * @author kamingpan
 * @since 2018-07-12
 */
@Slf4j
@Component
public class SqlRunner implements CommandLineRunner {

    // 默认初始化sql脚本路径
    private static final String DEFAULT_SQL_PATH = "sql/infrastructure.sql";

    @Autowired
    private DruidProperties druidProperties;

    @Autowired
    private SystemProperties systemProperties;

    @Override
    public void run(String... args) throws Exception {
        this.databaseInit();
    }

    /**
     * 数据库脚本初始化
     */
    private void databaseInit() {
        // 如果默认脚本和用户自定义脚本都不执行，则直接结束
        if (!this.systemProperties.isInitDefaultSql() && !this.systemProperties.isInitCustomSql()) {
            return;
        }

        // 数据库初始化
        try {
            String url = this.druidProperties.getUrl();
            String driverClassName = this.druidProperties.getDriverClassName();
            String username = this.druidProperties.getUsername();
            String password = this.druidProperties.getPassword();

            Class.forName(driverClassName).newInstance();
            Connection connection = DriverManager.getConnection(url, username, password);
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            Resources.setCharset(Charset.forName("UTF-8"));
            if (this.systemProperties.isInitDefaultSql()) {
                runner.runScript(Resources.getResourceAsReader(SqlRunner.DEFAULT_SQL_PATH));
            }

            // 初始化用户自定义脚本
            String customSqlPath = this.systemProperties.getCustomSqlPath();
            if (this.systemProperties.isInitCustomSql() && null != customSqlPath && !customSqlPath.trim().isEmpty()) {
                // 字符串按照指定字符分割成数组
                String[] customSqlPaths = customSqlPath.split(",");
                for (String customSql : customSqlPaths) {
                    if (null == customSql || customSql.trim().isEmpty()) {
                        continue;
                    }
                    runner.runScript(Resources.getResourceAsReader(customSql));
                }
            }
            log.info("数据库初始化成功");
        } catch (Exception exception) {
            log.warn("数据库初始化异常", exception);
        }
    }
}
