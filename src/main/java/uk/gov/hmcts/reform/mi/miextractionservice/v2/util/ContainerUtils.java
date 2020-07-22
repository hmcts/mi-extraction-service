package uk.gov.hmcts.reform.mi.miextractionservice.v2.util;

import static uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.Constants.DASH_DELIMITER;

public final class ContainerUtils {

    public static final String getContainerPrefix(String source) {
        return source + DASH_DELIMITER;
    }

    private ContainerUtils() {
        // Private Constructor
    }
}
