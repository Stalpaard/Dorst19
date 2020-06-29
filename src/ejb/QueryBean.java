package ejb;

import jpa.entities.Bar;
import jpa.entities.MenuEntry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;

@Stateless(name = "BarQueryEJB")
public class QueryBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public QueryBean() {

    }

    public List<Bar> queryBars() {
        TypedQuery<Bar> query = entityManager.createNamedQuery("QUERY_BARS", Bar.class);
        return query.getResultList();
    }

    public Set<MenuEntry> queryMenuFromBar(int barId) {
        Bar findBar = entityManager.find(Bar.class, barId);
        if (findBar != null) {
            return findBar.getMenu();
        }
        return null;
    }

    public List<String> queryUsers() {
        TypedQuery<String> query = entityManager.createNamedQuery("QUERY_USERNAMES", String.class);
        return query.getResultList();
    }
}
