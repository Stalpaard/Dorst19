package dorst19_entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "ITEM",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name","alcohol_percentage","volume"})
)
public abstract class Item {
    @Id
    @Column(name = "name", nullable = false, updatable = false)
    protected String name;

    protected Item()
    {

    }

    public Item(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrinkItem that = (DrinkItem) o;
        return Objects.equals(name, that.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
