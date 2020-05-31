package jpa.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ITEM_RESERVATION")
public class ItemReservation {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_fk", nullable = false, updatable = false) //niet updatable vanwege stock ook
    private Item item;
    @Column(name = "amount", nullable = false, updatable = false) //niet updatable om bijv. stock te kunnen managen
    private int amountOfDrinks;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
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

    protected ItemReservation(Bar bar, Item item, int amountOfDrinks)
    {
        this.bar = bar;
        this.item = item;
        this.amountOfDrinks = amountOfDrinks;
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public int getAmountOfDrinks() {
        return amountOfDrinks;
    }

    public Customer getCustomer() {
        return customer;
    }

    protected boolean setCustomer(Customer customer) {
        this.customer = customer;
        return customer.addReservation(this);
    }

    public Bar getBar() {
        return bar;
    }

    protected void setBar(Bar bar)
    {
        this.bar = bar;
    }

    protected boolean cancelReservation()
    {
        this.bar = null;
        boolean success_removal = customer.removeReservation(this);
        if(success_removal)
        {
            this.bar = null;
            this.customer = null;
            return true;
        }
        else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemReservation that = (ItemReservation) o;
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(item, that.item) &&
                Objects.equals(amountOfDrinks, that.amountOfDrinks) &&
                Objects.equals(customer,that.customer)&&
                Objects.equals(bar, that.bar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bar, item, amountOfDrinks, customer);
    }

}
