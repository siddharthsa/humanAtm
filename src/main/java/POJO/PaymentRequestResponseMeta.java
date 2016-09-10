package POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequestResponseMeta {

    private int paymentRequestId;
    private String resultantIds[];
}
