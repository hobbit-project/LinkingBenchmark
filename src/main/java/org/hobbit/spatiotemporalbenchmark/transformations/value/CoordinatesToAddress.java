package org.hobbit.spatiotemporalbenchmark.transformations.value;

import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import static org.hobbit.spatiotemporalbenchmark.transformations.PointsAddressesCache.pointAddressMap;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 *
 * @author jsaveta view this: google-maps-services-java
 *
 * Be careful { "error_message" : "You have exceeded your daily request quota
 * for this API. We recommend registering for a key at the Google Developers
 * Console:
 * https://console.developers.google.com/apis/credentials?project=AIzaSyAuovHeAGdnBfX8b9GaSSXAKsqxGJCb76w_",
 * "results" : [], "status" : "OVER_QUERY_LIMIT" } Google is limiting the number
 * of requests to the Geocoding API to 2,500 requests per day and second
 *
 * Nominatin
 *
 * Foursquare
 */
public class CoordinatesToAddress implements DataValueTransformation {

    private static boolean googleMapsApiLimit = false;
    private static boolean foursquareApiLimit = false;
    private static boolean nominatimLimit = false;

    public CoordinatesToAddress() {
    }

    public String getAddress(double latitude, double longitude) {
        String strAddress = "";
        String point = latitude + "," + longitude;
        if (pointAddressMap.containsKey(point)) {
            strAddress = pointAddressMap.get(point);
        } else if (!googleMapsApiLimit) {
            strAddress = AddressFromGoogleMapsApi.getAddress(latitude, longitude);
            if (strAddress.equals("")) {
                googleMapsApiLimit = true;
            }
        }       
        else if (!foursquareApiLimit) {
            strAddress = AddressFromFoursquareApi.getAddress(latitude, longitude);
            if (strAddress.equals("")) {
                foursquareApiLimit = true;
            }
        } else if (!nominatimLimit) {
            strAddress = AddressFromNominatim.getAddress(latitude, longitude);
            if (strAddress.equals("")) {
                nominatimLimit = true;
            }
        }
        return strAddress;
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
        double latitude = Double.parseDouble(st1.getObject().stringValue());
        double longitude = Double.parseDouble(st2.getObject().stringValue());
        String address = getAddress(latitude, longitude);
        Model model = null;
        if (!address.equals("")) {
            model = new LinkedHashModel();
            Value object = ValueFactoryImpl.getInstance().createLiteral(address);
            URI predicate = ValueFactoryImpl.getInstance().createURI("http://www.tomtom.com/ontologies/traces#label");
            model.add(st1.getSubject(), predicate, object, st1.getContext());
        }
        return model;
    }

//    public static void main(String arg[]) {
//
//        //double latitude,longitude; 
//        CoordinatesToAddress ga = new CoordinatesToAddress();
//
//        String str = ga.getAddress(10.11674, 48.45084);
//        System.out.println("1. " + str);
//    }

}
