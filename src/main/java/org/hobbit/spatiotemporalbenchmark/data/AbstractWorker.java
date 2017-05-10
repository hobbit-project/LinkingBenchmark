package org.hobbit.spatiotemporalbenchmark.data;

import org.openrdf.model.Model;

public abstract class AbstractWorker {

    protected static final String SOURCE_FILENAME = "%s%ssource-%04d.";
    protected static final String TARGET_FILENAME = "%s%starget-%04d.";
    protected static final String GOLDSTANDARD_FILENAME = "%s%sgoldStandard-%04d.";
    protected static final String DETAILED_GOLDSTANDARD_FILENAME = "%s%sdetailedGoldStandard-%04d.";
    protected static final String OAEI_GOLDSTANDARD_FILENAME = "%s%soaeiGoldStandard-%04d.";
     
    public void run() {
        try {
            execute();
        } catch (Exception e) {
            System.out.println("Exception caught by : " + Thread.currentThread().getName() + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method will be called for execution of a concrete task
     */
    public abstract void execute() throws Exception;

    public abstract Model createSourceModel(Model model) throws Exception;

    public abstract Model createTargetModel(Model model) throws Exception;
}
