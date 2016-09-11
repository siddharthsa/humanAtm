package POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionPOJO {
    public TransactionPOJO(){}
    private int fulfillerId;
    private int amount;

}
