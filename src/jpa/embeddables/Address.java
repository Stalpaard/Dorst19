package jpa.embeddables;

import com.sun.istack.NotNull;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@XmlRootElement
@Access(AccessType.FIELD)
public class Address implements Serializable {
    //Huisnummer weggelaten omdat cafés meerdere huisnummers kunnen hebben => veel werk
    //Velden worden updatable gehouden opdat bars kunnen verhuizen

    @Size(max = 90, message = "limited to 90 characters")
    @NotBlank(message = "cannot be blank")
    @Column(name = "street", length = 90)
    private String street;

    @Size(max = 90, message = "limited to 90 characters")
    @NotBlank(message = "cannot be blank")
    @Column(name = "city", length = 90)
    private String city;

    public Address()
    {

    }

    public Address(String street, String city)
    {
        this();
        this.street = street;
        this.city = city;
    }
    @XmlElement
    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }
    @XmlElement
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address that = (Address) o;
        return Objects.equals(street, that.street) &&
                Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city);
    }
}
