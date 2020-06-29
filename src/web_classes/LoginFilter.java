package web_classes;

import jsf_managed_beans.UserManagedBean;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


//@WebFilter(urlPatterns = {"/Dorst19/boss/*", "/Dorst19/customer/*"})
//@WebFilter("/Dorst19/boss/*")
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Inject
    private UserManagedBean userManagedBean;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String loginURI = httpRequest.getContextPath() + "/login";

        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);

        boolean isLoginPage = httpRequest.getRequestURI().contains("/login.xhtml");

        if (userManagedBean.isLoggedIn() && (isLoginRequest || isLoginPage)) {
            // the user is already logged in and he's trying to login again
            // then forwards to the admin's homepage
            userManagedBean.attemptLogin();
            /**if (userManagedBean.isUserCustomer()) {

                RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/customer.xhtml");
                dispatcher.forward(request, response);
            }
            if (userManagedBean.isUserBoss()) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/boss/boss.xhtml");
                dispatcher.forward(request, response);
            }**/

        } else {
            // continues the filter chain
            // allows the request to reach the destination
            chain.doFilter(request, response);
        }

    }

    public void destroy() {
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}