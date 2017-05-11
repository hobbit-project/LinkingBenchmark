/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.platformConnection.systems;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
import org.hobbit.core.components.AbstractSystemAdapter;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.hobbit.spatiotemporalbenchmark.util.FileUtil;
import org.hobbit.spatiotemporalbenchmark.util.SesameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jsaveta
 */
public class LogMapSystemAdapter extends AbstractSystemAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogMapSystemAdapter.class);
    private String receivedGeneratedDataFilePath;
    private String dataFormat;
    private String taskFormat;
    private File resultsFile;

    @Override
    public void init() throws Exception {
        LOGGER.info("Initializing LogMap test system...");
        super.init();
        LOGGER.info("LogMap initialized successfully .");

    }

    @Override
    public void receiveGeneratedData(byte[] data) {
        LOGGER.info("Starting receiveGeneratedData..");

        ByteBuffer dataBuffer = ByteBuffer.wrap(data);
        // read the file path
        dataFormat = RabbitMQUtils.readString(dataBuffer);
        receivedGeneratedDataFilePath = RabbitMQUtils.readString(dataBuffer);
        byte[] receivedGeneratedData = RabbitMQUtils.readByteArray(dataBuffer);
        try {
            FileUtils.writeByteArrayToFile(new File(receivedGeneratedDataFilePath), receivedGeneratedData);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LogMapSystemAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        LOGGER.info("Received data from receiveGeneratedData..");

    }

    @Override
    public void receiveGeneratedTask(String taskId, byte[] data) {
        LOGGER.info("Starting receiveGeneratedTask..");
        LOGGER.info("Task " + taskId + " received from task generator");
        try {

            ByteBuffer taskBuffer = ByteBuffer.wrap(data);
//            // read the relation
//            String taskRelation = RabbitMQUtils.readString(taskBuffer);
//            LOGGER.info("taskRelation " + taskRelation);

            // read the file path
            taskFormat = RabbitMQUtils.readString(taskBuffer);
            String receivedGeneratedTaskFilePath = RabbitMQUtils.readString(taskBuffer);
            byte[] receivedGeneratedTask = RabbitMQUtils.readByteArray(taskBuffer);

            FileUtils.writeByteArrayToFile(new File(receivedGeneratedTaskFilePath), receivedGeneratedTask);

            LOGGER.info("Received task from receiveGeneratedTask..");

            
            linkingController(receivedGeneratedDataFilePath, receivedGeneratedTaskFilePath);
            byte[][] resultsArray = new byte[1][];
            resultsArray[0] = FileUtils.readFileToByteArray(resultsFile);
            byte[] results = RabbitMQUtils.writeByteArrays(resultsArray);
            try {

                sendResultToEvalStorage(taskId, results);
                LOGGER.info("Results sent to evaluation storage.");
            } catch (IOException e) {
                LOGGER.error("Exception while sending storage space cost to evaluation storage.", e);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(LogMapSystemAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void linkingController(String source, String target) throws IOException {
        resultsFile = new File("./datasets/LimesSystemAdapterResults/mappings." + SesameUtils.parseRdfFormat(dataFormat).getDefaultFileExtension());

        LOGGER.info("Started LogMap Controller.. ");
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("java -jar ./lib/logmap2_standalone.jar "
                + "MATCHER file:" + source + " "
                + "file:" + target + " "
                + ""+resultsFile.getAbsolutePath()+" true");
        
        //-Xms512m -Xmx1152m
        //an treksei allakse kai to memory tis java stin entoli

        File[] files = new File("file.getName()").listFiles();
//If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
            if (file.isFile()) {
                LOGGER.info("Logmap - Results file.getName() " + file.getName());
                
                BufferedReader br = new BufferedReader(new FileReader("./datasets/LogMapSystemAdapterResults/"+file.getName()));
                String line = null;
                while ((line = br.readLine()) != null) {
                    LOGGER.info("-> "+line);
                }
            }
        }
        LOGGER.info("LogMap Controller finished..");
    }

    @Override
    public void close() throws IOException {
        LOGGER.info("Closing System Adapter...");
        // Always close the super class after yours!
        super.close();
        LOGGER.info("System Adapter closed successfully.");
    }
}

//https://sourceforge.net/projects/logmap-matcher/files/Standalone%20distribution/
//java -jar logmap2_standalone.jar MATCHER file:/home/ontos/cmt.owl file:/home/ontos/ekaw.owl /home/mappings/output true
