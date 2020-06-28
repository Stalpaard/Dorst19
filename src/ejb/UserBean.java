package ejb;

import com.sun.xml.ws.util.StringUtils;
import jpa.entities.*;
import utilities.PasswordHasher;
import utilities.UserType;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.*;
import java.util.HashSet;
import java.util.Set;

@Stateless(name = "UserManagementBean")
public class UserBean {
    @PersistenceContext(name = "DorstPersistenceUnit")
    EntityManager entityManager;

    public UserBean() {
    }
    @Interceptors(LogInterceptor.class)
    public User createUser(String username, String password, UserType type)
    {
        if(entityManager.find(User.class,username) == null)// && !username.isEmpty() && !password.isEmpty())
        {
            User new_user = null;
            switch(type)
            {
                case CUSTOMER:
                    new_user = new Customer(username, password);
                    validateCustomer((Customer)new_user);
                    break;
                case BOSS:
                    new_user = new BarBoss(username, password);
                    validateBoss((BarBoss)new_user);
                    break;
                default:
                    break;
            }
            if(new_user == null) return null;
            new_user.setPassword(PasswordHasher.hashPw(password));
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

    private void validateCustomer(Customer customer) throws RuntimeException
    {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<Customer> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage() + "\t|\t");
            }

            throw new RuntimeException(String.join("\n",violationMessages));
        }
    }

    private void validateBoss(BarBoss boss) throws RuntimeException
    {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<BarBoss>> constraintViolations = validator.validate(boss);

        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<BarBoss> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage() + "\t|\t");
            }

            throw new RuntimeException(String.join("\n",violationMessages));
        }
    }

    private void validateUser(User user) throws RuntimeException
    {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<User> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage() + "\t|\t");
            }

            throw new RuntimeException(String.join("\n",violationMessages));
        }
    }

    public User authenticateUser(String username, String password) throws EJBException
    {
        if(username == null || password == null) throw new RuntimeException("Please fill in the credentials form");
        User user = entityManager.find(User.class, username);
        if(user != null)
        {
            if(PasswordHasher.checkPw(password, user.getPassword())) return user;
        }
        else throw new EJBException("User not found");
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
