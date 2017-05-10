package org.hobbit.spatiotemporalbenchmark.data;


import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.spatiotemporalbenchmark.properties.Definitions;
import org.hobbit.spatiotemporalbenchmark.transformations.TransformationsCall;
import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.hobbit.spatiotemporalbenchmark.transformations.PointsAddressesCache;

/**
 * The class responsible for managing data generation for the benchmark. It is
 * the entry point for any data generation related process.
 *
 */
public class Generator {

    private static AtomicLong filesCount = new AtomicLong(0);
    private static DataValueTransformation transform;
    private static Configurations configurations;
    private static Definitions definitions;
    private static Random randomGenerator;//is important not to remove seed from random (deterministic)
    private static TransformationsCall call;

    public Generator() {

        configurations = new Configurations();
        definitions = new Definitions();
        randomGenerator = new Random(0);//is important not to remove seed from random (deterministic)
        call = new TransformationsCall();
        new PointsAddressesCache();

    }

    public static Random getRandom() {
        return randomGenerator;
    }

    public static AtomicLong getAtomicLong() {
        return filesCount;
    }

    public static TransformationsCall getRelationsCall() {
        return call;
    }

    public static Configurations getConfigurations() {
        return configurations;
    }

    public static Definitions getDefinitions() {
        return definitions;
    }

    public static DataValueTransformation getValueTransformation() {
        return transform;
    }

    public static void setRelationsCall(TransformationsCall c) {
        call = c;
    }

    public static void setConfigurations(Configurations conf) {
        configurations = conf;
    }

    public static void setDefinitions(Definitions def) {
        definitions = def;
    }

    public static void setValueTransformation(DataValueTransformation tr) {
        transform = tr;
    }

    public static void loadPropertiesFile(String file) throws IOException {
        configurations.loadFromFile(file);
    }

    public void exec() {
        try {
            System.out.println("eimai sto exec tou generator");
            call.valueBasedCases(); //check
            transform = (DataValueTransformation) call.getValueTransformationConfiguration(); //check
            System.out.println("exec configurations instances " + getConfigurations().getString(Configurations.INSTANCES));

            Worker worker = new Worker();
            System.out.println("the kano exec ton worker");
            worker.execute();

        } catch (Exception ex) {
            Logger.getLogger(Generator.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
