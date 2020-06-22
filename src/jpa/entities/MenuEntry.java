package jpa.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@Entity
@Table(
        name = "MENU_ENTRY"
)
@NamedQuery(name = "CHECK_DRINK_REF", query = "SELECT m FROM MenuEntry m WHERE m.item.id = :id")
//@NamedQuery(name = "CHECK_EXISTING_MENU", query = "SELECT m FROM MenuEntry m WHERE (b.barInfo = :barinfo)")
@XmlRootElement
public class MenuEntry {
    @Id @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_fk")
    private Item item;
    @Column(name = "price", nullable = false)
    private float price;
    @Column(name = "stock", nullable = false)
    private int stock;

    protected MenuEntry()
    {

    }

    protected MenuEntry(Item item, float price, int stock)
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
