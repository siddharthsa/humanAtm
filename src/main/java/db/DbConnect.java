package db;

import entities.User;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by siddharth on 9/10/16.
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
