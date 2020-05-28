package dorst19_entities;

import com.sun.istack.NotNull;
import dorst19_embeddables.Address;
import dorst19_embeddables.TimePeriod;
import dorst19_utilities.DaysOfTheWeek;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Bar {

    @Id @GeneratedValue
    private int id;
    @NotNull
    private String name;
    @Embedded
    private Address address;
    @ElementCollection
    private List<TimePeriod> openingHours = new ArrayList<>(7);
    @ManyToOne
    private BarBoss owner;
    @OneToMany
    private List<BarDrink> stock;
    @OneToMany
    private List<Shift> shifts;
    @NotNull
    private int capacity;

    public List<Shift> getShifts(){
        return shifts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BarBoss getOwner() {
        return owner;
    }

    public void setOwner(BarBoss owner) {
        this.owner = owner;
    }

    public List<BarDrink> getStock() {
        return stock;
    }

    public boolean addToStock(BarDrink drink) {
        if(stock.contains(drink) == false) return stock.add(drink);
        else return false;
    }

    public boolean removeFromStock(BarDrink drink) {
        return stock.remove(drink);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<TimePeriod> getOpeningHours()
    {
        return openingHours;
    }

    public void setOpeningHourAtDay(DaysOfTheWeek day, TimePeriod timePeriod)
    {
        this.openingHours.set(day.ordinal(), timePeriod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar that = (Bar) o;
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(openingHours, that.openingHours) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(stock, that.stock) &&
                Objects.equals(capacity, that.capacity) &&
                Objects.equals(shifts, that.shifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, openingHours, owner, stock, capacity, shifts);
    }
}
