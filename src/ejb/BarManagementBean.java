package ejb;

import jpa.embeddables.BarInfo;
import jpa.embeddables.TimePeriod;
import jpa.entities.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Stateful(name = "BarManagementEJB")
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 60)
public class BarManagementBean implements Serializable {

    @PersistenceContext(name = "DorstPersistenceBean")
    EntityManager entityManager;

    private Bar managedBar = null;

    @Resource
    private SessionContext ctx;

    public BarManagementBean() {
    }

    public boolean isManaged()
    {
        return managedBar != null;
    }

    public BarInfo getManagedBarInfo()
    {
        if(managedBar != null) return managedBar.getBarInfo();
        else return null;
    }

    public boolean attachBar(int managedBarId)
    {
        Bar findBar = entityManager.find(Bar.class, managedBarId);
        if(findBar != null)
        {
            managedBar = entityManager.merge(findBar);
            return true;
        }
        return false;
    }

    public void detachBar()
    {
        managedBar = null;
    }


    @PrePassivate
    private void detachPersistenceBar()
    {
        if(managedBar != null) entityManager.detach(managedBar);
    }

    @PostActivate
    private void refreshPersistenceBar()
    {
        if(managedBar != null)
        {
            managedBar = entityManager.find(Bar.class,managedBar.getId());
            entityManager.refresh(managedBar);
        }
    }

    public void removeMenuItem(int id)
    {
        if(managedBar != null)
        {
            Item removed = managedBar.removeFromMenu(id);
            int removed_id = removed.getId();
            if(removed != null)
            {
                entityManager.merge(managedBar);
                TypedQuery<MenuEntry> removeDrinkQuery = entityManager.createNamedQuery("CHECK_DRINK_REF", MenuEntry.class)
                        .setParameter("id", removed_id);
                if(removeDrinkQuery.getResultList().size() <= 0)
                {
                    if(removed instanceof DrinkItem) entityManager.remove(entityManager.find(DrinkItem.class, removed_id));
                }
            }
        }
    }

    public void addStockToMenuItem(int menuEntryId, int amount)
    {
        if(amount > 0 && managedBar != null)
        {
            MenuEntry menuEntry = managedBar.getMenuEntryById(menuEntryId);
            if(menuEntry != null)
            {
                menuEntry.setStock(menuEntry.getStock() + amount);
                entityManager.merge(managedBar);
            }
        }
    }

    public Set<MenuEntry> getMenu()
    {
        if(managedBar != null)
        {
            return managedBar.getMenu();
        }
        return null;
    }

    public void addMenuItem(Item item, float price, int stock)
    {
        if(managedBar != null)
        {
            if(item instanceof DrinkItem)
            {
                TypedQuery<DrinkItem> drinkQuery = entityManager.createNamedQuery("QUERY_DRINKS", DrinkItem.class)
                        .setParameter("name", item.getName())
                        .setParameter("alc", ((DrinkItem) item).getAlcoholPercentage())
                        .setParameter("volume", ((DrinkItem) item).getVolume());
                List<DrinkItem> resultList = drinkQuery.getResultList();
                if(resultList.size() <= 0) entityManager.persist(item);
                else
                {
                    if(item instanceof DrinkItem) item = entityManager.find(DrinkItem.class, resultList.get(0).getId());
                }
            }
            if(managedBar.addToMenu(item, price, stock)) entityManager.merge(managedBar);
        }
    }

    public boolean removeBar()
    {
        //Misschien op een manier nog fixen da enkel bosses hun eigen cafés kunnen verwijderen
        //if(!ctx.isCallerInRole("boss")) throw new SecurityException("Only bosses may remove cafés");
        if(managedBar != null)
        {
            managedBar = entityManager.find(Bar.class, managedBar.getId());
            for(ItemReservation reservation : managedBar.getReservations())
            {
                Customer customer = entityManager.find(Customer.class, reservation.getCustomer().getUsername());
                customer.removeReservation(reservation);
                entityManager.merge(customer);
            }
            entityManager.remove(managedBar);
            managedBar = null;
            return true;
        }
        return false;
    }
}
