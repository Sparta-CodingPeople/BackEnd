package com.server.delivery.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Aspect
@Slf4j
@Component
public class LogAspect {

    private static final Set<String> SENSITIVE_KEYS = Set.of("password", "token", "code", "accessToken", "refreshToken");

    private static JSONObject getFilteredParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            String value = request.getParameter(param);

            if (isSensitiveKey(replaceParam)) {
                jsonObject.put(replaceParam, "****");
            } else {
                jsonObject.put(replaceParam, value);
            }
        }
        return jsonObject;
    }

    private static boolean isSensitiveKey(String key) {
        return SENSITIVE_KEYS.contains(key.toLowerCase());
    }

    private static Object maskSensitiveData(Object returnValue) {
        if (returnValue == null) {
            return null;
        }

        if (returnValue instanceof String strValue) {
            // JWT 형식 감지 후 마스킹
            if (strValue.matches("^Bearer\\s[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$")) {
                return "Bearer ***JWT_TOKEN_MASKED***";
            }
        } else if (returnValue instanceof Map<?, ?> mapValue) {
            Map<Object, Object> filteredMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
                if (isSensitiveKey(entry.getKey().toString())) {
                    filteredMap.put(entry.getKey(), "****");
                } else {
                    filteredMap.put(entry.getKey(), entry.getValue());
                }
            }
            return filteredMap;
        } else if (returnValue instanceof JSONObject jsonObject) {
            JSONObject filteredJson = new JSONObject(jsonObject.toMap());
            for (String key : SENSITIVE_KEYS) {
                if (filteredJson.has(key)) {
                    filteredJson.put(key, "****");
                }
            }
            return filteredJson;
        }
        return returnValue;
    }

    @Pointcut("within(com.server.delivery.domain..*Controller)")
    public void controller() {
    }

    @Around("controller()")
    public Object loggingBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String controllerName = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        Map<String, Object> params = new HashMap<>();

        try {
            String decodedURI = URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8);

            params.put("controller", controllerName);
            params.put("method", methodName);
            params.put("params", getFilteredParams(request));
            params.put("log_time", System.currentTimeMillis());
            params.put("request_uri", decodedURI);
            params.put("http_method", request.getMethod());
        } catch (Exception e) {
            log.error("LoggerAspect error", e);
        }

        log.info("[{}] {}", params.get("http_method"), params.get("request_uri"));
        log.info("method: {}.{}", params.get("controller"), params.get("method"));
        log.info("params: {}", params.get("params"));

        return joinPoint.proceed();
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterReturningLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("### End Request {}", joinPoint.getSignature().toShortString());

        Object filteredReturnValue = maskSensitiveData(returnValue);
        if (filteredReturnValue != null) {
            log.info("\t{}", filteredReturnValue);
        }
    }

    @Pointcut("within(com.server.delivery.domain..*ServiceImpl)")
    public void service() {
    }

    @Around("service()")
    public Object loggingService(ProceedingJoinPoint joinPoint) throws Throwable {
        String serviceName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Service method started: {}.{}()", serviceName, methodName);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                log.info("Arg[{}]: {}", i, args[i]);
            }
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - startTime;

        log.info("Service method finished: {}.{}() [Execution time: {} ms]", serviceName, methodName, elapsedTime);

        Object filteredResult = maskSensitiveData(result);
        log.info("Return value: {}", filteredResult);

        return result;  // 마스킹된 값이 아닌 원본 값을 반환
    }

    @AfterReturning(pointcut = "service()", returning = "returnValue")
    public void afterReturningServiceLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("### Service method finished: {}", joinPoint.getSignature().toShortString());

        Object filteredReturnValue = maskSensitiveData(returnValue);
        if (filteredReturnValue != null) {
            log.info("Service return value: {}", filteredReturnValue);
        }
    }
}