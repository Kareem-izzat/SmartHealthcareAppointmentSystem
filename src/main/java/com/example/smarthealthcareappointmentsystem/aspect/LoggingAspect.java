package com.example.smarthealthcareappointmentsystem.aspect;

import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
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

    @AfterReturning(pointcut = "prescriptionServicePointcut() || appointmentServicePointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        String details;

        switch (method) {
            case "bookAppointment" ->
                    details = String.format(
                            "Booked appointment: patientId=%s, slotId=%s",
                            args.length > 0 ? args[0] : "N/A",
                            args.length > 1 ? args[1] : "N/A"
                    );

            case "cancelAppointment" ->
                    details = String.format(
                            "Cancelled appointment: patientId=%s, appointmentId=%s",
                            args.length > 0 ? args[0] : "N/A",
                            args.length > 1 ? args[1] : "N/A"
                    );

            case "addPrescription" -> {
                if (args.length > 0) {
                    RequestPrescriptionDto dto = (RequestPrescriptionDto) args[0];

                    details = String.format(
                            "Added prescription: doctorId=%s, patientId=%s, appointmentId=%s, medicines=%s, notes=%s",
                            dto.getDoctorId(),
                            dto.getPatientId(),
                            dto.getAppointmentId(),
                            dto.getMedicines(),
                            dto.getNotes()
                    );
                } else {
                    details = "addPrescription called with no arguments";
                }
            }

            default ->
                    details = String.format("Executed %s with args=%s", method, Arrays.toString(args));
        }

        log.info(details);
    }


    // log info after throwing any exception
    @AfterThrowing(pointcut = "execution(* com.example.smarthealthcareappointmentsystem.service..*(..))", throwing = "e")
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
