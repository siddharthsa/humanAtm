package POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class PaymentRequestResponseMeta {

    private int paymentRequestId;
    private ArrayList<String> resultantIds;
}
