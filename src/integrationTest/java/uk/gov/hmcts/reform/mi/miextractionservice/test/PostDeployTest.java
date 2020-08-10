package uk.gov.hmcts.reform.mi.miextractionservice.test;

import com.azure.storage.blob.BlobServiceClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import uk.gov.hmcts.reform.mi.micore.factory.BlobServiceClientFactory;
import uk.gov.hmcts.reform.mi.miextractionservice.TestConfig;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.hmcts.reform.mi.miextractionservice.data.TestConstants.DASH_DELIMITER;
import static uk.gov.hmcts.reform.mi.miextractionservice.data.TestConstants.EXPORT_CONTAINER_NAME;
import static uk.gov.hmcts.reform.mi.miextractionservice.data.TestConstants.TEST_EXPORT_BLOB;
import static uk.gov.hmcts.reform.mi.miextractionservice.util.TestUtils.cleanUpTestFiles;

@SpringBootTest(classes = TestConfig.class)
public class PostDeployTest {

    @Value("${azure.storage-account.export.connection-string}")
    private String exportConnectionString;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private BlobServiceClientFactory blobServiceClientFactory;

    private BlobServiceClient exportBlobServiceClient;

    private String exportContainer;

    @BeforeEach
    public void setUp() {
        // Set up blob clients.
        exportBlobServiceClient = blobServiceClientFactory
            .getBlobClientWithConnectionString(exportConnectionString);

        exportContainer = buildVersion + DASH_DELIMITER + EXPORT_CONTAINER_NAME;
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        cleanUpTestFiles(exportBlobServiceClient, exportContainer);
    }

    @Test
    public void givenTestBlob_whenExportBlobData_thenTestBlobsExistInExport() {
        // Verify blob is copied over to the staging blob storage account.
        assertTrue(exportBlobServiceClient
            .getBlobContainerClient(exportContainer)
            .getBlobClient(TEST_EXPORT_BLOB)
            .exists(), "Blob was not successfully exported over to export storage.");
    }
}
