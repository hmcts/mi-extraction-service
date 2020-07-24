package uk.gov.hmcts.reform.mi.miextractionservice.util;

import static uk.gov.hmcts.reform.mi.miextractionservice.domain.Constants.DASH_DELIMITER;

public final class ContainerUtils {

    public static String getContainerPrefix(String source) {
        return source + DASH_DELIMITER;
    }

    private ContainerUtils() {
        // Private Constructor
    }
}
