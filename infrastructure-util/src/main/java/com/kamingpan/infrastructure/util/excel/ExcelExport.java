package com.kamingpan.infrastructure.util.excel;

import com.kamingpan.infrastructure.util.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.EachCommand;
import org.jxls.command.ImageCommand;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出excel表格
 *
 * @author kamingpan
 * @since 2018-08-14
 */
@Slf4j
public class ExcelExport {

    // 默认输出文件名
    private static final String DEFAULT_EXCEL_NAME = "template.xlsx";

    static {
        // 添加自定义指令（可覆盖jxls原指令）
        XlsCommentAreaBuilder.addCommandMapping("image", ImageCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("each", EachCommand.class);
        // XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
        // XlsCommentAreaBuilder.addCommandMapping("link", LinkCommand.class);
    }

    public static void export(ExcelVO excel) throws IOException {
        if (null == excel) {
            return;
        }

        // 获取并判断输入流
        InputStream inputStream;
        if (null == (inputStream = excel.getInputStream())) {
            inputStream = ExcelExport.class.getClassLoader().getResourceAsStream(excel.getTemplatePath());
        }
        if (null == inputStream) {
            throw new IOException("输入流为空");
        }

        // 获取输出流
        OutputStream outputStream = excel.getOutputStream();

        // 获取响应信息
        HttpServletResponse response = excel.getResponse();
        if (null != response) {
            // 获取文件名并判断文件名
            String filename = (null == excel.getFilename() || excel.getFilename().isEmpty()) ?
                    URLEncoder.encode(ExcelExport.DEFAULT_EXCEL_NAME, "UTF-8") :
                    URLEncoder.encode(excel.getFilename(), "UTF-8");

            // 设置响应信息
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("content-disposition", "attachment;filename=" + filename);

            // 获取输出流
            if (null == outputStream) {
                outputStream = response.getOutputStream();
            }
        }

        // 判断输出流
        if (null == outputStream) {
            throw new IOException("输出流为空");
        }

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(inputStream, outputStream);
        JexlExpressionEvaluator jexlExpressionEvaluator
                = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();

        // 设置静默模式，不报警告
        jexlExpressionEvaluator.getJexlEngine().setSilent(true);

        // 添加自定义功能
        Map<String, Object> function = new HashMap<String, Object>();
        function.put("dateUtil", new DateUtil());
        // function.put("jsonUtil", new JsonUtil());
        jexlExpressionEvaluator.getJexlEngine().setFunctions(function);

        // 设置数据和输出文件到输出流
        Context context = new Context(excel.getData());
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        inputStream.close();
        outputStream.close();
    }

    public static void exportWithoutException(ExcelVO excel) {
        try {
            ExcelExport.export(excel);
        } catch (IOException exception) {
            log.warn("导出excel异常", exception);
        }
    }

    public static void main(String[] args) {
        List<Data> list = new ArrayList<Data>();
        list.add(new ExcelExport.Data("1", "壹"));
        list.add(new ExcelExport.Data("2", "贰"));

        ExcelVO excel = new ExcelVO();
        excel.addData("list", list);
        excel.setFilename("文件名.xlsx");
        excel.setResponse(null); // 要设置为响应或者设置为本地文件输出流
        excel.setTemplatePath("template/template.xlsx"); // 注意：遍历数据表示在excel模板的标注中，需要按实际情况更改
        ExcelExport.exportWithoutException(excel);
    }

    public static class Data {
        private String id;
        private String name;

        public Data(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
