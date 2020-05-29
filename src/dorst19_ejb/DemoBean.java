package dorst19_ejb;

import dorst19_embeddables.Address;
import dorst19_entities.Bar;
import dorst19_entities.DrinkItem;
import dorst19_entities.Item;
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
        Item drankje = new DrinkItem("bier",5.2f,33f);
        entityManager.persist(drankje);
        Address address = new Address("tiense","3000","Leuven","vb","b");
        Bar hdr = new Bar("hdr",address,200);
        hdr.addToMenu(new MenuEntry(drankje,5,60));
        entityManager.persist(hdr);
        return "hallo";
    }
}
