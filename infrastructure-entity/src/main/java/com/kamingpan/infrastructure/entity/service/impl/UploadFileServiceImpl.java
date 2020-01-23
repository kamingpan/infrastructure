package com.kamingpan.infrastructure.entity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.entity.dao.UploadFileDao;
import com.kamingpan.infrastructure.entity.model.entity.UploadFile;
import com.kamingpan.infrastructure.entity.model.vo.UploadFileVO;
import com.kamingpan.infrastructure.entity.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传文件 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class UploadFileServiceImpl extends BaseServiceImpl<UploadFile, UploadFileDao> implements UploadFileService {

    /**
     * 批量保存上传文件
     *
     * @param uploadFiles 上传文件列表
     * @return 上传文件VO列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UploadFileVO> insertUploadFiles(List<UploadFile> uploadFiles) {
        List<UploadFileVO> uploadFileList = new ArrayList<UploadFileVO>(uploadFiles.size());
        for (UploadFile uploadFile : uploadFiles) {
            super.baseMapper.insert(uploadFile);

            UploadFileVO uploadFileVO
                    = new UploadFileVO(uploadFile.getId(), uploadFile.getFilename(), uploadFile.getUrl());
            uploadFileList.add(uploadFileVO);
        }
        return uploadFileList;
    }

    /**
     * 根据主键集合查询上传文件
     *
     * @param ids 主键集合
     * @return 上传文件列表
     */
    @Override
    public List<UploadFileVO> listUploadFileVOByIds(List<String> ids) {
        if (null == ids || ids.size() <= 0) {
            return null;
        }

        return super.baseMapper.listByIds(ids, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据主键查询上传文件
     *
     * @param id 主键
     * @return 上传文件
     */
    @Override
    public UploadFileVO getUploadFileById(String id) {
        if (null == id || id.isEmpty()) {
            return null;
        }

        return super.baseMapper.getById(id, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据关键字查询上传文件
     *
     * @param keyWord 关键字
     * @return 上传文件
     */
    @Override
    public UploadFile getByKeyWord(String keyWord) {
        if (null == keyWord || keyWord.isEmpty()) {
            return null;
        }

        QueryWrapper<UploadFile> uploadFileQueryWrapper = new QueryWrapper<UploadFile>();
        uploadFileQueryWrapper.eq("key_word", keyWord);

        return this.baseMapper.selectOne(uploadFileQueryWrapper);
    }

}
