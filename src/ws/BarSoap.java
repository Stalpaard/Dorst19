package ws;

import ejb.QueryBean;
import jpa.entities.MenuEntry;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import java.util.Set;



@Stateless
@WebService(serviceName = "BarSoap")
public class BarSoap {

    @EJB
    QueryBean q;

    public Set<MenuEntry> provideMenu(int cafe)
    {
        return  q.queryMenuFromBar(cafe);
    }


}

