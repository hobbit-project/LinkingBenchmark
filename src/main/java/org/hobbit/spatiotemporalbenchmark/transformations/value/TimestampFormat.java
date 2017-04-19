/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations.value;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;

/**
 *
 * @author jsaveta This class changes <UTC unix time stamp [ms]> format to ..
 */
public class TimestampFormat implements DataValueTransformation {

    private int format; //SHORT = 3 /MEDIUM = 2 /LONG = 1 /FULL = 0 ...
    //private DateFormat sourceFormat;

    //the timestamp from TomTom is already in ms, if there wasn't he had to * 1000
    public TimestampFormat(int format) {
        this.format = format;

    }

    @Override
    public Object execute(Object arg) {

        Locale.setDefault(Locale.ENGLISH);
        String f = arg.toString();
        Date date = new Date();
        date.setTime(Long.parseLong(f));
        DateFormat df = DateFormat.getDateTimeInstance(format, format);
        f = df.format(date);
        return f;
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
