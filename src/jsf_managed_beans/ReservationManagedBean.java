package jsf_managed_beans;

import ejb.QueryBean;
import ejb.ReservationBean;
import ejb.ReservationCounterBean;
import ejb.UserBean;
import jpa.entities.Customer;
import jpa.entities.ItemReservation;
import jpa.entities.MenuEntry;

import javax.annotation.ManagedBean;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

@Named
@DependsOn({
        "UserManagedBean",
        "ReservationBean",
        "ReservationCounterBean",
        "QueryBean"
})
@SessionScoped
public class ReservationManagedBean implements Serializable {

    @EJB
    ReservationBean reservationBean;

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
    int reservationAmount = -1;
    int removeReservationId = -1;

    public String getReservationsInfo()
    {
        String s = "";
        for(ItemReservation reservation : ((Customer)userManagedBean.getUser()).getReservations())
        {
            s = s + reservation.getId() + " ";
        }
        return s;
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
        reservationBean.setReservation(reservationCafeId, reservationMenuEntryId, ((Customer)userManagedBean.getUser()), reservationAmount);
    }

    public boolean payReservation()
    {
        return reservationBean.payReservation();
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

    public void removeReservation()
    {
        userBean.removeUserReservation((Customer)userManagedBean.getUser(), removeReservationId);
    }

    public boolean readyToPay()
    {
        return reservationBean.readyToPay();
    }

    public String reservationStatus()
    {
        return reservationBean.getStatus();
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

    public ReservationBean getReservationBean() {
        return reservationBean;
    }

    public void setReservationBean(ReservationBean reservationBean) {
        this.reservationBean = reservationBean;
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
