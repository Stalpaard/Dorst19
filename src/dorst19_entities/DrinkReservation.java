package dorst19_entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DRINK_RESERVATION")
public class DrinkReservation  {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "bardrink_fk", nullable = false, updatable = false) //niet updatable vanwege stock ook
    private BarDrink barDrink;
    @Column(name = "amount", nullable = false, updatable = false) //niet updatable om bijv. stock te kunnen managen
    private int amountOfDrinks;
    @ManyToOne
    @JoinColumn(name = "customer_fk", nullable = false)  //updatable kan gebruikt worden om een reservation te giften
    private Customer customer;

    public int getId() {
        return id;
    }

    public BarDrink getBarDrink() {
        return barDrink;
    }

    public int getAmountOfDrinks() {
        return amountOfDrinks;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrinkReservation that = (DrinkReservation) o;
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(barDrink, that.barDrink) &&
                Objects.equals(amountOfDrinks, that.amountOfDrinks) &&
                Objects.equals(customer,that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, barDrink, amountOfDrinks, customer);
    }

}
