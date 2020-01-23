package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.constant.FinalConstant;
import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.base.enumeration.ContentTypeEnum;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.core.util.file.FileStorageFactory;
import com.kamingpan.infrastructure.entity.constant.UploadFileConstant;
import com.kamingpan.infrastructure.entity.model.entity.UploadFile;
import com.kamingpan.infrastructure.entity.model.vo.UploadFileVO;
import com.kamingpan.infrastructure.entity.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 上传文件 controller
 *
 * @author kamingpan
 * @since 2017-06-12
 */
@Slf4j
@RestController
@RequestMapping("/upload-file")
public class UploadFileController extends BaseController {

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private FileStorageFactory fileStorageFactory;

    /**
     * 获取上传文件信息
     *
     * @param id 主键集合
     * @return 响应数据
     */
    @GetMapping("/info")
    public ResponseData info(@RequestParam("id") String id) {
        log.debug("查询上传文件“{}”信息...", id);

        List<String> ids = Arrays.asList(id.split(","));
        List<UploadFileVO> uploadFiles = this.uploadFileService.listUploadFileVOByIds(ids);
        return ResponseData.build(uploadFiles);
    }

    /**
     * 获取上传文件
     *
     * @param keyWord  关键字
     * @param response 响应
     * @param preview  是否预览
     * @throws IOException io异常
     */
    @GetMapping("/download/{keyWord}")
    public void download(@PathVariable("keyWord") String keyWord, HttpServletResponse response,
                         @RequestParam(value = "preview", required = false, defaultValue = "false") boolean preview)
            throws IOException {
        log.debug("下载上传文件“{}”...", keyWord);

        UploadFile uploadFile = this.uploadFileService.getByKeyWord(keyWord);
        if (null == uploadFile) {
            uploadFile = this.uploadFileService.getById(keyWord);
            if (null == uploadFile) {
                throw new ValidateException("文件不存在");
            }
        }

        // 输出文件
        this.fileStorageFactory.get(uploadFile, preview, response);
    }

    /**
     * 上传文件列表
     *
     * @param files    文件列表
     * @param belong   所属对象
     * @param belongId 所属主键
     * @return 响应数据
     * @throws IOException io异常
     */
    @PostMapping("")
    public ResponseData upload(@RequestParam("file") List<MultipartFile> files, @RequestParam("belong") String belong,
                               @RequestParam(value = "belongId", required = false) String belongId,
                               @RequestParam(value = "belongVariable", required = false) String belongVariable,
                               HttpServletRequest request) throws IOException {
        if (null == files || files.size() <= 0) {
            return ResponseData.build(ResponseStatus.PARAMETER_ERROR, "请至少上传一个文件");
        }

        List<UploadFile> uploadFiles = new ArrayList<UploadFile>(files.size());
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseData.build(ResponseStatus.PARAMETER_ERROR, "文件为空");
            }

            // 获取文件名的后缀格式
            String filename = file.getOriginalFilename();
            String suffix = FinalConstant.Strings.EMPTY;
            if (null != filename && !filename.isEmpty()) {
                int index = filename.lastIndexOf(".");

                if (index > -1) {
                    suffix = filename.substring(index);
                }
            }

            // 获取文件内容类型，如果文件内容类型为空，则通过后缀名获取
            String contentType = file.getContentType();
            if (null == contentType || contentType.isEmpty()) {
                contentType = ContentTypeEnum.getByFormat(suffix).getContentType();
            }

            UploadFile uploadFile = new UploadFile();
            uploadFile.preInsert(); // 先设置id和修改信息等
            uploadFile.setKeyWord(uploadFile.getId() + suffix);
            uploadFile.setBelong(belong);
            uploadFile.setBelongId(belongId);
            uploadFile.setBelongVariable(belongVariable);
            uploadFile.setFilename(file.getOriginalFilename());
            uploadFile.setType(UploadFileConstant.Type.FILE);
            uploadFile.setContentType(contentType);
            uploadFile.setSize(file.getSize());

            // 把文件存储至文件服务器或硬盘
            this.fileStorageFactory.save(file, uploadFile, request);

            // 将文件对象添加到列表中，后续做统一持久化
            uploadFiles.add(uploadFile);
        }

        // 数据持久化
        List<UploadFileVO> uploadFileList = uploadFileService.insertUploadFiles(uploadFiles);
        return ResponseData.build(uploadFileList);
    }

    /**
     * 上传图片列表
     *
     * @param files    图片列表
     * @param belong   所属对象
     * @param belongId 所属主键
     * @return 响应数据
     * @throws IOException io异常
     */
    @PostMapping("/upload-images")
    public ResponseData uploadImages(@RequestParam("file") List<MultipartFile> files,
                                     @RequestParam("belong") String belong, HttpServletRequest request,
                                     @RequestParam(value = "belongId", required = false) String belongId,
                                     @RequestParam(value = "belongVariable", required = false) String belongVariable)
            throws IOException {
        if (null == files || files.size() <= 0) {
            return ResponseData.build(ResponseStatus.PARAMETER_ERROR, "请至少上传一个图片");
        }

        List<UploadFile> uploadFiles = new ArrayList<UploadFile>(files.size());
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseData.build(ResponseStatus.PARAMETER_ERROR, "图片为空");
            }

            // 获取文件名的后缀格式
            String filename = file.getOriginalFilename();
            String suffix = FinalConstant.Strings.EMPTY;
            if (null != filename && !filename.isEmpty()) {
                int index = filename.lastIndexOf(".");

                if (index > -1) {
                    suffix = filename.substring(index);
                }
            }

            // 获取文件内容类型，如果文件内容类型为空，则通过后缀名获取
            String contentType = file.getContentType();
            if (null == contentType || contentType.isEmpty()) {
                contentType = ContentTypeEnum.getByFormat(suffix).getContentType();
            }

            UploadFile uploadFile = new UploadFile();
            uploadFile.preInsert(); // 先设置id和修改信息等
            uploadFile.setKeyWord(uploadFile.getId() + suffix);
            uploadFile.setBelong(belong);
            uploadFile.setBelongId(belongId);
            uploadFile.setBelongVariable(belongVariable);
            uploadFile.setFilename(file.getOriginalFilename());
            uploadFile.setType(UploadFileConstant.Type.IMAGE);
            uploadFile.setContentType(contentType);
            uploadFile.setSize(file.getSize());

            // 把文件存储至文件服务器或硬盘
            this.fileStorageFactory.save(file, uploadFile, request);

            // 将文件对象添加到列表中，后续做统一持久化
            uploadFiles.add(uploadFile);
        }

        // 数据持久化
        List<UploadFileVO> uploadFileList = uploadFileService.insertUploadFiles(uploadFiles);
        return ResponseData.build(uploadFileList);
    }

}
