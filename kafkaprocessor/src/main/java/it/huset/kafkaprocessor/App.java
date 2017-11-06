package it.huset.kafkaprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.processor.internals.ProcessorTopology;
import se.ithuset.bank.AccountBalanceEvent;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App {

    final static ObjectMapper om = new ObjectMapper();

    public static void main( String[] args ) throws InterruptedException {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "olof-dbot-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:32768");

        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        KStreamBuilder streamBuilder = new KStreamBuilder();

        final KStream<String, String> balanceChanges = streamBuilder.stream("balance2");


        final KTable<String, String> sums = balanceChanges
                .mapValues(val -> {
                    try {
                        final AccountBalanceEvent accountBalanceEvent = om.readValue(val, AccountBalanceEvent.class);
                        return String.valueOf(accountBalanceEvent.getAmount().intValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "0";
                    }
                })
                .selectKey((o, o2) -> "1")
                .groupByKey()
                .reduce(App::sumStrings, "fisk");

      /*
        KStream<String, String> sums = balanceChanges
                .mapValues(value -> {
                    try {
                        final AccountBalanceEvent accountBalanceEvent = om.readValue(value, AccountBalanceEvent.class);
                        return String.valueOf(accountBalanceEvent.getAmount().intValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "0";
                    }
                });
*/
        sums.to(Serdes.String(), Serdes.String(),"balance-out2");

        final CountDownLatch latch = new CountDownLatch(1);

        KafkaStreams streams = new KafkaStreams(streamBuilder, props);
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        streams.start();
        latch.await();

    }

    private static String sumStrings(String v1, String v2) {
        return String.valueOf(Integer.parseInt(v1) + Integer.parseInt(v2));

    }
}
