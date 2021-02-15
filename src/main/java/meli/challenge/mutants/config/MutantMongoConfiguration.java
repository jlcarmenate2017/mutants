package meli.challenge.mutants.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "meli.challenge.mutants.repository", mongoTemplateRef = "mutantMongoTemplate")
public class MutantMongoConfiguration {
}
