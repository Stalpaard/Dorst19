package jsf_managed_beans;

import ejb.*;
import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.BarBoss;
import jpa.entities.User;
import utilities.UserType;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class DemoManagedBean implements Serializable {
    @EJB
    BarQueryBean barQueryBean;

    @EJB
    BarCreationBean barCreationBean;

    @EJB
    BarManagementBean barManagementBean;

    @EJB
    UserBean userBean;

    public String caféNamen(){

        String s = "";
        for(BarInfo b : barQueryBean.queryBars()) s = s + b.getName();
        return s;
    }

    public void createRecup() {
        BarBoss baasje = (BarBoss)userBean.createUser("kaashaas", "pasen", UserType.BOSS);
        //Indien user al bestaat is baasje = null ==> inloggen dan gwn met validate
        if(baasje == null) baasje = (BarBoss)userBean.validateUser("kaashaas", "pasen");
        Address address = new Address("tiense", "leuven");
        BarInfo barInfo = new BarInfo("recup", address);
        barCreationBean.createBar(baasje, barInfo, 200);
    }

    public String isRecupManaged()
    {
        if(barManagementBean.getManagedBar() == null) return "neeeee";
        return "jaaa";
    }

    public void manageRecup() {
        //Om de named query te testen heb ik het zo gedaan
        for(BarInfo b : barQueryBean.queryBars())
        {
            if(b.getName().equals("recup")) barManagementBean.attachBar(b);
        }
    }

    public void removeRecup() {
        //remove attached bar
        barManagementBean.removeBar();
    }
}
