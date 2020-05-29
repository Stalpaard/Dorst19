package dorst19_ejb;

import dorst19_entities.Bar;
import dorst19_entities.MenuEntry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "DemoEJB")
public class DemoBean {
    @PersistenceContext(unitName = "DorstPersistenceUnit")
    EntityManager entityManager;
    int kak = 0;
    public DemoBean() {
    }

    public String epischeActie()
    {

        return "hallo";
    }
}
