package POJO;

import lombok.Data;

@Data
public class PaymentRequest {

    public PaymentRequest() {

    }

    private int userId;
    private int amount;
    private double lat;
    private double lon;
    private int distanceMeters = 2000;
}
