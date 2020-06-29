package webservices;

import jpa.entities.Bar;
import jpa.entities.MenuEntry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("/menu")
@Stateless
@ApplicationPath("/resources")
public class RestService extends Application {

    @PersistenceContext(unitName = "DorstPersistenceUnit")
    EntityManager entityManager;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<MenuEntry> getBarMenu(@PathParam("id") String id) {
        Bar bar = entityManager.find(Bar.class, Integer.parseInt(id));
        if (bar != null) {
            return bar.getMenu();
        } else throw new NotFoundException();
    }
}
