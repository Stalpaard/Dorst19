package dorst19_entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("C")
public class Customer extends User {
    @Column(name = "credit", nullable = false)
    private float credit;
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    @JoinTable(
            name = "jnd_customer_reservation",
            joinColumns = @JoinColumn(name = "customer_fk"),
            inverseJoinColumns = @JoinColumn(name = "reservation_fk")
    )
    private List<ItemReservation> reservations = new ArrayList<>();

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public boolean addReservation(ItemReservation itemReservationEntity)
    {
        if(reservations.contains(itemReservationEntity) == false)
        {
            reservations.add(itemReservationEntity);
            return true;
        }
        return false;
    }

    public boolean removeReservation(ItemReservation itemReservationEntity)
    {
        return reservations.remove(itemReservationEntity); //returns true if collection contained the reservation
    }

    public List<ItemReservation> getReservations()
    {
        return reservations;
    }
}
