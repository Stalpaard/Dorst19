package dorst19_entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "BAR_DRINK")
public class BarDrink {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "bar_fk", nullable = false, updatable = false) //Niet updatable voor existing orders
    private Bar bar;
    @ManyToOne
    @JoinColumn(name = "drink_fk", nullable = false, updatable = false) //Niet updatable voor existing orders
    private Drink drink;

    @Column(name = "price", nullable = false)
    private float price;
    @Column(name = "stock", nullable = false)
    private int stock;

    public int getId() {
        return id;
    }

    public Bar getBar() {
        return bar;
    }

    public Drink getDrink() {
        return drink;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarDrink that = (BarDrink) o;
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(bar, that.bar) &&
                Objects.equals(drink, that.drink) &&
                Objects.equals(price,that.price) &&
                Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bar, drink, price, stock);
    }
}
