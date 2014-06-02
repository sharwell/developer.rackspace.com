import static org.jclouds.compute.predicates.NodePredicates.inGroup;
import static org.jclouds.rackspace.cloudloadbalancers.v1.domain.VirtualIP.Type.PUBLIC;
import static org.jclouds.rackspace.cloudloadbalancers.v1.domain.internal.BaseLoadBalancer.Algorithm.RANDOM;
import static org.jclouds.rackspace.cloudloadbalancers.v1.domain.internal.BaseNode.Condition.DISABLED;
import static org.jclouds.rackspace.cloudloadbalancers.v1.domain.internal.BaseNode.Condition.ENABLED;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.openstack.nova.v2_0.domain.zonescoped.ZoneAndId;
import org.jclouds.rackspace.cloudloadbalancers.v1.CloudLoadBalancersApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.domain.AccessRule;
import org.jclouds.rackspace.cloudloadbalancers.v1.domain.AddNode;
import org.jclouds.rackspace.cloudloadbalancers.v1.domain.ConnectionThrottle;
import org.jclouds.rackspace.cloudloadbalancers.v1.domain.CreateLoadBalancer;
import org.jclouds.rackspace.cloudloadbalancers.v1.domain.HealthMonitor;
import org.jclouds.rackspace.cloudloadbalancers.v1.domain.LoadBalancer;
import org.jclouds.rackspace.cloudloadbalancers.v1.domain.Node;
import org.jclouds.rackspace.cloudloadbalancers.v1.features.AccessRuleApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.features.ConnectionApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.features.ContentCachingApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.features.ErrorPageApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.features.HealthMonitorApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.features.LoadBalancerApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.features.NodeApi;
import org.jclouds.rackspace.cloudloadbalancers.v1.predicates.LoadBalancerPredicates;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;

public class CloudLoadBalancers {
   public static final String PROVIDER = System.getProperty("provider", "rackspace-cloudloadbalancers-us");
   public static final String ZONE = System.getProperty("region", "IAD");

   public static final String LOAD_BALANCER_NAME = System.getProperty("loadBalancerName", "sample_load_balancer");
   public static final String SERVER_NAME = System.getProperty("serverName", "sample_server");
   public static final String GROUP_NAME = System.getProperty("groupName", "group");

   public static final String USERNAME = System.getProperty("username", "{username}");
   public static final String API_KEY = System.getProperty("apikey", "{apiKey}");

   private static ComputeService computeService;
   private static CloudLoadBalancersApi clbApi;
   private static LoadBalancerApi lbApi;
   private static HealthMonitorApi hmApi;

   public static void main(String[] args) throws Exception {
       computeService = ContextBuilder.newBuilder("rackspace-cloudservers-us")
            .credentials(USERNAME, API_KEY)
            .buildView(ComputeServiceContext.class).getComputeService();
      
       clbApi = ContextBuilder.newBuilder(PROVIDER)
           .credentials(USERNAME, API_KEY)
           .buildApi(CloudLoadBalancersApi.class);
       lbApi = clbApi.getLoadBalancerApiForZone(ZONE);

       // create the server and load balancer nodes
       Set<? extends NodeMetadata> serverNode = createServer();
       Set<AddNode> loadBalancerNodes = createNodes(serverNode);

       // create a load balancer
       LoadBalancer loadBalancer = createLoadBalancer(loadBalancerNodes);

       // create a health monitor
       createHealthMonitor(loadBalancer);
       HealthMonitor healthMonitor = getHealthMonitor(loadBalancer);

       // set load balancer features
       setThrottling(loadBalancer);
       blacklistIPs(loadBalancer);
       enableContentCaching(loadBalancer);
       setCustomErrorPage(loadBalancer);

       // deleted the nodes, load balancer and server
       deleteNodes(loadBalancer);
       deleteLoadBalancer(loadBalancer);
       deleteServer();

       Closeables.close(computeService.getContext(), true);
       Closeables.close(clbApi, true);
   }

   public static Set<? extends NodeMetadata> createServer() throws RunNodesException {
       ZoneAndId zoneAndId = ZoneAndId.fromZoneAndId(ZONE, "performance1-1");
       Template template = computeService.templateBuilder()
           .locationId(ZONE)
           .osDescriptionMatches(".*Ubuntu 12.04.*")
           .hardwareId(zoneAndId.slashEncode())
           .build();

       // This method will continue to poll for the server status and won't return until this server is ACTIVE
       Set<? extends NodeMetadata> serverNode = computeService.createNodesInGroup(GROUP_NAME, 1, template);

       return serverNode;
   }


   public static LoadBalancer createLoadBalancer(Set<AddNode> addNodes) throws TimeoutException {
       CreateLoadBalancer createLB = CreateLoadBalancer.builder()
           .name(LOAD_BALANCER_NAME)
           .protocol("HTTP")
           .port(80)
           .algorithm(RANDOM)
           .nodes(addNodes)
           .virtualIPType(PUBLIC)
           .build();

       LoadBalancer loadBalancer = lbApi.create(createLB);

       // Wait for the Load Balancer to become Active before moving on
       if (!LoadBalancerPredicates.awaitAvailable(lbApi).apply(loadBalancer)) {
           throw new TimeoutException("Timeout on creating load balancer: " + loadBalancer);
       }

       return loadBalancer;
   }

   public static Set<AddNode> createNodes(Set<? extends NodeMetadata> serverNodes) {
       Set<AddNode> addLBNodes = Sets.newHashSet();

       AddNode node1 = AddNode.builder()
           .address("10.180.1.1")
           .condition(DISABLED)
           .port(80)
           .weight(20)
           .build();

       AddNode node2 = AddNode.builder()
           .address("10.180.1.2")
           .condition(ENABLED)
           .port(80)
           .weight(20)
           .build();

       addLBNodes.add(node1);
       addLBNodes.add(node2);
       for (NodeMetadata node : serverNodes) {
           String privateAddress = node.getPrivateAddresses().iterator().next();

           AddNode addNode = AddNode.builder()
               .address(privateAddress)
               .condition(ENABLED)
               .port(80)
               .weight(20)
               .build();

           addLBNodes.add(addNode);
       }

       return addLBNodes;
   }

   public static void createHealthMonitor(LoadBalancer loadBalancer) {
       HealthMonitor healthMonitor = HealthMonitor.builder()
           .type(HealthMonitor.Type.CONNECT)
           .delay(3599)
           .timeout(30)
           .attemptsBeforeDeactivation(2)
           .build();

       clbApi.getHealthMonitorApiForZoneAndLoadBalancer(ZONE, loadBalancer.getId())
             .createOrUpdate(healthMonitor);
   }

   public static HealthMonitor getHealthMonitor(LoadBalancer loadBalancer) {
       hmApi = clbApi.getHealthMonitorApiForZoneAndLoadBalancer(ZONE, loadBalancer.getId());
       HealthMonitor monitor = hmApi.get();

       return monitor;
   }

   public static void setThrottling(LoadBalancer loadBalancer) {
       ConnectionThrottle throttle = ConnectionThrottle.builder()
           .maxConnectionRate(10000)
           .maxConnections(5000)
           .minConnections(2)
           .rateInterval(5)
           .build();

       ConnectionApi connectionApi = clbApi.getConnectionApiForZoneAndLoadBalancer(ZONE, loadBalancer.getId());
       connectionApi.createOrUpdateConnectionThrottle(throttle);
   }

   public static void blacklistIPs(LoadBalancer loadBalancer) {
       AccessRule rule1 = AccessRule.deny("206.160.165.0/24");
       AccessRule rule2 = AccessRule.allow("206.160.165.0/2");
       AccessRule blacklisted = AccessRule.deny("0.0.0.0/0");

       List<AccessRule> accessList = ImmutableList.<AccessRule> of(rule1, rule2, blacklisted);

       AccessRuleApi accessRuleApi = clbApi.getAccessRuleApiForZoneAndLoadBalancer(ZONE, loadBalancer.getId());
       accessRuleApi.create(accessList);
   }

   public static void enableContentCaching(LoadBalancer loadBalancer) {
       ContentCachingApi contentCachingApi = clbApi.getContentCachingApiForZoneAndLoadBalancer(ZONE, loadBalancer.getId());
       contentCachingApi.enable();
   }

   public static void setCustomErrorPage(LoadBalancer loadBalancer) {
       ErrorPageApi errorPageApi = clbApi.getErrorPageApiForZoneAndLoadBalancer(ZONE, loadBalancer.getId());

       String content = "<html><body>Something went wrong...</body></html>";
       errorPageApi.create(content);
   }

   public static void deleteNodes(LoadBalancer loadBalancer) throws TimeoutException {
       NodeApi nodeApi = clbApi.getNodeApiForZoneAndLoadBalancer(ZONE, loadBalancer.getId());
       Set<Node> nodes = nodeApi.list().concat().toSet();

       Iterable<Integer> nodeIds = Iterables.transform(nodes, new NodeToId());
       nodeApi.remove(nodeIds);
   }

   public static void deleteLoadBalancer(LoadBalancer loadBalancer) throws TimeoutException {
       lbApi.delete(loadBalancer.getId());
       if (!LoadBalancerPredicates.awaitDeleted(lbApi).apply(loadBalancer)) {
           throw new TimeoutException("Timeout on deleting load balancer: " + loadBalancer);
       }
   }

   public static void deleteServer() {
       computeService.destroyNodesMatching(inGroup(GROUP_NAME));
   }

   private static class NodeToId implements Function<Node, Integer> {
       public Integer apply(Node node) {
           return node.getId();
       }
   }
}
