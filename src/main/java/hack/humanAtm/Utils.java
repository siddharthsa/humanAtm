package hack.humanAtm;

import POJO.*;
import db.DbConnect;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by siddharth on 9/10/16.
 */
@Slf4j
public class Utils {
    public static int registerUser(DbConnect db, User user) {
        return db.writeNewUserToDB(user);
    }

    public static boolean processPaymentRequest(DbConnect db, PaymentRequest request) {
        PaymentRequestResponseMeta prrm;
        try {
            prrm = db.getUsersInVicinity(request);
        } catch (SQLException e) {
            log.error("Can't query DB", e);
            return false;
        }
        try {
            //TODO - get name from db for this request
            sendBorrowRequestNotifications(prrm.getResultantIds(), prrm.getPaymentRequestId(), "Siddharth", request.getAmount());
        } catch (IOException e) {
            log.error("Can't send gcm msg", e);
            return false;
        }

        return true;
    }

    private static void sendBorrowRequestNotifications(ArrayList<String> eligibleUsers, int requestId, String name, int amount) throws IOException {
        for (String gcm : eligibleUsers) {
            String message = "{ \"data\": {\n" +
                    "    \"requestId\": \"" + requestId + "\",\n" +
                    "    \"name\": \"" + name + "\"\n" +
                    "    \"amount\": \"" + amount + "\"\n" +
                    "  },\n" +
                    "  \"to\" : \"" + gcm + "\"\n" +
                    "}";
            makeHTTPRequest(message);
        }
    }

    private static boolean makeHTTPRequest(String message) throws IOException {


        String url = "https://fcm.googleapis.com/fcm/send";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(url);

        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setHeader("Authorization", "key=AIzaSyBwEIo8YIAuoZLxAUNRTQowEpsl4Ljy08M");

        StringEntity msg = new StringEntity(message);
        httpRequest.setEntity(msg);
        HttpResponse httpresponse = httpClient.execute(httpRequest);

        boolean returnVal = httpresponse.getStatusLine().getStatusCode() == 200 ? true : false;

        return returnVal;

    }

    public static boolean processAgreePaymentRequest(DbConnect db, AgreePaymentRequest request) {
        return db.updateTransationFulfillers(request);
    }

    public static FulfillerMetaData getAllFulfillersMetaOfPayment(DbConnect db, UserIdPOJO user) {
        return db.getFulfillerMeta(user);
    }

    public static boolean acknowledgeTransaction(DbConnect db, TransactionPOJO transactionPOJO) {
        PaymentSuccessMeta psm = db.processTransactionSuccess(transactionPOJO);

        try {
            //TODO - get name from db for this request
            sendSuccessNotifications(psm.getGcmId(), psm.getAmount());
        } catch (IOException e) {
            log.error("Can't send gcm msg", e);
            return false;
        }
        return true;
    }

    private static void sendSuccessNotifications(String gcmId, int amount) throws IOException {
        String message = "{ \"data\": {\n" +
                "    \"type\": \"" + "success" + "\"\n" +
                "    \"amount\": \"" + amount + "\"\n" +
                "  },\n" +
                "  \"to\" : \"" + gcmId + "\"\n" +
                "}";
        makeHTTPRequest(message);
    }
}
