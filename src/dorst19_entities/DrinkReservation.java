package dorst19_entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class DrinkReservation  {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "bardrink_fk")
    private BarDrink barDrink;
    @Column(name = "amount", nullable = false)
    private int amountOfDrinks;
    @ManyToOne
    private Customer customer;

    public int getId() {
        return id;
    }

    public BarDrink getBarDrink() {
        return barDrink;
    }

    public void setBarDrink(BarDrink barDrink) {
        this.barDrink = barDrink;
    }

    public int getAmountOfDrinks() {
        return amountOfDrinks;
    }

    public void setAmountOfDrinks(int amountOfDrinks) {
        this.amountOfDrinks = amountOfDrinks;
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
