package uk.gov.hmcts.reform.mi.miextractionservice.v2.service.export;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.gov.hmcts.reform.mi.miextractionservice.factory.ExtractionBlobServiceClientFactory;
import uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.ExportProperties;
import uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.SourceProperties;

import java.util.List;
import java.util.stream.Collectors;

import static uk.gov.hmcts.reform.mi.miextractionservice.v2.util.ContainerUtils.getContainerPrefix;

@AllArgsConstructor
@Service
public class ExportServiceImpl implements ExportService {

    private final ExtractionBlobServiceClientFactory extractionBlobServiceClientFactory;
    private final ExportProperties exportProperties;

    @Value("${container-whitelist}")
    private final List<String> containerWhitelist;

    @Override
    public void exportData() {
        BlobServiceClient stagingClient = extractionBlobServiceClientFactory.getStagingClient();
        BlobServiceClient exportClient = extractionBlobServiceClientFactory.getExportClient();

        exportProperties.getSources().forEach(
            (source, properties) -> exportDataForSource(stagingClient, exportClient, source, properties)
        );
    }

    private void exportDataForSource(BlobServiceClient stagingClient,
                                     BlobServiceClient exportClient,
                                     String source,
                                     SourceProperties properties) {

        List<BlobContainerItem> containersToParse = stagingClient.listBlobContainers().stream()
            .filter(container -> containerWhitelist.isEmpty() || containerWhitelist.contains(container.getName()))
            .filter(container -> container.getName().startsWith(getContainerPrefix(source)))
            .collect(Collectors.toList());

        for (BlobContainerItem containerItem : containersToParse) {

        }
    }
}
