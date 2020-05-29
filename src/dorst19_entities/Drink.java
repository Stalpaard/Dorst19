package dorst19_entities;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Drink {
    @Id @GeneratedValue
    private int id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "alcohol_percentage", nullable = true)
    private float alcoholPercentage;
    @Column(name = "volume", nullable = false)
    private float volume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(float alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drink that = (Drink) o;
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(alcoholPercentage, that.alcoholPercentage) &&
                Objects.equals(volume,that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, alcoholPercentage, volume);
    }
}
