package uk.gov.hmcts.reform.mi.miextractionservice.domain;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.Value;

@SuppressWarnings({"checkstyle:MemberName","PMD.FieldNamingConventions","PMD.TooManyFields"})
@Builder
@Value
public class OutputCoreCaseData {

    @CsvBindByPosition(position = 0)
    private String extraction_date;

    @CsvBindByPosition(position = 1)
    private String ce_id;

    @CsvBindByPosition(position = 2)
    private String ce_case_data_id;

    @CsvBindByPosition(position = 3)
    private String ce_created_date;

    @CsvBindByPosition(position = 4)
    private String ce_case_type_id;

    @CsvBindByPosition(position = 5)
    private String ce_case_type_version;

    @CsvBindByPosition(position = 6)
    private String ce_state_id;

    @CsvBindByPosition(position = 7)
    private String ce_state_name;

    @CsvBindByPosition(position = 8)
    private String ce_summary;

    @CsvBindByPosition(position = 9)
    private String ce_description;

    @CsvBindByPosition(position = 10)
    private String ce_event_id;

    @CsvBindByPosition(position = 11)
    private String ce_event_name;

    @CsvBindByPosition(position = 12)
    private String ce_user_id;

    @CsvBindByPosition(position = 13)
    private String ce_user_first_name;

    @CsvBindByPosition(position = 14)
    private String ce_user_last_name;

    @CsvBindByPosition(position = 15)
    private String ce_data;

    @CsvBindByPosition(position = 16)
    private String cd_case_data_id;

    @CsvBindByPosition(position = 17)
    private String cd_created_date;

    @CsvBindByPosition(position = 18)
    private String cd_last_modified;

    @CsvBindByPosition(position = 19)
    private String cd_jurisdiction;

    @CsvBindByPosition(position = 20)
    private String cd_latest_state;

    @CsvBindByPosition(position = 21)
    private String cd_reference;

    @CsvBindByPosition(position = 22)
    private String cd_security_classification;

    @CsvBindByPosition(position = 23)
    private String cd_version;

    @CsvBindByPosition(position = 24)
    private String cd_last_state_modified_date;

    @CsvBindByPosition(position = 25)
    private String ce_data_classification;

    @CsvBindByPosition(position = 26)
    private String ce_security_classification;
}
