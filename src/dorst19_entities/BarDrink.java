package dorst19_entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class BarDrink {
    @Id @GeneratedValue
    private int id;
    @ManyToOne
    private Bar bar;
    @ManyToOne
    private Drink drink;
    @NotNull
    private float price;
    @NotNull
    private int stock;

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
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
