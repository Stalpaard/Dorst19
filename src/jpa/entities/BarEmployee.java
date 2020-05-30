package jpa.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("E")
public class BarEmployee extends User {
    @ManyToMany(mappedBy = "employees")
    private List<Shift> shifts = new ArrayList<>();
}
