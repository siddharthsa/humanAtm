package db;

import POJO.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by siddharth on 9/10/16.
 */
/*
Schema:
CREATE TABLE Users(id int NOT NULL AUTO_INCREMENT,username varchar(255),phonenumber bigint NOT NULL,email varchar(255),password varchar(255),gcmId varchar(255),status varchar(255),created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,PRIMARY KEY (id));

CREATE TABLE payment_requests(id int NOT NULL AUTO_INCREMENT,requester_id int NOT NULL,amount int NOT NULL,requested_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,status varchar(255),fulfilled_by int NOT NULL, PRIMARY KEY (id));

 alter table Users add lat DOUBLE;
  alter table Users add lon DOUBLE;

 */
@Slf4j
public class DbConnect {

    private static final BasicDataSource dataSource = new BasicDataSource();
    private static String host;

    private final String HARD_CODED_GCM = "fi2KKGLUyLM:APA91bHH3DmX84TM5JmwG0SEeWGIRBBW2AhqrQ7bH9Nj7HTiB71PR5fC8uDJYeaJnW92HSk5vTNcdbh5bd7it26m4UeOpZP7SkyR1MvYH8q4_H_FzCU7p_lw3kkmqJiEeCpiJiHlaUTn";

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        host = "localhost";
        dataSource.setUrl("jdbc:mysql://" + host + ":3306/atmApp");
        dataSource.setUsername("root");
        dataSource.setPassword("");
    }

    public DbConnect(String host) {
        this.host = host;
        //
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public int writeNewUserToDB(User user) {
        String insertSQL = "insert into Users (username,phonenumber,email,password,gcmId,status,lat,lon) values ('"
                + user.getUsername()
                + "',"
                + user.getPhoneNumber()
                + ",'" +
                user.getEmail()
                + "','"
                + user.getPassword()
                + "','"
                + user.getGcmId()
                + "','pending',"
                + "12.971599"
                + ",77.594563"
                + ")";

        PreparedStatement statement = null;
        int result = 0;
        try {
            statement = getConnection().prepareStatement(insertSQL);
            result = statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        if (result != 1)

        {
            return -1;
        }

        String userIdSQL = "select id from Users where phonenumber = " + user.getPhoneNumber() + " order by created_on desc";
        int userId = -1;
        try

        {
            PreparedStatement statement1 = getConnection().prepareStatement(userIdSQL);
            ResultSet rs = statement1.executeQuery();

            while (rs.next()) {
                userId = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException e) {
            log.error("Exception while querying db", e);
            return -1;
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return userId;
    }

    public PaymentRequestResponseMeta getUsersInVicinity(PaymentRequest request) throws SQLException {

        //Insert into payment_requests
        String insertSQL = "insert into payment_requests (requester_id,amount,status) values ('"
                + request.getUserId()
                + "',"
                + request.getAmount()
                + ",'pending')";

        PreparedStatement statement = null;
        int result = 0;
        try {
            statement = getConnection().prepareStatement(insertSQL);
            result = statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (result != 1) {
            throw new SQLException("Failed to insert record");
        }

        //Get id of this payment_requests
        String lastRequestQuery = "select * from payment_requests where requester_id = " + request.getUserId() + " order by requested_time desc limit 1";
        PreparedStatement statement1 = null;
        int paymentRequestId = -1;
        try {
            statement1 = getConnection().prepareStatement(lastRequestQuery);
            ResultSet rs = statement1.executeQuery();

            while (rs.next()) {
                paymentRequestId = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        //Get all eligible users

        ArrayList<String> resultantIds = new ArrayList<>(10);

        int iter = 0;
        String allUsersQuery = "select * from Users";
        PreparedStatement statement2 = null;
        try {
            statement2 = getConnection().prepareStatement(allUsersQuery);
            ResultSet rs1 = statement2.executeQuery();
            while (rs1.next()) {
                int userId = rs1.getInt("id");
                // do not send notification to yourself
                if (userId != request.getUserId()) {
                    String gcmId = rs1.getString("gcmId");
                    double lat = rs1.getDouble("lat");
                    double lon = rs1.getDouble("lon");

                    if (euclideanDistance(request, lat, lon) < request.getDistanceMeters() && iter < 1000) {
                        resultantIds.add(gcmId);
                        iter++;
                    }
                }
            }
            rs1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        //TODO - hack : remove it


        if (iter == 0) {
            resultantIds.add(HARD_CODED_GCM);
        }

        return new PaymentRequestResponseMeta(paymentRequestId, resultantIds);
    }

    private double euclideanDistance(PaymentRequest request, double lat, double lon) {
        double latDist = Math.abs(lat - request.getLat());
        double lonDist = Math.abs(lon - request.getLon());

        return Math.sqrt(latDist * latDist + lonDist * lonDist);

    }

    public boolean updateTransationFulfillers(AgreePaymentRequest request) {
        String updateSQL = "update payment_requests set fulfilled_by = " + request.getUserId() + " where id = " + request.getPaymentId();

        PreparedStatement statement = null;
        int result = 0;
        try {
            statement = getConnection().prepareStatement(updateSQL);
            result = statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        if (result == 1) {
            return true;
        }
        return false;
    }

    public FulfillerMetaData getFulfillerMeta(UserIdPOJO user) {
        //Get id of this payment_requests
        String lastRequestQuery = "select fulfilled_by from payment_requests where requester_id = " + user.getUserId() + " order by requested_time desc limit 1";
        PreparedStatement statement1 = null;
        try {
            statement1 = getConnection().prepareStatement(lastRequestQuery);
        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        }
        ResultSet rs = null;
        int fulfillerId = -1;
        try {
            rs = statement1.executeQuery();
            while (rs.next()) {
                fulfillerId = rs.getInt("fulfilled_by");
            }
            rs.close();
        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement1.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        if (fulfillerId == -1) {
            return null;
        }

        String userQuery = "select * from Users where id = " + fulfillerId;

        FulfillerMetaData ffmd = null;

        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(userQuery);
        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        }
        ResultSet rs1 = null;
        try {
            rs1 = statement.executeQuery();
            while (rs1.next()) {

                ffmd = new FulfillerMetaData(rs1.getInt("id"),
                        rs1.getString("username"),
                        0,
                        rs1.getDouble("lat"),
                        rs1.getDouble("lon"));
            }
            rs1.close();
        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ffmd;


    }

    public PaymentSuccessMeta processTransactionSuccess(int transactionId) {
        String updateSQL = "update payment_requests set status = 'done' where id = " + transactionId;

        PreparedStatement statement = null;
        int result = 0;
        try {
            statement = getConnection().prepareStatement(updateSQL);
            result = statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        if (result != 1) {
            return null;
        }

        // get fulfiller id, amount and borrowner name

        String lastRequestQuery = "select *  from payment_requests where id = " + transactionId;
        PreparedStatement statement1 = null;

        ResultSet rs = null;
        int fulfillerId = -1;
        int amount = -1;
        try {
            statement1 = getConnection().prepareStatement(lastRequestQuery);
            rs = statement1.executeQuery();
            while (rs.next()) {
                fulfillerId = rs.getInt("fulfilled_by");
                amount = rs.getInt("amount");
            }
            rs.close();
        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // get gcmId of fulfiller

        String gcmQuery = "select *  from users where id = " + fulfillerId;
        PreparedStatement statement2 = null;
        ResultSet rs1 = null;
        String gcmId = null;
        try {
            statement2 = getConnection().prepareStatement(gcmQuery);
            rs1 = statement2.executeQuery();

            while (rs1.next()) {
                gcmId = rs.getString("gcmId");
            }
            rs1.close();
        } catch (SQLException e) {
            log.error("Exception while querying DB", e);
        } finally {
            try {

                statement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return new PaymentSuccessMeta(gcmId, amount);


        // send gcm confirmation to provider
    }
}
