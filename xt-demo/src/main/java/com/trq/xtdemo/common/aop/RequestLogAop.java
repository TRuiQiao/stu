package com.trq.xtdemo.common.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 请求日志参数及耗时打印
 */
@Slf4j
@Aspect
@Component
public class RequestLogAop {

    /**
     * 定义一个切入点，
     * 拦截com.trq.xtdemo包下所类名后缀为Controller的所有方法
     */
    @Pointcut("execution(* com.trq.xtdemo..*.*Controller.*(..))")
    private void controllerMethod() {

    }

    @Around("controllerMethod()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        String msgInfo = "Aop--" + Thread.currentThread().getId() + "--[" + point.getSignature().getDeclaringTypeName()
                + "." + point.getSignature().getName() + "]";// 所在的类，方法
        Object[] methodArgs = point.getArgs();
        if (methodArgs != null && methodArgs.length > 0) {
            log.info(msgInfo + "-->>Start>>----输入参数：" + StringUtils.rightPad("", methodArgs.length * 3 - 1, "{}"), methodArgs);
        }
        // 开始时间
        long startTime = System.currentTimeMillis();

        Object result = null;
        try {
            // 执行完方法的返回值，调用proceed()方法，就会触发切入点方法执行，result的值就是被拦截方法的返回值
            result = point.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            // 方法的执行时间
            long handleTime = System.currentTimeMillis() - startTime;
            StringBuffer endStr = new StringBuffer();
            endStr.append(msgInfo).append("-->>end>>----");
            endStr.append("耗时（" + handleTime + "ms）");
            try {
                if (result != null) {
                    if (result instanceof String) {
                        endStr.append(result);
                    } else {
                        JSONObject json = (JSONObject) JSONObject.toJSON(result);
                        endStr.append("reqId:" + json.getString("reqId") + " ");
                        endStr.append(JSONObject.toJSON(json));
                    }

                    if (endStr.length() > 500) {
                        endStr.setLength(500);
                        endStr.append(" ...");
                    }
                }
            } catch (Exception e) {
            }
            log.info(endStr.toString());
        }
        return result;
    }

}
