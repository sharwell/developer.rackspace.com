import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jclouds.ContextBuilder;
import org.jclouds.rackspace.clouddns.v1.CloudDNSApi;
import org.jclouds.rackspace.clouddns.v1.domain.*;
import org.jclouds.rackspace.clouddns.v1.features.DomainApi;
import org.jclouds.rackspace.clouddns.v1.features.RecordApi;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import static org.jclouds.rackspace.clouddns.v1.predicates.JobPredicates.awaitComplete;

public class CloudDNS {
    public static final String PROVIDER = System.getProperty("provider", "rackspace-clouddns-us");
    public static final String DOMAIN = System.getProperty("domain", "domain.com");

    public static final String USERNAME = System.getProperty("username", "{username}");
    public static final String API_KEY = System.getProperty("apikey", "{apiKey}");

    public static void main(String[] args) throws Exception {
        CloudDNSApi cloudDNSApi = ContextBuilder.newBuilder(PROVIDER)
                .credentials(USERNAME, API_KEY)
                .buildApi(CloudDNSApi.class);

        Set<Domain> domains = createZone(cloudDNSApi);
        Domain domain = getZone(cloudDNSApi, domains);
        modifyZone(cloudDNSApi, domain);
        Set<RecordDetail> records = createRecord(cloudDNSApi, domain);
        RecordDetail record = getRecord(cloudDNSApi, domain, records);
        modifyRecord(cloudDNSApi, domain, record);
        deleteRecord(cloudDNSApi, domain, record);
        deleteZone(cloudDNSApi, domain);
    }

    private static Set<Domain> createZone(CloudDNSApi cloudDNSApi) throws TimeoutException {
        DomainApi domainApi = cloudDNSApi.getDomainApi();
        List<CreateDomain> createDomains = Lists.newArrayList();
        CreateDomain createDomain = CreateDomain.builder()
                .name(DOMAIN)
                .email("admin@domain.com")
                .ttl(300)
                .build();

        createDomains.add(createDomain);

        Set<Domain> domains = awaitComplete(cloudDNSApi, domainApi.create(createDomains));

        return domains;
    }

    private static Domain getZone(CloudDNSApi cloudDNSApi, Set<Domain> domains) {
        DomainApi domainApi = cloudDNSApi.getDomainApi();
        Domain domain = domainApi.get(domains.iterator().next().getId());

        return domain;
    }

    private static void modifyZone(CloudDNSApi cloudDNSApi, Domain domain) throws TimeoutException {
        DomainApi domainApi = cloudDNSApi.getDomainApi();
        UpdateDomain updateDomain = UpdateDomain.builder()
                .email("changed@domain.com")
                .ttl(3600)
                .build();

        awaitComplete(cloudDNSApi, domainApi.update(domain.getId(), updateDomain));
    }

    private static Set<RecordDetail> createRecord(CloudDNSApi cloudDNSApi, Domain domain) throws TimeoutException {
        RecordApi recordApi = cloudDNSApi.getRecordApiForDomain(domain.getId());
        Record createARecord = Record.builder()
                .type("A")
                .name("app." + DOMAIN)
                .data("192.168.1.1")
                .ttl(3600)
                .build();

        List<Record> createRecords = ImmutableList.of(createARecord);

        Set<RecordDetail> records = awaitComplete(cloudDNSApi, recordApi.create(createRecords));

        return records;
    }

    private static RecordDetail getRecord(CloudDNSApi cloudDNSApi, Domain domain, Set<RecordDetail> records) {
        RecordApi recordApi = cloudDNSApi.getRecordApiForDomain(domain.getId());
        RecordDetail record = recordApi.get(records.iterator().next().getId());

        return record;
    }

    private static void modifyRecord(CloudDNSApi cloudDNSApi, Domain domain, RecordDetail record) throws TimeoutException {
        RecordApi recordApi = cloudDNSApi.getRecordApiForDomain(domain.getId());
        Record updateRecord = Record.builder()
                .data("192.168.1.2")
                .build();

        awaitComplete(cloudDNSApi, recordApi.update(record.getId(), updateRecord));
    }

    private static void deleteRecord(CloudDNSApi cloudDNSApi, Domain domain, RecordDetail record) throws TimeoutException {
        List<String> recordIds = ImmutableList.of(record.getId());
        RecordApi recordApi = cloudDNSApi.getRecordApiForDomain(domain.getId());

        awaitComplete(cloudDNSApi, recordApi.delete(recordIds));
    }

    private static void deleteZone(CloudDNSApi cloudDNSApi, Domain domain) throws TimeoutException {
        List<Integer> domainIds = ImmutableList.of(domain.getId());
        DomainApi domainApi = cloudDNSApi.getDomainApi();

        awaitComplete(cloudDNSApi, domainApi.delete(domainIds, true));
    }
}
