package web_classes;

import jsf_managed_beans.UserManagedBean;

import java.io.IOException;
import java.util.Random;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class LoginFilter implements Filter {

    @Inject
    private UserManagedBean userManagedBean;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        Random rand = new Random();

        int rand1 = rand.nextInt(10);
        int rand2 = rand.nextInt(100);
        if (userManagedBean.isLoggedIn() && userManagedBean.isUserCustomer() && rand1 == 1) {
            session.setAttribute("message", "Surprise: " + rand2 + " Credits added");
            session.setAttribute("amountToAdd", (float) rand2);
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}