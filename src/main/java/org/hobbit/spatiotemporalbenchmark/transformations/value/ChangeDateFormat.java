package org.hobbit.spatiotemporalbenchmark.transformations.value;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;

/**
 * @author Alfio Ferrara, Universita` degli Studi di Milano
 * @date 19/mag/2010
 *
 * This takes a Date as input and returns a string representing the date
 * according to various formats
 *
 */
public class ChangeDateFormat implements DataValueTransformation {

    private int format; //SHORT = 3 /MEDIUM = 2 /LONG = 1 /FULL = 0 ...
    private DateFormat sourceFormat;

    public ChangeDateFormat(String sourceFormat) {
        this.format = (int) ((Math.random() * 4));
        this.sourceFormat = new SimpleDateFormat(sourceFormat, Locale.ENGLISH);
    }

    public String print() {
        String name = this.getClass().toString().substring(this.getClass().toString().lastIndexOf(".") + 1);
        return name + "\t" + format;
    }

    /* (non-Javadoc)
	 * @see it.unimi.dico.islab.iimb.transfom.Transformation#execute(java.lang.Object)
     */
    @SuppressWarnings("finally")
    @Override
    public Object execute(Object arg) {
        Locale.setDefault(Locale.ENGLISH);
        String f = arg.toString();
        try {
            Date d = this.sourceFormat.parse(f);
            DateFormat df = DateFormat.getDateInstance(this.format);
            f = df.format(d);
        } catch (ParseException e) {
        } finally {
            return f;
        }
    }

    @Override
    public Model execute(Statement st1, Statement st2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
