package dev.loskutnikov.onlinelibrary.books.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookEventSender {

    private final static Logger logger = LoggerFactory.getLogger(BookEventSender.class);

    private final KafkaTemplate kafkaTemplate;

    public BookEventSender(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(BookKafkaEvent bookKafkaEvent){
        logger.info("Sending event: event={}", bookKafkaEvent);
        var result = kafkaTemplate.send(
                "books-topik",
                bookKafkaEvent.bookId(),
                bookKafkaEvent);

        result.thenAccept(sendResult -> {
            logger.info("Send successful");
        });
    }
}
