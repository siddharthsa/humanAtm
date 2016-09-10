package hack.humanAtm;

import POJO.*;
import com.codahale.metrics.annotation.Timed;
import db.DbConnect;
import io.dropwizard.jersey.params.LongParam;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

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
    @Produces(MediaType.APPLICATION_JSON)
    public UserIdPOJO registerUser(User user) {
        if (Utils.registerUser(db, user) != -1) {
            return new UserIdPOJO(Utils.registerUser(db, user));
        } else {
            return null;
        }
    }

    @POST
    @Timed
    @Path("/requestPayment")
    public String requestPaymentAPI(PaymentRequest request) {
        if (Utils.processPaymentRequest(db, request)) {
            return "Payment Request Accepted";
        } else {
            return "Payment Request Failed";
        }
    }


    @POST
    @Timed
    @Path("/agreePayment")
    public String agreePaymentAPI(AgreePaymentRequest request) {
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
    public ArrayList<FulfillerMetaData> getAllFulfillers(UserIdPOJO user) {

        //TODO - make array
        ArrayList<FulfillerMetaData> returnVal = new ArrayList<>();
        returnVal.add(Utils.getAllFulfillersMetaOfPayment(db, user));
        return returnVal;
    }


    @POST
    @Timed
    @Path("/updateLocation")
    public String updateLocationAPI(@NotNull @PathParam("userId") LongParam userId,
                                    @NotNull @PathParam("lat") double lat,
                                    @NotNull @PathParam("lon") double lon) {
        return "Update location successful";
    }


}
