/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hobbit.spatiotemporalbenchmark.transformations.value.CoordinatesToAddress;

/**
 *
 * @author jsaveta
 */
public class PointsAddressesCache {

    public static File f;
    public static Map<String, String> pointAddressMap;

    public PointsAddressesCache() {
        System.out.println("LoadPointsAddressesFile");
        try {
            this.f = new File("PointsAddressesMap.txt");
            if (!f.exists()) {
                f.createNewFile();
                pointAddressMap = new HashMap<String, String>();
            } else {
                //fill map
                pointAddressMap = new HashMap<String, String>();
                BufferedReader in = new BufferedReader(new FileReader("PointsAddressesMap.txt"));
                String line = "";
                while ((line = in.readLine()) != null) {
                    String parts[] = line.split("=");
                    pointAddressMap.put(parts[0], parts[1]);
                }
                in.close();
            }

            new FileOutputStream("PointsAddressesMap.txt", true).close();
        } catch (IOException ex) {
            Logger.getLogger(CoordinatesToAddress.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
