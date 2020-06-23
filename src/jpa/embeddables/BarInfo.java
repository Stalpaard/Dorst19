package jpa.embeddables;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@XmlRootElement
@Access(AccessType.FIELD)
public class BarInfo implements Serializable {
    @Column(name = "name", length = 20)
    private String name;
    @Embedded
    private Address address;

    protected BarInfo()
    {

    }
    public BarInfo(String name, Address address)
    {
        this();
        this.name = name;
        this.address = address;
    }
    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @XmlElement
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarInfo that = (BarInfo) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }
}

