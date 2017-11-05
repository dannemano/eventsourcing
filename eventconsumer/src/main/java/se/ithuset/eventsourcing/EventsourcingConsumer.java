package se.ithuset.eventsourcing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class EventsourcingConsumer {

	private static final Logger logger = LoggerFactory.getLogger(EventsourcingConsumer.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(EventsourcingConsumer.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner() {
//		return new CommandLineRunner() {
//			@Override
//			public void run(String... strings) throws Exception {
//				final List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from tickets;");
//				maps.stream().forEach(map -> logger.info(map.toString()));
//			}
//		};
//	}

	@Bean
	public CommandLineRunner commandLineRunner2() {
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {

			}
		};
	}
}
