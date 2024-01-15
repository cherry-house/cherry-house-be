package com.cherryhouse.server._core.exception;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;

@Aspect
@Component
public class GlobalValidationHandler {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {}

    @Before("postMapping() || putMapping()")
    public void validationAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        Arrays.stream(args)
                .filter(Errors.class::isInstance)
                .forEach(arg -> {
                    Errors errors = (Errors) arg;

                    if (errors.hasErrors()) {
                        FieldError error = errors.getFieldErrors().get(0);
                        System.out.println(error.getDefaultMessage() + ":" + error.getField());
                        throw new ApiException(
                                ExceptionCode.INVALID_REQUEST_DATA,
                                error.getDefaultMessage() + ":" + error.getField()
                        );
                    }
                });
    }
}
