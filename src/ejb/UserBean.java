package ejb;

import jpa.entities.BarBoss;
import jpa.entities.BarEmployee;
import jpa.entities.Customer;
import jpa.entities.User;
import utilities.PasswordHasher;
import utilities.UserType;
import utilities.jbcrypt.BCrypt;

import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "UserManagementBean")
public class UserBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public UserBean() {
    }

    public User createUser(String username, String password, UserType type)
    {
        if(entityManager.find(User.class,username) == null && !username.isEmpty() && !password.isEmpty())
        {
            User new_user;
            String hashed_pw = PasswordHasher.hashPw(password);
            switch(type)
            {
                case CUSTOMER:
                    new_user = new Customer(username, hashed_pw);
                    break;
                case BOSS:
                    new_user = new BarBoss(username, hashed_pw);
                    break;
                case EMPLOYEE:
                    new_user = new BarEmployee(username, hashed_pw);
                    break;
                default:
                    new_user = null;
                    break;
            }
            if(new_user == null) return null;

            entityManager.persist(new_user);
            return new_user;
        }
        return null;
    }

    public boolean removeUser(String username)
    {
        User to_remove = entityManager.find(User.class, username);
        if(to_remove != null)
        {
            entityManager.remove(to_remove);
            return true;
        }
        return false;
    }

    public User validateUser(String username, String password)
    {
        User user = entityManager.find(User.class, username);
        if(user != null)
        {
            if(PasswordHasher.checkPw(password, user.getPassword())) return entityManager.merge(user);
        }
        return null;
    }
/*
    public boolean isCustomer(String username)
    {
        User user = entityManager.find(User.class, username);
        if(user == null) return false;
        if(user.getClass().getAnnotation(DiscriminatorValue.class).value() == "C") return true;
        else return false;
    }

*/
}
