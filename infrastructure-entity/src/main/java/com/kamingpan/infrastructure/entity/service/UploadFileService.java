package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.entity.model.entity.UploadFile;
import com.kamingpan.infrastructure.entity.model.vo.UploadFileVO;

import java.util.List;

/**
 * 上传文件 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface UploadFileService extends BaseService<UploadFile> {

    /**
     * 批量保存上传文件
     *
     * @param uploadFiles 上传文件列表
     * @return 上传文件VO列表
     */
    List<UploadFileVO> insertUploadFiles(List<UploadFile> uploadFiles);

    /**
     * 根据主键集合查询上传文件
     *
     * @param ids 主键集合
     * @return 上传文件列表
     */
    List<UploadFileVO> listUploadFileVOByIds(List<String> ids);

    /**
     * 根据主键查询上传文件
     *
     * @param id 主键
     * @return 上传文件
     */
    UploadFileVO getUploadFileById(String id);

    /**
     * 根据关键字查询上传文件
     *
     * @param keyWord 关键字
     * @return 上传文件
     */
    UploadFile getByKeyWord(String keyWord);

}
