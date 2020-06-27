package ejb;

import jpa.entities.*;
import utilities.ReservationInfo;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.TimeUnit;

@Stateful(name = "ReservationEJB")
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 60)
public class PlaceReservationBean {
    @EJB
    ReservationCounterBean reservationCounterBean;

    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    @Resource(mappedName = "jms/reservationMsgDest")
    private Queue reservationDest;

    @Resource(mappedName = "jms/reservationMsg")
    private ConnectionFactory queue;

    int barId = -1;
    int menuEntryId = -1;
    String customerUsername = null; //zou uit sessioncontext moeten gehaald worden
    int amount = 0;
    float total = 0;
    boolean ready =false;
    String status = "No reservation set";

    public PlaceReservationBean()
    {
    }

    public void setReservation(int barId, int menuEntryId, String customerUsername, int amount)
    {
        Bar bar = entityManager.find(Bar.class, barId);
        MenuEntry menuEntry = bar.getMenuEntryById(menuEntryId);
        Customer customer = entityManager.find(Customer.class, customerUsername);
        if(menuEntry != null && bar != null && customer != null)
        {
            this.customerUsername = customerUsername;
            this.barId = barId;
            this.menuEntryId = menuEntryId;
            this.amount = amount;
            total = bar.getMenuEntryById(menuEntryId).getPrice() * amount;
            if(menuEntry.getStock() >= amount){
                if(customer.getCredit() >= total)
                {
                    ready = true;
                    status = amount + " of " + bar.getMenuEntryById(menuEntryId).getItem().getName()
                            + " in " + bar.getBarInfo().getName()
                            + " with total price: " + total;
                }
                else
                {
                    status = "Insufficient amount of credit (total: " + total + ")";
                    reset();
                }
            }
            else
            {
                status = "Not enough stock in café";
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
        this.customerUsername = null;
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

    @Interceptors(LogInterceptor.class)
    public void payReservation()
    {
        //Produces ObjectMessage (needs to be serializable!) containing the ItemReservation
        ReservationInfo reservationInfo = new ReservationInfo(barId, menuEntryId, customerUsername, amount);

        try {
            Connection conn = queue.createConnection();
            Session s = conn.createSession();
            MessageProducer mp = s.createProducer(reservationDest);
            ObjectMessage reservationMsg = s.createObjectMessage();
            reservationMsg.setObject(reservationInfo);
            mp.send(reservationMsg);
            status = "Reservation sent to Bar";
            ready = false;
        }
        catch (Exception e)
        {
            System.out.println(e);
            status = "Exception occurred, please try again";
        }

    }
}
