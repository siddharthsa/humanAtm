package hack.humanAtm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/atm")
public class HumanAtmResource {

    @GET
    @Path("/something")
    public void getSomething(){

    }
}
