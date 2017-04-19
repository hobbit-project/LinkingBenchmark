/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.hobbit.spatiotemporalbenchmark.Trace;
import org.hobbit.spatiotemporalbenchmark.transformations.RelationsCall;
import org.hobbit.spatiotemporalbenchmark.transformations.Transformation;
import org.hobbit.spatiotemporalbenchmark.transformations.value.CoordinatesToAddress;
import org.hobbit.spatiotemporalbenchmark.transformations.value.AddPoint;
import org.hobbit.spatiotemporalbenchmark.util.RandomUtil;
import org.hobbit.spatiotemporalbenchmark.util.StringUtil;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 *
 * @author jsaveta
 */
public class CreateInstances {

    private static Map<Resource, Resource> URIMap = new HashMap<Resource, Resource>(); //sourceURI, targetURI, this contains also th bnodes
    private RandomUtil ru = new RandomUtil();
    private static String exactmatch = "http://www.w3.org/2004/02/skos/core#exactMatch";
    private static int count = 0;
    private Model detailedGSModel;
    private Model GSModel;
    private Trace trace;
    private Trace extendedTrace;

    public CreateInstances() {
    }

    public void sourceInstance(Model givenModel) {
        RelationsCall call = new RelationsCall();
        //Model sourceModel = new LinkedHashModel();
        trace = new Trace();
        extendedTrace = new Trace();
        Model point = new LinkedHashModel();
        Model extendedPoint = new LinkedHashModel();

        Iterator<Statement> it = givenModel.iterator();
        while (it.hasNext()) {
            Statement statement = it.next();
            if (statement.getPredicate().getLocalName().equals("lat")) {
                call.valueSourceCases();
                Transformation transform = call.getSourceTransformationConfigurations();
                if (transform != null) {
                    double latitude = Double.parseDouble(statement.getObject().stringValue());
                    Statement statementLAT = statement;
                    statement = it.next();
                    double longitude = Double.parseDouble(statement.getObject().stringValue());
                    Statement statementLONG = statement;

                    CoordinatesToAddress c = new CoordinatesToAddress();
                    String address = c.getAddress(latitude, longitude);
                    //If address == null it means that we reached the limit of google maps api or there is no address
                    if (!address.equals("")) {
                        Value object = ValueFactoryImpl.getInstance().createLiteral(address);
                        URI predicate = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#label");
                        point.add(statement.getSubject(), predicate, object, statement.getContext());
                        extendedPoint.add(statement.getSubject(), predicate, object, statement.getContext());
                        extendedPoint.add(statementLAT);
                        extendedPoint.add(statementLONG);
                    } else {
                        point.add(statementLAT);
                        point.add(statementLONG);

                    }
                } else {
                    point.add(statement);
                    extendedPoint.add(statement);
                }
            } else {
                if (statement.getPredicate().getLocalName().equals("hasPoint") || !it.hasNext()) {
                    call.keepPointCases();
                    if (!it.hasNext()) {
                        point.add(statement);
                        extendedPoint.add(statement);
                    }
                    if (call.getKeepPoint()) { //check alloccation of config file for the percentage of points to keep
                        trace.addPointsOfTrace(point);
                        extendedTrace.addPointsOfTrace(extendedPoint);
                    }
                    point = new LinkedHashModel();
                    extendedPoint = new LinkedHashModel();
                }
                point.add(statement);
                extendedPoint.add(statement);
            }
        }
    }

    public Model targetInstance(Trace sourceTrace) {

        detailedGSModel = new LinkedHashModel();
        GSModel = new LinkedHashModel();

        //uri change, delete-add points,and value based changes
        Resource targetURI = null;
        Model targetModel = new LinkedHashModel();
        RelationsCall call = new RelationsCall();
        String relation = "Equal";  //no changes 
        call.equalAndRealtionCases();
        ArrayList<Transformation> EandRtr = call.getEqualAndRelationConfiguration();

        if (EandRtr.size() == 1) { //equal or relation
            relation = StringUtil.getClassName(EandRtr.get(0).getClass().toString());
        }
        if (EandRtr.size() == 2) { //equal and relation
            relation = StringUtil.getClassName(EandRtr.get(0).getClass().toString()) + " and " + StringUtil.getClassName(EandRtr.get(1).getClass().toString());
        }

        for (int i = 0; i < sourceTrace.getPointsOfTrace().size(); i++) {

            //add delete points here    
            Model sourcePointModel = sourceTrace.getPointsOfTrace().get(i);
            if (relation.contains("VALUE") && i > 0) { //0 index contains rdf:type trace
                call.AddDeletePointsCases();
                Transformation addDelete = call.getAdditionDeletionPointConfiguration();
                if (addDelete != null) {
                    if (StringUtil.getClassName(addDelete.getClass().toString()).equals("DeletePoint")) {
                        Iterator<Statement> it = sourcePointModel.iterator();
                        Statement statement = it.next();
                        //write intermediate gs
                        WriteDetailedGS(statement.getSubject(), "DeletePoint " + statement.getObject());
                        continue;  //skip the iteration
                    } else if (StringUtil.getClassName(addDelete.getClass().toString()).equals("AddPoint") && (i < (sourceTrace.size() - 1))) { //and is not the last point
                        if (i < (sourceTrace.getPointsOfTrace().size() - 1)) {
                            AddPoint add = new AddPoint();
                            int index = i;
                            Model m1 = sourcePointModel;
                            Model m2 = sourceTrace.getPointsOfTrace().get(index + 1);

                            Model newPoint = add.pointBetween(m1, m2);
                            sourceTrace.addPointsOfTrace(index + 1, newPoint);

                            Iterator<Statement> itP = newPoint.iterator();
                            Statement statementPoint = itP.next();
                            WriteDetailedGS(statementPoint.getSubject(), "Added Point in index " + index + 1);
                        }

                    }
                }
            }

            Iterator<Statement> it = sourcePointModel.iterator();

            while (it.hasNext()) {
                Statement statement = it.next();

                //change source URI
                if (!URIMap.containsKey(statement.getSubject())) {
                    targetURI = targetSubject(statement);

                    WriteDetailedGS(statement.getSubject(), targetURI, relation, null);
                    String r = relation.replace("VALUE", "EQUALS");
                    WriteGS(statement.getSubject(), targetURI, r);

                } else if (URIMap.containsKey(statement.getSubject())) {
                    targetURI = URIMap.get(statement.getSubject());
                }

                if (URIMap.containsKey(statement.getObject())) {
                    targetModel.add(targetURI, statement.getPredicate(), URIMap.get(statement.getObject()), statement.getContext());
                } else {
                    //change bnode
                    Value targetObject = targetObject(statement);
                    if (targetObject == null) {
                        if (relation.contains("VALUE")) {
                            if (statement.getPredicate().getLocalName().equals("label")) { //maybe i can make this: object instanceof String
                                call.valueBasedCases();
                                Transformation transform = call.getEqualTransformationConfiguration();
                                if (transform != null) { //transformation case
                                    String targetObjectStr = transform.execute(statement.getObject().stringValue()).toString();
                                    Value targetObjectLiteral = ValueFactoryImpl.getInstance().createLiteral(targetObjectStr, XMLSchema.STRING);
                                    targetModel.add(targetURI, statement.getPredicate(), targetObjectLiteral, statement.getContext());
                                    //detailed gs 
                                    WriteDetailedGS(statement.getSubject(), targetURI, StringUtil.getClassName(transform.getClass().toString()), "label");
                                } else {
                                    targetModel.add(targetURI, statement.getPredicate(), statement.getObject(), statement.getContext());
                                }
                                //skip long lat if there is a label, we keep all info in order to be able to add new points
                                statement = it.next();
                                statement = it.next();
                            } else if (statement.getPredicate().getLocalName().equals("hasTimestamp")) {
                                call.TimestampCases();
                                Transformation transform = call.getEqualTransformationConfiguration();
                                if (transform != null) { //transformation case
                                    String targetObjectStr = transform.execute(statement.getObject().stringValue()).toString();
                                    Value targetObjectLiteral = ValueFactoryImpl.getInstance().createLiteral(targetObjectStr);

                                    targetModel.add(targetURI, statement.getPredicate(), targetObjectLiteral, statement.getContext());
                                    //detailed gs 
                                    WriteDetailedGS(statement.getSubject(), targetURI, StringUtil.getClassName(transform.getClass().toString()), "hasTimestamp");
                                } else {
                                    targetModel.add(targetURI, statement.getPredicate(), statement.getObject(), statement.getContext());
                                }
                            } else if (statement.getPredicate().getLocalName().equals("lat")) {
                                call.CoordinateCases();
                                Transformation transform = call.getEqualTransformationConfiguration();
                                if (transform != null) { //transformation case
                                    Statement st1 = statement;
                                    Statement st2 = it.next();
                                    Model model = transform.execute(st1, st2);
                                    if (model != null) {
                                        Iterator<Statement> itm = model.iterator();
                                        while (itm.hasNext()) {
                                            Statement stm = itm.next();
                                            targetModel.add(targetURI, stm.getPredicate(), stm.getObject(), statement.getContext());
                                        }
                                        //detailed gs 
                                        WriteDetailedGS(statement.getSubject(), targetURI, StringUtil.getClassName(transform.getClass().toString()), "lat/long");
                                    } else {
                                        targetModel.add(targetURI, st1.getPredicate(), st1.getObject(), st1.getContext());
                                        targetModel.add(targetURI, st2.getPredicate(), st2.getObject(), st2.getContext());
                                    }

                                } else {
                                    targetModel.add(targetURI, statement.getPredicate(), statement.getObject(), statement.getContext());
                                }

                            } else {
                                targetModel.add(targetURI, statement.getPredicate(), statement.getObject(), statement.getContext());
                            }
                        } else if (statement.getPredicate().getLocalName().equals("label")) { //maybe i can make this: object instanceof String
                            targetModel.add(targetURI, statement.getPredicate(), statement.getObject(), statement.getContext());
                            //skip long lat if there is a label, we keep all info in order to be able to add new points
                            statement = it.next();
                            statement = it.next();

                        } else {
                            targetModel.add(targetURI, statement.getPredicate(), statement.getObject(), statement.getContext());
                        }
                    } else {
                        targetModel.add(targetURI, statement.getPredicate(), targetObject, statement.getContext());
                    }

                }
            }

        }
        return targetModel;
    }

    public Map<Resource, Resource> getURIMap() {
        return URIMap;
    }

    public Resource targetObject(Statement statement) {
        Resource object;
        if (statement.getObject() instanceof Literal) {
            return null;
        }
        if (statement.getObject() instanceof BNode) {
            object = ru.randomUniqueBNode();
            URIMap.put((Resource) statement.getObject(), object);

        } else if (statement.getObject() instanceof Resource && !statement.getPredicate().stringValue().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
            object = ru.randomUniqueURI();
            URIMap.put((Resource) statement.getObject(), object);

        } else {
            object = (Resource) statement.getObject();
        }
        return object;
    }

    public Resource targetSubject(Statement statement) {
        Resource subject;
        if (!(statement.getSubject() instanceof BNode)) {
            subject = ru.randomUniqueURI();
            URIMap.put(statement.getSubject(), subject);
        } else {
            subject = ru.randomUniqueBNode();
            URIMap.put(statement.getSubject(), subject);
        }
        return subject;
    }

    public void WriteDetailedGS(Resource sourceInstance, Resource targetInstance, String transformation, String property) {
        URI predicate = ValueFactoryImpl.getInstance().createURI(exactmatch);
        this.detailedGSModel.add(sourceInstance, predicate, targetInstance); //value1 equals value2

        Resource transf = ValueFactoryImpl.getInstance().createURI("http://www.transformation#" + Long.toString(count)); //atomiclong
        URI numOfTrans = ValueFactoryImpl.getInstance().createURI("http://www.isTransformed");
        URI type_ = ValueFactoryImpl.getInstance().createURI("http://www.type");
        URI prop_ = ValueFactoryImpl.getInstance().createURI("http://www.onProperty");
        if (transformation != null) {
            Value transformation_ = ValueFactoryImpl.getInstance().createLiteral(transformation);
            this.detailedGSModel.add(sourceInstance, numOfTrans, transf); // value1 has trans_num transf
            this.detailedGSModel.add(transf, type_, transformation_); //transf has transfrormation type a number from the map
        }

        if (property != null && !property.equals("")) {
            if (property.startsWith("http")) {
                URI prop_name_u = ValueFactoryImpl.getInstance().createURI(property);
                this.detailedGSModel.add(transf, prop_, prop_name_u);
            } else {
                Value prop_name_v = ValueFactoryImpl.getInstance().createLiteral(property);
                this.detailedGSModel.add(transf, prop_, prop_name_v);
            }
        }

        count++;
    }

    public void WriteDetailedGS(Resource subject, String transformation) {
        URI type = ValueFactoryImpl.getInstance().createURI("http://www.type");
        Value transformation_ = ValueFactoryImpl.getInstance().createLiteral(transformation);
        this.detailedGSModel.add(subject, type, transformation_);
    }

    public void WriteGS(Resource sourceInstance, Resource targetInstance, String r) {
        String relation = "http://www.relation#" + r;
        URI predicate = ValueFactoryImpl.getInstance().createURI(relation);
        this.GSModel.add(sourceInstance, predicate, targetInstance); //value1 relation value2
    }

    public Model getDetailedGSModel() {
        return this.detailedGSModel;
    }

    public Model getGSModel() {
        return this.GSModel;
    }

    public Trace getSourceTrace() {
        return this.trace;
    }

    public Trace getExtendedSourceTrace() {
        return this.extendedTrace;
    }

    public void printPoints(Trace trace) {
        for (int i = 0; i < trace.getPointsOfTrace().size(); i++) {
            Model model = trace.getPointsOfTrace().get(i);
            Iterator<Statement> it1 = model.iterator();
            while (it1.hasNext()) {
                Statement statement = it1.next();
                System.out.println(statement.toString());
            }
        }
    }
}
