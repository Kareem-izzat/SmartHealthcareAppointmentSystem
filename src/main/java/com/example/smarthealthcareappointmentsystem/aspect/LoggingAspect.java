package com.example.smarthealthcareappointmentsystem.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    // log info about rest
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    // to log info about appointments
    @Pointcut("execution(* com.example.smarthealthcareappointmentsystem.service.AppointmentService.*(..))")
    public void appointmentServicePointcut(){}
    // to log info about prescriptions
    @Pointcut("execution(* com.example.smarthealthcareappointmentsystem.service.PrescriptionService.*(..))")
    public void prescriptionServicePointcut(){}


    @Pointcut("within(com.example.smarthealthcareappointmentsystem..*)")
    public void applicationPackagePointcut() {}

    @AfterReturning(pointcut = "prescriptionServicePointcut() && appointmentServicePointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String details = switch (method) {
            case "bookAppointment" -> "Patient [" + args[0] + "] booked appointment with slot [" + args[1] + "]";
            case "cancelAppointment" -> "Patient [" + args[0] + "] cancelled appointment [" + args[1] + "]";
            case "addPrescription" -> "Doctor [" + args[0] + "] added prescription for patient [" + args[1] + "]";
            default -> "Executed " + method + " by user [" + username + "]";
        };

        log.info(details);
    }


    // log info after throwing any exception
    @AfterThrowing(pointcut = "applicationPackagePointcut() || springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with message = {} and cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                e.getMessage(),
                e.getCause() != null ? e.getCause() : "NULL");
    }

    // log methods that the program use when executed
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
