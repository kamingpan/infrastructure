package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.core.security.UserDetail;
import com.kamingpan.infrastructure.core.util.file.FileStorageFactory;
import com.kamingpan.infrastructure.entity.constant.AdminConstant;
import com.kamingpan.infrastructure.entity.constant.ManagementInformationConstant;
import com.kamingpan.infrastructure.entity.constant.ModuleConstant;
import com.kamingpan.infrastructure.entity.model.entity.ManagementInformation;
import com.kamingpan.infrastructure.entity.model.entity.UploadFile;
import com.kamingpan.infrastructure.entity.model.vo.AdminOperateLogVO;
import com.kamingpan.infrastructure.entity.model.vo.ManagementInformationVO;
import com.kamingpan.infrastructure.entity.model.vo.ModuleListVO;
import com.kamingpan.infrastructure.entity.service.AdminOperateLogService;
import com.kamingpan.infrastructure.entity.service.AdminService;
import com.kamingpan.infrastructure.entity.service.DataDictionaryService;
import com.kamingpan.infrastructure.entity.service.ManagementInformationService;
import com.kamingpan.infrastructure.entity.service.ModuleService;
import com.kamingpan.infrastructure.entity.service.PermissionService;
import com.kamingpan.infrastructure.entity.service.UploadFileService;
import com.kamingpan.infrastructure.management.security.cache.AdminCache;
import com.kamingpan.infrastructure.management.security.filter.TokenAuthenticationFilter;
import com.kamingpan.infrastructure.management.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.management.security.provider.LoginAuthenticationProvider;
import com.kamingpan.infrastructure.management.security.provider.TokenAuthenticationProvider;
import com.kamingpan.infrastructure.management.util.OperatorUtil;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统相关
 *
 * @author kamingpan
 * @since 2017-02-14
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private FileStorageFactory fileStorageFactory;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private AdminOperateLogService adminOperateLogService;

    @Autowired
    private RedisService<String, AdminCache> adminCacheRedisService;

    @Autowired
    private RedisService<String, ArrayList<String>> listRedisService;

    @Autowired
    private ManagementInformationService managementInformationService;

    /**
     * 跳转登录提示
     *
     * @param response 响应
     * @return 响应数据
     */
    @RequestMapping("/to-login")
    public ResponseData toLogin(HttpServletResponse response) {
        log.debug("未登录跳转...");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ResponseData.build(ResponseStatus.NOT_LOGIN);
    }

    /**
     * 验证管理员登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 响应数据
     */
    // @PostMapping("/login")
    @Deprecated
    public ResponseData login(@RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "password", required = false) String password) {
        return ResponseData.success();
    }

    /**
     * 退出登录
     *
     * @return 响应数据
     */
    @PostMapping("/logout")
    public ResponseData logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("用户“{}”正在退出登录...", ((UserDetail) authentication.getDetails()).getUsername());

        // 根据主键查询当前已登录的该管理员token列表
        String adminKey = String.format(LoginAuthenticationProvider.REDIS_ADMIN_KEY, authentication.getPrincipal());

        // 遍历并移除当前退出登录的管理员
        ArrayList<String> tokens = this.listRedisService.get(adminKey);
        if (null != tokens) {
            for (int i = 0; i < tokens.size(); i++) {
                if (authentication.getCredentials().equals(tokens.get(0))) {
                    tokens.remove(i);
                    this.listRedisService.set(adminKey, tokens);
                    break;
                }
            }
        }

        // 从redis中移除该token
        this.listRedisService.delete(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY,
                authentication.getCredentials()));
        return ResponseData.success();
    }

    /**
     * 获取系统信息
     *
     * @return 响应数据
     */
    @GetMapping("/information")
    public ResponseData message(HttpServletRequest request) {
        log.debug("获取系统信息...");

        // 从请求头中获取token，如果不存在，则从参数中获取
        String token = request.getHeader(TokenAuthenticationFilter.ACCESS_TOKEN_KEY);
        if (null == token || token.trim().isEmpty()) {
            token = request.getParameter(TokenAuthenticationFilter.ACCESS_TOKEN_KEY);
        }

        ManagementInformationVO managementInformation = this.managementInformationService
                .getManagementInformationById(ManagementInformationConstant.Id.DEFAULT);

        // 赋值是否已登录
        if (null != token && !token.trim().isEmpty()) {
            // 从redis中查询用户主键
            String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token);
            AdminCache adminCache = this.adminCacheRedisService.get(tokenKey);

            // 校验ip合法性
            if (null != adminCache) {
                String ip = IP.getIP(request);
                managementInformation.setLogin(null == adminCache.getIp()
                        || adminCache.getIp().isEmpty() || adminCache.getIp().equals(ip));
            }
        }

        return ResponseData.build(managementInformation);
    }

    /**
     * 获取系统logo
     */
    @GetMapping("/logo")
    public void logo() throws IOException {
        log.debug("获取系统logo...");
        ManagementInformation managementInformation
                = this.managementInformationService.getById(ManagementInformationConstant.Id.DEFAULT);

        if (null == managementInformation.getLogo() || managementInformation.getLogo().isEmpty()) {
            return;
        }

        // 获取response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            return;
        }

        HttpServletResponse response = requestAttributes.getResponse();
        if (null == response) {
            return;
        }

        // 输出文件
        UploadFile uploadFile = this.uploadFileService.getById(managementInformation.getLogo());
        this.fileStorageFactory.get(uploadFile, false, response);
    }

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 响应数据
     */
    @GetMapping("/username-exist")
    public ResponseData usernameExist(@RequestParam("username") final String username) {
        log.debug("查询用户名“{}”是否存在...", username);

        int count = this.adminService.countByUsername(username);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

    /**
     * 获取登录用户
     *
     * @return 响应数据
     */
    @GetMapping("/operator")
    public ResponseData operator() {
        log.debug("获取登录用户...");
        return ResponseData.build(OperatorUtil.getOperator());
    }

    /**
     * 获取登录用户模块
     *
     * @return 响应数据
     */
    @GetMapping("/module")
    public ResponseData module() {
        log.debug("查询模块列表...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return ResponseData.build(ResponseStatus.NOT_LOGIN);
        }

        UserDetail userDetail = (UserDetail) authentication.getDetails();
        List<ModuleListVO> modules = !AdminConstant.Username.SUPER_ADMIN.equals(userDetail.getUsername())
                ? this.moduleService.listByAdminId(userDetail.getUserId())
                : this.moduleService.listModuleVOBySuperAdmin();

        // 创建模块对象，用于处理模块层级关系
        ModuleListVO moduleTemp = new ModuleListVO();
        List<ModuleListVO> moduleList = new LinkedList<ModuleListVO>();
        moduleTemp.setId(ModuleConstant.Superior.HIGHEST);
        moduleTemp.setChildren(moduleList);
        this.handleModuleLevel(modules, moduleTemp);

        return ResponseData.build(moduleList);
    }

    /**
     * 获取登录用户权限
     *
     * @param request 请求
     * @return 响应数据
     */
    @GetMapping("/permission")
    public ResponseData permission(HttpServletRequest request) {
        log.debug("查询权限列表...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return ResponseData.build(ResponseStatus.NOT_LOGIN);
        }

        // 查询权限列表，同时更新到redis中，保证权限为最新
        UserDetail userDetail = (UserDetail) authentication.getDetails();
        List<String> permissions = !AdminConstant.Username.SUPER_ADMIN.equals(userDetail.getUsername())
                ? this.permissionService.listAuthenticationByAdminId(userDetail.getUserId())
                : this.permissionService.listAuthenticationBySuperAdmin();

        // 从请求头中获取token，如果不存在，则从参数中获取
        String token = request.getHeader(TokenAuthenticationFilter.ACCESS_TOKEN_KEY);
        if (null == token || token.trim().isEmpty()) {
            token = request.getParameter(TokenAuthenticationFilter.ACCESS_TOKEN_KEY);
        }

        // 判断token是否存在
        if (null != token && !token.trim().isEmpty()) {
            // 从redis中查询用户主键
            String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token);
            try {
                AdminCache adminCache = this.adminCacheRedisService.get(tokenKey);

                // 更新redis中的权限列表
                if (null != adminCache) {
                    adminCache.setPermissions(permissions);
                    this.adminCacheRedisService.set(tokenKey, adminCache, this.securityProperties.getSessionTimeout(),
                            TimeUnit.MINUTES);
                }
            } catch (Exception exception) {
                log.warn("获取用户缓存信息异常", exception);
            }
        }

        return ResponseData.build(permissions);
    }

    /**
     * 根据类和变量查询数据字典
     *
     * @param clazz    类
     * @param variable 变量
     * @return 响应数据
     */
    @GetMapping("/data-dictionary")
    public ResponseData list(@RequestParam("clazz") final String clazz,
                             @RequestParam("variable") final String variable) {
        log.debug("查询“{}”类中“{}”字段的数据字典...", clazz, variable);

        return ResponseData.build(this.dataDictionaryService.listByClazzAndVariable(clazz, variable));
    }

    /**
     * 根据所属对象和所属主键查询操作日志
     *
     * @param belong    所属对象
     * @param belongId  所属主键
     * @param orderType 排序类型
     * @return 响应数据
     */
    @GetMapping("/operate-log")
    public ResponseData operateLog(@RequestParam("belong") String belong,
                                   @RequestParam("belongId") String belongId,
                                   @RequestParam(value = "orderType", required = false) String orderType) {
        log.debug("查询“{}”类中主键为“{}”的操作日志...", belong, belongId);

        // 设置默认倒序排序
        if (null == orderType || orderType.isEmpty()) {
            orderType = "desc";
        }

        List<AdminOperateLogVO> adminOperateLogs = this.adminOperateLogService.listAdminOperateLogByBelong(belong, belongId, orderType);
        return ResponseData.build(adminOperateLogs);
    }

    /**
     * 处理模块层级关系（递归）
     *
     * @param moduleListSource 模块源列表
     * @param moduleSource     模块源
     */
    private void handleModuleLevel(List<ModuleListVO> moduleListSource, ModuleListVO moduleSource) {
        // 如果源模块列表为空，即没有可在分供处理的模块了，则直接返回
        if (null == moduleListSource || moduleListSource.isEmpty()) {
            return;
        }

        // 获取子模块
        List<ModuleListVO> children = moduleSource.getChildren();

        // 浅拷贝创建一个缓存列表，用于遍历（因源列表需要移除，所以不能用于遍历）
        List<ModuleListVO> moduleListSourceTemp = new LinkedList<ModuleListVO>(moduleListSource);

        // 遍历源列表
        for (ModuleListVO moduleSourceTemp : moduleListSourceTemp) {
            // 如果结果列表的主键和结果源列表中的上级主键不一致，则开始下一轮循环
            if (!moduleSource.getId().equals(moduleSourceTemp.getSuperior())) {
                continue;
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            if (null == children) {
                // 如果子模块为空，则新建对象存放
                children = new LinkedList<ModuleListVO>();
                moduleSource.setChildren(children);
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            children.add(moduleSourceTemp);
            moduleListSource.remove(moduleSourceTemp);
        }

        // 如果子模块没有需要再处理的列表，则直接结束
        if (null == children || children.isEmpty()) {
            return;
        }

        // 通过递归，处理当前子模块的层级关系
        for (ModuleListVO module : children) {
            this.handleModuleLevel(moduleListSource, module);
        }
    }

}
