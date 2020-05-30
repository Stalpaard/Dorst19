package dorst19_jsf_beans;

import dorst19_ejb.DemoBean;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class DemoManagedBean implements Serializable {
    @EJB
    DemoBean demoBean;

    public String getDemo(){
        demoBean.epischeActie();
        return "jippie gelukt";
    }
}
