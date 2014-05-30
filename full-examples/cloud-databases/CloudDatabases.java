import org.jclouds.ContextBuilder;
import org.jclouds.openstack.trove.v1.TroveApi;
import org.jclouds.openstack.trove.v1.domain.Flavor;
import org.jclouds.openstack.trove.v1.domain.Instance;
import org.jclouds.openstack.trove.v1.features.DatabaseApi;
import org.jclouds.openstack.trove.v1.features.FlavorApi;
import org.jclouds.openstack.trove.v1.features.InstanceApi;
import org.jclouds.openstack.trove.v1.features.UserApi;
import org.jclouds.openstack.trove.v1.utils.TroveUtils;

import com.google.common.collect.Iterables;

public class CloudDatabases {
    public static final String PROVIDER = System.getProperty("provider", "rackspace-clouddatabases-us");
    public static final String REGION = System.getProperty("region", "IAD");

    public static final String INSTANCE_NAME = System.getProperty("instanceName", "sample_instance");
    public static final String DATABASE_NAME = System.getProperty("databaseName", "sample_database");
    public static final String DATABASE_USER_NAME = System.getProperty("databaseUserName", "sampleDbUser");

    public static final String USERNAME = System.getProperty("username", "{username}");
    public static final String API_KEY = System.getProperty("apikey", "{apiKey}");

    private static TroveApi troveApi;

    public static void main(String[] args) throws Exception {

        troveApi = ContextBuilder.newBuilder("rackspace-clouddatabases-us") 
            .credentials(USERNAME, API_KEY)
            .buildApi(TroveApi.class);

        Instance instance = createInstance();
        createDatabase(instance);
        createUser(instance);

        String rootPassword = enableRootUser(instance);
    }

    public static Instance createInstance() {
        TroveUtils utils = new TroveUtils(troveApi);

        FlavorApi flavorApi = troveApi.getFlavorApiForZone(REGION);
        Flavor flavor = Iterables.getFirst(flavorApi.list(), null);

        Instance instance = utils.getWorkingInstance(REGION, INSTANCE_NAME, "" + flavor.getId(), 1);

        return instance;
    }

    public static String enableRootUser(Instance instance) {
        InstanceApi instanceApi = troveApi.getInstanceApiForZone(REGION);
        String password = instanceApi.enableRoot(instance.getId());

        return password;
    }

    public static void createDatabase(Instance instance) {
        DatabaseApi databaseApi = troveApi.getDatabaseApiForZoneAndInstance(REGION, instance.getId());
        databaseApi.create(DATABASE_NAME);
    }

    public static void createUser(Instance instance) {
        UserApi userApi = troveApi.getUserApiForZoneAndInstance(REGION, instance.getId());
        userApi.create(DATABASE_USER_NAME, "password123", DATABASE_NAME);
    }
}
