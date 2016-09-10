package POJO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgreePaymentRequest {

    private int userId;
    private int paymentId;
}
