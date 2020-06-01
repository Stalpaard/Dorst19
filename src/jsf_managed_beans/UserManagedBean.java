package jsf_managed_beans;

import com.sun.faces.component.search.SearchKeywordResolverImplAll;
import ejb.QueryBean;
import ejb.ReservationBean;
import ejb.ReservationCounterBean;
import ejb.UserBean;
import jpa.entities.*;
import utilities.UserType;

import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Named
@DependsOn({
        "QueryBean",
        "UserBean"
})
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

    public void attemptLogin()
    {
        if(username != null && password != null)
        {
            User login_user = userBean.validateUser(username, password);
            if(login_user != null)
            {
                user = login_user;
                loginStatus = "logged in as " + login_user.getUsername();
            }
            else loginStatus = "User doesn't exist";
        }
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
        user = null;
        //if(barManagementBean.isManaged()) barManagementBean.detachBar();
    }

    public String stringOfAllUsers(){
        String s = "";
        for(String u : queryBean.queryUsers()) s = s + " " + u;
        return s;
    }

    public void createUser()
    {

        if(username != null && password != null && userType != null)
        {
            User new_user = userBean.createUser(username, password, userType);
            if(new_user != null)
            {
                user = new_user;
                loginStatus = "User " + user.getUsername() + " is logged in";
            }
            else loginStatus = "User already exists";
        }
    }

    public void removeUser()
    {
        if(username != null && password != null)
        {
            userBean.removeUser(getUser().getUsername());
            user = null;
            loginStatus = "User not logged in";
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

    public boolean isUserEmployee()
    {
        if(getUser() != null) return user instanceof BarEmployee;
        else return false;
    }

    public Map<String, Object> getUserTypes()
    {
        Map<String,Object> userTypes = new LinkedHashMap<>();
        for(UserType type : UserType.values())
        {
            userTypes.put(type.name(), type);
        }
        return userTypes;
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
