package hack.humanAtm;

import db.DbConnect;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AtmApplication extends Application<AtmConfiguration> {

    private static DbConnect db;

    @Override
    public void initialize(Bootstrap bootstrap) {
    }


    @Override
    public void run(AtmConfiguration atmConfiguration, Environment environment) throws Exception {
        db = new DbConnect(atmConfiguration.getHost());
        HumanAtmAPI api = new HumanAtmAPI(db);
        environment.jersey().register(api);
    }

    public static void main(String[] args) throws Exception {
        new AtmApplication().run(args);
    }
}
