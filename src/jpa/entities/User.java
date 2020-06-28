package jpa.entities;

import ch.qos.cal10n.IMessageConveyor;

import javax.management.remote.JMXServerErrorException;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity
@Table(name = "USER")
@NamedQuery(name = "QUERY_USERNAMES", query = "SELECT DISTINCT u.username FROM User u")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.CHAR)
public abstract class User {
    @Id
    @NotBlank(message = "cannot be blank")
    @Size(max = 20, message = "Username is limited to 20 characters")
    @Column(name = "username", length=20, nullable = false)
    protected String username;

    @NotBlank(message = "cannot be blank")
    @Column(name = "password", nullable = false)
    protected String password;

    public String getUsername()
    {
        return username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
