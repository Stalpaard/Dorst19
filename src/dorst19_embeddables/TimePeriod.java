package dorst19_embeddables;

import javax.persistence.Embeddable;

@Embeddable
public class TimePeriod {
    private int beginHour = 0;
    private int endHour = 0;

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
}
