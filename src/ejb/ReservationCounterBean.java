package ejb;

import javax.ejb.*;

@Startup
@Singleton(name = "SingletonEJB")
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ReservationCounterBean {
    private int reservations_done;

    public ReservationCounterBean() {
    }

    public void incReservationsDone()
    {
        reservations_done = reservations_done+1;
    }

    public int getReservationsDone()
    {
        return reservations_done;
    }
}
