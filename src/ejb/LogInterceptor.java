package ejb;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class LogInterceptor {
    @AroundInvoke
    public Object aroundInvoke(InvocationContext context) throws Exception {
        System.out.println("EJB call to: " + context.getMethod().getName() + " intercepted");
        return context.proceed();
    }
}
