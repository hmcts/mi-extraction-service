package uk.gov.hmcts.reform.mi.miextractionservice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class ContainerUtilsTest {

    @Test
    void givenSourceHasNoDelimiter_whenGetContainerPrefix_returnSourceWithDelimiter() {
        assertEquals("test-", ContainerUtils.getContainerPrefix("test"),
                     "Non-delimited source should return with delimiter.");
    }

    @Test
    void givenSourceIsStartsWithDelimiter_whenGetContainerPrefix_returnSourceDirectly() {
        assertEquals("anothertest-", ContainerUtils.getContainerPrefix("anothertest-"),
                     "Delimited source should return same value.");
    }

    @Test
    void givenEmptyList_whenCheckWhiteList_thenReturnTrue() {
        assertTrue(ContainerUtils.checkWhitelist(Collections.emptyList(), "anyString"),
                   "Empty whitelist should return true.");
    }

    @Test
    void givenMissingContainerNameToMatchInList_whenCheckWhiteList_thenReturnFalse() {
        assertFalse(ContainerUtils.checkWhitelist(Collections.singletonList("anything"), ""),
                    "Missing container name should return false.");
    }

    @Test
    void givenMatchingContainerNameInList_whenCheckWhiteList_thenReturnTrue() {
        assertTrue(ContainerUtils.checkWhitelist(Collections.singletonList("matching"), "matching"),
                   "Matching container name to whitelist item should return true.");
    }

    @Test
    void givenPatternMatchingContainerNameInList_whenCheckWhiteList_thenReturnTrue() {
        assertTrue(ContainerUtils.checkWhitelist(Collections.singletonList("^match.*"), "matchingpattern"),
                   "Matching container name to whitelist pattern should return true.");
    }

    @Test
    void givenNotMatchingContainerInList_whenCheckWhiteList_thenReturnFalse() {
        assertFalse(ContainerUtils.checkWhitelist(Collections.singletonList("notMatching"), "doesNotMatch"),
                    "Non-matching container name to whitelist should return false.");
    }
}
