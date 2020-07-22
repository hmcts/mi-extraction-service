package uk.gov.hmcts.reform.mi.miextractionservice.v2.util;

import uk.gov.hmcts.reform.mi.miextractionservice.v2.exception.ExportException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.Constants.FILE_PREFIX;
import static uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.Constants.FILE_SUFFIX;

public final class FileUtils {

    public static final Path createTemporaryFile() {
        try {
            return Files.createTempFile(FILE_PREFIX, FILE_SUFFIX);
        } catch (IOException e) {
            throw new ExportException("Unable to create a temporary file.", e);
        }
    }

    public static final BufferedWriter openTemporaryWriter() throws IOException {
        return Files.newBufferedWriter(createTemporaryFile());
    }

    private FileUtils() {
        // Private Constructor
    }
}
