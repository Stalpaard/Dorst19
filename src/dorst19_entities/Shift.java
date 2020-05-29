package dorst19_entities;

import dorst19_embeddables.TimePeriod;
import dorst19_utilities.DaysOfTheWeek;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Shift {
    @Id @GeneratedValue
    private int id;
    @ManyToOne
    private Bar bar;
    @ManyToOne
    private BarEmployee employee;
    @Embedded
    private TimePeriod timePeriod;
    @Enumerated(EnumType.STRING)
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

    public BarEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(BarEmployee employee) {
        this.employee = employee;
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
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(bar, that.bar) &&
                Objects.equals(employee, that.employee) &&
                Objects.equals(timePeriod,that.timePeriod) &&
                Objects.equals(dayOfTheWeek, that.dayOfTheWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bar, employee, timePeriod, dayOfTheWeek);
    }
}
