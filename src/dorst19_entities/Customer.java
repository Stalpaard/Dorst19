package dorst19_entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("C")
public class Customer extends User {
    private float credit;
    @OneToMany
    private List<DrinkReservation> reservations;

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public boolean addReservation(DrinkReservation drinkReservationEntity)
    {
        if(reservations.contains(drinkReservationEntity) == false)
        {
            reservations.add(drinkReservationEntity);
            return true;
        }
        return false;
    }

    public boolean removeReservation(DrinkReservation drinkReservationEntity)
    {
        return reservations.remove(drinkReservationEntity); //returns true if collection contained the reservation
    }

    public List<DrinkReservation> getReservations()
    {
        return reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer that = (Customer) o;
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(credit, that.credit) &&
                Objects.equals(reservations, that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, credit, reservations);
    }
}
