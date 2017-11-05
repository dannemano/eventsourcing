package se.ithuset.eventsourcing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import se.ithuset.bank.AccountBalanceEvent;

import java.math.BigDecimal;

@SpringBootApplication
public class EventsourcingProducer {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KafkaTemplate kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(EventsourcingProducer.class, args);
	}


	@Bean
	public CommandLineRunner commandLineRunner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				AccountBalanceEvent event = new AccountBalanceEvent(1L, new BigDecimal(100));
				kafkaTemplate.send("dbot",objectMapper.writeValueAsString(event));
			}
		};
	}

}
