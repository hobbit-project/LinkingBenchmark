package org.hobbit.spatiotemporalbenchmark.data;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hobbit.spatiotemporalbenchmark.main.Main;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.spatiotemporalbenchmark.transformations.RelationsCall;

/**
 * The class responsible for managing data generation for the benchmark. It is
 * the entry point for any data generation related process.
 *
 */
public class Generator {

    private AtomicLong filesCount = new AtomicLong(0);

    public void execute() {
        try {
            new RelationsCall();
            String destinationPath = Main.getConfigurations().getString(Configurations.DATASETS_PATH);
            String serializationFormat = Main.getConfigurations().getString(Configurations.GENERATED_DATA_FORMAT);
            Worker worker = new Worker(filesCount, destinationPath, serializationFormat);
            worker.execute();
        } catch (Exception ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
