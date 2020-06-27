package jsf_managed_beans;

import ejb.BarCreationBean;
import ejb.BarManagementBean;
import ejb.QueryBean;
import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.*;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Named
@FlowScoped("boss")
public class CafeManagedBean implements Serializable {
    String newCafeName = null;
    String newCafeStreet = null;
    String newCafeCity = null;
    @PositiveOrZero
    int newCafeCapacity = 0;

    String newDrinkName;
    @PositiveOrZero @Max(value = 100 , message = "MAX 100%")
    float newDrinkAlc = 0;
    @PositiveOrZero
    float newDrinkVol = 0;
    @PositiveOrZero
    float newDrinkPrice= 0;
    @PositiveOrZero
    int newDrinkStock = 0;

    int menuEntryId = -1;

    @PositiveOrZero
    int addToStock = 0;

    @EJB
    BarCreationBean barCreationBean;

    @EJB
    QueryBean queryBean;

    @EJB
    BarManagementBean barManagementBean;

    @Inject
    UserManagedBean userManagedBean;

    public List<Bar> getOwnedCafes()
    {
        BarBoss boss = null;
        List<Bar> ownedBars = null;
        if(userManagedBean != null) boss = ((BarBoss)userManagedBean.getUser());
        if(boss != null)ownedBars = boss.getOwnedBars();
        return  ownedBars;
    }

    public Map<String, Object> mapManagedMenu()
    {
        Map<String, Object> menumap = new TreeMap<>();
        for(MenuEntry m : barManagementBean.getMenu())
        {
            String key = m.getId() + " " + m.getItem().getName();
            menumap.put(key, m.getId());
        }
        return menumap;
    }

    public Set<MenuEntry> getManagedMenu()
    {
        return barManagementBean.getMenu();
    }

    public void createCafe() {
        User user = null;
        if(userManagedBean != null) user = userManagedBean.getUser();
        if(user != null) {
            Address address = new Address(newCafeStreet, newCafeCity);
            BarInfo barInfo = new BarInfo(newCafeName, address);
            barCreationBean.createBar((BarBoss)userManagedBean.getUser(), barInfo, newCafeCapacity);
        }
    }

    public String manageCafe(int cafeId) {
        //Om de named query te testen heb ik het zo gedaan
        if(barManagementBean.attachBar(cafeId)) return "boss-management";
        return "";
    }

    public String unmanageCafe()
    {
        barManagementBean.detachBar();
        return "boss";
    }

    public String getManagedCafeNaam()
    {
        if(barManagementBean.isManaged())
            return barManagementBean.getManagedBarInfo().getName();
        else return "";
    }

    public void removeCafe(int cafeId) {
        barCreationBean.removeBar(cafeId);
    }


    public void addDrinkToMenu()
    {
        DrinkItem drinkItem = new DrinkItem(newDrinkName, newDrinkAlc, newDrinkVol);
        barManagementBean.addMenuItem(drinkItem, newDrinkPrice, newDrinkStock);
    }

    public void removeDrinkFromMenu(int menuEntry)
    {
        barManagementBean.removeMenuItem(menuEntry);
    }

    public void addStockToDrink()
    {
        barManagementBean.addStockToMenuItem(menuEntryId, addToStock);
    }










    public String getNewCafeName() {
        return newCafeName;
    }

    public void setNewCafeName(String newCafeName) {
        this.newCafeName = newCafeName;
    }

    public String getNewCafeStreet() {
        return newCafeStreet;
    }

    public void setNewCafeStreet(String newCafeStreet) {
        this.newCafeStreet = newCafeStreet;
    }

    public String getNewCafeCity() {
        return newCafeCity;
    }

    public void setNewCafeCity(String newCafeCity) {
        this.newCafeCity = newCafeCity;
    }

    public int getNewCafeCapacity() {
        return newCafeCapacity;
    }

    public void setNewCafeCapacity(int newCafeCapacity) {
        this.newCafeCapacity = newCafeCapacity;
    }

    public int getAddToStock() {
        return addToStock;
    }

    public void setAddToStock(int addToStock) {
        this.addToStock = addToStock;
    }

    public String getNewDrinkName() {
        return newDrinkName;
    }

    public void setNewDrinkName(String newDrinkName) {
        this.newDrinkName = newDrinkName;
    }

    public float getNewDrinkAlc() {
        return newDrinkAlc;
    }

    public void setNewDrinkAlc(float newDrinkAlc) {
        this.newDrinkAlc = newDrinkAlc;
    }

    public float getNewDrinkVol() {
        return newDrinkVol;
    }

    public void setNewDrinkVol(float newDrinkVol) {
        this.newDrinkVol = newDrinkVol;
    }

    public int getMenuEntryId() {
        return menuEntryId;
    }

    public void setMenuEntryId(int menuEntryId) {
        this.menuEntryId = menuEntryId;
    }

    public float getNewDrinkPrice() {
        return newDrinkPrice;
    }

    public void setNewDrinkPrice(float newDrinkPrice) {
        this.newDrinkPrice = newDrinkPrice;
    }

    public int getNewDrinkStock() {
        return newDrinkStock;
    }

    public void setNewDrinkStock(int newDrinkStock) {
        this.newDrinkStock = newDrinkStock;
    }

    public BarCreationBean getBarCreationBean() {
        return barCreationBean;
    }

    public void setBarCreationBean(BarCreationBean barCreationBean) {
        this.barCreationBean = barCreationBean;
    }

    public QueryBean getQueryBean() {
        return queryBean;
    }

    public void setQueryBean(QueryBean queryBean) {
        this.queryBean = queryBean;
    }

    public BarManagementBean getBarManagementBean() {
        return barManagementBean;
    }

    public void setBarManagementBean(BarManagementBean barManagementBean) {
        this.barManagementBean = barManagementBean;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }
}
