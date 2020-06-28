package web_classes;

import jpa.entities.Bar;
import jsf_managed_beans.UtilityManagedBean;
import org.primefaces.event.SelectEvent;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named("dtSelect")
@ViewScoped
public class Select implements Serializable {

    private Bar bar;

    @Inject
    private UtilityManagedBean utilityManagedBean;

    public Bar getBar()
    {
        return bar;
    }

    public void setBar(Bar bar)
    {
        this.bar = bar;
    }

    public void onRowSelect(SelectEvent event) throws IOException {
        utilityManagedBean.xmlMenuRedirect(bar.getId());
    }
}



