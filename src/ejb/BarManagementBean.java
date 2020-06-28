package ejb;

import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.*;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Stateful(name = "BarManagementEJB")
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 60)
public class BarManagementBean implements Serializable {

    @PersistenceContext(name = "DorstPersistenceBean")
    EntityManager entityManager;

    private Bar managedBar = null;

    private int barId = -1;

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
    private void passivateBar()
    {
        if(managedBar != null)
        {
            barId = managedBar.getId();
            entityManager.detach(managedBar);
            managedBar = null;
        }
        else barId = -1;
    }

    @PostActivate
    private void activateBar()
    {
        if(barId > -1)
        {
            managedBar = entityManager.find(Bar.class, barId);
        }
        else managedBar = null;
    }

    public boolean removeMenuItem(int id)
    {
        if(managedBar != null)
        {
            Item removed = managedBar.removeFromMenu(id);
            int removed_id = removed.getId();
            entityManager.merge(managedBar);
            TypedQuery<MenuEntry> removeDrinkQuery = entityManager.createNamedQuery("CHECK_DRINK_REF", MenuEntry.class)
                    .setParameter("id", removed_id);
            if(removeDrinkQuery.getResultList().size() <= 0)
            {
                if(removed instanceof DrinkItem) entityManager.remove(entityManager.find(DrinkItem.class, removed_id));
            }
            return true;
        }
        return false;
    }

    public boolean addStockToMenuItem(int menuEntryId, int amount)
    {
        if(amount > 0 && managedBar != null)
        {
            MenuEntry menuEntry = managedBar.getMenuEntryById(menuEntryId);
            if(menuEntry != null)
            {
                menuEntry.setStock(menuEntry.getStock() + amount);
                entityManager.merge(managedBar);
                return true;
            }
        }
        return false;
    }

    public Set<MenuEntry> getMenu()
    {
        if(managedBar != null)
        {
            return managedBar.getMenu();
        }
        return null;
    }

    public boolean addMenuItem(Item item, float price, int stock)
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
                if(resultList.size() <= 0)
                {
                    validateDrinkItem((DrinkItem) item);
                    entityManager.persist(item);
                }
                else
                {
                    item = entityManager.find(DrinkItem.class, resultList.get(0).getId());
                }
            }
            MenuEntry menuEntry = new MenuEntry(item, price, stock);
            validateMenuEntry(menuEntry);
            if(managedBar.addToMenu(menuEntry))
            {
                entityManager.merge(managedBar);
                return true;
            }
            else return false;
        }
        return false;
    }

    private void validateDrinkItem(DrinkItem drinkItem) throws DorstException
    {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<DrinkItem>> constraintViolations = validator.validate(drinkItem);

        if (constraintViolations.size() > 0) {

            Set<String> violationMessages = new HashSet<>();
            for (ConstraintViolation<DrinkItem> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage() + "\t|\t");
            }

            throw new DorstException(String.join("\n",violationMessages));
        }
    }

    private void validateMenuEntry(MenuEntry menuEntry) throws DorstException
    {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<MenuEntry>> constraintViolations = validator.validate(menuEntry);

        if (constraintViolations.size() > 0) {

            Set<String> violationMessages = new HashSet<>();
            for (ConstraintViolation<MenuEntry> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage() + "\t|\t");
            }

            throw new DorstException(String.join("\n",violationMessages));
        }
    }

}

