package jsf_managed_beans;

import ejb.*;
import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.*;
import utilities.UserType;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

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
    ReservationBean reservationBean;

    @EJB
    ReservationCounterBean reservationCounterBean;

    @EJB
    UserBean userBean;

    String username = null;
    String password = null;
    String loginStatus = null;
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

    int reservationCafeId = -1;
    Map<String,Object> reservationMenu;
    int reservationMenuEntryId = -1;
    int reservationAmount = -1;
    int removeReservationId = -1;


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
        if(barManagementBean.isManaged()) barManagementBean.detachBar();
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

    public String getReservationsInfo()
    {
        String s = "";
        for(ItemReservation reservation : ((Customer)getUser()).getReservations())
        {
            s = s + reservation.getId() + " ";
        }
        return s;
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



    public String queryAllCafes(){
        String s = "";
        for(Bar b : queryBean.queryBars()){
            BarInfo info = b.getBarInfo();
            s = s + info.getName() + " in " + info.getAddress().getStreet() + ", " + info.getAddress().getCity() + " | ";
        }
        return s;
    }

    public Map<String, Object> mapAllCafes()
    {
        Map<String, Object> cafeMap = new TreeMap<>();
        for(Bar b : queryBean.queryBars())
        {
            BarInfo barInfo = b.getBarInfo();
            String key = barInfo.getName() + barInfo.getAddress().getCity() + barInfo.getAddress().getStreet();
            cafeMap.put(key, b.getId());
        }
        return cafeMap;
    }

    public String queryUsers(){
        String s = "";
        for(String u : queryBean.queryUsers()) s = s + " " + u;
        return s;
    }

    public String queryManagedDrinks()
    {
        String s = "";
        Set<MenuEntry> menu = barManagementBean.getMenu();
        for(MenuEntry m : menu) s = s + " " + m.getItem().getName();
        return s;
    }


    public Map<String, Object> mapOwnedCafes()
    {
        Map<String,Object> ownedBarsMap = new TreeMap<>();
        BarBoss boss = ((BarBoss)getUser());
        List<Bar> ownedBars = null;
        if(boss != null)ownedBars = boss.getOwnedBars();
        if(ownedBars != null)
        {
            for(Bar owned : ownedBars) ownedBarsMap.put(owned.getBarInfo().getName(),owned.getId());
        }
        return ownedBarsMap;
    }

    public Map<String, Object> mapManagedMenu()
    {
        Map<String, Object> menumap = new TreeMap<>();
        for(MenuEntry m : barManagementBean.getMenu())
        {
            Item item = m.getItem();
            String key = "";
            if(item instanceof DrinkItem) key = item.getName() + ((DrinkItem)item).getVolume();
            menumap.put(key, m.getId());
        }
        return menumap;
    }

    public void onReservationBarChange() {
        if (reservationCafeId > -1) {
            Map<String, Object> temp = new TreeMap<>();
            for (MenuEntry m : queryBean.queryMenuFromBar(reservationCafeId))
                temp.put(m.getItem().getName(), m.getId());
            reservationMenu = temp;
        } else
        {
            reservationMenu = new TreeMap<>();
        }
    }

    public boolean isReservationMenuLoaded()
    {
        return reservationMenu != null;
    }

    public void prepareReservation()
    {
        reservationBean.setReservation(reservationCafeId, reservationMenuEntryId, ((Customer)getUser()), reservationAmount);
    }

    public boolean payReservation()
    {
        return reservationBean.payReservation();
    }

    public Map<String, Object> getUserReservations()
    {
        Map<String, Object> reservationsMap = new TreeMap<>();
        for(ItemReservation r : ((Customer)getUser()).getReservations())
        {
            reservationsMap.put(Integer.toString(r.getId()), r.getId());
        }
        return reservationsMap;
    }

    public void removeReservation()
    {
        userBean.removeUserReservation((Customer)getUser(), removeReservationId);
    }

    public boolean readyToPay()
    {
        return reservationBean.readyToPay();
    }

    public String reservationStatus()
    {
        return reservationBean.getStatus();
    }

    public void createCafe() {
        if(user != null) {
            Address address = new Address(cafeStraat, cafeStad);
            BarInfo barInfo = new BarInfo(cafeNaam, address);
            barCreationBean.createBar((BarBoss)getUser(), barInfo, cafeCapaciteit);
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

    public Map<String, Object> getReservationMenu() {
        return reservationMenu;
    }

    public void setReservationMenu(Map<String, Object> reservationMenu) {
        this.reservationMenu = reservationMenu;
    }

    public int getRemoveReservationId() {
        return removeReservationId;
    }

    public void setRemoveReservationId(int removeReservationId) {
        this.removeReservationId = removeReservationId;
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
        return userBean.refreshUser(user);
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

    public int getReservationCafeId() {
        return reservationCafeId;
    }

    public void setReservationCafeId(int reservationCafeId) {
        this.reservationCafeId = reservationCafeId;
    }

    public int getReservationMenuEntryId() {
        return reservationMenuEntryId;
    }

    public void setReservationMenuEntryId(int reservationMenuEntryId) {
        this.reservationMenuEntryId = reservationMenuEntryId;
    }

    public int getReservationAmount() {
        return reservationAmount;
    }

    public void setReservationAmount(int reservationAmount) {
        this.reservationAmount = reservationAmount;
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
