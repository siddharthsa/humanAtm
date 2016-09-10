package hack.humanAtm;

import com.codahale.metrics.annotation.Timed;
import db.DbConnect;
import entities.User;
import io.dropwizard.jersey.params.LongParam;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/atmApp")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class HumanAtmAPI {


    private final DbConnect db;

    public HumanAtmAPI(DbConnect db) {
        this.db = db;
    }


    @POST
    @Timed
    @Path("/registerUser")
    public String registerUser(User user) {
//        User user = User.builder()
//                .username(username)
//                .phoneNumber(phoneNumber)
//                .email(email)
//                .password(password)
//                .gcmId(gcmId)
//                .upi(upi)
//                .build();
        if (Utils.registerUser(db, user)) {
            return "Registration Successful";
        } else {
            return "Registration Failed";
        }
    }

    @POST
    @Timed
    @Path("/requestPayment")
    public String registerUser(@NotNull @PathParam("userId") LongParam userId,
                               @NotNull @PathParam("amount") LongParam amount,
                               @NotNull @PathParam("lat") double lat,
                               @NotNull @PathParam("lon") double lon) {
        return "Payment request successful";
    }

    @POST
    @Timed
    @Path("/updateLocation")
    public String registerUser(@NotNull @PathParam("userId") LongParam userId,
                               @NotNull @PathParam("lat") double lat,
                               @NotNull @PathParam("lon") double lon) {
        return "Update location successful";
    }


}
