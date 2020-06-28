package ejb;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class DorstException extends RuntimeException{

    public DorstException(String message)
    {
        super(message);
    }
}
