/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hobbit.spatiotemporalbenchmark.main.Main.configurations;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;

/**
 *
 * @author jsaveta
 */
public class TimeUtil {

    public TimeUtil() {
    }
//a 2010-02-26T01:39:53.000000
//b 2010-02-26T01:39:55.000000
//midpoint 2010-02-26T01:39:54.000000
    public String averageDateTime(String a, String b) {
        String midpointStr = null;
        try {
            DateFormat formatter = new SimpleDateFormat(configurations.getString(Configurations.DATE_FORMAT));
            Date dateA = formatter.parse(a);
            Date dateB = formatter.parse(b);

            Date midpoint = new Date((dateA.getTime() + dateB.getTime()) / 2);
            midpointStr = formatter.format(midpoint);

        } catch (ParseException ex) {
            Logger.getLogger(TimeUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return midpointStr;
    }
}
