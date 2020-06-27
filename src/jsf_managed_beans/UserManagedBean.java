package jsf_managed_beans;

import ejb.QueryBean;
import ejb.UserBean;
import jpa.entities.*;
import utilities.UserType;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Named
@SessionScoped
public class UserManagedBean implements Serializable {

    @EJB
    UserBean userBean;

    @EJB
    QueryBean queryBean;

    User user = null;

    String username = null;
    String password = null;
    String loginStatus = null;
    UserType userType = null;

    float amountToAdd = 0;

    private static DecimalFormat geldFormat = new DecimalFormat("0.00");

    public String attemptLogin()
    {
        if(username != null && password != null)
        {
            User login_user = userBean.validateUser(username, password);
            if(login_user != null)
            {
                user = login_user;
                loginStatus = "logged in as " + login_user.getUsername();
                if(isUserCustomer()) return "customer";
                if(isUserBoss()) return "boss";
            }
            else loginStatus = "User doesn't exist";
        }
        return "/index.xhtml";
    }

    public boolean isLoggedIn()
    {
        return user != null;
    }

    public String getLoginStatus()
    {
        if(!isLoggedIn()) return "User not logged in";
        return loginStatus;
    }

    public String logout()
    {
        user = null;
        return "/index.xhtml";
    }


    public String createUser()
    {

        if(username != null && password != null && userType != null)
        {
            User new_user = userBean.createUser(username, password, userType);
            if(new_user != null)
            {
                return attemptLogin();
            }
            else loginStatus = "User already exists";
        }
        return "/index.xhtml";
    }

    public String removeUser()
    {
        if(username != null && password != null)
        {
            userBean.removeUser(getUser().getUsername());
            user = null;
            loginStatus = "User not logged in";
        }
        return "/index.xhtml";
    }

    public boolean isUserBoss()
    {
        if(getUser() != null) return user instanceof BarBoss;
        else return false;
    }

    public boolean isUserCustomer()
    {
        if(getUser() != null) return user instanceof Customer;
        else return false;
    }

    public String getUserCredit()
    {
        geldFormat.setRoundingMode(RoundingMode.UP);
        return geldFormat.format(((Customer)getUser()).getCredit());
    }

    public void addCredit()
    {
        if(amountToAdd > 0) userBean.addCreditToUser((Customer)getUser(), amountToAdd);
    }










    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public User getUser() {
        return userBean.refreshUser(user);
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public float getAmountToAdd() {
        return amountToAdd;
    }

    public void setAmountToAdd(float amountToAdd) {
        this.amountToAdd = amountToAdd;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public QueryBean getQueryBean() {
        return queryBean;
    }

    public void setQueryBean(QueryBean queryBean) {
        this.queryBean = queryBean;
    }
}
