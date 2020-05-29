package dorst19_entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("E")
public class BarEmployee extends User {
    @ManyToMany(mappedBy = "employees")
    private List<Shift> shifts = new ArrayList<>();
}
