package POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentSuccessMeta {
    private String gcmId;
    private int amount;
}
