package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.AdminConstant;
import com.kamingpan.infrastructure.entity.group.OperatorGroup;
import com.kamingpan.infrastructure.entity.model.dto.AdminDTO;
import com.kamingpan.infrastructure.entity.model.entity.Admin;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminVO;
import com.kamingpan.infrastructure.entity.service.AdminService;
import com.kamingpan.infrastructure.management.security.cache.AdminCache;
import com.kamingpan.infrastructure.management.security.filter.TokenAuthenticationFilter;
import com.kamingpan.infrastructure.management.security.provider.TokenAuthenticationProvider;
import com.kamingpan.infrastructure.management.util.OperatorUtil;
import com.kamingpan.infrastructure.util.md5.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作员 controller
 *
 * @author kamingpan
 * @since 2017-08-28
 */
@Slf4j
@RestController
@RequestMapping("/operator")
public class OperatorController extends BaseController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisService<String, AdminCache> adminCacheRedisService;

    /**
     * 获取操作员信息
     *
     * @return 响应数据
     */
    @GetMapping("")
    public ResponseData info() {
        String id = OperatorUtil.getOperatorId();
        AdminVO admin = null == id ? null : this.adminService.getAdminById(id);

        if (null != admin) {
            admin.setStatus(null); // 状态不返回
            admin.setRestrictIp(null); // 限制ip不返回
            admin.setUpdater(null); // 最后更新人不返回
            admin.setUpdateTime(null); // 最后更新时间不返回
        }
        return ResponseData.build(admin);
    }

    /**
     * 操作员修改
     *
     * @param adminDTO 管理员
     * @return 响应数据
     */
    @PutMapping("")
    public ResponseData update(@ModelAttribute @Validated(OperatorGroup.Update.class) AdminDTO adminDTO) {
        log.debug("更新操作员“{}”...", adminDTO.getUsername());

        Admin admin = adminDTO.toAdmin();
        admin.setId(OperatorUtil.getOperatorId());
        admin.setUsername(null); // 用户名不允许做修改
        admin.setPassword(null); // 密码不允许做修改
        admin.setSuperior(null); // 上级不允许做修改
        admin.setStatus(null); // 状态不允许做修改
        admin.setRestrictIp(null); // 限制ip不允许做修改
        this.adminService.update(admin, new AdminOperateLog());

        return ResponseData.success();
    }

    /**
     * 修改个人密码
     *
     * @param oldPassword 旧密码
     * @param password    新密码
     * @param request     请求
     * @return 响应数据
     */
    @PatchMapping("/password")
    public ResponseData password(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("password") String password, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return ResponseData.build(ResponseStatus.NOT_LOGIN);
        }
        log.debug("“{}”修改密码...", authentication.getDetails());

        // 根据主键查询管理员
        Admin admin = this.adminService.getById(authentication.getPrincipal().toString());

        // 对比新旧密码
        if (null == password || password.isEmpty()) {
            throw new ValidateException("新密码不能为空");
        }
        if (password.equals(oldPassword)) {
            throw new ValidateException("新密码和旧密码不能相同");
        }

        // 验证MD5加密密码
        StringBuffer stringBuffer = new StringBuffer(oldPassword);
        stringBuffer.append(AdminConstant.Password.LEFT_BRACKET).append(admin.getUsername())
                .append(AdminConstant.Password.RIGHT_BRACKET);
        String md5Password;
        try {
            md5Password = MD5.encryption(stringBuffer.toString());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            throw new ValidateException("MD5加密错误，请重新输入");
        }

        // 校验旧密码
        if (!admin.getPassword().equals(md5Password)) {
            throw new ValidateException("旧密码错误，请重新输入");
        }

        stringBuffer = new StringBuffer(password);
        stringBuffer.append(AdminConstant.Password.LEFT_BRACKET).append(admin.getUsername())
                .append(AdminConstant.Password.RIGHT_BRACKET);
        try {
            md5Password = MD5.encryption(stringBuffer.toString());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            throw new ValidateException("MD5加密错误，请重新输入");
        }

        // 修改用户密码
        Admin temp = new Admin();
        temp.setId(admin.getId());
        temp.setPassword(md5Password);
        this.adminService.updatePassword(temp, new AdminOperateLog());

        // 移除当前登录缓存，让管理员重新登录系统
        // 从请求头中获取token，如果不存在，则从参数中获取
        String token = request.getHeader(TokenAuthenticationFilter.ACCESS_TOKEN_KEY);
        if (null == token || token.trim().isEmpty()) {
            token = request.getParameter(TokenAuthenticationFilter.ACCESS_TOKEN_KEY);
        }

        // 判断token是否存在
        if (null != token && !token.trim().isEmpty()) {
            try {
                // 从redis中移除用户缓存
                this.adminCacheRedisService.delete(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token));
            } catch (Exception exception) {
                log.warn("移除用户缓存信息异常", exception);
            }
        }

        return ResponseData.build(ResponseStatus.SUCCESS.getStatus(), "修改成功，请重新登录");
    }

    /**
     * 校验个人密码
     *
     * @param password 密码
     * @return 响应数据
     */
    @GetMapping("/password-verifier")
    public ResponseData passwordVerifier(@RequestParam("password") String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication) {
            return ResponseData.build(ResponseStatus.NOT_LOGIN);
        }
        log.debug("校验管理员“{}”个人密码...", authentication.getDetails());

        // 根据主键查询管理员
        Admin admin = this.adminService.getById(authentication.getPrincipal().toString());

        if (null == password || password.isEmpty()) {
            throw new ValidateException("旧密码不能为空");
        }

        // 验证MD5加密密码
        String md5Password = password + AdminConstant.Password.LEFT_BRACKET + admin.getUsername()
                + AdminConstant.Password.RIGHT_BRACKET;
        try {
            md5Password = MD5.encryption(md5Password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            throw new ValidateException("MD5加密错误，请重新输入");
        }

        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", admin.getPassword().equals(md5Password));
        return ResponseData.build(result);
    }

}
