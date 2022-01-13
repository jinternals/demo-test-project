package com.jinternals.demo.utils;

import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

//Test util for testing annotated beans
public class ValidationTestUtil {
   public static <T> T validationProxy(T target) {
        if(AnnotationUtils.isAnnotated(target.getClass(), Validated.class))
        {
            ProxyFactory factory = new ProxyFactory(target);
            MethodBeforeAdvice beforeAdvice = new MethodBeforeAdvice() {
                private ExecutableValidator executableValidator;

                {
                    LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
                    factory.afterPropertiesSet();
                    executableValidator = factory.getValidator().forExecutables();
                    factory.close();
                }

                @Override
                public void before(Method method, Object[] args, Object target) {
                    Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(target, method, args);
                    if (!violations.isEmpty()) {
                        throw new ConstraintViolationException(violations);
                    }
                }
            };
            factory.addAdvice(beforeAdvice);
            return (T) factory.getProxy();
        }

        return target;
    }
}
