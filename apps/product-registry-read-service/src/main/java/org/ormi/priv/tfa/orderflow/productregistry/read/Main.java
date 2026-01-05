package org.ormi.priv.tfa.orderflow.productregistry.read;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d'entrée (bootstrap) pour l'application Quarkus
 * {@code product-registry-read-service}.
 *
 * <p>Démarre l'application via {@link Quarkus#run(Class, java.util.function.BiConsumer, String[])}
 * en déclarant l'implémentation {@link QuarkusApplication} interne qui
 * attend indéfiniment l'arrêt de l'application via {@link Quarkus#waitForExit()}.
 *
 * todo doc OK
 */

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(
            ProductRegistryReadApplication.class,
            (exitCode, exception) -> {},
            args);
    }

    public static class ProductRegistryReadApplication implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
