package uk.gov.hmcts.reform.mi.miextractionservice.data;

public final class TestConstants {

    // Generated static UUID
    private static final String TEST_ID = "1c9640ba-d3e4-4cdb-bf04-36f00e751189";

    public static final String TEST_CONTAINER_NAME = "ccd-testcontainer-" + TEST_ID;
    public static final String TEST_BLOB_NAME = "testblob-" + TEST_ID + "-1970-01";

    public static final String CCD_EXPORT_CONTAINER_NAME = "ccd";
    public static final String TEST_EXPORT_BLOB = "1970-01-01-1970-01-02-CCD_EXTRACT.zip";

    public static final String TEST_CCD_JSONL = "{\"extraction_date\":\"19991201-1010\","
        + "\"case_metadata_event_id\":1000001,\"ce_case_data_id\":100001,\"ce_created_date\":1001,"
        + "\"ce_case_type_id\":\"CASETYPE\",\"ce_case_type_version\":1001,\"ce_state_id\":\"StateId\",\"ce_data\":{}}";

    public static final String NOTIFY_TEST_CONTAINER_NAME = "notify-testcontainer-" + TEST_ID;
    public static final String NOTIFY_EXPORT_CONTAINER_NAME = "notify";
    public static final String NOTIFY_TEST_EXPORT_BLOB = "1970-01-01-1970-01-02-NOTIFY_EXTRACT.zip";

    public static final String TEST_NOTIFY_JSONL = "{\"extraction_date\":\"19991201-1010\","
        + "\"id\":\"id\",\"service\":\"service\",\"reference\":\"reference\","
        + "\"type\":\"type\",\"status\":\"status\",\"created_at\":\"1970-01-01T01:00:00.000000Z\"}";

    private TestConstants() {
        // Private constructor
    }
}
