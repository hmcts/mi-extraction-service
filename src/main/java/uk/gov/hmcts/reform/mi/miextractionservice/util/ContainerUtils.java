package uk.gov.hmcts.reform.mi.miextractionservice.util;

import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static uk.gov.hmcts.reform.mi.miextractionservice.domain.Constants.DASH_DELIMITER;

public final class ContainerUtils {

    public static String getContainerPrefix(String source) {
        return source + DASH_DELIMITER;
    }

    public static boolean checkWhitelist(List<String> whitelist, String containerName) {
        return whitelist.isEmpty()
            || isNotBlank(containerName)
            && whitelist.stream()
                .map(Pattern::compile)
                .anyMatch(pattern -> pattern.matcher(containerName).matches());
    }

    private ContainerUtils() {
        // Private Constructor
    }
}
