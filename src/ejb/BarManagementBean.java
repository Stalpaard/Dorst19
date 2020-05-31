package ejb;

import jpa.embeddables.BarInfo;
import jpa.embeddables.TimePeriod;
import jpa.entities.Bar;
import jpa.entities.BarBoss;
import jpa.entities.MenuEntry;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

//@SessionScoped
@Stateful(name = "BarManagementEJB")
public class BarManagementBean implements Serializable {

    @PersistenceContext(name = "DorstPersistenceBean")
    EntityManager entityManager;

    Bar managedBar = null;

    @Resource
    private SessionContext ctx;

    public BarManagementBean() {
    }

    public boolean attachBar(BarInfo findBarInfo)
    {
        Bar findBar = entityManager.find(Bar.class, findBarInfo);
        if(findBar != null)
        {
            managedBar = findBar;
            return true;
        }
        return false;
    }

    public Bar getManagedBar() {
        return managedBar;
    }

    @PrePassivate
    private void detachBar()
    {
        if(managedBar != null) entityManager.detach(managedBar);
    }

    @PostActivate
    private void refreshBar()
    {
        if(managedBar != null){
            managedBar = entityManager.find(Bar.class,managedBar.getBarInfo());
            entityManager.refresh(managedBar);
        }
    }

    public boolean removeBar()
    {
        //Misschien op een manier nog fixen da enkel bosses hun eigen cafés kunnen verwijderen
        //if(!ctx.isCallerInRole("boss")) throw new SecurityException("Only bosses may remove cafés");
        if(managedBar != null)
        {
            entityManager.remove(entityManager.find(Bar.class,managedBar.getBarInfo()));
            managedBar = null;
            return true;
        }
        return false;
    }
}
