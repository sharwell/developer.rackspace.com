package org.jclouds.examples.rackspace;

import java.net.URI;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobRequestSigner;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.http.HttpRequest;
import org.jclouds.io.Payload;
import org.jclouds.io.Payloads;
import org.jclouds.openstack.swift.v1.blobstore.RegionScopedBlobStoreContext;
import org.jclouds.openstack.swift.v1.domain.SwiftObject;
import org.jclouds.openstack.swift.v1.features.AccountApi;
import org.jclouds.openstack.swift.v1.features.ObjectApi;
import org.jclouds.rackspace.cloudfiles.v1.CloudFilesApi;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSource;

public class CloudFiles {
    public static final String PROVIDER = System.getProperty("provider", "rackspace-cloudfiles-us");
    public static final String REGION = System.getProperty("region", "IAD");

    public static final String CONTAINER_NAME = System.getProperty("containerName", "sample_container");
    public static final String OBJECT_NAME = System.getProperty("objectName", "sample_object");
    public static final String TEMP_URL_KEY = System.getProperty("tempUrlKey", "jnRB6#1sduo8YGUF&%7r7guf6f");

    public static final String USERNAME = System.getProperty("username", "{username}");
    public static final String API_KEY = System.getProperty("apikey", "{apiKey}");

    public static BlobStore blobStore;
    public static BlobRequestSigner signer;
    public static CloudFilesApi cloudFilesApi;

    public static void main(String[] args) throws Exception {

        ContextBuilder builder = ContextBuilder.newBuilder(PROVIDER)
            .credentials(USERNAME, API_KEY);

        blobStore = builder.buildView(RegionScopedBlobStoreContext.class).blobStoreInRegion(REGION);
        signer = builder.buildView(RegionScopedBlobStoreContext.class).signerInRegion(REGION);
        cloudFilesApi = blobStore.getContext().unwrapApi(CloudFilesApi.class);

        createContainer();
        uploadObject();

        SwiftObject object = getObject();
        updateObjectMetadata();

        URI tempURL = getObjectTempUrl();

        URI uri = enableCDN();
        disableCDN();

        deleteObject();
        deleteContainer();
    }

    public static void createContainer() {
        cloudFilesApi.getContainerApiForRegion(REGION).create(CONTAINER_NAME);
    }

    public static void deleteContainer() {
        cloudFilesApi.getContainerApiForRegion(REGION).deleteIfEmpty(CONTAINER_NAME);
    }

    public static void uploadObject() {
        Payload payload = Payloads.newByteSourcePayload(ByteSource.wrap("Hello Cloud Files!".getBytes()));

        ObjectApi objectApi = cloudFilesApi.getObjectApiForRegionAndContainer(REGION, CONTAINER_NAME);
        objectApi.put(OBJECT_NAME, payload);
    }

    public static SwiftObject getObject() {
        ObjectApi objectApi = cloudFilesApi.getObjectApiForRegionAndContainer(REGION, CONTAINER_NAME);
        SwiftObject object = objectApi.get(OBJECT_NAME);

        return object;
    }

    public static void updateObjectMetadata() {
        ObjectApi objectApi = cloudFilesApi.getObjectApiForRegionAndContainer(REGION, CONTAINER_NAME);
        objectApi.updateMetadata(OBJECT_NAME, ImmutableMap.of("some-key", "another-value"));
    }

    public static URI getObjectTempUrl() {
        AccountApi accountApi = cloudFilesApi.getAccountApiForRegion(REGION);
        accountApi.updateTemporaryUrlKey(TEMP_URL_KEY);

        HttpRequest request = signer.signGetBlob(CONTAINER_NAME, OBJECT_NAME);
        URI tempUrl = request.getEndpoint();

        return tempUrl;
    }

    public static void deleteObject() {
        ObjectApi objectApi = cloudFilesApi.getObjectApiForRegionAndContainer(REGION, CONTAINER_NAME);
        objectApi.delete(OBJECT_NAME);
    }

    public static URI enableCDN() {
        URI cdnUri = cloudFilesApi.getCDNApiForRegion(REGION).enable(CONTAINER_NAME);

        return cdnUri;
    }

    public static void disableCDN() {
        cloudFilesApi.getCDNApiForRegion(REGION).disable(CONTAINER_NAME);
    }
}

