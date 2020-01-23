package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.UploadFile;
import com.kamingpan.infrastructure.entity.model.vo.UploadFileVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 上传文件 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface UploadFileDao extends BaseDao<UploadFile> {

    /**
     * 根据主键集合查询上传文件vo列表
     *
     * @param ids     主键集合
     * @param deleted 数据状态
     * @return 上传文件vo列表
     */
    List<UploadFileVO> listByIds(@Param("ids") List<String> ids, @Param("deleted") Integer deleted);

    /**
     * 根据主键查询上传文件vo
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 上传文件vo
     */
    UploadFileVO getById(@Param("id") String id, @Param("deleted") Integer deleted);

}
