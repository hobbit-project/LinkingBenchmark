package org.hobbit.spatiotemporalbenchmark.platformConnection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hobbit.core.components.AbstractDataGenerator;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.hobbit.spatiotemporalbenchmark.data.Generator;
import static org.hobbit.spatiotemporalbenchmark.data.Generator.getConfigurations;
import static org.hobbit.spatiotemporalbenchmark.data.Generator.getDefinitions;
import static org.hobbit.spatiotemporalbenchmark.data.Generator.getRandom;
import org.hobbit.spatiotemporalbenchmark.data.Worker;
import org.hobbit.spatiotemporalbenchmark.platformConnection.util.PlatformConstants;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.spatiotemporalbenchmark.properties.Definitions;
import org.hobbit.spatiotemporalbenchmark.util.AllocationsUtil;

/**
 *
 * @author jsaveta
 */
public class DataGenerator extends AbstractDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

    private int numberOfDataGenerators; //TODO: use this
    private int population;
    public static String serializationFormat;
    private double keepPoints;
    private double severity;
    private double changeTimestamp;
    private double sourcePointsToLabels;
    private String addRemovePoints;
    private String targetPointsTransformations;
    private String valueBasedTransformations;

//    public static final String SEVERITY = "severity";
//    public static final String CHANGE_TIMESTAMP = "change_timestamp";    
//    public static final String SOURCE_POINTS_TO_LABELS = "source_points_to_labels";    
//    public static final String ADD_REMOVE_POINTS = "add_remove_points";
//    public static final String TARGET_POINTS_TRANSFORMATIONS = "target_points_transformations";
//    public static final String VALUE_BASED_TRANSFORMATIONS = "value_based_transformations";
    private int taskId = 0;

    public static Generator dataGeneration = new Generator();

    public static String testPropertiesFile = System.getProperty("user.dir") + File.separator + "test.properties";
    public static String definitionsPropertiesFile = System.getProperty("user.dir") + File.separator + "definitions.properties";
    public static String datasetsPath = System.getProperty("user.dir") + File.separator + "datasets";
    public static String givenDatasetsPath = System.getProperty("user.dir") + File.separator + "datasets" + File.separator + "givenDatasets";

    private Task task;

    @Override
    public void init() throws Exception {
        LOGGER.info("Initializing Data Generator '" + getGeneratorId() + "'");
        super.init();

        // Initialize data generation parameters through the environment variables given by user
        initFromEnv();

        // Given the above input, update configuration files that are necessary for data generation
        reInitializeProperties();

        task = new Task(Integer.toString(taskId++), null, null);

    }

    @Override
    protected void generateData() throws Exception {
        // Create your data inside this method. You might want to use the
        // id of this data generator and the number of all data generators
        // running in parallel.

//        int dataGeneratorId = getGeneratorId();
//        int numberOfGenerators = getNumberOfGenerators();
        LOGGER.info("Generate data.. ");
        try {
            Worker worker = new Worker();
            worker.execute();

            File sourcePath = new File(getConfigurations().getString(Configurations.DATASETS_PATH) + File.separator + "SourceDatasets");
            ArrayList<File> sourceFiles = new ArrayList<File>(Arrays.asList(sourcePath.listFiles()));

            File targetPath = new File(getConfigurations().getString(Configurations.DATASETS_PATH) + File.separator + "TargetDatasets");
            ArrayList<File> targetFiles = new ArrayList<File>(Arrays.asList(targetPath.listFiles()));

            File gsPath = new File(getConfigurations().getString(Configurations.DATASETS_PATH) + File.separator + "GoldStandards");
            ArrayList<File> gsFiles = new ArrayList<File>(Arrays.asList(gsPath.listFiles()));
            // send generated data to system adapter
            for (File file : sourceFiles) {
                byte[][] generatedFileArray = new byte[3][];
                // send the file name and its content
                generatedFileArray[0] = RabbitMQUtils.writeString(serializationFormat);
                generatedFileArray[1] = RabbitMQUtils.writeString(file.getAbsolutePath());
                generatedFileArray[2] = FileUtils.readFileToByteArray(file);
                // convert them to byte[]
                byte[] generatedFile = RabbitMQUtils.writeByteArrays(generatedFileArray);
                // send data to system
                sendDataToSystemAdapter(generatedFile);
                LOGGER.info(file.getAbsolutePath() + " (" + (double) file.length() / 1000 + " KB) sent to System Adapter.");

            }

            for (File file : gsFiles) {
                byte[][] generatedFileArray = new byte[3][];
                // send the file name and its content
                generatedFileArray[0] = RabbitMQUtils.writeString(serializationFormat);
                generatedFileArray[1] = RabbitMQUtils.writeString(file.getAbsolutePath());
                LOGGER.info("file.getAbsolutePath() gs " + file.getAbsolutePath());
                generatedFileArray[2] = FileUtils.readFileToByteArray(file);
                // convert them to byte[]
                byte[] generatedFile = RabbitMQUtils.writeByteArrays(generatedFileArray);

                task.setExpectedAnswers(generatedFile);
                LOGGER.info("Gold Standard successfully added to Task.");
            }

            // send generated tasks along with their expected answers to task generator
            for (File file : targetFiles) {
                byte[][] generatedFileArray = new byte[3][];
                // send the file name and its content
                generatedFileArray[0] = RabbitMQUtils.writeString(serializationFormat);
                generatedFileArray[1] = RabbitMQUtils.writeString(file.getAbsolutePath());
                generatedFileArray[2] = FileUtils.readFileToByteArray(file);
                // convert them to byte[]
                byte[] generatedFile = RabbitMQUtils.writeByteArrays(generatedFileArray);
                task.setTarget(generatedFile);

                byte[] data = SerializationUtils.serialize(task);

                sendDataToTaskGenerator(data);
                LOGGER.info("Target data successfully sent to Task Generator.");
            }

        } catch (Exception e) {
            LOGGER.error("Exception while sending file to System Adapter or Task Generator(s).", e);
        }

    }

    public void initFromEnv() {

        LOGGER.info("Getting Data Generator's properites from the environment...");

        Map<String, String> env = System.getenv();
        serializationFormat = (String) getFromEnv(env, PlatformConstants.GENERATED_DATA_FORMAT, "");
        population = (Integer) getFromEnv(env, PlatformConstants.GENERATED_POPULATION, 0);
        numberOfDataGenerators = (Integer) getFromEnv(env, PlatformConstants.NUMBER_OF_DATA_GENERATORS, 0);
        keepPoints = (double) getFromEnv(env, PlatformConstants.KEEP_POINTS, 0.0);

        severity = (double) getFromEnv(env, PlatformConstants.SEVERITY, 0.0);
        changeTimestamp = (double) getFromEnv(env, PlatformConstants.CHANGE_TIMESTAMP, 0.0);
        sourcePointsToLabels = (double) getFromEnv(env, PlatformConstants.SOURCE_POINTS_TO_LABELS, 0.0);
        addRemovePoints = (String) getFromEnv(env, PlatformConstants.ADD_REMOVE_POINTS, "");
        targetPointsTransformations = (String) getFromEnv(env, PlatformConstants.TARGET_POINTS_TRANSFORMATIONS, "");
        valueBasedTransformations = (String) getFromEnv(env, PlatformConstants.VALUE_BASED_TRANSFORMATIONS, "");
        
        //to keep points to diavazei kala alla oxi ta upoloipa floats! giati?
        LOGGER.info("keepPoints " + keepPoints);
        LOGGER.info("severity " + severity); //severity 0.20000000298023224 ??
        LOGGER.info("changeTimestamp " + changeTimestamp);
        LOGGER.info("sourcePointsToLabels " + sourcePointsToLabels);
        LOGGER.info("addRemovePoints " + addRemovePoints);
        LOGGER.info("targetPointsTransformations " + targetPointsTransformations);
        LOGGER.info("valueBasedTransformations " + valueBasedTransformations);
        
        //des kai sto transformations call pos ektuponontai
        
    }

    /**
     * A generic method for initialize benchmark parameters from environment
     * variables
     *
     * @param env a map of all available environment variables
     * @param parameter the property that we want to get
     * @param paramType a dummy parameter to recognize property's type
     */
    @SuppressWarnings("unchecked")
    private <T> T getFromEnv(Map<String, String> env, String parameter, T paramType) {
        if (!env.containsKey(parameter)) {
            LOGGER.error(
                    "Environment variable \"" + parameter + "\" is not set. Aborting.");
            throw new IllegalArgumentException(
                    "Environment variable \"" + parameter + "\" is not set. Aborting.");
        }
        try {
            if (paramType instanceof String) {
                return (T) env.get(parameter);
            } else if (paramType instanceof Integer) {
                return (T) (Integer) Integer.parseInt(env.get(parameter));
            } else if (paramType instanceof Long) {
                return (T) (Long) Long.parseLong(env.get(parameter));
            } else if (paramType instanceof Double) {
                return (T) (Double) Double.parseDouble(env.get(parameter));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Couldn't get \"" + parameter + "\" from the environment. Aborting.", e);
        }
        return paramType;
    }

    public void reInitializeProperties() throws IOException {

//        int numberOfGenerators = getNumberOfGenerators();
//        int generatorId = getGeneratorId();
        loadPropertiesConfigurationFiles();

        getDefinitions().initializeAllocations(getRandom());

        // re-initialize test.properties file that is required for data generation
        getConfigurations().setStringProperty(Configurations.INSTANCES, String.valueOf(population));
        getConfigurations().setStringProperty(Configurations.GENERATED_DATA_FORMAT, serializationFormat);
        getConfigurations().setStringProperty(Configurations.DATASETS_PATH, datasetsPath);
        getConfigurations().setStringProperty(Configurations.GIVEN_DATASETS_PATH, givenDatasetsPath);
        getConfigurations().setStringProperty(Configurations.VALUE_SEVERITY, String.valueOf(severity));
        
//TODO : check if keep points < 1.0
        ArrayList<Double> points = new ArrayList<Double>();
        points.add(keepPoints);
        points.add(1.0 - keepPoints);
        Random random = new Random();
        Definitions.keepPointsAllocation = new AllocationsUtil(points, random);

        //timestamp
        ArrayList<Double> timestamp = new ArrayList<Double>();
        timestamp.add(changeTimestamp);
        timestamp.add(1.0 - changeTimestamp);
        Definitions.timestampAllocation = new AllocationsUtil(timestamp, random);

        //source points to labels
        ArrayList<Double> sourceLabels = new ArrayList<Double>();
        sourceLabels.add(sourcePointsToLabels);
        sourceLabels.add(1.0 - sourcePointsToLabels);
        Definitions.valueSourceAllocation = new AllocationsUtil(sourceLabels, random);
        
        //add/remove points
        Definitions.addRemovePointAllocation = new AllocationsUtil(stringToArray(addRemovePoints), random);

        //target points transformations
        Definitions.coordinatesAllocation = new AllocationsUtil(stringToArray(targetPointsTransformations), random);

        //value-based transformations
        Definitions.valueBasedAllocation = new AllocationsUtil(stringToArray(valueBasedTransformations), random);

        //print an ta pairnei sosta
        //mipos pragmatika den krataei to keep points gia fen exo kanei set opos pararakato ?
        //des to kai sto spatial! 
        
//        ArrayList<Double> relationArrayList = new ArrayList<Double>();
//        for (int i = 0; i < 10; i++) {
//            relationArrayList.add(0.0);
//        }
//        int index = spatialRelation.valueOf(relation).ordinal();
//        relationArrayList.add(index, 1.0);
//        Definitions.spatialRelationsAllocation = new AllocationsUtil(relationArrayList, random);
//
//        getRelationsCall().spatialRelationsCases();
//        setSpatialTransformation(getRelationsCall().getSpatialRelationsConfiguration());
    }

    public static void loadPropertiesConfigurationFiles() throws IOException {
        getConfigurations().loadFromFile(testPropertiesFile);
        getDefinitions().loadFromFile(definitionsPropertiesFile);
        //getDefinitions().loadFromFile(configurations.getString(Configurations.DEFINITIONS_PATH));
    }

    public double[] stringToArray(String allocations) {
        String[] allocationsAsStrings = allocations.split(",");
        double[] allocationsAsDoubles = new double[allocationsAsStrings.length];

        for (int i = 0; i < allocationsAsDoubles.length; i++) {
            allocationsAsDoubles[i] = Double.parseDouble(allocationsAsStrings[i]);
        }
        return allocationsAsDoubles;
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing Data Generator...");
        super.close();
        LOGGER.info("Data Generator closed successfully.");
    }

}
