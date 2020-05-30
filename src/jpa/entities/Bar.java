package jpa.entities;

import jpa.embeddables.TimePeriod;
import jpa.embeddables.BarInfo;
import utilities.DaysOfTheWeek;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(
        name = "BAR"
)
public class Bar implements Serializable {

    @EmbeddedId
    private BarInfo barInfo;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "OPENING_HOURS")
    @OrderColumn(name = "day_order")
    private List<TimePeriod> openingHours = new ArrayList<>(Collections.nCopies(7, new TimePeriod(0,0)));

    @ManyToMany(targetEntity = BarBoss.class)
    @JoinTable(
            name = "jnd_bar_boss",
            joinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name"),
                    @JoinColumn(name = "street", referencedColumnName = "street"),
                    @JoinColumn(name = "city", referencedColumnName = "city"),
            },
            inverseJoinColumns = @JoinColumn(name = "boss_fk")
    )
    private List<BarBoss> bosses = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "jnd_bar_menu",
            joinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name"),
                    @JoinColumn(name = "street", referencedColumnName = "street"),
                    @JoinColumn(name = "city", referencedColumnName = "city"),
            },
            inverseJoinColumns = @JoinColumn(name = "menu_entry_fk")
    )
    private List<MenuEntry> menu = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "jnd_bar_shift",
            joinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name"),
                    @JoinColumn(name = "street", referencedColumnName = "street"),
                    @JoinColumn(name = "city", referencedColumnName = "city"),
            },
            inverseJoinColumns = @JoinColumn(name = "shift_fk")
    )
    private List<Shift> shifts = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "jnd_bar_reservations",
            joinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name"),
                    @JoinColumn(name = "street", referencedColumnName = "street"),
                    @JoinColumn(name = "city", referencedColumnName = "city"),
            },
            inverseJoinColumns = @JoinColumn(name = "reservation_fk")
    )
    private List<ItemReservation> reservations = new ArrayList<>();

    protected Bar()
    {

    }

    public Bar(BarInfo barInfo, int capacity)
    {
        this.barInfo = barInfo;
        this.capacity = capacity;
        for(DaysOfTheWeek d : DaysOfTheWeek.values())
        {
            setOpeningHourAtDay(d, new TimePeriod(0,0));
        }
    }

    public BarInfo getBarInfo()
    {
        return barInfo;
    }

    public List<Shift> getShifts(){
        return shifts;
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
        return Objects.equals(barInfo, that.barInfo) &&
                Objects.equals(openingHours, that.openingHours) &&
                Objects.equals(bosses, that.bosses) &&
                Objects.equals(menu, that.menu) &&
                Objects.equals(capacity, that.capacity);
    }
    @Override
    public int hashCode() {
        return Objects.hash(barInfo, openingHours, bosses, menu, capacity);
    }
}
