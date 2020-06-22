package jsf_managed_beans;

import ejb.BarCreationBean;
import ejb.BarManagementBean;
import ejb.QueryBean;
import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.*;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Named
@SessionScoped
public class CafeManagedBean implements Serializable {
    String cafeNaam = null;
    String cafeStraat = null;
    String cafeStad = null;
    int cafeCapaciteit = 0;

    String drank_name;
    float drank_alc;
    float drank_vol;
    float price;
    int stock;

    int managedCafeId = -1;
    int menuEntryId = -1;

    int addToStock = -1;

    @EJB
    BarCreationBean barCreationBean;

    @EJB
    QueryBean queryBean;

    @EJB
    BarManagementBean barManagementBean;

    @Inject
    UserManagedBean userManagedBean;

    public String stringOfManagedMenu()
    {
        String s = "";
        Set<MenuEntry> menu = barManagementBean.getMenu();
        for(MenuEntry m : menu) s = s + " " + m.getId() + m.getItem().getName();
        return s;
    }

    public String stringOfAllCafes() {
        String s = "";
        for (Bar b : queryBean.queryBars()) {
            BarInfo info = b.getBarInfo();
            s = s + info.getName() + " in " + info.getAddress().getStreet() + ", " + info.getAddress().getCity() + " | ";
        }
        return s;
    }

    public Map<String, Object> mapAllCafes() {
        Map<String, Object> cafeMap = new TreeMap<>();
        for (Bar b : queryBean.queryBars()) {
            BarInfo barInfo = b.getBarInfo();
            String key = barInfo.getName() + barInfo.getAddress().getCity() + barInfo.getAddress().getStreet();
            cafeMap.put(key, b.getId());
        }
        return cafeMap;
    }

    public Map<String, Object> mapOwnedCafes()
    {
        Map<String,Object> ownedBarsMap = new TreeMap<>();
        BarBoss boss = null;
        if(userManagedBean != null) boss = ((BarBoss)userManagedBean.getUser());
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
            String key = m.getId() + m.getItem().getName() + " " + "stock: " + m.getStock();
            menumap.put(key, m.getId());
        }
        return menumap;
    }

    public void createCafe() {
        User user = null;
        if(userManagedBean != null) user = userManagedBean.getUser();
        if(user != null) {
            Address address = new Address(cafeStraat, cafeStad);
            BarInfo barInfo = new BarInfo(cafeNaam, address);
            barCreationBean.createBar((BarBoss)userManagedBean.getUser(), barInfo, cafeCapaciteit);
        }
    }

    public String manageCafe() {
        //Om de named query te testen heb ik het zo gedaan
        if(barManagementBean.attachBar(managedCafeId)) return "cafemanagement";
        return "boss";
    }

    public String unmanageCafe()
    {
        managedCafeId = -1;
        barManagementBean.detachBar();
        return "boss";
    }

    public String getManagedCafeNaam()
    {
        if(barManagementBean.isManaged())
            return barManagementBean.getManagedBarInfo().getName();
        else return "";
    }

    public String removeCafe() {
        //remove attached bar
        managedCafeId = -1;
        barManagementBean.removeBar();
        return "boss?faces-redirect = true";
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

    public void addStockToDrink()
    {
        if(addToStock > -1)
        {
            barManagementBean.addStockToMenuItem(menuEntryId, addToStock);
        }
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

    public int getAddToStock() {
        return addToStock;
    }

    public void setAddToStock(int addToStock) {
        this.addToStock = addToStock;
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

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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
