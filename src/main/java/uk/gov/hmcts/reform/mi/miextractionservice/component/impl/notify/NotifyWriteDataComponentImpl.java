package uk.gov.hmcts.reform.mi.miextractionservice.component.impl.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import uk.gov.hmcts.reform.mi.micore.model.NotificationOutput;
import uk.gov.hmcts.reform.mi.miextractionservice.component.FilterComponent;
import uk.gov.hmcts.reform.mi.miextractionservice.component.LineWriterComponent;
import uk.gov.hmcts.reform.mi.miextractionservice.component.WriteDataComponent;

import java.io.BufferedWriter;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@Qualifier("notify")
public class NotifyWriteDataComponentImpl implements WriteDataComponent {

    @Autowired
    private FilterComponent<NotificationOutput> filterComponent;

    @Autowired
    private LineWriterComponent lineWriterComponent;

    @Override
    public void writeData(BufferedWriter writer, List<String> data, OffsetDateTime fromDate, OffsetDateTime toDate) {
        List<String> filteredData = filterComponent.filterDataInDateRange(data, fromDate, toDate);

        lineWriterComponent.writeLines(writer, filteredData);
    }
}
