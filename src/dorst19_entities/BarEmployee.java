package dorst19_entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("E")
public class BarEmployee extends User {
    @OneToMany(mappedBy = "employee")
    private List<Shift> shifts = new ArrayList<>();

    public boolean addShift(Shift shift)
    {
        if(shifts.contains(shift) == false)
        {
            return shifts.add(shift);
        }
        else return false;
    }

    public boolean removeShift(Shift shift)
    {
        return shifts.remove(shift);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarEmployee that = (BarEmployee) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(shifts,that.shifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, shifts);
    }
}
