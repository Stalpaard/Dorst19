package jsf_managed_beans;

import ejb.BarCreationBean;
import ejb.BarManagementBean;
import ejb.DorstException;
import ejb.QueryBean;
import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.*;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    @PositiveOrZero(message = "Cafe capacity can't be negative")
    int newCafeCapacity = 0;

    String newDrinkName;
    @PositiveOrZero(message = "Alcohol percentage can't be negative") @Max(value = 100 , message = "Alcohol percentage is max 100%")
    float newDrinkAlc = 0;
    @Positive(message = "Drink volume has to be greater than 0")
    float newDrinkVol = 1;
    @PositiveOrZero(message = "Price can't be negative")
    float newDrinkPrice= 0;
    @PositiveOrZero(message = "Stock can't be negative")
    int newDrinkStock = 0;

    private boolean menuNotEmpty = false;

    int menuEntryId = -1;

    @Positive(message = "Stock additions have to be greater than zero")
    int addToStock = 1;

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
        Set<MenuEntry> barMenu = barManagementBean.getMenu();
        if(!(barMenu.isEmpty()) && barMenu != null)
        {
            for(MenuEntry m : barMenu)
            {
                String key = m.getId() + " " + m.getItem().getName();
                menumap.put(key, m.getId());
            }
        }
        return menumap;
    }

    public Set<MenuEntry> getManagedMenu()
    {
        return barManagementBean.getMenu();
    }

    public void checkMenuEmpty()
    {
        if(barManagementBean.getMenu().isEmpty())
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Stock", "Menu is empty"));
        }
    }

    public void createCafe() {
        User user = null;
        if(userManagedBean != null) user = userManagedBean.getUser();
        if(user != null) {
            Address address = new Address(newCafeStreet, newCafeCity);
            BarInfo barInfo = new BarInfo(newCafeName, address);
            try{
                if(barCreationBean.createBar((BarBoss)userManagedBean.getUser(), barInfo, newCafeCapacity))
                {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Café " + newCafeName + " created",
                            "Street: " + newCafeStreet + " City: " + newCafeCity + " Capacity: " + newCafeCapacity));
                }
                else
                {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bar already exists", "Unique name/street/city combination required"));
                }
            }
            catch(DorstException e)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid bar details", e.getMessage()));
            }
        }
    }

    public String manageCafe(int cafeId) {
        //Om de named query te testen heb ik het zo gedaan
        if(barManagementBean.attachBar(cafeId))
        {
            if(barManagementBean.getMenu().isEmpty()) menuNotEmpty = false;
            else menuNotEmpty = true;
            return "boss-management";
        }
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
        try{
            barCreationBean.removeBar(cafeId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Café removed", "Café with id: " + cafeId + " removed"));
        }
        catch (DorstException e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to remove bar", e.getMessage()));
        }
    }


    public void addDrinkToMenu()
    {
        DrinkItem drinkItem = new DrinkItem(newDrinkName, newDrinkAlc, newDrinkVol);
        try {
            if(barManagementBean.addMenuItem(drinkItem, newDrinkPrice, newDrinkStock))
            {
                if(!menuNotEmpty) menuNotEmpty = true;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(newDrinkName + " added to menu"));
            }
            else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Failed to add item","Item already in menu"));
        }
        catch (DorstException e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Invalid item details",e.getMessage()));
        }

    }

    public void removeDrinkFromMenu(int menuEntry)
    {
        if(barManagementBean.removeMenuItem(menuEntry))
        {
            if(barManagementBean.getMenu().isEmpty()) menuNotEmpty = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item removed", "Menu entry with id: " + menuEntry + " was removed"));
        }
        else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "REMOVE ERROR", "Internal server error"));
    }

    public void addStockToDrink()
    {
        try{
            barManagementBean.addStockToMenuItem(menuEntryId, addToStock);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Stock added",addToStock + " added to menu entry with id: " + menuEntryId));
        }
        catch (DorstException e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"STOCK ERROR", e.getMessage()));
        }
    }

    public boolean isMenuNotEmpty() {
        return menuNotEmpty;
    }

    public void setMenuNotEmpty(boolean menuNotEmpty) {
        this.menuNotEmpty = menuNotEmpty;
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
