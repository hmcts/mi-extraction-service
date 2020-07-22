package uk.gov.hmcts.reform.mi.miextractionservice.v2.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.gov.hmcts.reform.mi.micore.parser.MiDateDeserializer;
import uk.gov.hmcts.reform.mi.miextractionservice.v2.domain.ExportProperties;

import java.time.Clock;
import java.time.OffsetDateTime;

@Configuration
public class BeanConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ObjectMapper objectMapper(MiDateDeserializer dateDeserialize) {
        SimpleModule module = new SimpleModule("CustomCarDeserializer");
        module.addDeserializer(OffsetDateTime.class, dateDeserialize);
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(module)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Configuration Properties

    @Bean
    @ConfigurationProperties(prefix = "export")
    public ExportProperties exportProperties() {
        return new ExportProperties();
    }
}
