import java.util.List;
import java.util.concurrent.TimeoutException;

import org.jclouds.ContextBuilder;
import org.jclouds.openstack.cinder.v1.CinderApi;
import org.jclouds.openstack.cinder.v1.domain.Snapshot;
import org.jclouds.openstack.cinder.v1.domain.Volume;
import org.jclouds.openstack.cinder.v1.features.SnapshotApi;
import org.jclouds.openstack.cinder.v1.features.VolumeApi;
import org.jclouds.openstack.cinder.v1.options.CreateSnapshotOptions;
import org.jclouds.openstack.cinder.v1.options.CreateVolumeOptions;
import org.jclouds.openstack.cinder.v1.predicates.SnapshotPredicates;
import org.jclouds.openstack.cinder.v1.predicates.VolumePredicates;


public class CloudBlockStorage {
    public static final String PROVIDER = System.getProperty("provider", "rackspace-cloudblockstorage-us");
    public static final String REGION = System.getProperty("region", "IAD");

    public static final String VOLUME_NAME = System.getProperty("volumeName", "sample_volume");
    public static final String SNAPSHOT_NAME = System.getProperty("snapshotName", "sample_snapshot");

    public static final String USERNAME = System.getProperty("username", "{username}");
    public static final String API_KEY = System.getProperty("apikey", "{apiKey}");

    private static CinderApi cinderApi;
    private static VolumeApi volumeApi;
    private static SnapshotApi snapshotApi;

    public static void main(String[] args) throws Exception {

        cinderApi = ContextBuilder.newBuilder(PROVIDER)
            .credentials(USERNAME, API_KEY)
            .buildApi(CinderApi.class);

        // jclouds refers to "regions" as "zones"
        volumeApi = cinderApi.getVolumeApiForZone(REGION);
        snapshotApi = cinderApi.getSnapshotApiForZone(REGION);

        Volume volume = createVolume();
        Volume showVolume = showVolume(volume.getId());

        List<? extends Volume> volumes = listVolumes();

        Snapshot snapshot = createSnapshot(volume);
        Snapshot showSnapshot = showSnapshot(snapshot.getId());

        List<? extends Snapshot> snapshots = listSnapshots();

        deleteSnapshot(snapshot);
        deleteVolume(volume);
    }

    public static Volume createVolume() throws TimeoutException {
        CreateVolumeOptions options = CreateVolumeOptions.Builder
            .name(VOLUME_NAME)
            .description("Volume Description")
            .volumeType("SATA");

        Volume volume = volumeApi.create(100, options);
        
        if (!VolumePredicates.awaitAvailable(volumeApi).apply(volume)) {
           throw new TimeoutException("Timeout on creating volume: " + volume);
        }
        
        return volume;
    }

    public static Volume showVolume(String volumeId) {
        Volume volume = volumeApi.get(volumeId);

        return volume;
    }

    public static List<? extends Volume> listVolumes() {
       return volumeApi.listInDetail().toList();
    }

    public static Snapshot createSnapshot(Volume volume) throws TimeoutException {
        CreateSnapshotOptions options = CreateSnapshotOptions.Builder
            .name(SNAPSHOT_NAME)
            .description("Snapshot Description");

        Snapshot snapshot = snapshotApi.create(volume.getId(), options);

        if (!SnapshotPredicates.awaitAvailable(snapshotApi).apply(snapshot)) {
           throw new TimeoutException("Timeout on creating snapshot: " + snapshot);
        }

        return snapshot;
    }

    public static Snapshot showSnapshot(String snapshotId) {
       Snapshot snapshot = snapshotApi.get(snapshotId);

       return snapshot;
    }

    public static List<? extends Snapshot> listSnapshots() {
       return snapshotApi.listInDetail().toList();
    }

    public static void deleteSnapshot(Snapshot snapshot) throws TimeoutException {
        snapshotApi.delete(snapshot.getId());

        if (!SnapshotPredicates.awaitDeleted(snapshotApi).apply(snapshot)) {
           throw new TimeoutException("Timeout on deleting snapshot: " + snapshot);
        }
    }

    public static void deleteVolume(Volume volume) throws TimeoutException {
       volumeApi.delete(volume.getId());

       if (!VolumePredicates.awaitDeleted(volumeApi).apply(volume)) {
          throw new TimeoutException("Timeout on deleting volume: " + volume);
       }
    }
}
