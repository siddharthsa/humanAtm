package POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FulfillerMetaData {

    public FulfillerMetaData(){}
    private int userId;
    private String name;
    private double distance;
    private double lat;
    private double lon;
}
