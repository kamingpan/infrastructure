package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.constant.FinalConstant;
import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.AdminLoginLogConstant;
import com.kamingpan.infrastructure.entity.model.dto.AdminLoginLogDTO;
import com.kamingpan.infrastructure.entity.model.vo.AdminLoginLogVO;
import com.kamingpan.infrastructure.entity.service.AdminLoginLogService;
import com.kamingpan.infrastructure.util.date.DateUtil;
import com.kamingpan.infrastructure.util.excel.ExcelExport;
import com.kamingpan.infrastructure.util.excel.ExcelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录日志 controller
 *
 * @author kamingpan
 * @since 2017-08-18
 */
@Slf4j
@RestController
@RequestMapping("/admin-login-log")
public class AdminLoginLogController extends BaseController {

    @Autowired
    private AdminLoginLogService adminLoginLogService;

    /**
     * 登录日志查询
     *
     * @param adminLoginLog 管理员日志dto
     * @param pager         分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(AdminLoginLogConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute AdminLoginLogDTO adminLoginLog, @ModelAttribute Pager pager) {
        log.debug("查询登录日志列表...");

        return ResponseData.buildPagination(this.adminLoginLogService.listByAdminLoginLog(adminLoginLog, pager), pager);
    }

    /**
     * 登录日志详情
     *
     * @param id 登录日志主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(AdminLoginLogConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") String id) {
        log.debug("查询登录日志“{}”详情...", id);
        AdminLoginLogVO adminLoginLogVO = this.adminLoginLogService.getAdminLoginLogById(id);
        if (null == adminLoginLogVO) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }

        return ResponseData.build(adminLoginLogVO);
    }

    /**
     * 登录日志导出
     *
     * @param adminLoginLog 管理员日志dto
     */
    @GetMapping("/export")
    @PreAuthorize(AdminLoginLogConstant.Authentication.EXPORT)
    public void export(@ModelAttribute AdminLoginLogDTO adminLoginLog, HttpServletResponse response) {
        log.debug("导出登录日志列表...");

        // 设置登录时间倒序排序
        Pager pager = new Pager();
        pager.setOrderBy("login_time desc");
        pager.setPageSize(Integer.MAX_VALUE - 1); // 每页数量设置为最大，表示全部查询

        List<AdminLoginLogVO> adminLoginLogs
                = this.adminLoginLogService.listByAdminLoginLog(adminLoginLog, pager);

        // 创建导出对象
        ExcelVO excel = new ExcelVO();
        excel.addData("list", adminLoginLogs); // 设置遍历列表
        excel.setFilename("登录日志列表.xlsx"); // 设置文件名
        excel.setResponse(response); // 设置为响应
        excel.setTemplatePath("excel-template/AdminLoginLog.xlsx"); // 注意：遍历数据表示在excel模板的标注中，需要按实际情况更改
        ExcelExport.exportWithoutException(excel);
    }

    /**
     * 查询最后10次管理员登录日志
     *
     * @return 响应数据
     */
    @GetMapping("/last")
    public ResponseData last() {
        // 获取后10条信息
        Pager pager = new Pager();
        pager.setOrderBy("login_time desc");

        // 只查询登录成功的登录日志
        AdminLoginLogDTO adminLoginLog = new AdminLoginLogDTO();
        adminLoginLog.setStatus(AdminLoginLogConstant.Status.SUCCESS);
        List<AdminLoginLogVO> adminLoginLogVOs = this.adminLoginLogService.listByAdminLoginLog(adminLoginLog, pager);

        return ResponseData.build(adminLoginLogVOs);
    }

    /**
     * 查询最后一个月登录日志
     *
     * @return 响应数据
     */
    @GetMapping("/last-month")
    public ResponseData lastMonth() {
        Date now = new Date();
        Date lastMonth = DateUtil.getLastMonth(now);
        Date lastDay = DateUtil.getLastDay(now);

        // 上个月的登录日期
        List<Date> dates = this.adminLoginLogService.listLastMonth(lastMonth, lastDay);

        // 每天登录次数
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        String lastDayString = DateUtil.dateToDay(lastDay); // 最后一天的日期

        // 将日期转换为日历对象，方便计算使用
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastMonth);

        // 遍历把最近一个月的日期放到Map中
        while (lastMonth.before(lastDay)) {
            // 键为日期，值为0（默认是登录次数为0）
            result.put(DateUtil.dateToDay(lastMonth), FinalConstant.Integers.ZERO);

            // 日期增加一天
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            lastMonth = calendar.getTime();
        }

        // 整理每天的登录日期及次数
        for (Date date : dates) {
            String dateString = DateUtil.dateToDay(date);
            Integer count = result.get(dateString);
            if (null == count) {
                count = FinalConstant.Integers.ZERO;
            }
            count++;
            result.put(dateString, count);
        }

        // 如果最后一天没有登录次数，则添加登录次数为0
        result.putIfAbsent(lastDayString, FinalConstant.Integers.ZERO);

        return ResponseData.build(result);
    }
}
