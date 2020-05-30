package jpa.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ITEM_RESERVATION")
public class ItemReservation {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "item_fk", nullable = false, updatable = false) //niet updatable vanwege stock ook
    private Item item;
    @Column(name = "amount", nullable = false, updatable = false) //niet updatable om bijv. stock te kunnen managen
    private int amountOfDrinks;
    @ManyToOne(optional = false)
    @JoinTable(
            name = "jnd_customer_reservation",
            joinColumns = @JoinColumn(name = "reservation_fk", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "customer_fk", insertable = false, updatable = false)
    )
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinTable(
            name = "jnd_bar_reservations",
            joinColumns = @JoinColumn(name = "reservation_fk", insertable = false, updatable = false),
            inverseJoinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name",insertable = false, updatable = false),
                    @JoinColumn(name = "street", referencedColumnName = "street",insertable = false, updatable = false),
                    @JoinColumn(name = "city", referencedColumnName = "city",insertable = false, updatable = false),
            }
    )
    private Bar bar;

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

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Bar getBar() {
        return bar;
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
                Objects.equals(customer,that.customer) &&
                Objects.equals(bar, that.bar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, amountOfDrinks, customer, bar);
    }

}
