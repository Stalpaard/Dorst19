package jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Objects;

@Entity
@NamedQuery(name = "QUERY_DRINKS", query = "SELECT d FROM DrinkItem d WHERE (d.name = :name AND d.alcoholPercentage = :alc AND d.volume = :volume)")
@DiscriminatorValue("D")
public class DrinkItem extends Item {

    @PositiveOrZero(message = "can't be negative")
    @Column(name = "alcohol_percentage")
    private float alcoholPercentage;

    @Positive(message = "has to be greater than 0")
    @Column(name = "volume")
    private float volume;

    protected DrinkItem() {

    }

    public DrinkItem(String name, float percentage, float volume) {
        super(name);
        this.alcoholPercentage = percentage;
        this.volume = volume;
    }


    public float getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public float getVolume() {
        return volume;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DrinkItem that = (DrinkItem) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(alcoholPercentage, that.alcoholPercentage) &&
                Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), alcoholPercentage, volume);
    }
}
