package com.kamingpan.infrastructure.core.log;

import com.kamingpan.infrastructure.core.base.model.OperatorLog;
import com.kamingpan.infrastructure.core.base.service.OperatorLogService;
import com.kamingpan.infrastructure.core.exception.LogErrorException;
import com.kamingpan.infrastructure.util.ip.IP;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求记录切面
 *
 * @author kamingpan
 * @since 2016-08-30
 */
@Aspect
@Component
public class OperateLogAop {

    private static final String operateLogClass = "AdminOperateLog";

    @Autowired
    private OperatorLogService operatorLogService;

    //配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
    @Pointcut("@annotation(com.kamingpan.infrastructure.core.log.OperateLog)")
    public void aspect() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint 切入点
     * @return 对象
     * @throws Throwable 异常
     */
    @SuppressWarnings("unchecked")
    @Around("aspect() && @annotation(operateLog)")
    public Object process(ProceedingJoinPoint joinPoint, OperateLog operateLog) throws Throwable {

        // 执行切面方法
        Object object = joinPoint.proceed();

        // 获取注解信息
        String clazz = operateLog.clazz();

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            // 如果对象为空，或者对象名和传进来的不一致，或者无法强转为操作日志对象，则进行下一次循环
            if (null == arg || !clazz.equals(arg.getClass().getSimpleName()) || !(arg instanceof OperatorLog)) {
                continue;
            }

            // 获取request
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

            // 赋值剩余的日志信息
            OperatorLog operatorLog = (OperatorLog) arg;

            // 判断必需参数是否已赋值
            if (null == operatorLog.getBelong() || operatorLog.getBelong().isEmpty()) {
                throw new LogErrorException();
            } else if (null == operatorLog.getBelongId() || operatorLog.getBelongId().isEmpty()) {
                throw new LogErrorException();
            }

            operatorLog.setType(operateLog.type());
            if (attributes instanceof ServletRequestAttributes) {
                HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
                operatorLog.setIp(IP.getIP(request));
                operatorLog.setUrl(request.getRequestURI() + "(" + request.getMethod() + ")");
            }
            operatorLog.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName());
            operatorLog.initOperator();
            this.operatorLogService.insert(operatorLog, false);
        }

        return object;
    }

}
