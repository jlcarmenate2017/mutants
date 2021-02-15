package meli.challenge.mutants.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@Data
@ConfigurationProperties(prefix = "mutants")
public class MutantsProperties {

    private final MutantsProperties.ClientApp clientApp = new MutantsProperties.ClientApp();

    private final MutantsProperties.MongoDb mongodb = new MutantsProperties.MongoDb();
    @Data
    public static class ClientApp {
        private String name = MutantsDefaults.ClientApp.name;
    }

    @Data
    public static class MongoDb {
        private final MutantsProperties.MongoDb.Primary primary = new MutantsProperties.MongoDb.Primary();

        @Data
        public static class Primary {
            private String uri = MutantsDefaults.MongoDb.Primary.uri;
            private final MutantsProperties.MongoDb.Primary.Collections collections = new MutantsProperties.MongoDb.Primary.Collections();

            @Data
            public static class Collections {
                private String mutant = MutantsDefaults.MongoDb.Primary.Collections.mutant;
            }
        }

    }

}
