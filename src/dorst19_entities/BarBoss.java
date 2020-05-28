package dorst19_entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("B")
public class BarBoss extends User {
    @OneToMany
    private List<Bar> ownedBars;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarBoss that = (BarBoss) o;
        return id == that.id &&
                Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(ownedBars,that.ownedBars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, ownedBars);
    }
}
