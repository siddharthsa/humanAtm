package POJO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgreePaymentRequest {

    public AgreePaymentRequest(){}
    private int userId;
    private int paymentId;
}
