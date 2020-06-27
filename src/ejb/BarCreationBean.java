package ejb;

import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.BarBoss;
import jpa.entities.Customer;
import jpa.entities.ItemReservation;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


@Stateless(name = "BarCreationEJB")
@Interceptors(LogInterceptor.class)
public class BarCreationBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public BarCreationBean() {
    }

    public boolean createBar(BarBoss initBoss, BarInfo newBarInfo, int capacity)
    {
        TypedQuery<Bar> barQuery = entityManager.createNamedQuery("CHECK_EXISTING_BARS", Bar.class)
                .setParameter("barinfo", newBarInfo);

        if(barQuery.getResultList().size() <= 0)
        {
                Bar new_bar = new Bar(newBarInfo, capacity);
                new_bar.addBoss(initBoss);
                entityManager.persist(new_bar);
                entityManager.merge(initBoss);
                return true;
        }
        return false;
    }

    public boolean removeBar(int cafeId)
    {
        Bar toDelete = entityManager.find(Bar.class, cafeId);
        if(toDelete != null)
        {
            for(ItemReservation reservation : toDelete.getReservations())
            {
                Customer customer = entityManager.find(Customer.class, reservation.getCustomer().getUsername());
                customer.removeReservation(reservation);
                entityManager.merge(customer);
            }
            entityManager.remove(toDelete);
            return true;
        }
        else return false;
    }
}
