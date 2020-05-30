package jpa.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("E")
public class BarEmployee extends User {
    @ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    private List<Shift> shifts = new ArrayList<>();

    protected BarEmployee()
    {

    }

    public BarEmployee(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    protected boolean addShift(Shift shift){
        if(shifts.contains(shift) == false) return shifts.add(shift);
        else return false;
    }

    protected boolean removeShift(Shift shift)
    {
        return shifts.remove(shift);
    }
}
