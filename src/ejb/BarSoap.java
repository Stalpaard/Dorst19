package ejb;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

@Stateless
@WebService(name = "BarSoap")
public class BarSoap {
    @WebMethod(operationName = "sampleText")
    public String zegIets()
    {
        return "hallo";
    }
}
