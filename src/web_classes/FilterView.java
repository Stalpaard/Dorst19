package web_classes;

import jpa.entities.Bar;

import jsf_managed_beans.UtilityManagedBean;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@Named("dtFilterView")
@ViewScoped
public class FilterView implements Serializable {

    private List<Bar> bars;

    private List<Bar> filteredBars;

    @Inject
    private UtilityManagedBean utilityManagedBean;

    @PostConstruct
    public void init() {
        bars = utilityManagedBean.getAllCafes();
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
        int filterInt = getInteger(filterText);
        Bar bar = (Bar) value;
        return bar.getBarInfo().getName().toLowerCase().contains(filterText)
                || bar.getBarInfo().getAddress().getStreet().toLowerCase().contains(filterText)
                || bar.getBarInfo().getAddress().getCity().toLowerCase().contains(filterText)
                || bar.getCapacity() > filterInt;

    }

    private int getInteger(String string) {
        try {
            return Integer.valueOf(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }

    public List<Bar> getBars() {
        return bars;
    }

    public List<Bar> getFilteredBars() {
        return filteredBars;
    }

    public void setFilteredBars(List<Bar> filteredBars) {
        this.filteredBars = filteredBars;
    }
}



