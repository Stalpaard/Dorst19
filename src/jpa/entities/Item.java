package jpa.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
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
    @XmlTransient
    public int getId() {
        return id;
    }

    public Item(String name)
    {
        this.name = name;
    }
    @XmlElement
    public String getName() {
        return name;
    }
}
