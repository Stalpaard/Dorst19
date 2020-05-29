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
        //Drink drink = entityManager.find(Drink.class, "vaasbeerdrank");
        //drink.setAlcoholPercentage(50);
        //return ((Float)drink.getAlcoholPercentage()).toString();
        return "hallo";
    }
}
