package meli.challenge.mutants.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({MutantsProperties.class})
public class MongoConfiguration {

    private final MutantsProperties mutantsProperties;

    @Bean(name = "mutantMongoTemplate")
    public MongoTemplate mutantMongoTemplate() {
        return new MongoTemplate(primaryMongoClient(), mutantsProperties.getMongodb().getPrimary().getCollections().getMutant());
    }

    @Bean(name = "primaryMongoClient")
    public MongoClient primaryMongoClient() {
        return MongoClients.create(Objects.requireNonNull(mutantsProperties.getMongodb().getPrimary().getUri()));
    }
}
