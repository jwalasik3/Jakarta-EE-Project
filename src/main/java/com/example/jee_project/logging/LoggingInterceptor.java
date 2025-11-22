package com.example.jee_project.logging;
import com.example.jee_project.logging.annotation.LoggedOperation;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;
import java.util.logging.Logger;

@Interceptor
@LoggedOperation
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {
    private static final Logger logger = Logger.getLogger(LoggingInterceptor.class.getName());

    @Inject
    private SecurityContext securityContext;

    @AroundInvoke
    public Object logMethod(InvocationContext ctx) throws Exception {
        String user = "unknown";
        try {
            if (securityContext != null && securityContext.getCallerPrincipal() != null) {
                user = securityContext.getCallerPrincipal().getName();
            }
        } catch (Exception e) {
            // ignore
        }

        String method = ctx.getMethod().getName();
        Object target = ctx.getTarget();
        Object[] params = ctx.getParameters();

        logger.info("User: " + user + " invoked method: " + method + " on " + target + " with params: " + java.util.Arrays.toString(params));
        System.out.println("User: " + user + " invoked method: " + method + " on " + target + " with params: " + java.util.Arrays.toString(params));
        return ctx.proceed();
    }
}
