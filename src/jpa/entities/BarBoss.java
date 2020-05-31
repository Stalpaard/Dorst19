package jpa.entities;

import jpa.embeddables.BarInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("B")
public class BarBoss extends User {
    @ManyToMany(mappedBy = "bosses", fetch = FetchType.LAZY)
    private List<Bar> ownedBars = new ArrayList<>();

    protected BarBoss()
    {

    }

    public BarBoss(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public List<Bar> getOwnedBars()
    {
        return ownedBars;
    }

    protected boolean addBar(Bar bar)
    {
        if(ownedBars.contains(bar) == false) return ownedBars.add(bar);
        else return false;
    }

    protected boolean removeBar(Bar bar)
    {
        return ownedBars.remove(bar);
    }

}
