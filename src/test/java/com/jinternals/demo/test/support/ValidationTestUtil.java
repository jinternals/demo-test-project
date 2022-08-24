package com.jinternals.demo.test.support;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.validator.HibernateValidator;
import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

//Test util for testing annotated bean methods
public class ValidationTestUtil {
   public static <T> T validationProxy(T target) {
        if(isAnnotated(target.getClass(), Validated.class))
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
                    Class<?>[] groups = determineValidationGroups(method, target);
                    Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(target, method, args, groups);
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

    private static Class<?>[] determineValidationGroups(Method method, Object target) {
        Validated validatedAnn = org.springframework.core.annotation.AnnotationUtils.findAnnotation(method, Validated.class);
        if (validatedAnn == null) {
            Assert.state(target != null, "Target must not be null");
            validatedAnn = org.springframework.core.annotation.AnnotationUtils.findAnnotation(target.getClass(), Validated.class);
        }
        return (validatedAnn != null ? validatedAnn.value() : new Class<?>[0]);
    }

}
