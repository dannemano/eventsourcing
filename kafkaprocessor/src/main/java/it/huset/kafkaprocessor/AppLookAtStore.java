package it.huset.kafkaprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class AppLookAtStore {

    final static ObjectMapper om = new ObjectMapper();

    public static void main( String[] args ) throws InterruptedException, IOException {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "query-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:32768");

        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final KStreamBuilder kStreamBuilder = new KStreamBuilder();
       // kStreamBuilder.stream("balance-out2");
        kStreamBuilder.globalTable("balance-out2", "fisk");

        KafkaStreams streams = new KafkaStreams(kStreamBuilder, props);
        streams.start();
        final ReadOnlyKeyValueStore<String, String> sum = streams.store("fisk", QueryableStoreTypes.<String, String>keyValueStore());

        System.out.println("State: " + sum.get("1"));

        Thread.sleep(4000L);
        streams.close();
    }

}
