package dev.loskutnikov.onlinelibrary;

import dev.loskutnikov.onlinelibrary.books.event.BookKafkaEvent;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<Long, BookKafkaEvent> kafkaTemplate(
            KafkaProperties kafkaProperties
    ) {
        var prop = kafkaProperties.buildProducerProperties();
        ProducerFactory<Long, BookKafkaEvent> producerFactory =
                new DefaultKafkaProducerFactory<>(prop);

        return new KafkaTemplate<>(producerFactory);
    }
}
