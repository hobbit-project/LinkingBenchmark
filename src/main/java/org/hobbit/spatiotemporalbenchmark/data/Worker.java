package org.hobbit.spatiotemporalbenchmark.data;

import org.hobbit.spatiotemporalbenchmark.data.oaei.OAEIRDFAlignmentFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.hobbit.spatiotemporalbenchmark.main.Main;
import org.apache.commons.io.FileUtils;
import org.hobbit.spatiotemporalbenchmark.Trace;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.spatiotemporalbenchmark.util.FileUtil;
import org.hobbit.spatiotemporalbenchmark.util.SesameUtils;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.QueryResults;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.Rio;
import org.openrdf.sail.memory.MemoryStore;

public class Worker extends AbstractWorker {

    protected long targetTriples;
    protected long totalTriplesForWorker;
    protected String destinationPath;
    protected String serializationFormat;
    protected AtomicLong filesCount;

    public Worker(AtomicLong filesCount, String destinationPath, String serializationFormat) {
        this.filesCount = filesCount;
        this.destinationPath = destinationPath;
        this.serializationFormat = serializationFormat;
    }

    @Override
    public void execute() throws Exception {
        FileOutputStream sourceFos = null;
        FileOutputStream targetFos = null;
        FileOutputStream gsFos = null;
        FileOutputStream detailedGSFos = null;
        FileOutputStream oaeiGSFos = null;
        RDFFormat rdfFormat = SesameUtils.parseRdfFormat(serializationFormat);

        String sourceDestination = destinationPath + "/SourceDatasets";
        String targetDestination = destinationPath + "/TargetDatasets";
        String goldStandardDestination = destinationPath + "/GoldStandards";
        String detailedGoldStandardDestination = destinationPath + "/DetailedGoldStandards";
        String OAEIGoldStandardDestination = destinationPath + "/OAEIGoldStandards";

        File theFileS = new File(sourceDestination);
        theFileS.mkdirs();
        FileUtils.cleanDirectory(theFileS); // will create a folder for the transformed data if not exists                     

        File theFileT = new File(targetDestination);
        theFileT.mkdirs();
        FileUtils.cleanDirectory(theFileT);

        File theFilegs = new File(goldStandardDestination);
        theFilegs.mkdirs();
        FileUtils.cleanDirectory(theFilegs);

        File theFileDetailedGS = new File(detailedGoldStandardDestination);
        theFileDetailedGS.mkdirs();
        FileUtils.cleanDirectory(theFileDetailedGS);

        File theFileOAEIGS = new File(OAEIGoldStandardDestination);
        theFileOAEIGS.mkdirs();
        FileUtils.cleanDirectory(theFileOAEIGS);

        long currentFilesCount = filesCount.incrementAndGet();
        String sourceFileName = String.format(SOURCE_FILENAME + rdfFormat.getDefaultFileExtension(), sourceDestination, File.separator, currentFilesCount);
        String targetFileName = String.format(TARGET_FILENAME + rdfFormat.getDefaultFileExtension(), targetDestination, File.separator, currentFilesCount);
        String gsFileName = String.format(GOLDSTANDARD_FILENAME + rdfFormat.getDefaultFileExtension(), goldStandardDestination, File.separator, currentFilesCount);
        String extendedGSFileName = String.format(DETAILED_GOLDSTANDARD_FILENAME + rdfFormat.getDefaultFileExtension(), detailedGoldStandardDestination, File.separator, currentFilesCount);
        String oaeiGSFileName = String.format(OAEI_GOLDSTANDARD_FILENAME + rdfFormat.getDefaultFileExtension(), OAEIGoldStandardDestination, File.separator, currentFilesCount);

        RDFFormat format = RDFFormat.TURTLE; //fix format based on mimicking
        String path = Main.getConfigurations().getString(Configurations.GIVEN_DATASETS_PATH);
       
        List<File> collectedFiles = new ArrayList<File>();
        RepositoryConnection con = null;
        FileUtil.collectFilesList(path, collectedFiles, "*", true);

        int numOfInstances = Main.getConfigurations().getInt(Configurations.INSTANCES);
        if ((numOfInstances > collectedFiles.size()) || (numOfInstances == 0)) {
            numOfInstances = collectedFiles.size();
        }
        //oaei gold standard
        OAEIRDFAlignmentFormat oaeiRDF = new OAEIRDFAlignmentFormat(oaeiGSFileName, sourceFileName, targetFileName);

        try {
            sourceFos = new FileOutputStream(sourceFileName);
            targetFos = new FileOutputStream(targetFileName);
            gsFos = new FileOutputStream(gsFileName);
            detailedGSFos = new FileOutputStream(extendedGSFileName);
            oaeiGSFos = new FileOutputStream(oaeiGSFileName);
 
            for (int i = 0; i < numOfInstances; i++) {
                Repository repository = new SailRepository(new MemoryStore());
                con = null;
                repository.initialize();
                con = repository.getConnection();
                con.add(collectedFiles.get(i), "", format);
                System.out.println("i " + i + " " + collectedFiles.get(i).getName());

                //ids of traces for defined number of instances 
                String queryNumInstances = "SELECT ?s WHERE {"
                        + "?s  a  <http://www.tomtom.com/ontologies/traces#Trace> . }";

                TupleQuery query = con.prepareTupleQuery(QueryLanguage.SPARQL, queryNumInstances);
                TupleQueryResult result = query.evaluate();

                BindingSet nextResult = result.next();
                String traceID = nextResult.getBinding("s").getValue().stringValue();
                String queryString
                        = "CONSTRUCT{"
                        + "<" + traceID + ">  a  <http://www.tomtom.com/ontologies/traces#Trace> ."
                        + "<" + traceID + "> <http://www.tomtom.com/ontologies/traces#hasPoint> ?o1 . " //Point
                        + "?o1 <http://www.tomtom.com/ontologies/traces#hasTimestamp> ?o2 . "
                        + "?o1 <http://www.tomtom.com/ontologies/traces#lat> ?o3 . "
                        + "?o1 <http://www.tomtom.com/ontologies/traces#long> ?o4 . "
                        + "?o1 <http://www.tomtom.com/ontologies/traces#hasSpeed> ?o5 . " //Speed
                        + "?o5 <http://www.tomtom.com/ontologies/traces#velocityValue> ?o6 . "
                        + "?o5 <http://www.tomtom.com/ontologies/traces#velocityMetric> ?o7 . }"
                        + "WHERE {"
                        + "<" + traceID + ">  a  <http://www.tomtom.com/ontologies/traces#Trace> ."
                        + "<" + traceID + "> <http://www.tomtom.com/ontologies/traces#hasPoint> ?o1 . " //Point
                        + "?o1 <http://www.tomtom.com/ontologies/traces#hasTimestamp> ?o2 . "
                        + "?o1 <http://www.tomtom.com/ontologies/traces#lat> ?o3 . "
                        + "?o1 <http://www.tomtom.com/ontologies/traces#long> ?o4 . "
                        + "?o1 <http://www.tomtom.com/ontologies/traces#hasSpeed> ?o5 . " //Speed
                        + "?o5 <http://www.tomtom.com/ontologies/traces#velocityValue> ?o6 . "
                        + "?o5 <http://www.tomtom.com/ontologies/traces#velocityMetric> ?o7 . "
                        + "}";

                GraphQueryResult graphResult = con.prepareGraphQuery(QueryLanguage.SPARQL, queryString).evaluate();
                Model resultsModel = QueryResults.asModel(graphResult);
                CreateInstances create = new CreateInstances();

                Model givenInstanceModel = new LinkedHashModel();
                Iterator<Statement> it = resultsModel.iterator();

                //retrieve each Trace instance 
                while (it.hasNext()) {
                    Statement statement = it.next();

                    if (statement.getObject().stringValue().endsWith("Trace") || !it.hasNext()) {
                        if (!givenInstanceModel.isEmpty()) {
                            //source instance - Trace

                            if (!it.hasNext()) {
                                givenInstanceModel.add(statement);
                            }
                            create.sourceInstance(givenInstanceModel);
                            Trace sourceTrace = create.getSourceTrace();

                            //write source instance here after applying points to label transformation if defined
                            Model model = new LinkedHashModel();
                            for (int j = 0; j < sourceTrace.getPointsOfTrace().size(); j++) {
                                model.addAll(sourceTrace.getPointsOfTrace().get(j));
                            }
                            Rio.write(model, sourceFos, rdfFormat);
                            //target instance
                            //here I have the extended source trace because I want to know the coordinates of the 
                            //when havind a label
                            Model targetSesameModel = create.targetInstance(create.getExtendedSourceTrace());
                            //apply uri change and transformations on instance to create target.
                            Rio.write(targetSesameModel, targetFos, rdfFormat);

                            //detailed gold standard
                            Model detailedGS = create.getDetailedGSModel();
                            Rio.write(detailedGS, detailedGSFos, rdfFormat);

                            //simple gold standard
                            Model GS = create.getGSModel();
                            Rio.write(GS, gsFos, rdfFormat);

                            //oaei gold standard
                            Iterator<Statement> itGS = GS.iterator();
                            Statement stGS = itGS.next();
                            try {
                                oaeiRDF.addMapping2Output(stGS.getSubject().stringValue(), stGS.getObject().stringValue(), 0, 1.0);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            givenInstanceModel = new LinkedHashModel();
                        }
                    }
                    givenInstanceModel.add(statement);
                }
            }
            try {
                oaeiRDF.saveOutputFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            con.close();
            flushClose(sourceFos);
            flushClose(targetFos);
            flushClose(gsFos);
            flushClose(detailedGSFos);
            flushClose(oaeiGSFos);

        } catch (RDFHandlerException e) {
            con.close();
            flushClose(sourceFos);
            flushClose(targetFos);
            flushClose(gsFos);
            flushClose(detailedGSFos);
            flushClose(oaeiGSFos);

            throw new IOException("A problem occurred while generating RDF data: " + e.getMessage());
        }
    }

    protected synchronized void flushClose(OutputStream fos) throws IOException {
        if (fos != null) {
            fos.flush();
            fos.close();
        }
    }

    @Override
    public Model createSourceModel(Model model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model createTargetModel(Model model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
