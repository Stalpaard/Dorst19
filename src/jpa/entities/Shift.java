package jpa.entities;

import jpa.embeddables.TimePeriod;
import utilities.DaysOfTheWeek;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "SHIFT"
)
public class Shift {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "jnd_shift_bar",
            joinColumns = {
                    @JoinColumn(name = "shift_fk")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name"),
                    @JoinColumn(name = "street", referencedColumnName = "street"),
                    @JoinColumn(name = "city", referencedColumnName = "city")
            }
    )
    private Bar bar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "jnd_shift_employee",
            joinColumns = @JoinColumn(name = "shift_fk"),
            inverseJoinColumns = @JoinColumn(name = "employee_fk"))

    private List<BarEmployee> employees = new ArrayList<>();

    @Embedded
    private TimePeriod timePeriod;
    @Enumerated(EnumType.STRING)
    @Column(name = "day", updatable = false, nullable = false)
    private DaysOfTheWeek dayOfTheWeek;

    protected Shift()
    {

    }

    protected Shift(Bar bar, TimePeriod timePeriod, DaysOfTheWeek dayOfTheWeek)
    {
        this.bar = bar;
        this.timePeriod = timePeriod;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public List<BarEmployee> getEmployees() {
        return employees;
    }

    public boolean addEmployee(BarEmployee barEmployee)
    {
        if(employees.contains(barEmployee) == false)
        {
            boolean add_shift = barEmployee.addShift(this);
            return employees.add(barEmployee) && add_shift;
        }
        else return false;
    }

    public boolean removeEmployee(BarEmployee barEmployee)
    {
        boolean remove_shift = barEmployee.removeShift(this);
        return employees.remove(barEmployee) && remove_shift;
    }

    public int getId() {
        return id;
    }

    public Bar getBar() {
        return bar;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public DaysOfTheWeek getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift that = (Shift) o;
        return Objects.equals(bar, that.bar) &&
                Objects.equals(timePeriod,that.timePeriod) &&
                Objects.equals(dayOfTheWeek, that.dayOfTheWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bar, timePeriod, dayOfTheWeek);
    }
}
