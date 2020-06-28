package jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "ITEM_RESERVATION")
public class ItemReservation {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menuEntry_fk")
    private MenuEntry menuEntry;

    @Positive(message = "has to be greater than 0")
    @Column(name = "amount")
    private int amountOfDrinks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "jnd_reservation_customer",
            joinColumns = @JoinColumn(name = "reservation_fk"),
            inverseJoinColumns = @JoinColumn(name = "customer_fk")
    )
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "jnd_reservations_bar",
            joinColumns = {
                    @JoinColumn(name = "reservation_fk")
            },
            inverseJoinColumns = @JoinColumn(name = "bar_fk", referencedColumnName = "id")
    )
    private Bar bar;

    protected ItemReservation()
    {

    }

    public ItemReservation(Bar bar, MenuEntry menuEntry, int amountOfDrinks, Customer customer)
    {
        this.bar = bar;
        this.menuEntry = menuEntry;
        this.amountOfDrinks = amountOfDrinks;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public MenuEntry getMenuEntry() {
        return menuEntry;
    }

    public void setMenuEntry(MenuEntry menuEntry) {
        this.menuEntry = menuEntry;
    }

    public int getAmountOfDrinks() {
        return amountOfDrinks;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar)
    {
        this.bar = bar;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemReservation that = (ItemReservation) o;
        return id == that.id &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuEntry, amountOfDrinks, customer, bar);
    }

}
