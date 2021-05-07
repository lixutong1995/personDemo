package com.person.aop;

import com.person.Utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description Controller 方法切面，打印请求参数和返回数据对象信息，并且记录操作日志
 * @Author Xutong Li
 * @Date 2021/5/7
 */
@Aspect
@Component
@Slf4j
public class ControllerMethodLogAop {

    /**
     * 在Controller注解下的所有方法
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param
     * @return void
     */
    @Pointcut("!@annotation(com.person.annotation.NoLog) && @within(org.springframework.stereotype.Controller)")
    public void pointCut(){
        //DO NOTHING
    }

    /**
     * 使用环绕通知，不要使用单独的前后通知
     * 因为前后处理都需要HttpServletRequest，但是HttpServletRequest只能读取一次，第二次读取的熟悉为null
     * 所以在环绕通知的时候，一次性读取所以需要HttpServletRequest处理的参数
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param        
     * @return java.lang.Object
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("======================reqeust begin==============================");
        HttpServletRequest request = RequestUtils.getRequst();
        log.debug(ParamPrintProperty.REQUEST_ADDRESS + request.getRequestURI());
        log.debug(ParamPrintProperty.REQUEST_METHOD_TYPE + request.getMethod());

        //打印方法请求参数
        printMethodParams(proceedingJoinPoint);
        log.debug("======================reqeust end==============================");
        log.debug(ParamPrintProperty.WRAP);

        //执行方法
        //调用方法
        Object result = null;

        try {
            //必须要获取返回值并return，否则页面获取不到请求的返回数据
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    /**
     * 打印方法请求参数
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param proceedingJoinPoint
     * @return void
     */
    private void printMethodParams(ProceedingJoinPoint proceedingJoinPoint) throws ClassNotFoundException {
        Class<?> target = proceedingJoinPoint.getTarget().getClass();
        String className = target.getName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        log.debug(ParamPrintProperty.API_THE_METHOD_CALLED_IS, methodName);

        //获取方法参数名称
        String[] paramArgNames = getFieldName(className, methodName);

        //获取方法参数值数组
        Object[] paramArgValues = proceedingJoinPoint.getArgs();

        //如果没有参数名称,return
        if(ArrayUtils.isEmpty(paramArgNames)){
            return;
        }

        StringBuilder stringBuilder = new StringBuilder(ParamPrintProperty.REQUEST_PARAM);
        for(int i = 0; i < paramArgNames.length; i++){
            //参数名
            String argName = paramArgNames[i];
            if(!ParamPrintProperty.REQUEST.equals(argName) && !ParamPrintProperty.RESPONSE.equals(argName)){
                //参数值
                Object argValue = paramArgValues[i];
                argValue = Optional.ofNullable(argValue).orElse("null");
                stringBuilder.append(argName.concat(ParamPrintProperty.EQUAL));
                stringBuilder.append(argValue.toString());
            }
        }
        log.debug(stringBuilder.toString());
    }

    /**
     * 使用javassist来获取方法参数名称
     * @Author Xutong Li
     * @Date 2021/5/7
     * @param className
     * @param methodName       
     * @return java.lang.String[]
     */
    private String[] getFieldName(String className, String methodName) throws ClassNotFoundException{
        Class<?> clazz = Class.forName(className);
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(clazz));

        CtMethod ctMethod = null;

        try {
            ctMethod = pool.get(clazz.getName()).getDeclaredMethod(methodName);
        } catch (NotFoundException e) {
            //DO NOTHING
        }

        if(Objects.isNull(ctMethod)){
            return new String[0];
        }

        CodeAttribute codeAttribute = ctMethod.getMethodInfo().getCodeAttribute();
        LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        if(Objects.isNull(attribute)){
            return new String[0];
        }

        String[] paramArgNames = new String[0];

        try {
            paramArgNames = new String[ctMethod.getParameterTypes().length];
        } catch (NotFoundException e) {
            //DO NOTHING
        }

        for(int i = 0; i < paramArgNames.length; i++){
            paramArgNames[i] = attribute.variableName(i + (Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1));
        }
        return paramArgNames;
    }

    private static class ParamPrintProperty {

        public static final String WRAP = "\n";

        public static final String API_THE_METHOD_CALLED_IS = "Api Method: {}";

        public static final String REQUEST_ADDRESS = "Request Address: ";
        public static final String REQUEST_PARAM = "Request Param: ";

        public static final String REQUEST_METHOD_TYPE = "Request Method Type: ";

        public static final String EQUAL = " = ";

        public static final String JSON_RESULT = "Response Result:";

        public static final String REQUEST = "request";

        public static final String RESPONSE = "response";

    }

}
