package se.ithuset.eventsourcing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class EventsourcingProducer {

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
				kafkaTemplate.send("dbot","meddelande");
			}
		};
	}

}
