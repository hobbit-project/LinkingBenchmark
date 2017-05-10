package org.hobbit.spatiotemporalbenchmark.main;

import java.io.IOException;
import org.hobbit.spatiotemporalbenchmark.data.Generator;
import static org.hobbit.spatiotemporalbenchmark.data.Generator.getConfigurations;
import static org.hobbit.spatiotemporalbenchmark.data.Generator.getDefinitions;
import static org.hobbit.spatiotemporalbenchmark.data.Generator.getRandom;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;

public class Main {

    private static Generator generateData;

    public static void main(String[] args) throws IOException {
        generateData = new Generator();
        getConfigurations().loadFromFile("test.properties");
        getDefinitions().loadFromFile(getConfigurations().getString(Configurations.DEFINITIONS_PATH));
        getDefinitions().initializeAllocations(getRandom());

        generateData.exec();
    }

}
