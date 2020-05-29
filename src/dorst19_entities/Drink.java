package dorst19_entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "DRINK")
public class Drink {
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "alcohol_percentage", updatable = false) //voor existing bardrinks
    private float alcoholPercentage = 200;
    @Column(name = "volume", nullable = false, updatable = false) //voor existing bardrinks
    private float volume;

    public String getName() {
        return name;
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
        Drink that = (Drink) o;
        return  Objects.equals(name, that.name) &&
                Objects.equals(alcoholPercentage, that.alcoholPercentage) &&
                Objects.equals(volume,that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, alcoholPercentage, volume);
    }
}
