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
@Table(name = "BAR")
public class Bar {

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    private Address address;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "OPENING_HOURS")
    private List<TimePeriod> openingHours = new ArrayList<>(7);
    @ManyToMany
    @JoinTable(name = "jnd_bar_barboss",
    joinColumns = @JoinColumn(name = "bar_fk"),
    inverseJoinColumns = @JoinColumn(name = "barboss_fk"))
    private List<BarBoss> bosses = null;
    @OneToMany(mappedBy = "bar")
    private List<BarDrink> stock = new ArrayList<>();
    @OneToMany(mappedBy = "bar")
    private List<Shift> shifts = new ArrayList<>();
    @Column(name = "capacity", nullable = false)
    private int capacity;

    public int getId() {
        return id;
    }

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

    public List<BarBoss> getBosses() {
        return bosses;
    }

    public boolean addBoss(BarBoss boss)
    {
        if(bosses.contains(boss) == false) return bosses.add(boss);
        else return false;
    }

    public boolean removeBoss(BarBoss boss)
    {
        return bosses.remove(boss);
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
                Objects.equals(bosses, that.bosses) &&
                Objects.equals(stock, that.stock) &&
                Objects.equals(capacity, that.capacity) &&
                Objects.equals(shifts, that.shifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, openingHours, bosses, stock, capacity, shifts);
    }
}
