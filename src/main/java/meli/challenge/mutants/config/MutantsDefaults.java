package meli.challenge.mutants.config;

public interface MutantsDefaults {

    interface ClientApp {
        String name = "Mutants";
    }

    interface MongoDb {
        interface Primary {
            String uri = "mongodb://localhost:27017";
            interface Collections {
                String mutant = "meli-mutant";
            }
        }
    }
}
