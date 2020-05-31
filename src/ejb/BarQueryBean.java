package ejb;

import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.MenuEntry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "BarQueryEJB")
public class BarQueryBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public BarQueryBean()
    {

    }

    public List<BarInfo> queryBars()
    {
        TypedQuery<BarInfo> query = entityManager.createNamedQuery("QUERY_BARINFO", BarInfo.class);
        return query.getResultList();
    }

    public List<MenuEntry> getMenuFromBar(BarInfo barInfo)
    {
        Bar findBar = entityManager.find(Bar.class, barInfo);
        if(findBar != null)
        {
            entityManager.detach(findBar);
            return findBar.getMenu();
        }
        return null;
    }
}
