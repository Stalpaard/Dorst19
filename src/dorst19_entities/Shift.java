package dorst19_entities;

import dorst19_embeddables.TimePeriod;
import dorst19_utilities.DaysOfTheWeek;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SHIFT")
public class Shift {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinTable(
            name = "jnd_bar_shift",
            joinColumns = @JoinColumn(name = "shift_fk", insertable = false, updatable = false),
            inverseJoinColumns = {
                    @JoinColumn(name = "name", referencedColumnName = "name", insertable = false, updatable = false),
                    @JoinColumn(name = "street", referencedColumnName = "street", insertable = false, updatable = false),
                    @JoinColumn(name = "city", referencedColumnName = "city", insertable = false, updatable = false),
            }
    )
    private Bar bar;
    @ManyToMany
    @JoinTable(name = "jnd_shift_employee",
            joinColumns = @JoinColumn(name = "shift_fk"),
            inverseJoinColumns = @JoinColumn(name = "employee_fk"))
    private List<BarEmployee> employees = new ArrayList<>();
    @Embedded
    private TimePeriod timePeriod;
    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private DaysOfTheWeek dayOfTheWeek;

    public int getId() {
        return id;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public List<BarEmployee> getEmployees() {
        return employees;
    }

    public boolean addEmployee(BarEmployee barEmployee)
    {
        if(employees.contains(barEmployee) == false) return employees.add(barEmployee);
        else return false;
    }

    public boolean removeEmployee(BarEmployee barEmployee)
    {
        return employees.remove(barEmployee);
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public DaysOfTheWeek getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(DaysOfTheWeek dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
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
