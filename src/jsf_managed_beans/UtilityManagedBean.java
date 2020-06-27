package jsf_managed_beans;

import ejb.QueryBean;
import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.MenuEntry;
import utilities.UserType;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ApplicationScoped
public class UtilityManagedBean {

    @EJB
    QueryBean queryBean;

    public Map<String, Object> mapAllCafes() {
        Map<String, Object> cafeMap = new TreeMap<>();
        for (Bar b : queryBean.queryBars()) {
            BarInfo barInfo = b.getBarInfo();
            String key = barInfo.getName() + barInfo.getAddress().getCity() + barInfo.getAddress().getStreet();
            cafeMap.put(key, b.getId());
        }
        return cafeMap;
    }

    public String menuRedirect(int barId)
    {
        return "menuDemo.xhtml?faces-redirect=true&barId=" + barId;
    }

    public String stringOfAllCafes() {
        String s = "";
        for (Bar b : queryBean.queryBars()) {
            BarInfo info = b.getBarInfo();
            s = s + info.getName() + " in " + info.getAddress().getStreet() + ", " + info.getAddress().getCity() + " | ";
        }
        return s;
    }

    public Map<String, Object> getUserTypes()
    {
        Map<String,Object> userTypes = new LinkedHashMap<>();
        for(UserType type : UserType.values())
        {
            userTypes.put(type.name(), type);
        }
        return userTypes;
    }

    public String stringOfAllUsers(){
        String s = "";
        for(String u : queryBean.queryUsers()) s = s + " " + u;
        return s;
    }

    public List<Bar> queryBars()
    {
        return queryBean.queryBars();
    }

    public Set<MenuEntry> queryMenuFromBar(int barId)
    {
        return queryBean.queryMenuFromBar(barId);
    }
}
