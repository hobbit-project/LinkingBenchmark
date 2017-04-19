/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations.value;

import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.openrdf.model.Model;
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
public class ChangeCoordinatesFormat implements DataValueTransformation {

    private int format;
    private Statement st1;
    private Statement st2;
    public static int DMS = 0; // Degrees, Minutes, Seconds
    public static int UTM = 1; //Universal Transverse Mercator (UTM)
    public static int MGRUTM = 2; //Military grid reference system

    public ChangeCoordinatesFormat() {

    }

    @Override
    public Model execute(Statement st1, Statement st2) {
        this.st1 = st1;
        this.st2 = st2;
        double latitude = Double.parseDouble(st1.getObject().stringValue());
        double longitude = Double.parseDouble(st2.getObject().stringValue());

        Model model = (Model) execute(latitude, longitude);
        return model;
    }

    public Object execute(double latitude, double longitude) {
        Model model = new LinkedHashModel();

        this.format = (int) ((Math.random() * 3));
        String f = null;//auto mporei na allaksei vasika

        if (this.format == DMS) { //DMSlat< > DMSlong
            f = getFormattedLocationInDegree(latitude, longitude);
            URI dmslat = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#DMSlat");
            URI dmslong = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#DMSlong");

            String[] a = f.split(" ");
            Value lat = ValueFactoryImpl.getInstance().createLiteral(a[0], XMLSchema.STRING);
            Value lon = ValueFactoryImpl.getInstance().createLiteral(a[1], XMLSchema.STRING);

            model.add(st1.getSubject(), dmslat, lat, st1.getContext());
            model.add(st2.getSubject(), dmslong, lon, st2.getContext());

        } else if (this.format == UTM) { //UTM< >Zone< >UTMEasting< >UTMNorthing
            CoordinateConversion convert = new CoordinateConversion();
            f = convert.latLon2UTM(latitude, longitude);

            URI UTMZone = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#UTMZone");
            URI UTMEasting = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#UTMEasting");
            URI UTMNorthing = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#UTMNorthing");

            String[] a = f.split(" ");
            Value zone = ValueFactoryImpl.getInstance().createLiteral(a[0] + " " + a[1], XMLSchema.STRING);
            Value easting = ValueFactoryImpl.getInstance().createLiteral(a[2], XMLSchema.DECIMAL);
            Value northing = ValueFactoryImpl.getInstance().createLiteral(a[3], XMLSchema.DECIMAL);

            model.add(st1.getSubject(), UTMZone, zone, st1.getContext());
            model.add(st1.getSubject(), UTMEasting, easting, st1.getContext());
            model.add(st1.getSubject(), UTMNorthing, northing, st1.getContext());

        } else if (this.format == MGRUTM) {
            CoordinateConversion convert = new CoordinateConversion();
            f = convert.latLon2MGRUTM(latitude, longitude);
            URI MGRUTM = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#MGRUTM");
            Value mgrutm = ValueFactoryImpl.getInstance().createLiteral(f, XMLSchema.STRING);

            model.add(st1.getSubject(), MGRUTM, mgrutm, st1.getContext());

        }

        return model;
    }

    public String getFormattedLocationInDegree(double latitude, double longitude) {
        try {
            int latSeconds = (int) Math.round(latitude * 3600);
            int latDegrees = latSeconds / 3600;
            latSeconds = Math.abs(latSeconds % 3600);
            int latMinutes = latSeconds / 60;
            latSeconds %= 60;

            int longSeconds = (int) Math.round(longitude * 3600);
            int longDegrees = longSeconds / 3600;
            longSeconds = Math.abs(longSeconds % 3600);
            int longMinutes = longSeconds / 60;
            longSeconds %= 60;
            String latDegree = latDegrees >= 0 ? "N" : "S";
            String lonDegrees = longDegrees >= 0 ? "E" : "W";

            return Math.abs(latDegrees) + "°" + latMinutes + "'" + latSeconds
                    + "\"" + latDegree + " " + Math.abs(longDegrees) + "°" + longMinutes
                    + "'" + longSeconds + "\"" + lonDegrees;
        } catch (Exception e) {
            return "" + String.format("%8.5f", latitude) + "  "
                    + String.format("%8.5f", longitude);
        }
    }

    @Override
    public Object execute(Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String print() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    //48.900400 2.362850
//    public static void main(String arg[]) {
//        //double latitude,longitude; 
//        ChangeCoordinatesFormat ga = new ChangeCoordinatesFormat();
//        System.out.println(ga.getFormattedLocationInDegree(48.900400, 2.362850));
//
//    }

}
