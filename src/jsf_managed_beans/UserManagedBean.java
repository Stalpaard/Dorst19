package jsf_managed_beans;

import ejb.DorstException;
import ejb.QueryBean;
import ejb.UserBean;
import jpa.entities.*;
import utilities.UserType;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
        try {
            User login_user = userBean.authenticateUser(username, password);
            user = login_user;
            loginStatus = "logged in as " + login_user.getUsername();
            if(isUserCustomer()) return "customer";
            if(isUserBoss()) return "boss";
        }
        catch (EJBException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Login failed", e.getMessage()));
            return "login";
        }
        return "login";
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

    public void logout()
    {
        username = null;
        password = null;
        user = null;
    }



    public String createUser()
    {
        try{
            User new_user = userBean.createUser(username, password, userType);
            if(new_user != null)
            {
                return attemptLogin();
            }
            else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failed to create user", "User already exists"));
            return "login";
        }
        catch(DorstException e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid credentials", e.getMessage()));
            return "login";
        }
    }

    public void removeUser()
    {
        if(username != null && password != null)
        {
            userBean.removeUser(getUser().getUsername());
            user = null;
        }
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
        if(amountToAdd > 0)
        {
            if(userBean.addCreditToUser((Customer)getUser(), amountToAdd))
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Credit added", amountToAdd + " was added to user credit"));
            }
            else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "CREDIT ERROR", "Internal server error, try again later"));
        }
        else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credit amount", "Amount has to greater than 0"));
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
