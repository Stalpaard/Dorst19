package ejb;

import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.MenuEntry;
import jpa.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless(name = "BarQueryEJB")
public class QueryBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public QueryBean()
    {

    }

    public List<Bar> queryBars()
    {
        TypedQuery<Bar> query = entityManager.createNamedQuery("QUERY_BARS", Bar.class);
        return query.getResultList();
    }

    public List<MenuEntry> queryMenuFromBar(BarInfo barInfo)
    {
        Bar findBar = entityManager.find(Bar.class, barInfo);
        if(findBar != null)
        {
            entityManager.detach(findBar);
            return findBar.getMenu();
        }
        return null;
    }

    public List<String> queryUsers()
    {
        TypedQuery<String> query = entityManager.createNamedQuery("QUERY_USERNAMES", String.class);
        return query.getResultList();
    }
}
