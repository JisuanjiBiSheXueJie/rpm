package com.rpm.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rpm.annotations.AutoLoginAnno;
import com.rpm.annotations.AutoOptAnno;
import com.rpm.domain.SysLogLogin;
import com.rpm.domain.SysOperationLog;
import com.rpm.domain.User;
import com.rpm.service.ISysLogLoginService;
import com.rpm.service.ISysOperationLogService;
import com.rpm.service.IUserService;
import com.rpm.utils.request.HttpContextUtils;
import com.rpm.utils.request.IpUtil;
import com.rpm.utils.shiro.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 登录切面
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {

    @Autowired
    private ISysLogLoginService iSysLogLoginService;

    @Autowired
    private ISysOperationLogService iSysOperationLogService;

    @Autowired
    private IUserService userService;

    /**
     * 登录日志切点
     */
    @Pointcut("@annotation(com.rpm.annotations.AutoLoginAnno)")
    public void loginPointcut(){}

    /**
     * 操作日志切入点
     */
    @Pointcut("@annotation(com.rpm.annotations.AutoOptAnno)")
    public void optPointcut(){}

    /**
     * 登录环绕通知
     */
    @Around("loginPointcut()")
    public Object login(ProceedingJoinPoint joinPoint) throws Throwable {
        //开始执行时间
        long beginTime = System.currentTimeMillis();
        //执行的方法
        Object result = joinPoint.proceed();
        //一共执行的时间
        long time = System.currentTimeMillis() - beginTime;

        log.info("===========登录操作执行时间==============>"+time);
        //当前登陆者token
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        if (token!=null){
            //保存日志
            saveLogin(joinPoint);
        }

        return result;
    }

    /**
     *操作环绕通知
     */
    @Around("optPointcut()")
    public Object opt(ProceedingJoinPoint joinPoint) throws Throwable {
        //开始执行时间
        long beginTime = System.currentTimeMillis();
        //执行的方法
        Object result = joinPoint.proceed();
        //一共执行的时间
        long time = System.currentTimeMillis() - beginTime;

        log.info("===========操作执行时间==============>"+time);
        //保存日志
        saveOpt(joinPoint);
        return result;
    }

    /**
     * 记录登录日志
     * @param joinPoint
     */
    public void saveLogin(ProceedingJoinPoint joinPoint){
        //通过反射获取注解方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //得到注解
        AutoLoginAnno autoLogin = method.getAnnotation(AutoLoginAnno.class);
        SysLogLogin sysLogLogin = new SysLogLogin();

        //获取当前操作
        sysLogLogin.setOpt(autoLogin.loginEnum());

        //当前登陆者token
        String token = (String) SecurityUtils.getSubject().getPrincipal();

        //解析token
        Map<String, Object> user = JWTUtil.getUsername(token);
        //当前用户
        String username = (String) user.get("username");
        String role = (String) user.get("role");

        //通过用户名查询到对应用户id
        User currUser = userService.getOne(new QueryWrapper<User>().eq("username", username).last("limit 1"));

        sysLogLogin.setLoginname(username);
        sysLogLogin.setLoginRole(role);
        sysLogLogin.setLoginId(currUser.getId());

        //IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        sysLogLogin.setIp(IpUtil.getIpAddr(request));

        boolean save = iSysLogLoginService.save(sysLogLogin);
        if (save){
            log.info("==========登录日志写入==========");
        }else {
            log.info("==========执行出错==========");
        }
    }


    /**
     * 记录操作日志
     */
    public void saveOpt(ProceedingJoinPoint joinPoint){
        //通过反射获取注解方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //操作注解
        AutoOptAnno annotation = method.getAnnotation(AutoOptAnno.class);
        SysOperationLog sysOperationLog = new SysOperationLog();

        //执行的方法
        log.info("============执行的方法的===============>"+method.getName());
        sysOperationLog.setMethods(method.getName());

        //描述
        String detail = annotation.detail();
        log.info("===============描述=================>"+detail);
        sysOperationLog.setDetail(detail);

        //操作等级
        sysOperationLog.setLogLevel(annotation.levelEnum());
        //操作方法
        sysOperationLog.setOptType(annotation.optTypeEnum());

        //当前登陆者token
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        //解析token
        Map<String, Object> user = JWTUtil.getUsername(token);
        //当前用户
        String username = (String) user.get("username");
        String role = (String) user.get("username");
        //当前操作人和角色
        sysOperationLog.setOptname(username);
        sysOperationLog.setRole(role);

        //IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        sysOperationLog.setIp(IpUtil.getIpAddr(request));

        boolean save = iSysOperationLogService.save(sysOperationLog);
        if (save){
            log.info("==========操作日志写入==========");
        }else {
            log.info("==========执行出错==========");
        }
    }
}
