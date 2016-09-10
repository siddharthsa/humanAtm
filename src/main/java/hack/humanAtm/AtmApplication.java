package hack.humanAtm;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AtmApplication extends Application<AtmConfiguration> {
    private GuiceBundle<AtmConfiguration> guiceBundle;
    @Override
    public void initialize(Bootstrap bootstrap) {
        
    }


    @Override
    public void run(AtmConfiguration atmConfiguration, Environment environment) throws Exception {
        environment.jersey().register(HumanAtmResource.class);
    }

    public static void main(String[] args) throws Exception {
        new AtmApplication().run(args);
    }
}
