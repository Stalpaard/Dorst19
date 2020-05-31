package ejb;

import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.BarBoss;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "BarCreationEJB")
public class BarCreationBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public BarCreationBean() {
    }

    public boolean createBar(BarBoss initBoss, BarInfo newBarInfo, int capacity)
    {
        if(entityManager.find(Bar.class, newBarInfo) == null)
        {
                Bar new_bar = new Bar(newBarInfo, capacity);
                new_bar.addBoss(initBoss);
                entityManager.persist(new_bar);
                return true;
        }
        return false;
    }
}
