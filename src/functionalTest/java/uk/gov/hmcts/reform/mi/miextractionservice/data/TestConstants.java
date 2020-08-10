package uk.gov.hmcts.reform.mi.miextractionservice.data;

@SuppressWarnings("PMD.AvoidUsingHardCodedIP")
public final class TestConstants {

    // Generated static UUID
    private static final String TEST_ID = "a934d7e9-7726-4e09-95e1-157371c51811";

    public static final String TEST_CONTAINER_NAME = "test-testcontainer-" + TEST_ID;
    public static final String TEST_EXTRACTION_CONTAINER_NAME = "test-extraction-testcontainer-" + TEST_ID;
    public static final String TEST_BLOB_NAME = "testblob-" + TEST_ID + "-1970-01";

    public static final String EXPORT_CONTAINER_NAME = "test";
    public static final String EXPORT_EXTRACTION_CONTAINER_NAME = "test-extraction";
    public static final String TEST_EXPORT_BLOB = "test-1970-01-01-1970-01-02.zip";
    public static final String TEST_EXTRACTION_EXPORT_BLOB = "test-extraction-1970-01-01-1970-01-02.zip";

    public static final String TEST_JSONL = "{\"extraction_date\":\"19700101-1010\",\"date_updated\":\"1970-01-01T10:00:00.000Z\"}";

    private TestConstants() {
        // Private constructor
    }
}
