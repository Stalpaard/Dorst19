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

    public void loginUser()
    {
        if(username != null && password != null)
        {
            User login_user = userBean.validateUser(username, password);
            if(login_user != null) user = login_user;
            else loginStatus = "User doesn't exist";
        }
    }

    public boolean isLoggedIn()
    {
        return user != null;
    }

    public String loginStatus()
    {
        if(!isLoggedIn()) return "User not logged in";
        return loginStatus;
    }

    public void logoutUser()
    {
        user = null;
        //if(barManagementBean.isManaged()) barManagementBean.detachBar();
    }

    public String queryUsers(){
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
        if(user != null) return user instanceof BarBoss;
        else return false;
    }

    public boolean isUserCustomer()
    {
        if(user != null) return user instanceof Customer;
        else return false;
    }

    public boolean isUserEmployee()
    {
        if(user != null) return user instanceof BarEmployee;
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

    public float getUserCredit()
    {

        return ((Customer)getUser()).getCredit();
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

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public QueryBean getQueryBean() {
        return queryBean;
    }

    public void setQueryBean(QueryBean queryBean) {
        this.queryBean = queryBean;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}
