package jpa.entities;

import jpa.embeddables.TimePeriod;
import jpa.embeddables.BarInfo;
import utilities.DaysOfTheWeek;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@NamedQuery(name = "QUERY_BARINFO", query = "SELECT DISTINCT b.barInfo FROM Bar b")
@Table(
        name = "BAR"
)
public class Bar implements Serializable
{

    @EmbeddedId
    private BarInfo barInfo;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "OPENING_HOURS")
    @OrderColumn(name = "day_order")
    private List<TimePeriod> openingHours;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "jnd_bar_boss",
            joinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name"),
                    @JoinColumn(name = "street", referencedColumnName = "street"),
                    @JoinColumn(name = "city", referencedColumnName = "city"),
            },
            inverseJoinColumns = @JoinColumn(name = "boss_fk")
    )
    private List<BarBoss> bosses;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "jnd_bar_menu",
            joinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name"),
                    @JoinColumn(name = "street", referencedColumnName = "street"),
                    @JoinColumn(name = "city", referencedColumnName = "city"),
            },
            inverseJoinColumns = @JoinColumn(name = "menu_entry_fk")
    )
    private List<MenuEntry> menu;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bar")
    private List<Shift> shifts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bar")
    private List<ItemReservation> reservations;

    protected Bar()
    {

    }

    public Bar(BarInfo barInfo, int capacity)
    {
        this.barInfo = barInfo;
        this.capacity = capacity;
        this.bosses = new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.openingHours = new ArrayList<>(Collections.nCopies(7, new TimePeriod(0,0)));
        for(DaysOfTheWeek d : DaysOfTheWeek.values())
        {
            setOpeningHourAtDay(d, new TimePeriod(0,0));
        }
    }

    public BarInfo getBarInfo()
    {
        return barInfo;
    }

    public boolean addEmployeeToShift(BarEmployee barEmployee, TimePeriod timePeriod, DaysOfTheWeek day)
    {
        Shift shift = new Shift(this, timePeriod, day);
        boolean add_shift = true;
        if(shifts.contains(shift) == false)
        {
            add_shift = this.addShift(shift);
        }
        return add_shift && shifts.get(shifts.indexOf(shift)).addEmployee(barEmployee);
    }

    public boolean removeEmployeeFromShift(BarEmployee barEmployee, Shift shift)
    {
        if(shifts.contains(shift))
        {

            return shifts.get(shifts.indexOf(shift)).removeEmployee(barEmployee);
        }
        else return false;
    }

    private boolean addShift(Shift new_shift)
    {
        if(shifts.contains(new_shift) == false)
        {
            return shifts.add(new_shift);
        }
        else return false;
    }

    public boolean removeShift(Shift shift)
    {
        if(shifts.contains(shift))
        {
            Shift remove_shift = shifts.get(shifts.indexOf(shift));
            boolean success_removal = true;
            for(BarEmployee employee : remove_shift.getEmployees()){
                success_removal = remove_shift.removeEmployee(employee);
                if(success_removal == false) return false;
            }
            return shifts.remove(shift);
        }
        else return false;
    }

    public List<Shift> getShifts(){
        return shifts;
    }

    public boolean addReservation(Customer customer, Item item, int amount)
    {
        ItemReservation reservation = new ItemReservation(this, item, amount);
        if(reservations.contains(reservation) == false)
        {
            boolean add_reservation_customer = reservation.setCustomer(customer);
            boolean add_reservation = reservations.add(reservation);
            return (add_reservation_customer && add_reservation);
        }
        return false;
    }

    public boolean removeReservation(ItemReservation reservation)
    {
        if(reservations.contains(reservation)) {

            reservation.cancelReservation();
        }
        else return false;
        return reservations.remove(reservation);
    }

    public List<ItemReservation> getReservations()
    {
        return reservations;
    }

    public List<BarBoss> getBosses() {
        return bosses;
    }

    public boolean addBoss(BarBoss boss)
    {
        if(bosses.contains(boss) == false)
        {
            boolean add_bar = boss.addBar(this);
            return bosses.add(boss) && add_bar;
        }
        else return false;
    }

    public boolean removeBoss(BarBoss boss)
    {
        boolean remove_bar = boss.removeBar(this);
        return bosses.remove(boss) && remove_bar;
    }

    public List<MenuEntry> getMenu() {
        return menu;
    }

    public boolean addToMenu(Item item, float price, int stock) {
        MenuEntry new_entry = new MenuEntry(item, price, stock);
        if(menu.contains(new_entry) == false) return menu.add(new_entry);
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

