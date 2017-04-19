package org.hobbit.spatiotemporalbenchmark.transformations.value;

import java.util.Random;
import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.hobbit.spatiotemporalbenchmark.transformations.InvalidTransformation;
import org.hobbit.spatiotemporalbenchmark.util.RandomUtil;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;

/**
 * @author Alfio Ferrara, Universita` degli Studi di Milano
 * @date 19/mag/2010
 */
public class TokenAddition implements DataValueTransformation {

    private String splitter;
    private double severity;

    public TokenAddition(double severity) {
        this.splitter = String.valueOf(RandomUtil.pickChar());
        this.severity = severity;
    }

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
        String f = (String) arg;
        if (arg instanceof String) {
            String[] tokens = f.split(this.splitter);
            f = "";
            if (tokens.length > 1) {
                Random coin = new Random();
                for (int i = 0; i < tokens.length; i++) {
                    if (coin.nextDouble() > this.severity) {
                        f += tokens[i] + this.splitter;
                    } else {
                        String toadd = "";
                        for (int k = 0; k < tokens[i].length(); k++) {
                            toadd += RandomUtil.pickChar();
                        }
                        f += toadd + this.splitter + tokens[i] + this.splitter;
                    }
                }
            } else {
                f = tokens[0] + this.splitter;
            }
            if (f.length() > this.splitter.length()) {
                f = f.substring(0, f.length() - this.splitter.length());
            }
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
