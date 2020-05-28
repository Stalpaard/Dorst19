package dorst19_entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("E")
public class BarEmployee extends User {
    @OneToMany
    private List<Shift> shifts;

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
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(shifts,that.shifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, shifts);
    }
}
