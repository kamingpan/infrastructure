package com.kamingpan.infrastructure.core.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 根据主键查询数据数量 方法
 *
 * @author kamingpan
 * @since 2018-11-06
 */
@Slf4j
public class CountByIdMethod extends AbstractMethod {

    /**
     * 根据主键查询数据数量 方法
     *
     * @param mapperClass mapper类
     * @param modelClass  实体类
     * @param tableInfo   实体表名
     * @return {@link MappedStatement}
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        /* 执行 SQL ，动态 SQL 参考类 SqlMethod */
        String sql = "<script>select count(id) from %s where id = #{id}</script>";
        sql = String.format(sql, tableInfo.getTableName());

        /* mapper 接口方法名一致 */
        String method = "countById";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        MappedStatement mappedStatement = this.addMappedStatement(mapperClass, method, sqlSource,
                SqlCommandType.SELECT, null, null, Integer.class, new NoKeyGenerator(),
                null, null);

        log.debug("添加自定义方法“{}”成功", method);
        return mappedStatement;
    }
}
