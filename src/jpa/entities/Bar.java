package jpa.entities;

import jpa.embeddables.TimePeriod;
import jpa.embeddables.BarInfo;
import utilities.DaysOfTheWeek;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.*;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name = "QUERY_BARS", query = "SELECT DISTINCT b FROM Bar b"),
        @NamedQuery(name = "CHECK_EXISTING_BARS", query = "SELECT b FROM Bar b WHERE (b.barInfo = :barinfo)")
})
@Table(
        name = "BAR"
)
public class Bar implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Embedded
    private BarInfo barInfo;

    @PositiveOrZero(message = "can't be negative")
    @Column(name = "capacity")
    private int capacity;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "OPENING_HOURS")
    @OrderColumn(name = "day_order")
    private List<TimePeriod> openingHours;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "jnd_bar_boss",
            joinColumns = @JoinColumn(name = "bar_fk", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "boss_fk")
    )
    private List<BarBoss> bosses;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(
            name = "jnd_bar_menu",
            joinColumns = @JoinColumn(name = "bar_fk", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "menu_entry_fk")
    )
    @OrderBy("id ASC")
    private Set<MenuEntry> menu;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bar", orphanRemoval = true)
    private List<Shift> shifts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bar", orphanRemoval = true)
    private List<ItemReservation> reservations;

    protected Bar() {

    }

    public Bar(BarInfo barInfo, int capacity) {
        this.barInfo = barInfo;
        this.capacity = capacity;
        this.bosses = new ArrayList<>();
        this.shifts = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.menu = new TreeSet<>();
        this.openingHours = new ArrayList<>(Collections.nCopies(7, new TimePeriod(0, 0)));
        for (DaysOfTheWeek d : DaysOfTheWeek.values()) {
            setOpeningHourAtDay(d, new TimePeriod(0, 0));
        }
    }

    public BarInfo getBarInfo() {
        return barInfo;
    }

    public boolean addEmployeeToShift(BarEmployee barEmployee, TimePeriod timePeriod, DaysOfTheWeek day) {
        Shift shift = new Shift(this, timePeriod, day);
        boolean add_shift = true;
        if (shifts.contains(shift) == false) {
            add_shift = this.addShift(shift);
        }
        return add_shift && shifts.get(shifts.indexOf(shift)).addEmployee(barEmployee);
    }

    public boolean removeEmployeeFromShift(BarEmployee barEmployee, Shift shift) {
        if (shifts.contains(shift)) {

            return shifts.get(shifts.indexOf(shift)).removeEmployee(barEmployee);
        } else return false;
    }

    private boolean addShift(Shift new_shift) {
        if (shifts.contains(new_shift) == false) {
            return shifts.add(new_shift);
        } else return false;
    }

    public boolean removeShift(Shift shift) {
        if (shifts.contains(shift)) {
            Shift remove_shift = shifts.get(shifts.indexOf(shift));
            boolean success_removal = true;
            for (BarEmployee employee : remove_shift.getEmployees()) {
                success_removal = remove_shift.removeEmployee(employee);
                if (success_removal == false) return false;
            }
            return shifts.remove(shift);
        } else return false;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public ItemReservation addReservation(Customer customer, int menuEntryId, int amount) {
        MenuEntry menuEntry = getMenuEntryById(menuEntryId);
        if (menuEntry.getStock() >= amount) {
            ItemReservation reservation = new ItemReservation(this, menuEntry, amount, customer);
            reservations.add(reservation);
            menuEntry.setStock(menuEntry.getStock() - amount);
            return reservation;
        }
        return null;
    }

    private boolean removeReservation(ItemReservation reservation) {
        return reservations.remove(reservation);
    }

    public boolean cancelReservation(ItemReservation reservation) {
        MenuEntry menuEntry = getMenuEntryById(reservation.getMenuEntry().getId());
        menuEntry.setStock(menuEntry.getStock() + reservation.getAmountOfDrinks());
        return removeReservation(reservation);
    }

    public List<ItemReservation> getReservations() {
        return reservations;
    }

    public List<BarBoss> getBosses() {
        return bosses;
    }

    public boolean addBoss(BarBoss boss) {
        if (bosses.contains(boss) == false) {
            boolean add_bar = boss.addBar(this);
            return bosses.add(boss) && add_bar;
        } else return false;
    }

    public boolean removeBoss(BarBoss boss) {
        boolean remove_bar = boss.removeBar(this);
        return bosses.remove(boss) && remove_bar;
    }

    public Set<MenuEntry> getMenu() {
        return menu;
    }

    public MenuEntry getMenuEntryById(int id) {
        for (MenuEntry m : menu) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    public boolean addToMenu(MenuEntry menuEntry) {
        return menu.add(menuEntry);
    }

    public Item removeFromMenu(int entryId) {

        MenuEntry temp = getMenuEntryById(entryId);
        if (temp != null) {
            if (menu.remove(temp)) {
                return temp.getItem();
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<TimePeriod> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHourAtDay(DaysOfTheWeek day, TimePeriod timePeriod) {
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

