package org.hobbit.spatiotemporalbenchmark.transformations.value;

import java.util.Random;
import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.hobbit.spatiotemporalbenchmark.transformations.InvalidTransformation;
import org.hobbit.spatiotemporalbenchmark.util.RandomUtil;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;

/**
 * @author Alfio Ferrara, Universita` degli Studi di Milano
 * @date 18/mag/2010 Changes a random number of chars in a string (num of chars
 * modified depends on severity)
 */
public class RandomCharsModifier implements DataValueTransformation {

    public RandomCharsModifier(double severity) {
        this.severity = severity;
    }

    private double severity = 0.0;

    public String print() {
        String name = this.getClass().toString().substring(this.getClass().toString().lastIndexOf(".") + 1);
        return name + "\t" + this.severity;
    }

    /* (non-Javadoc)
	 * @see it.unimi.dico.islab.iimb.transfom.Transformation#execute(java.lang.Object)
     */
    @SuppressWarnings("finally")
    @Override
    public Object execute(Object arg) {
        String f = arg.toString();
        if (arg instanceof String) {
            //Do the job
            Random coin = new Random();
            String buffer = "";
            for (char c : f.toCharArray()) {
                if (coin.nextDouble() <= this.severity) {
                    buffer += RandomUtil.pickChar();
                } else {
                    buffer += c;
                }
            }
            f = buffer;
        } else {
            try {
                throw new InvalidTransformation();
            } catch (InvalidTransformation e) {
                e.printStackTrace();
            } finally {
                return arg;
            }
        }
        return f;
    }

    @Override
    public Model execute(Statement st1, Statement st2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
