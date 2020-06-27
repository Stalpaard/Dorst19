package jsf_managed_beans;

import ejb.QueryBean;
import ejb.PlaceReservationBean;
import ejb.ReservationCounterBean;
import ejb.UserBean;
import jpa.entities.Customer;
import jpa.entities.ItemReservation;
import jpa.entities.MenuEntry;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.flow.FlowScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Named
@FlowScoped("customer")
public class ReservationManagedBean implements Serializable {

    @EJB
    PlaceReservationBean placeReservationBean;

    @EJB
    UserBean userBean;

    @EJB
    QueryBean queryBean;

    @EJB
    ReservationCounterBean reservationCounterBean;

    @Inject
    UserManagedBean userManagedBean;

    int reservationCafeId = -1;
    Map<String,Object> reservationMenu;
    int reservationMenuEntryId = -1;
    @Positive
    int reservationAmount = 1;
    int removeReservationId = -1;

    private static DecimalFormat geldFormat = new DecimalFormat("0.00");

    public String getStringOfAllReservations()
    {
        String s = "";
        for(ItemReservation reservation : ((Customer)userManagedBean.getUser()).getReservations())
        {
            s = s + reservation.getId() + ": " + reservation.getAmountOfDrinks()
                    + " " + reservation.getMenuEntry().getItem().getName()
                    + " in " + reservation.getBar().getBarInfo().getName()
                    + " | ";
        }
        return s;
    }

    public void updateReservationMenu() {
        if (reservationCafeId > -1) {
            Set<MenuEntry> menu = queryBean.queryMenuFromBar(reservationCafeId);
            if(menu != null)
            {
                Map<String, Object> temp = new TreeMap<>();
                for (MenuEntry m : queryBean.queryMenuFromBar(reservationCafeId))
                    temp.put(m.getId() + m.getItem().getName() + " price: " + geldFormat.format(m.getPrice()), m.getId());
                reservationMenu = temp;
            }

        }
    }

    public boolean isReservationMenuLoaded()
    {
        return reservationMenu != null;
    }

    public void prepareReservation()
    {
        placeReservationBean.setReservation(reservationCafeId, reservationMenuEntryId, userManagedBean.getUser().getUsername(), reservationAmount);
    }

    public void payReservation()
    {
        placeReservationBean.payReservation();
    }

    public Map<String, Object> getUserReservations()
    {
        Map<String, Object> reservationsMap = new TreeMap<>();
        for(ItemReservation r : ((Customer)userManagedBean.getUser()).getReservations())
        {
            reservationsMap.put(Integer.toString(r.getId()), r.getId());
        }
        return reservationsMap;
    }

    public void cancelReservation()
    {
        userBean.cancelUserReservation((Customer)userManagedBean.getUser(), removeReservationId);
    }

    public boolean isReadyToPay()
    {
        return placeReservationBean.readyToPay();
    }

    public String getReservationStatus()
    {
        return placeReservationBean.getStatus();
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

    public PlaceReservationBean getPlaceReservationBean() {
        return placeReservationBean;
    }

    public void setPlaceReservationBean(PlaceReservationBean placeReservationBean) {
        this.placeReservationBean = placeReservationBean;
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

    public ReservationCounterBean getReservationCounterBean() {
        return reservationCounterBean;
    }

    public void setReservationCounterBean(ReservationCounterBean reservationCounterBean) {
        this.reservationCounterBean = reservationCounterBean;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }
}
