package webservices;

import ejb.QueryBean;
import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.entities.Bar;
import jpa.entities.MenuEntry;
import utilities.ReservationInfo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

@Path("/menu")
@Stateless
@ApplicationPath("/resources")
public class RestService extends Application {
        // /Dorst19/resources/epic/hello

    @PersistenceContext(unitName = "DorstPersistenceUnit")
    EntityManager entityManager;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<MenuEntry> getBarMenu(@PathParam("id") String id)
    {
        Bar bar = entityManager.find(Bar.class, Integer.parseInt(id));
        if(bar != null)
        {
            return bar.getMenu();
        }
        else throw new NotFoundException();
    }
}
