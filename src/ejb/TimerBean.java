package ejb;

import jpa.entities.Customer;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "TimerBean")
public class TimerBean {

    @PersistenceContext(unitName = "DorstPersistenceUnit")
    EntityManager entityManager;

    @EJB
    QueryBean queryBean;

    final static float giftAmount = 10;

    @Schedule(dayOfMonth="1", hour = "0", persistent = false)
    public void giftCredit()
    {
        for(String username : queryBean.queryUsers())
        {
            Customer customer = entityManager.find(Customer.class, username);
            if(customer != null) customer.setCredit(customer.getCredit() + giftAmount);
        }
    }
}
