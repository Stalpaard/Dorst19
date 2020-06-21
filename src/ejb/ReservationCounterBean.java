package ejb;

import javax.ejb.*;

@Startup
@Singleton(name = "SingletonEJB")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ReservationCounterBean {
    private int reservations_done = 0;

    public ReservationCounterBean() {
    }
    @Lock(LockType.WRITE)
    public void incReservationsDone()
    {
        reservations_done++;
    }

    @Lock(LockType.READ)
    public int getReservationsDone()
    {
        return reservations_done;
    }
}
