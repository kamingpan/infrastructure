package com.kamingpan.infrastructure.core.response;

import com.kamingpan.infrastructure.util.conversion.PropertiesConversion;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

/**
 * 响应状态
 *
 * @author kamingpan
 * @since 2016-04-06
 */
@Slf4j
public enum ResponseStatus {

    SUCCESS("00000", "SUCCESS"),
    ERROR("99999", "ERROR"),

    LOGIN_ERROR("01000", "管理端登录异常"),
    NOT_LOGIN("01001", "未登录"),
    REMOVED_LOGIN("01002", "登录被移除"),
    INVALID_ACCESS_TOKEN("01101", "无效的access token"),
    INVALID_REFRESH_TOKEN("01102", "无效的refresh token"),
    INVALID_CLIENT("01103", "无效的客户端"),
    INVALID_USERNAME_OR_PASSWORD("01104", "用户名或密码错误"),

    PERMISSION_ERROR("02000", "权限异常"),
    NOT_PERMISSION("02001", "无操作权限"),

    INTERFACE_ERROR("03000", "接口返回异常"),

    PARAMETER_ERROR("04000", "参数异常"),
    PARAMETER_NOT_SET("04001", "参数未设置"),
    VALIDATE_ERROR("04100", "参数校验异常"),

    SERVER_ERROR("05000", "服务器异常"),

    DATABASE_ERROR("06000", "数据库异常"),
    DATA_IS_EXIST("06001", "数据已存在"),
    DATA_IS_NOT_EXIST("06002", "数据不存在"),
    DATA_CAN_NOT_DELETE("06003", "数据不允许删除"),

    LOG_ERROR("07000", "日志异常"),
    ;

    private String status;
    private String message;

    static {
        try {
            // 读取响应状态的配置文件
            final Properties properties = PropertiesConversion.convertToProperties("response/response-status.properties");

            // 通过枚举静态方法values()遍历和赋值
            for (ResponseStatus responseStatus : ResponseStatus.values()) {
                if (null != properties.get(responseStatus.status)) {
                    responseStatus.message = properties.get(responseStatus.status).toString();
                }
            }

            // 通过反射遍历枚举和赋值
            /*for (ResponseStatus responseStatus : ResponseStatus.class.getEnumConstants()) {
                if (null != properties.get(responseStatus.status)) {
					responseStatus.message = properties.get(responseStatus.status).toString();
				}
			}*/
        } catch (IOException exception) {
            log.error("读取响应状态的配置文件异常", exception);
        }
    }

    ResponseStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
