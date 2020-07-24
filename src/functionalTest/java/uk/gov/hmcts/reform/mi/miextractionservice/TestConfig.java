package uk.gov.hmcts.reform.mi.miextractionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import uk.gov.hmcts.reform.mi.micore.parser.MiDateDeserializer;

import java.time.Clock;
import java.time.OffsetDateTime;

@SuppressWarnings({"PMD.CloseResource","PMD.AvoidUsingHardCodedIP"})
@Configuration
@ComponentScan(basePackages = "uk.gov.hmcts.reform",
    excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ApplicationRunner.class) })
public class TestConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ObjectMapper objectMapper(MiDateDeserializer itemDeserializer) {
        SimpleModule module = new SimpleModule("CustomCarDeserializer");
        module.addDeserializer(OffsetDateTime.class, itemDeserializer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper;
    }
}
