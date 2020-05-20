package uk.gov.hmcts.reform.mi.miextractionservice.component;

public interface SftpExportComponent {

    void copyFile(String file);

    void loadFile(String file, String destinyFilePath);

    void checkConnection();

}
