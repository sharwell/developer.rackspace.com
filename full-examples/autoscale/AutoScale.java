import java.io.IOException;

import org.jclouds.ContextBuilder;
import org.jclouds.rackspace.autoscale.v1.AutoscaleApi;
import org.jclouds.rackspace.autoscale.v1.domain.CreateScalingPolicy;
import org.jclouds.rackspace.autoscale.v1.domain.CreateScalingPolicy.ScalingPolicyTargetType;
import org.jclouds.rackspace.autoscale.v1.domain.CreateScalingPolicy.ScalingPolicyType;
import org.jclouds.rackspace.autoscale.v1.domain.Group;
import org.jclouds.rackspace.autoscale.v1.domain.GroupConfiguration;
import org.jclouds.rackspace.autoscale.v1.domain.LaunchConfiguration;
import org.jclouds.rackspace.autoscale.v1.domain.LaunchConfiguration.LaunchConfigurationType;
import org.jclouds.rackspace.autoscale.v1.domain.LoadBalancer;
import org.jclouds.rackspace.autoscale.v1.domain.Personality;
import org.jclouds.rackspace.autoscale.v1.domain.ScalingPolicy;
import org.jclouds.rackspace.autoscale.v1.domain.Webhook;
import org.jclouds.rackspace.autoscale.v1.features.GroupApi;
import org.jclouds.rackspace.autoscale.v1.features.PolicyApi;
import org.jclouds.rackspace.autoscale.v1.features.WebhookApi;
import org.jclouds.rackspace.autoscale.v1.utils.AutoscaleUtils;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class AutoScale {
   private static final String PROVIDER = System.getProperty("provider.autoscale", "rackspace-autoscale-us");
   private static final String ZONE = System.getProperty("zone", "DFW");

   private static final String NAME = "jclouds-example";

   //private static final String USERNAME = System.getProperty("username", "{username}");
   //private static final String API_KEY = System.getProperty("apikey", "{apiKey}");

   private static final String USERNAME = System.getProperty("username", "zackracktest");
   private static final String API_KEY = System.getProperty("apikey", "7c8b97d7dff1cf3d979bc48f5e5f460c");


   public static void main(String[] args) throws Exception {
      AutoscaleApi autoscaleApi = ContextBuilder.newBuilder(PROVIDER)
            .credentials(USERNAME, API_KEY)
            .buildApi(AutoscaleApi.class);

      GroupApi groupApi = autoscaleApi.getGroupApiForZone(ZONE);
      String groupId = createGroup(groupApi);
      PolicyApi policyApi = autoscaleApi.getPolicyApiForZoneAndGroup(ZONE, groupId);
      String policyId = getPolicyId(policyApi);
      WebhookApi webhookApi = autoscaleApi.getWebhookApiForZoneAndGroupAndPolicy(ZONE, groupId, policyId);
      String webhookId = createWebhook(webhookApi);
      executeWebhook(webhookApi, webhookId);
   }

   /**
    * @param groupApi
    * @return the group id
    */
   private static String createGroup(GroupApi groupApi) {
      GroupConfiguration groupConfiguration = GroupConfiguration.builder()
            .maxEntities(5)
            .cooldown(2)
            .name(NAME)
            .minEntities(0)
            .metadata(ImmutableMap.of("notes", "This is an autoscale group for examples"))
            .build();

      LaunchConfiguration launchConfiguration = LaunchConfiguration.builder()
            .loadBalancers(ImmutableList.of(LoadBalancer.builder().port(8080).id(9099).build()))
            .serverName(NAME)
            .serverImageRef("0d589460-f177-4b0f-81c1-8ab8903ac7d8")
            .serverFlavorRef("2")
            .serverDiskConfig("AUTO")
            .serverMetadata(ImmutableMap.of("notes","Server examples notes"))
            .networks(ImmutableList.<String>of("internal", "public"))
            .personalities(ImmutableList.of(
                  Personality.builder().path("filepath").contents("VGhpcyBpcyBhIHRlc3QgZmlsZS4=").build()))
                  .type(LaunchConfigurationType.LAUNCH_SERVER)
                  .build();

      CreateScalingPolicy scalingPolicy = CreateScalingPolicy.builder()
            .cooldown(0)
            .type(ScalingPolicyType.WEBHOOK)
            .name(NAME)
            .targetType(ScalingPolicyTargetType.PERCENT_CHANGE)
            .target("1")
            .build();

      Group g = groupApi.create(groupConfiguration, launchConfiguration, ImmutableList.of(scalingPolicy));
      return g.getId();
   }

   /**
    * @param policyApi
    * @return The policy id.
    */
   private static String getPolicyId(PolicyApi policyApi) {
      FluentIterable<ScalingPolicy> policies = policyApi.list();
      return policies.first().get().getId();
   }

   /**
    * @param webhookApi
    * @return the webhook id.
    */
   private static String createWebhook(WebhookApi webhookApi) {
      FluentIterable<Webhook> result = webhookApi.create(NAME, ImmutableMap.<String, Object>of());
      return result.first().get().getId();
   }

   /**
    * @param webhookApi
    * @return the webhook id.
    */
   private static void executeWebhook(WebhookApi webhookApi, String webhookId) throws IOException {
      AutoscaleUtils.execute(webhookApi.get(webhookId));
   }
}
