package uk.gov.hmcts.reform.mi.miextractionservice.service.export;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uk.gov.hmcts.reform.mi.miextractionservice.component.archive.ArchiveComponent;
import uk.gov.hmcts.reform.mi.miextractionservice.component.notification.NotifyTargetsComponent;
import uk.gov.hmcts.reform.mi.miextractionservice.component.writer.DataWriterComponent;
import uk.gov.hmcts.reform.mi.miextractionservice.domain.ExportProperties;
import uk.gov.hmcts.reform.mi.miextractionservice.domain.SourceProperties;
import uk.gov.hmcts.reform.mi.miextractionservice.exception.ExportException;
import uk.gov.hmcts.reform.mi.miextractionservice.factory.azure.ExtractionBlobServiceClientFactory;
import uk.gov.hmcts.reform.mi.miextractionservice.util.ContainerUtils;
import uk.gov.hmcts.reform.mi.miextractionservice.util.DateUtils;
import uk.gov.hmcts.reform.mi.miextractionservice.util.FileUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;
import static uk.gov.hmcts.reform.mi.miextractionservice.domain.Constants.JSONL_EXTENSION;
import static uk.gov.hmcts.reform.mi.miextractionservice.domain.Constants.NEWLINE_DELIMITER;
import static uk.gov.hmcts.reform.mi.miextractionservice.domain.Constants.ZIP_EXTENSION;

@Slf4j
@AllArgsConstructor
@Service
public class ExportServiceImpl implements ExportService {

    private final @Value("${archive.compression.enabled}") String archiveEnabled;
    private final @Value("${container-whitelist}") List<String> containerWhitelist;
    private final @Value("${retrieve-from-date}") String retrieveFromDate;
    private final @Value("${retrieve-to-date}") String retrieveToDate;
    private final ExtractionBlobServiceClientFactory extractionBlobServiceClientFactory;
    private final ExportProperties exportProperties;
    private final DataWriterComponent dataWriterComponent;
    private final ArchiveComponent archiveComponent;
    private final NotifyTargetsComponent notifyTargetsComponent;

    @Override
    public void exportData() {
        BlobServiceClient stagingClient = extractionBlobServiceClientFactory.getStagingClient();
        BlobServiceClient exportClient = extractionBlobServiceClientFactory.getExportClient();

        List<String> messages = new ArrayList<>();

        exportProperties.getSources().forEach(
            (source, properties) -> exportDataForSource(stagingClient, exportClient, source, properties, messages)
        );

        notifyTargetsComponent.sendMessage(String.join(NEWLINE_DELIMITER, messages));
    }

    @SuppressWarnings("squid:S899")
    @Override
    public void checkStorageConnection() {
        extractionBlobServiceClientFactory
            .getStagingClient()
            .listBlobContainers()
            .iterator()
            .hasNext();

        extractionBlobServiceClientFactory
            .getExportClient()
            .listBlobContainers()
            .iterator()
            .hasNext();
    }

    private void exportDataForSource(BlobServiceClient stagingClient,
                                     BlobServiceClient exportClient,
                                     String source,
                                     SourceProperties properties,
                                     List<String> messages) {

        final LocalDate fromDate = DateUtils.getRetrievalDate(retrieveFromDate);
        final LocalDate toDate = DateUtils.getRetrievalDate(retrieveToDate);
        final List<String> datesToParse = DateUtils.getListOfYearsAndMonthsBetweenDates(fromDate, toDate);

        final List<BlobContainerItem> containersToParse = stagingClient.listBlobContainers().stream()
            .filter(container -> containerWhitelist.isEmpty() || containerWhitelist.contains(container.getName()))
            .filter(container -> container.getName().startsWith(ContainerUtils.getContainerPrefix(source)))
            .collect(Collectors.toList());

        int totalRecords = 0;

        String fileName = FileUtils.getExportName(source, fromDate, toDate, JSONL_EXTENSION);

        try (BufferedWriter writer = FileUtils.openBufferedWriter(fileName)) {
            for (BlobContainerItem containerItem : containersToParse) {
                totalRecords += parseContainerForData(stagingClient, writer, containerItem.getName(),
                                                      properties, fromDate, toDate, datesToParse);
            }
        } catch (IOException e) {
            throw new ExportException("Exception occurred when writing data for source: " + source, e);
        }

        uploadFileToBlobStore(exportClient, source, fromDate, toDate);

        log.info("Uploaded total of {} records for source {} with file name {}", totalRecords, source, fileName);

        messages.add(String.format("Blob %s uploaded to container %s.", fileName, source));
    }

    private int parseContainerForData(BlobServiceClient serviceClient,
                                      BufferedWriter writer,
                                      String containerName,
                                      SourceProperties properties,
                                      LocalDate fromDate,
                                      LocalDate toDate,
                                      List<String> datesToParse) {

        final BlobContainerClient blobContainerClient = serviceClient.getBlobContainerClient(containerName);

        int totalRecords = 0;

        for (BlobItem blobItem : blobContainerClient.listBlobs()) {
            if (datesToParse.parallelStream().anyMatch(blobItem.getName()::contains)) {
                totalRecords += dataWriterComponent.writeRecordsForDateRange(writer, blobContainerClient.getBlobClient(blobItem.getName()),
                                                                             properties, fromDate, toDate);
            }
        }

        return totalRecords;
    }

    private void uploadFileToBlobStore(BlobServiceClient blobServiceClient,
                                       String source,
                                       LocalDate fromDate,
                                       LocalDate toDate) {

        String fileName = FileUtils.getExportName(source, fromDate, toDate, JSONL_EXTENSION);
        String zipName = FileUtils.getExportName(source, fromDate, toDate, ZIP_EXTENSION);

        if (TRUE.equals(archiveEnabled)) {
            archiveComponent.createArchive(Collections.singletonList(fileName), zipName);
        }

        String blobName = TRUE.equals(archiveEnabled) ? zipName : fileName;

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(source);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        blobClient.uploadFromFile(blobName, true);

        // Clean up
        FileUtils.deleteFile(fileName);
        FileUtils.deleteFile(zipName);
    }
}
