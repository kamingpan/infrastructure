package com.kamingpan.infrastructure.util.excel;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ExcelVO
 *
 * @author kamingpan
 * @since 2018-08-14
 */
public class ExcelVO {

    // 导出数据集合
    private Map<String, Object> data;

    // 文件名
    private String filename;

    // 模板目录
    private String templatePath;

    // 响应信息
    private HttpServletResponse response;

    // 输入流
    private InputStream inputStream;

    // 输出流
    private OutputStream outputStream;

    public ExcelVO() {
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void addData(String key, Object data) {
        if (null == key || key.isEmpty()) {
            return;
        }

        if (null == this.data) {
            this.data = new HashMap<String, Object>();
        }
        this.data.put(key, data);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
