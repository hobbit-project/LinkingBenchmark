/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations.value;

import org.hobbit.spatiotemporalbenchmark.Point;
import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.hobbit.spatiotemporalbenchmark.util.CoordinatesUtil;
import org.hobbit.spatiotemporalbenchmark.util.RandomUtil;
import org.hobbit.spatiotemporalbenchmark.util.SpeedUtil;
import org.hobbit.spatiotemporalbenchmark.util.TimeUtil;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.XMLSchema;

/**
 *
 * @author jsaveta
 */
public class AddPoint implements DataValueTransformation {

    RandomUtil ru = new RandomUtil();

    public AddPoint() {
    }

    public Model pointBetween(Model m1, Model m2) {
//        System.out.println("adding point ");
        //equal can only call intermediate points 

        //simple case: if both points have coordinates
        Model newPoint = new LinkedHashModel();

        Point p1 = new Point(m1);
        Point p2 = new Point(m2);

        Resource id = p1.getId();
        Resource pointId = ru.randomUniqueURI(); //ru.randomUniqueBNode(); for bnodes

        newPoint.add(id, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#hasPoint"), pointId);

        //-----------------------Middle time---------------------------------
        String t1 = p1.getTimeStamp();
        String t2 = p2.getTimeStamp();
        TimeUtil t = new TimeUtil();
        String mid = t.averageDateTime(t1, t2); //average time

        newPoint.add(pointId, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#hasTimestamp"), ValueFactoryImpl.getInstance().createLiteral(mid, XMLSchema.DATETIME));

        //-----------------------Middle long lat---------------------------------
        // add in model to return
        double lat1 = p1.getLatitude();
        double lon1 = p1.getLongitude();
        double lat2 = p2.getLatitude();
        double lon2 = p2.getLongitude();
        CoordinatesUtil c = new CoordinatesUtil();
        c.createMiddlePoint(lat1, lon1, lat2, lon2);
        String latM = c.getLatM();
        String longM = c.getLongM();

        newPoint.add(pointId, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#lat"), ValueFactoryImpl.getInstance().createLiteral(latM, XMLSchema.DECIMAL));
        newPoint.add(pointId, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#long"), ValueFactoryImpl.getInstance().createLiteral(longM, XMLSchema.DECIMAL));

//        System.out.println("lat1, lon1 " + lat1 +" " + lon1);
//        System.out.println("lat2, lon2 " + lat2 +" " + lon2);
//        System.out.println("latM, lonM " + latM +" " + lonM);
//        
        //-----------------------Middle speed---------------------------------
        //ru.randomUniqueURI(); for resources
        Resource speedId = ru.randomUniqueBNode();
        newPoint.add(pointId, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#hasSpeed"), speedId);

        double s1 = p1.getSpeedValue();
        double s2 = p2.getSpeedValue();
        SpeedUtil s = new SpeedUtil();
        double sM = s.averageSpeed(s1, s2);
        newPoint.add(speedId, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#velocityValue"), ValueFactoryImpl.getInstance().createLiteral(String.valueOf(sM), XMLSchema.DECIMAL));

        Value spM = p1.getSpeedMetric();
        newPoint.add(speedId, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#velocityMetric"), spM);

        //System.out.println("newPoint " +newPoint.toString());
        return newPoint;
    }

    public Model pointBefore(Model p) {
        //http://gis.stackexchange.com/questions/25877/how-to-generate-random-locations-nearby-my-location
        //if point has coordinates
        //if point has label
        return null;
    }

    public Model pointAfter(Model p) {
        //http://gis.stackexchange.com/questions/25877/how-to-generate-random-locations-nearby-my-location
        //if point has coordinates
        //if point has label
        return null;
    }

    @Override
    public Object execute(Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String print() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Model execute(Statement st1, Statement st2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
