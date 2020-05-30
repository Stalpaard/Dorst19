package jpa.embeddables;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Address implements Serializable {
    //Huisnummer weggelaten omdat cafés meerdere huisnummers kunnen hebben => veel werk
    //Velden worden updatable gehouden opdat bars kunnen verhuizen
    @Column(name = "street", length = 90)
    private String street;
    @Column(name = "city", length = 90)
    private String city;

    protected Address()
    {

    }

    public Address(String street, String city)
    {
        this();
        this.street = street;
        this.city = city;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

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
