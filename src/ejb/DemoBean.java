package ejb;

import jpa.embeddables.Address;
import jpa.embeddables.BarInfo;
import jpa.embeddables.TimePeriod;
import jpa.entities.*;
import utilities.DaysOfTheWeek;
import utilities.PasswordHasher;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "DemoEJB")
public class DemoBean {
    @PersistenceContext(unitName = "DorstPersistenceUnit")
    EntityManager entityManager;

    public DemoBean()
    {
    }

    public String hashTest()
    {
        return Boolean.toString(PasswordHasher.checkPw("kaas", "$2a$12$e3JNRxohzzc4juUZXAQnoekukQWLOnd170s1ZzBO/iytDnBSMi.Di"));
    }

    public String epischeActie()
    {
        //Users, drankjes en de bar moeten apart gepersist worden
        Customer customer = new Customer("elias", "elias");
        entityManager.persist(customer);
        BarBoss baas = new BarBoss("baas", "haas");
        entityManager.persist(baas);
        BarEmployee employee = new BarEmployee("emplo", "yee");
        entityManager.persist(employee);

        Item drankje = new DrinkItem("bier" ,5.2f,33f);
        entityManager.persist(drankje);

        Address address = new Address("tiense","leuven");
        Bar hdr = new Bar(new BarInfo("hdr",address),200);
        hdr.addToMenu(drankje, 1, 100);
        hdr.addBoss(baas);
        hdr.addEmployeeToShift(employee, new TimePeriod(5,2), DaysOfTheWeek.FRIDAY);
        hdr.addReservation(customer, drankje, 2);
        entityManager.persist(hdr);

        return baas.getOwnedBars().get(0).getBarInfo().getName();
    }
}
