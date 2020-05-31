package jsf_managed_beans;

import ejb.*;
import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.*;
import utilities.UserType;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.DiscriminatorValue;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Named
@SessionScoped
public class DemoManagedBean implements Serializable {
    @EJB
    QueryBean queryBean;

    @EJB
    BarCreationBean barCreationBean;

    @EJB
    BarManagementBean barManagementBean;

    @EJB
    UserBean userBean;

    String username = null;
    String password = null;
    UserType userType;

    String cafeNaam = null;
    String cafeStraat = null;
    String cafeStad = null;
    int cafeCapaciteit = 0;

    String drank_name;
    float drank_alc;
    float drank_vol;
    float price;
    int stock;

    User user = null;

    int managedCafeId = -1;
    int menuEntryId = -1;



    public void loginUser()
    {
        if(username != null && password != null) user = userBean.validateUser(username, password);
    }

    public boolean isLoggedIn()
    {
        return user != null;
    }

    public String loginStatus()
    {
        if(isLoggedIn()) return username + " logged in";
        else return "User not logged in!";
    }

    public void logoutUser()
    {
        user = null;
        if(barManagementBean.isManaged()) barManagementBean.detachBar();
    }

    public void createUser()
    {
        if(username != null && password != null && userType != null) user = userBean.createUser(username, password, userType);
    }

    public void removeUser()
    {
        if(username != null && password != null)
        {
            userBean.removeUser(user.getUsername());
            user = null;
        }
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



    public String queryCafes(){
        String s = "";
        for(Bar b : queryBean.queryBars()) s = s + " " + b.getBarInfo().getName();
        return s;
    }

    public String queryUsers(){
        String s = "";
        for(String u : queryBean.queryUsers()) s = s + " " + u;
        return s;
    }

    public String queryDrinks()
    {
        String s = "";
        List<MenuEntry> menu = barManagementBean.getMenu();
        for(MenuEntry m : menu) s = s + " " + m.getItem().getName();
        return s;
    }


    public Map<String, Object> getOwnedCafes()
    {
        Map<String,Object> ownedBars = new TreeMap<>();
        for(Bar owned : ((BarBoss)user).getOwnedBars())
        {
            ownedBars.put(owned.getBarInfo().getName(),owned.getId());
        }
        return ownedBars;
    }

    public Map<String, Object> getMenuMap()
    {
        Map<String, Object> menumap = new LinkedHashMap<>();
        for(MenuEntry m : barManagementBean.getMenu())
        {
            Item item = m.getItem();
            String key = "";
            if(item instanceof DrinkItem) key = item.getName() + ((DrinkItem)item).getVolume();
            menumap.put(key, m.getId());
        }
        return menumap;
    }

    public void createCafe() {
        if(user != null) {
            Address address = new Address(cafeStraat, cafeStad);
            BarInfo barInfo = new BarInfo(cafeNaam, address);
            barCreationBean.createBar((BarBoss)user, barInfo, cafeCapaciteit);
        }
    }

    public String manageCafe() {
        //Om de named query te testen heb ik het zo gedaan
        if(managedCafeId > -1) {
            boolean b = barManagementBean.attachBar(managedCafeId);
            if(b) return "epic";
            else return "fknheel";
        }
        return "kkkkk";
    }

    public void unmanageCafe()
    {
        barManagementBean.detachBar();
    }

    public boolean isCafeManaged()
    {
        return barManagementBean.isManaged();
    }

    public String managedCafeNaam()
    {
        if(barManagementBean.isManaged())
            return barManagementBean.getManagedBarInfo().getName();
        else return "";
    }

    public void removeCafe() {
        //remove attached bar
        barManagementBean.removeBar();
    }


    public void addDrinkToMenu()
    {
        DrinkItem drinkItem = new DrinkItem(drank_name, drank_alc, drank_vol);
        barManagementBean.addMenuItem(drinkItem, price, stock);
    }

    public void removeDrinkFromMenu()
    {
        barManagementBean.removeMenuItem(menuEntryId);
    }



    public int getManagedCafeId() {
        return managedCafeId;
    }

    public void setManagedCafeId(int managedCafeId) {
        this.managedCafeId = managedCafeId;
    }

    public String getDrank_name() {
        return drank_name;
    }

    public void setDrank_name(String drank_name) {
        this.drank_name = drank_name;
    }

    public float getDrank_alc() {
        return drank_alc;
    }

    public void setDrank_alc(float drank_alc) {
        this.drank_alc = drank_alc;
    }

    public float getDrank_vol() {
        return drank_vol;
    }

    public void setDrank_vol(float drank_vol) {
        this.drank_vol = drank_vol;
    }

    public int getMenuEntryId() {
        return menuEntryId;
    }

    public void setMenuEntryId(int menuEntryId) {
        this.menuEntryId = menuEntryId;
    }

    public float getPrice() {
        return price;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCafeNaam() {
        return cafeNaam;
    }

    public void setCafeNaam(String cafeNaam) {
        this.cafeNaam = cafeNaam;
    }

    public String getCafeStraat() {
        return cafeStraat;
    }

    public void setCafeStraat(String cafeStraat) {
        this.cafeStraat = cafeStraat;
    }

    public String getCafeStad() {
        return cafeStad;
    }

    public void setCafeStad(String cafeStad) {
        this.cafeStad = cafeStad;
    }

    public int getCafeCapaciteit() {
        return cafeCapaciteit;
    }

    public void setCafeCapaciteit(int cafeCapaciteit) {
        this.cafeCapaciteit = cafeCapaciteit;
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
}
