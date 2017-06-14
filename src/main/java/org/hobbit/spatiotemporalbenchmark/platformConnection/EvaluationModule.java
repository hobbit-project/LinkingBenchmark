package org.hobbit.spatiotemporalbenchmark.platformConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.hobbit.core.Commands;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractEvaluationModule;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.hobbit.core.rabbit.SimpleFileReceiver;
import static org.hobbit.spatiotemporalbenchmark.data.Generator.getConfigurations;
import org.hobbit.spatiotemporalbenchmark.platformConnection.util.PlatformConstants;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.vocab.HOBBIT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluationModule extends AbstractEvaluationModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationModule.class);
    private SimpleFileReceiver gsReceiver;

    private Property EVALUATION_RECALL = null;
    private Property EVALUATION_PRECISION = null;
    private Property EVALUATION_FMEASURE = null;
    private Property EVALUATION_TIME_PERFORMANCE = null;

    private Model finalModel = ModelFactory.createDefaultModel();

    private int truePositives = 0;
    private int falsePositives = 0;
    private int falseNegatives = 0;

    private double time_perfomance = 0;
    public long time_performance = 0;

    private boolean flag = true;

    @Override
    public void init() throws Exception {
        LOGGER.info("Initializing Evaluation Module started...");
        super.init();

        Map<String, String> env = System.getenv();

        /* time performance */
        if (!env.containsKey(PlatformConstants.EVALUATION_TIME_PERFORMANCE)) {
            throw new IllegalArgumentException("Couldn't get \"" + PlatformConstants.EVALUATION_TIME_PERFORMANCE
                    + "\" from the environment. Aborting.");
        }
        EVALUATION_TIME_PERFORMANCE = this.finalModel
                .createProperty(env.get(PlatformConstants.EVALUATION_TIME_PERFORMANCE));

        LOGGER.info("EVALUATION_TIME_PERFORMANCE setted");
        /* recall */
        if (!env.containsKey(PlatformConstants.EVALUATION_RECALL)) {
            throw new IllegalArgumentException("Couldn't get \"" + PlatformConstants.EVALUATION_RECALL
                    + "\" from the environment. Aborting.");
        }
        EVALUATION_RECALL = this.finalModel
                .createProperty(env.get(PlatformConstants.EVALUATION_RECALL));

        LOGGER.info("EVALUATION_RECALL setted");

        /* precision */
        if (!env.containsKey(PlatformConstants.EVALUATION_PRECISION)) {
            throw new IllegalArgumentException("Couldn't get \""
                    + PlatformConstants.EVALUATION_PRECISION + "\" from the environment. Aborting.");
        }
        EVALUATION_PRECISION = this.finalModel
                .createProperty(env.get(PlatformConstants.EVALUATION_PRECISION));

        LOGGER.info("EVALUATION_PRECISION setted");

        /* fmeasure */
        if (!env.containsKey(PlatformConstants.EVALUATION_FMEASURE)) {
            throw new IllegalArgumentException("Couldn't get \"" + PlatformConstants.EVALUATION_FMEASURE
                    + "\" from the environment. Aborting.");
        }
        EVALUATION_FMEASURE = this.finalModel
                .createProperty(env.get(PlatformConstants.EVALUATION_FMEASURE));

        LOGGER.info("EVALUATION_FMEASURE setted");

        LOGGER.info("Initializing Evaluation Module ended...");

    }

    @Override
    protected void evaluateResponse(byte[] expectedData, byte[] receivedData, long taskSentTimestamp,
            long responseReceivedTimestamp) throws Exception {

        time_performance = responseReceivedTimestamp - taskSentTimestamp;
        if (time_performance < 0) {
            time_performance = 0;
        }
        LOGGER.info("time_performance in ms: " + time_performance);

//        this.sumTaskDelay += delay;
        //expected data come from data generator and received data come from system adapter
        //make sure you know that the results coming from the system adapter have the same format
        // read expected data
        LOGGER.info("Read expected data");

        gsReceiver = SimpleFileReceiver.create(this.incomingDataQueueFactory, "gs_file");

        String[] receivedFiles = gsReceiver.receiveData("./datasets/GoldStandards/");
        LOGGER.info("receivedFiles " + Arrays.toString(receivedFiles));
//[pool-5-thread-4] ERROR org.hobbit.core.rabbit.SimpleFileReceiver - 
//Closed the file "goldStandard-0001.ttl" while there are still 
// 1 messages in its data buffer

        FileReader input = new FileReader("./datasets/GoldStandards/" + receivedFiles[0]);
        BufferedReader reader = new BufferedReader(input);
        HashMap<String, String> expectedMap = new HashMap<String, String>();
        try {

            while (true) {
                String line = reader.readLine();
                LOGGER.info("------------------------ "+line);
                
                if (line == null) {
                    break;
                }
                String[] fields = line.split("\n");

                for (String answer : fields) {
                    answer = answer.trim();
                    if (answer != null && !answer.equals("")) {
                        LOGGER.info("answer " + answer);

                        String source_temp = answer.split("<http://www.w3.org/2002/07/owl#sameAs>")[0];
                        LOGGER.info("EvaluationModule source_temp from gs " + source_temp);
                        String source = source_temp.substring(source_temp.indexOf("<") + 1);
                        source = source.split(">")[0];
                        LOGGER.info("EvaluationModule source from gs " + source);
                        //check this 
                        //source pred target
                        String target_temp = answer.split("<http://www.w3.org/2002/07/owl#sameAs>")[1];
                        LOGGER.info("EvaluationModule target_temp from gs " + target_temp);
                        String target = target_temp.substring(target_temp.indexOf("<") + 1);
                        target = target.split(">")[0];
                        LOGGER.info("EvaluationModule target from gs " + target);
                        expectedMap.put(source, target);
                    }
                    LOGGER.info("expected data into the map: " + expectedMap.toString());
                }
            }
        } finally {
            reader.close();
        }

        // read received data
        LOGGER.info("Read received data");
        //handle empty results! 
        String[] receivedDataAnswers = null;
        if (receivedData.length > 0) {
            receivedDataAnswers = RabbitMQUtils.readString(receivedData).split(System.getProperty("line.separator"));
        }

        LOGGER.info("receivedData----" + new String(receivedData) + "----");

        HashMap<String, String> receivedMap = new HashMap<String, String>();
        if (receivedDataAnswers != null) {
            for (String answer : receivedDataAnswers) {
                answer = answer.trim();
                if (answer != null && !answer.equals("")) {

                    String source_temp = answer.split(">")[0];
                    String source = source_temp.substring(source_temp.indexOf("<") + 1);

                    String target_temp = answer.split(">")[1];
                    String target = target_temp.substring(target_temp.indexOf("<") + 1);
                    receivedMap.put(source, target);
                }
                LOGGER.info("received data into the map: " + receivedMap.toString());
            }
        }

        if (!expectedMap.isEmpty() && !receivedMap.isEmpty()) {
            for (Map.Entry<String, String> expectedEntry : expectedMap.entrySet()) {
                String expectedKey = expectedEntry.getKey();
                String expectedValue = expectedEntry.getValue();

                boolean tpFound = false;
                for (Map.Entry<String, String> receivedEntry : receivedMap.entrySet()) {
                    tpFound = false;
                    String receivedKey = receivedEntry.getKey();
                    String receivedValue = receivedEntry.getValue();

                    if (expectedKey.equals(receivedKey) && expectedValue.equals(receivedValue)) {
                        tpFound = true;
                        break;
                    }
                }
                if (tpFound == true) {
                    truePositives++;
                } else {
                    falseNegatives++;
                }
            }
            // what is not TP in the received answers, is a FP
            falsePositives = receivedMap.size() - truePositives;
        }
        LOGGER.info("truePositives " + truePositives);
        LOGGER.info("falsePositives " + falsePositives);
        LOGGER.info("falseNegatives " + falseNegatives);

    }

    @Override
    public Model summarizeEvaluation() throws Exception {
        LOGGER.info("Summary of Evaluation begins.");

        if (this.experimentUri == null) {
            Map<String, String> env = System.getenv();
            this.experimentUri = env.get(Constants.HOBBIT_EXPERIMENT_URI_KEY);
        }
        double recall = 0.0;
        double precision = 0.0;
        double fmeasure = 0.0;

        if ((double) (this.truePositives + this.falseNegatives) > 0.0) {
            recall = (double) this.truePositives / (double) (this.truePositives + this.falseNegatives);
        }
        if ((double) (this.truePositives + this.falsePositives) > 0.0) {
            precision = (double) this.truePositives / (double) (this.truePositives + this.falsePositives);
        }
        if ((double) (recall + precision) > 0.0) {
            fmeasure = (double) (2.0 * recall * precision) / (double) (recall + precision);
        }
        //////////////////////////////////////////////////////////////////////////////////////////////
        Resource experiment = this.finalModel.createResource(experimentUri);
        this.finalModel.add(experiment, RDF.type, HOBBIT.Experiment);

        Literal timePerformanceLiteral = this.finalModel.createTypedLiteral(this.time_performance, XSDDatatype.XSDlong);
        this.finalModel.add(experiment, this.EVALUATION_TIME_PERFORMANCE, timePerformanceLiteral);

        Literal recallLiteral = this.finalModel.createTypedLiteral(recall,
                XSDDatatype.XSDdouble);
        this.finalModel.add(experiment, this.EVALUATION_RECALL, recallLiteral);

        Literal precisionLiteral = this.finalModel.createTypedLiteral(precision,
                XSDDatatype.XSDdouble);
        this.finalModel.add(experiment, this.EVALUATION_PRECISION, precisionLiteral);

        Literal fmeasureLiteral = this.finalModel.createTypedLiteral(fmeasure,
                XSDDatatype.XSDdouble);
        this.finalModel.add(experiment, this.EVALUATION_FMEASURE, fmeasureLiteral);

        LOGGER.info("Summary of Evaluation is over.");

        return this.finalModel;
    }

    @Override
    public void receiveCommand(byte command, byte[] data) {
        if (PlatformConstants.SYSTEM_ADAPTER_FINISHED == command) {
            LOGGER.info("my receiveCommand for gs");
            gsReceiver.terminate();
        }
        super.receiveCommand(command, data);
    }
}
