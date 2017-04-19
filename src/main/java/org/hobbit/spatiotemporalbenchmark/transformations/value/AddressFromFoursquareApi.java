/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations.value;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import static org.hobbit.spatiotemporalbenchmark.transformations.PointsAddressesCache.f;
import static org.hobbit.spatiotemporalbenchmark.transformations.PointsAddressesCache.pointAddressMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jsaveta
 */
public class AddressFromFoursquareApi {

    public static String getAddress(double latitude, double longitude) {
        String strAddress = "";
        String point = latitude + "," + longitude;

        String url = "https://api.foursquare.com/v2/venues/search?ll=" + latitude + "," + longitude + "&oauth_token=5TJR4WQZSOW0ZWTE4ENMXKO3Y415252GITEMRPQIVPMEGCYK&v=20120723&limit=1&accept-language=en";

        HttpPost httppost = new HttpPost(url);
        HttpClient httpclient = new DefaultHttpClient();
        String result = "";
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntityGet = response.getEntity();
            if (resEntityGet != null) {
                result = EntityUtils.toString(resEntityGet);
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
        try {
            /*Keeps the first address form the results*/
            if (result != null) {
                JSONObject json = new JSONObject(result);
                JSONObject jo = json.getJSONObject("response");
                JSONArray ja = jo.getJSONArray("venues");
                for (int i = 0; i < 1; i++) {
                    strAddress = ja.getJSONObject(i).getJSONObject("location").getJSONArray("formattedAddress").toString();
                    strAddress = strAddress.replace("},{", ",").replace("[", "").replace("]", "").replace("\"", "");
                }
            }
        } catch (Exception e) {
        }
        try {
            if (!strAddress.equals("")) {
                pointAddressMap.put(point, strAddress);
                FileUtils.writeStringToFile(f, point + "=" + strAddress + "\n", true);
            }
        } catch (IOException ex) {
            Logger.getLogger(CoordinatesToAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strAddress;
    }
}
