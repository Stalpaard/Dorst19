package ejb;

import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.BarBoss;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless(name = "BarCreationEJB")
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
}
