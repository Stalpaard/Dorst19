package ejb;

import jpa.entities.*;
import utilities.PasswordHasher;
import utilities.UserType;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "UserManagementBean")
public class UserBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public UserBean() {
    }
    @Interceptors(LogInterceptor.class)
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
    @Interceptors(LogInterceptor.class)
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

    public void cancelUserReservation(Customer managed_customer, int reservationId)
    {
        ItemReservation reservation = entityManager.find(ItemReservation.class, reservationId);
        if(reservation != null)
        {
            if(managed_customer != null)
            {
                managed_customer.removeReservation(reservation);
                Bar bar = entityManager.find(Bar.class, reservation.getBar().getId());
                if(bar != null)
                {
                    bar.cancelReservation(reservation);
                    entityManager.merge(managed_customer);
                    entityManager.merge(bar);
                    entityManager.flush();
                }
            }
        }
    }

    public User validateUser(String username, String password)
    {
        User user = entityManager.find(User.class, username);
        if(user != null)
        {
            if(PasswordHasher.checkPw(password, user.getPassword())) return user;
        }
        return null;
    }

    public void addCreditToUser(User user, float amount)
    {
        Customer customer = entityManager.find(Customer.class, user.getUsername());
        if(customer != null)
        {
            customer.setCredit(customer.getCredit() + amount);
            entityManager.merge(customer);
        }
    }

    public User refreshUser(User user)
    {
        if(user == null) return null;
        User retrieved = entityManager.find(User.class, user.getUsername());
        entityManager.refresh(retrieved);
        return retrieved;
    }
}
