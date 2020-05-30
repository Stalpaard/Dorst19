package dorst19_entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("B")
public class BarBoss extends User {
    @ManyToMany(mappedBy = "bosses")
    private List<Bar> ownedBars = new ArrayList<>();

    public List<Bar> getOwnedBars()
    {
        return ownedBars;
    }

    public boolean addBar(Bar bar)
    {
        if(ownedBars.contains(bar) == false)
        {
           return ownedBars.add(bar); //returns true if collection has changed
        }
        else return false;
    }

    public boolean removeBar(Bar bar)
    {
        return ownedBars.remove(bar);
    }

}
