package utilities;

import jpa.entities.Customer;

import java.io.Serializable;

public class ReservationInfo implements Serializable {

    public int barId, menuEntryId, amount;

    public String customerUsername;

    public ReservationInfo(int barId, int menuEntryId, String customerUsername, int amount)
    {
        this.barId = barId;
        this.menuEntryId = menuEntryId;
        this.customerUsername = customerUsername;
        this.amount = amount;
    }

}
