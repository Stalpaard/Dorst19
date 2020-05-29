package dorst19_ejb;

import dorst19_entities.Drink;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "DemoEJB")
public class DemoBean {
    @PersistenceContext(unitName = "DorstPersistenceUnit")
    EntityManager entityManager;

    public DemoBean() {
    }

    public String getHello()
    {
        Drink drink = new Drink();
        drink.setAlcoholPercentage(5);
        drink.setName("kaasbeer");
        drink.setVolume(5);
        entityManager.persist(drink);
        entityManager.flush();
        return "kaas";
    }
}
