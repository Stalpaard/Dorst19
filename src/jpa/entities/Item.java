package jpa.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false, updatable = false)
    protected String name;

    protected Item()
    {

    }

    public int getId() {
        return id;
    }

    public Item(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
