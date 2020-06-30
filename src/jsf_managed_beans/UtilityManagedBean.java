package jsf_managed_beans;

import ejb.QueryBean;
import ejb.ReservationCounterBean;
import ejb.TimerBean;
import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.MenuEntry;
import utilities.UserType;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.*;

@Named
@ApplicationScoped
public class UtilityManagedBean {

    @EJB
    QueryBean queryBean;

    @EJB
    TimerBean timerBean;

    @EJB
    ReservationCounterBean reservationCounterBean;

    @Inject
    private UserManagedBean userManagedBean;

    public Map<String, Object> mapAllCafes() {
        Map<String, Object> cafeMap = new TreeMap<>();
        for (Bar b : queryBean.queryBars()) {
            BarInfo barInfo = b.getBarInfo();
            String key = barInfo.getName() + barInfo.getAddress().getCity() + barInfo.getAddress().getStreet();
            cafeMap.put(key, b.getId());
        }
        return cafeMap;
    }

    public List<Bar> getAllCafes() {
        List<Bar> bars = new ArrayList<Bar>();
        bars.addAll(queryBean.queryBars());
        return bars;
    }

    public void xmlMenuRedirect(int cafeId) throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("http://localhost:8080/Dorst19/resources/menu/" + cafeId);
    }

    public Map<String, Object> getUserTypes() {
        Map<String, Object> userTypes = new LinkedHashMap<>();
        for (UserType type : UserType.values()) {
            userTypes.put(type.name(), type);
        }
        return userTypes;
    }

    public String stringOfAllUsers() {
        String s = "";
        for (String u : queryBean.queryUsers()) s = s + " " + u;
        return s;
    }

    public int getGlobalAmountOfReservations() {
        return reservationCounterBean.getReservationsDone();
    }

    public String giftDatesString() {
        String giftDates = "";
        for (Date d : timerBean.getNextGiftDates()) {
            giftDates = giftDates + d.toString() + "\t";
        }
        return giftDates;
    }

    public void checkSessionAtt()
    {
        String message = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("message");
        if (message != null) {
            System.out.println(message);
            Float amount = (float) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("amountToAdd");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Surprise!!!!", "Random credit drop"));
            userManagedBean.setAmountToAdd(amount);
            userManagedBean.addCredit();
        }
    }

    public List<Bar> queryBars() {
        return queryBean.queryBars();
    }

    public Set<MenuEntry> queryMenuFromBar(int barId) {
        return queryBean.queryMenuFromBar(barId);
    }
}
