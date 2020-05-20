package uk.gov.hmcts.reform.mi.miextractionservice.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class SftpConfigTest {

    private SftpConfig classToTest = new SftpConfig();

    @Test
    public void testJschCreation() {
        assertNotNull(classToTest.jsch(), "new Jsch expected");
    }
}
