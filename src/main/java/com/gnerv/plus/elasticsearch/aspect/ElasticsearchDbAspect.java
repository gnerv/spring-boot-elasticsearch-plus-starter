package com.gnerv.plus.elasticsearch.aspect;

import cn.hutool.core.util.ReflectUtil;
import com.gnerv.plus.elasticsearch.annotation.ElasticsearchDB;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ligen
 */
@Slf4j
@Aspect
@Component
public class ElasticsearchDbAspect {

    /**
     *
     * //@Pointcut("execution(* com.test.spring.aop.pointcutexp..JoinPointObjP2.*(..))")
     * 	//@Pointcut("within(com.test.spring.aop.pointcutexp..*)")
     * 	//@Pointcut("this(com.test.spring.aop.pointcutexp.Intf)")
     * 	//@Pointcut("target(com.test.spring.aop.pointcutexp.Intf)")
     * 	//@Pointcut("@within(org.springframework.transaction.annotation.Transactional)")
     * 	//@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
     */
    @Pointcut("@annotation(com.gnerv.plus.elasticsearch.annotation.ElasticsearchDB)")
    public void annotation() {
    }

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Before("annotation()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Method methodByName = ReflectUtil.getMethodByName(joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();

        Class<?>[] classes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            classes[i] = args[0].getClass();
        }

        Method method1 = ReflectUtil.getMethod(joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                classes);

        ElasticsearchDB annotation = method1.getAnnotation(ElasticsearchDB.class);
        String value = annotation.value();

        // 记录下请求内容
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @AfterReturning(returning = "ret", pointcut = "annotation()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("RESPONSE : " + ret);
        log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }


}

