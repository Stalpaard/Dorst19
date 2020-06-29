package jsf_managed_beans;

import ejb.*;
import jpa.entities.Customer;
import jpa.entities.ItemReservation;
import jpa.entities.MenuEntry;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
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
    int reservationMenuEntryId = -1;
    int reservationAmount = 1;
    int removeReservationId = -1;

    private static DecimalFormat geldFormat = new DecimalFormat("0.00");

    public Map<String,Object> updateReservationMenu() {
        Map<String, Object> temp = new TreeMap<>();
        Set<MenuEntry> menu = queryBean.queryMenuFromBar(reservationCafeId);
        if(menu != null)
        {
            for (MenuEntry m : queryBean.queryMenuFromBar(reservationCafeId))
                temp.put(m.getId() + m.getItem().getName() + " price: " + geldFormat.format(m.getPrice()), m.getId());
        }
        return temp;
    }

    public void reservationMenuListener(ValueChangeEvent event)
    {
        reservationCafeId = (int)event.getNewValue();
    }

    public void addReservation()
    {
        try {
            placeReservationBean.addReservation(reservationCafeId, reservationMenuEntryId, userManagedBean.getUser().getUsername(), reservationAmount);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Reservation sent to Bar", "Refresh to see the new reservation"));
        }
        catch (DorstException e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Reservation Failed", e.getMessage()));
        }
    }


    public Map<String, Object> getUserReservationsMap()
    {
        Map<String, Object> reservationsMap = new TreeMap<>();
        for(ItemReservation r : ((Customer)userManagedBean.getUser()).getReservations())
        {
            reservationsMap.put(Integer.toString(r.getId()), r.getId());
        }
        return reservationsMap;
    }

    public List<ItemReservation> getUserReservations()
    {
        return ((Customer)userManagedBean.getUser()).getReservations();
    }

    public void cancelReservation()
    {
        if(userBean.cancelUserReservation((Customer)userManagedBean.getUser(), removeReservationId))
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Remove complete", "Reservation with id: " + removeReservationId + " has been removed"));
        }
        else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Remove failed", "Internal server error"));
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
