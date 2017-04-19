/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark;

import java.util.Iterator;
import org.hobbit.spatiotemporalbenchmark.util.RandomUtil;
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
public class Point {

    private Resource id;
    private double longitude; //double
    private double latitude; //double
    private String label;
    private String timeStamp;
    private double speedValue;
    private Value speedMetric;
    private RandomUtil ru = new RandomUtil();

    public Point(Model p) {

        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.label = label;
        this.timeStamp = timeStamp;
        this.speedValue = speedValue;
        this.speedMetric = speedMetric;

        Iterator<Statement> it = p.iterator();
        while (it.hasNext()) {
            Statement st = it.next();
            Resource subject;
            URI predicate;
            Value object;

            switch (st.getPredicate().stringValue()) {
                case "http://www.tomtom.com/ontologies/traces#hasPoint":
                    subject = st.getSubject();
                    setId(subject);
                    break;
                case "http://www.tomtom.com/ontologies/traces#lat":
                    double lat = Double.parseDouble(st.getObject().stringValue());
                    setLatitude(lat);
                    break;
                case "http://www.tomtom.com/ontologies/traces#long":
                    double lon = Double.parseDouble(st.getObject().stringValue());
                    setLongitude(lon);
                    break;
                case "http://www.tomtom.com/ontologies/traces#label":
                    String label = st.getObject().stringValue();
                    setLabel(label);
                    break;
                case "http://www.tomtom.com/ontologies/traces#hasTimestamp":
                    String d = st.getObject().stringValue();
                    setTimeStamp(d);
                    break;
                case "http://www.tomtom.com/ontologies/traces#velocityValue":
                    double v = Double.parseDouble(st.getObject().stringValue());
                    setSpeedValue(v);
                    break;
                case "http://www.tomtom.com/ontologies/traces#velocityMetric":
                    Value m = st.getObject();
                    setSpeedMetric(m);
                    break;
                default:
                    break;
            }
        }

    }

    public Point() {

    }

    public Model createPoint(Resource id, String time, String lat, String lon, String spVal, Value spM) {
        Model point = new LinkedHashModel();
        Resource p = ru.randomUniqueBNode();
        Resource s = ru.randomUniqueBNode();

        point.add(id, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#hasPoint"), p);
        point.add(p, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#hasTimestamp"), ValueFactoryImpl.getInstance().createLiteral(time, XMLSchema.DATETIME));
        point.add(p, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#lat"), ValueFactoryImpl.getInstance().createLiteral(lat, XMLSchema.DECIMAL));
        point.add(p, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#long"), ValueFactoryImpl.getInstance().createLiteral(lon, XMLSchema.DECIMAL));
        point.add(p, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#hasSpeed"), s);
        point.add(s, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#velocityValue"), ValueFactoryImpl.getInstance().createLiteral(spVal, XMLSchema.DECIMAL));
        point.add(s, ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#velocityMetric"), spM);
        return point;
    }

    //getters
    public Resource getId() {
        return this.id;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public String getLabel() {
        return this.label;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public double getSpeedValue() {
        return this.speedValue;
    }

    public Value getSpeedMetric() {
        return this.speedMetric;
    }

    //setters
    public void setId(Resource i) {
        this.id = i;
    }

    public void setLongitude(double l) {
        this.longitude = l;
    }

    public void setLatitude(double l) {
        this.latitude = l;
    }

    public void setLabel(String l) {
        this.label = l;
    }

    public void setTimeStamp(String t) {
        this.timeStamp = t;
    }

    public void setSpeedValue(double v) {
        this.speedValue = v;
    }

    public void setSpeedMetric(Value m) {
        this.speedMetric = m;
    }

}
