package pl.mprzybysz.app.exception.advice;

import javax.annotation.Priority;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
@Priority(value = 0)
public class SecurityExceptionAdvice implements SecurityAdviceTrait {

}
