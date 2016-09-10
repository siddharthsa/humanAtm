package POJO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Created by siddharth on 9/10/16.
 */
@Data
public class User {

    public User() {

    }

    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private String gcmId;
    @JsonIgnore
    private String upi;
}
