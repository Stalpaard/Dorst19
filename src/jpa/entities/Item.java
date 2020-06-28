package jpa.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract public class Item {
    @Id @GeneratedValue
    @Column(name = "id")
    private int id;
    @NotBlank(message = "cannot be blank")
    @Size(max = 20, message = "limited to 20 characters")
    @Column(name = "name", nullable = false)
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
