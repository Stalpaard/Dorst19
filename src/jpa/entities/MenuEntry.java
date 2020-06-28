package jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Entity
@Table(
        name = "MENU_ENTRY"
)
@NamedQuery(name = "CHECK_DRINK_REF", query = "SELECT m FROM MenuEntry m WHERE m.item.id = :id")
@XmlRootElement
public class MenuEntry {
    @Id @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @NotNull(message = "has to have an item linked to it")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_fk", nullable = false)
    private Item item;

    @PositiveOrZero(message = "cannot be negative")
    @Column(name = "price", nullable = false)
    private float price;

    @PositiveOrZero(message = "cannot be negative")
    @Column(name = "stock", nullable = false)
    private int stock;

    protected MenuEntry()
    {

    }

    public MenuEntry(Item item, float price, int stock)
    {
        this.item = item;
        this.price = price;
        this.stock = stock;
    }
    @XmlElement
    public int getId() {
        return id;
    }
    @XmlElement
    public Item getItem() {
        return item;
    }
    @XmlElement
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    @XmlElement
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MenuEntry that = (MenuEntry) o;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }
}
