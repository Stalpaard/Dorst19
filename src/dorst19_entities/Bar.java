package dorst19_entities;

import dorst19_embeddables.Address;
import dorst19_embeddables.TimePeriod;
import dorst19_utilities.DaysOfTheWeek;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(
        name = "BAR",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name","city","country","state","street","zipcode"})
)
public class Bar {

    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "OPENING_HOURS")
    @OrderColumn(name = "day_order")
    private List<TimePeriod> openingHours = new ArrayList<>(Collections.nCopies(7, new TimePeriod(0,0)));

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "jnd_bar_barboss",
    joinColumns = @JoinColumn(name = "bar_fk"),
    inverseJoinColumns = @JoinColumn(name = "barboss_fk"))
    private List<BarBoss> bosses = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "jnd_bar_menu",
            joinColumns = @JoinColumn(name = "bar_fk"),
            inverseJoinColumns = @JoinColumn(name = "menu_entry_fk")
    )
    private List<MenuEntry> menu = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "jnd_bar_shift",
            joinColumns = @JoinColumn(name = "bar_fk"),
            inverseJoinColumns = @JoinColumn(name = "shift_fk")
    )
    private List<Shift> shifts = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "jnd_bar_reservations",
            joinColumns = @JoinColumn(name = "bar_fk"),
            inverseJoinColumns = @JoinColumn(name = "reservation_fk")
    )
    private List<ItemReservation> reservations = new ArrayList<>();

    protected Bar()
    {

    }

    public Bar(String name, Address address, int capacity)
    {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        for(DaysOfTheWeek d : DaysOfTheWeek.values())
        {
            setOpeningHourAtDay(d, new TimePeriod(0,0));
        }
    }

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

    public List<MenuEntry> getMenu() {
        return menu;
    }

    public boolean addToMenu(MenuEntry drink) {
        if(menu.contains(drink) == false) return menu.add(drink);
        else return false;
    }

    public boolean removeFromMenu(MenuEntry drink) {
        return menu.remove(drink);
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
        return Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(openingHours, that.openingHours) &&
                Objects.equals(bosses, that.bosses) &&
                Objects.equals(menu, that.menu) &&
                Objects.equals(capacity, that.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, openingHours, bosses, menu, capacity);
    }
}
