package ejb;

import jpa.entities.*;
import utilities.ReservationInfo;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "ReservationEJB")
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

    public PlaceReservationBean()
    {
    }

    public void addReservation(int barId, int menuEntryId, String customerUsername, int amount) throws DorstException
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
                    sendReservation();
                }
                else
                {
                    throw new DorstException("Insufficient amount of credit (total: " + total + ")");
                }
            }
            else
            {
                throw new DorstException("Not enough stock in café");
            }
        }
        else
        {
            throw new DorstException("Invalid reservation, try again");
        }

    }

    @Interceptors(LogInterceptor.class)
    private void sendReservation() throws DorstException
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
        }
        catch (Exception e)
        {
            throw new DorstException(e.getMessage());
        }

    }
}
