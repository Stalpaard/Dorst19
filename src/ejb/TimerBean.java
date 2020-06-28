package ejb;

import jpa.entities.Customer;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "TimerBean")
public class TimerBean {

    @PersistenceContext(unitName = "DorstPersistenceUnit")
    EntityManager entityManager;

    @Resource
    TimerService timerService;

    @EJB
    QueryBean queryBean;

    final static float giftAmount = 10.0f;

    @Schedule(dayOfMonth="1", hour = "0", persistent = false)
    public void giftCredit()
    {
        for(String username : queryBean.queryUsers())
        {
            Customer customer = entityManager.find(Customer.class, username);
            if(customer != null) customer.setCredit(customer.getCredit() + giftAmount);
        }
    }

    public List<Date> getNextGiftDates()
    {
        ArrayList<Date> dates = new ArrayList<>();
        for(Timer timer : timerService.getTimers())
        {
            dates.add(timer.getNextTimeout());
        }
        return dates;
    }
}
