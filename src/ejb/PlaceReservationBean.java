package ejb;

import jpa.entities.*;
import utilities.ReservationInfo;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Stateful(name = "ReservationEJB")
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 60)
public class PlaceReservationBean {

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

    public void addReservation(int barId, int menuEntryId, String customerUsername, int amount) throws EJBException
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
                    payReservation();
                    status = amount + " of " + bar.getMenuEntryById(menuEntryId).getItem().getName()
                            + " in " + bar.getBarInfo().getName()
                            + " with total price: " + total;
                }
                else
                {
                    reset();
                    //throw new EJBException("Insufficient amount of credit (total: " + total + ")");
                }
            }
            else
            {
                reset();
                //throw new EJBException("Not enough stock in café");
            }
        }
        else
        {
            reset();
            //throw new EJBException("Invalid reservation, try again");
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
    private void payReservation() throws EJBException
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
            throw new EJBException(e.getMessage());
        }

    }
}
