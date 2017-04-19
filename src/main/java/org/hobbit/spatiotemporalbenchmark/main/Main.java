package org.hobbit.spatiotemporalbenchmark.main;

import java.io.IOException;
import java.util.Random;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.spatiotemporalbenchmark.properties.Definitions;
import org.hobbit.spatiotemporalbenchmark.data.Generator;
import org.hobbit.spatiotemporalbenchmark.transformations.PointsAddressesCache;

public class Main {

    public static Configurations configurations = new Configurations();
    public static Definitions definitions = new Definitions();
    protected static Random randomGenerator = new Random(0); //is important not to remove seed from random (deterministic)
    public static Generator generateData = new Generator();

    public static void main(String[] args) throws IOException {
        loadPropertiesFile();
        start();
    }

    public static void start() throws IOException {
//        if( args.length < 1) {
//                throw new IllegalArgumentException("Missing parameter - the configuration file must be specified");
//        }
//        configurations.loadFromFile(args[0]);

        definitions.loadFromFile(configurations.getString(Configurations.DEFINITIONS_PATH));
        new PointsAddressesCache();
        definitions.initializeAllocations(randomGenerator);
        generateData.execute();

    }

    public static void loadPropertiesFile() throws IOException {
        configurations.loadFromFile("test.properties");
    }

    public static Configurations getConfigurations() {
        return configurations;
    }

    public static Definitions getDefinitions() {
        return definitions;
    }

}
