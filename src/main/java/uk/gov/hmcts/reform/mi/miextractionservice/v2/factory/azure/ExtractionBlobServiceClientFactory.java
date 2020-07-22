package uk.gov.hmcts.reform.mi.miextractionservice.v2.factory.azure;

import com.azure.storage.blob.BlobServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import uk.gov.hmcts.reform.mi.micore.factory.BlobServiceClientFactory;

@Slf4j
@AllArgsConstructor
@Component
public class ExtractionBlobServiceClientFactory {

    @Value("${azure.managed-identity.client-id}")
    private final String clientId;
    @Value("${azure.storage-account.staging.name}")
    private final String stagingName;
    @Value("${azure.storage-account.export.name}")
    private final String exportName;
    @Value("${azure.storage-account.staging.connection-string}")
    private final String stagingConnString;
    @Value("${azure.storage-account.export.connection-string}")
    private final String exportConnString;

    private final BlobServiceClientFactory blobServiceClientFactory;

    public BlobServiceClient getStagingClient() {
        if (StringUtils.isEmpty(clientId)) {
            log.info("Getting staging blob service client by connection string.");
            return blobServiceClientFactory.getBlobClientWithConnectionString(stagingConnString);
        } else {
            log.info("Getting staging blob service client by managed identity.");
            return blobServiceClientFactory.getBlobClientWithManagedIdentity(clientId, stagingName);
        }
    }

    public BlobServiceClient getExportClient() {
        if (StringUtils.isEmpty(clientId)) {
            log.info("Getting export blob service client by connection string.");
            return blobServiceClientFactory.getBlobClientWithConnectionString(exportConnString);
        } else {
            log.info("Getting export blob service client by managed identity.");
            return blobServiceClientFactory.getBlobClientWithManagedIdentity(clientId, exportName);
        }
    }
}
