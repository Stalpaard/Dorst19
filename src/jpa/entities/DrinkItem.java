package jpa.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "DRINK",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "volume"})}
)
public class DrinkItem extends Item{

    @Column(name = "alcohol_percentage", nullable = false, updatable = false) //voor existing bardrinks
    private float alcoholPercentage = 200;
    @Column(name = "volume", nullable = false, updatable = false) //voor existing bardrinks
    private float volume;

    protected DrinkItem()
    {

    }

    public DrinkItem(String name, float percentage, float volume)
    {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrinkItem that = (DrinkItem) o;
        return  Objects.equals(name, that.name) &&
                Objects.equals(alcoholPercentage, that.alcoholPercentage) &&
                Objects.equals(volume,that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, alcoholPercentage, volume);
    }
}
