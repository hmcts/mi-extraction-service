package uk.gov.hmcts.reform.mi.miextractionservice.exception;

public class ExportException extends RuntimeException {

    public static final long serialVersionUID = 124L;

    public ExportException(String message) {
        super(message);
    }
}
