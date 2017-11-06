package se.ithuset.eventsourcing.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import se.ithuset.bank.AccountBalanceEvent;
import se.ithuset.eventsourcing.services.AccountService;

import java.io.IOException;
import java.util.Map;

@Component
public class MainListener implements ConsumerSeekAware {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    ConsumerSeekCallback consumerSeekCallback;


    private static final Logger logger = LoggerFactory.getLogger(MainListener.class);

    @KafkaListener(topics = "balance")
    public void process(@Payload String content,
                        @Header(KafkaHeaders.OFFSET) long offset) throws IOException {
        final AccountBalanceEvent event = objectMapper.readValue(content, AccountBalanceEvent.class);
        logger.info("Received event: {}", event);

        accountService.updateBalance(event);
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback consumerSeekCallback) {
        this.consumerSeekCallback = consumerSeekCallback;
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> map, ConsumerSeekCallback consumerSeekCallback) {

    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> map, ConsumerSeekCallback consumerSeekCallback) {

    }
}
