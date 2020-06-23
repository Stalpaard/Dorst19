package ws;

import ejb.QueryBean;
import jpa.entities.Item;
import jpa.entities.MenuEntry;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.persistence.Entity;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceRef;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;



@Stateless
@WebService(serviceName = "BarSoap")
public class BarSoap {

    @EJB
    QueryBean q;

    public List<Item> provideMenu(int cafe)
    {
        List<Item> items = new ArrayList<>();
        Set<MenuEntry> menu = q.queryMenuFromBar(cafe);
        if (menu != null) {
            for (MenuEntry m : menu) {
                items.add(m.getItem());
                return items;
            }
        }
        return null;
    }


}

