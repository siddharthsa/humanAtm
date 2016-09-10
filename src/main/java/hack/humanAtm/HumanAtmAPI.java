package hack.humanAtm;

import POJO.*;
import com.codahale.metrics.annotation.Timed;
import db.DbConnect;
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
        if (Utils.registerUser(db, user)) {
            return "Registration Successful";
        } else {
            return "Registration Failed";
        }
    }

    @POST
    @Timed
    @Path("/requestPayment")
    public String registerUser(PaymentRequest request) {
        if (Utils.processPaymentRequest(db, request)) {
            return "Payment Request Accepted";
        } else {
            return "Payment Request Failed";
        }
    }


    @POST
    @Timed
    @Path("/agreePayment")
    public String registerUser(AgreePaymentRequest request) {
        if (Utils.processAgreePaymentRequest(db, request)) {
            return "Agree Payment Request Accepted";
        } else {
            return "Agree Payment Request Failed";
        }
    }


    @POST
    @Timed
    @Path("/getAllFulfillers")
    @Produces(MediaType.APPLICATION_JSON)
    public FulfillerMetaData getAllFulfillers(UserIdPOJO user) {
        return Utils.getAllFulfillersMetaOfPayment(db, user);
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
