package hack.humanAtm;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class AtmConfiguration extends Configuration {

    @NotEmpty
    private String host;


    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public String setHost(String host) {
        return this.host = host;
    }

}
