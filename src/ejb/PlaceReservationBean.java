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

    @EJB
    ReservationCounterBean reservationCounterBean;


    public PlaceReservationBean() {
    }

    public void addReservation(int barId, int menuEntryId, String customerUsername, int amount) throws DorstException {
        Bar bar = entityManager.find(Bar.class, barId);
        entityManager.refresh(bar);
        MenuEntry menuEntry = bar.getMenuEntryById(menuEntryId);
        Customer customer = entityManager.find(Customer.class, customerUsername);
        entityManager.refresh(customer);
        if (menuEntry != null && bar != null && customer != null) {
            if (menuEntry.getStock() >= amount) {
                float total = bar.getMenuEntryById(menuEntryId).getPrice() * amount;
                if (customer.getCredit() >= total) {
                    sendReservation(barId, menuEntryId, customerUsername, amount);
                } else {
                    throw new DorstException("Insufficient amount of credit (total: " + total + ")");
                }
            } else {
                throw new DorstException("Not enough stock in café");
            }
        } else {
            throw new DorstException("Invalid reservation, try again");
        }

    }

    @Interceptors(LogInterceptor.class)
    private void sendReservation(int barId, int menuEntryId, String customerUsername, int amount) throws DorstException {
        ReservationInfo reservationInfo = new ReservationInfo(barId, menuEntryId, customerUsername, amount);

        try {
            Connection conn = queue.createConnection();
            Session s = conn.createSession();
            MessageProducer mp = s.createProducer(reservationDest);
            ObjectMessage reservationMsg = s.createObjectMessage();
            reservationMsg.setObject(reservationInfo);
            reservationCounterBean.incReservationsDone();
            mp.send(reservationMsg);
        } catch (Exception e) {
            throw new DorstException(e.getMessage());
        }

    }
}
