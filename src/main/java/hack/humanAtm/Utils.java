package hack.humanAtm;

import db.DbConnect;
import entities.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Created by siddharth on 9/10/16.
 */
@Slf4j
public class Utils {
    public static boolean registerUser(DbConnect db, User user) {
        try {
            db.writeNewUserToDB(user);
            return true;
        } catch (SQLException e) {
            log.error("Got exception - ", e);
            return false;
        }
    }
}
