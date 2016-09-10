package db;

import entities.User;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by siddharth on 9/10/16.
 */
/*
Schema:
CREATE TABLE Users(id int NOT NULL AUTO_INCREMENT,username varchar(255),phonenumber bigint NOT NULL,email varchar(255),password varchar(255),gcmId varchar(255),status varchar(255),created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,PRIMARY KEY (id));

CREATE TABLE payment_requests(id int NOT NULL AUTO_INCREMENT,requester_id int NOT NULL,amount int NOT NULL,requested_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,status varchar(255),fulfilled_by int NOT NULL, PRIMARY KEY (id));

CREATE TABLE last_location(user_id int NOT NULL PRIMARY KEY, lat DOUBLE, lon DOUBLE);

 */
public class DbConnect {

    private static final BasicDataSource dataSource = new BasicDataSource();
    private static String host;

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


    public boolean writeNewUserToDB(User user) throws SQLException {
        String insertSQL = "insert into Users (username,phonenumber,email,password,gcmId,status) values ('" + user.getUsername() + "'," + user.getPhoneNumber() + ",'" +
                user.getEmail()
                + "','" + user.getPassword() + "','" + user.getGcmId() + "','pending')";

        PreparedStatement statement = getConnection().prepareStatement(insertSQL);

        int result = statement.executeUpdate();

        if (result == 1) {
            return true;
        }
        return false;
    }
}
