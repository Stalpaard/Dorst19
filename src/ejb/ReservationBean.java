package ejb;

import jpa.embeddables.BarInfo;
import jpa.entities.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@Stateful(name = "ReservationEJB")
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 60)
@DependsOn("ReservationCounterBean")
public class ReservationBean {
    @EJB
    ReservationCounterBean reservationCounterBean;

    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    int barId = -1;
    int menuEntryId = -1;
    Customer customer = null; //zou uit sessioncontext moeten gehaald worden
    int amount = 0;
    float total = 0;
    boolean ready =false;
    String status = "No reservation set";

    public ReservationBean()
    {
    }

    public void setReservation(int barId, int menuEntryId, Customer customer, int amount)
    {
        Bar bar = entityManager.find(Bar.class, barId);
        if(bar.getMenuEntryById(menuEntryId) != null && bar != null && amount > 0)
        {
            this.customer = customer;
            this.barId = barId;
            this.menuEntryId = menuEntryId;
            this.amount = amount;
            total = bar.getMenuEntryById(menuEntryId).getPrice() * amount;
            if(customer.getCredit() >= total){
                ready = true;
                status = "Reservation set, ready to pay";
            }
            else
            {
                status = "Insufficient amount of credit";
                reset();
            }
        }
        else
        {
            status =  "Invalid reservation, try again";
            reset();
        }

    }

    private void reset()
    {
        this.barId = barId;
        this.menuEntryId = menuEntryId;
        this.customer = null;
        this.amount = 0;
        ready = false;
    }

    public String getStatus()
    {
        return status;
    }

    public boolean readyToPay()
    {
        return ready;
    }

    public boolean payReservation()
    {
        Bar bar = entityManager.find(Bar.class, barId);
        if(bar != null && bar.getMenuEntryById(menuEntryId) != null && ready)
        {
            //customer = entityManager.find(Customer.class, customer.getUsername());
            customer.setCredit(customer.getCredit() - bar.getMenuEntryById(menuEntryId).getPrice());
            ItemReservation success = bar.addReservation(customer, menuEntryId, amount);
            if(success != null)
            {
                customer.addReservation(success);
                entityManager.merge(customer);
                entityManager.merge(bar);
                reservationCounterBean.incReservationsDone();
                status = "Reservation complete";
                reset();
                return true;
            }
            else
            {
                status = "Reservation failed";
                reset();
            }
        }
        return false;
    }
}
