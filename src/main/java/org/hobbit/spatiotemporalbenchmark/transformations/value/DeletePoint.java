/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations.value;

import org.hobbit.spatiotemporalbenchmark.transformations.DataValueTransformation;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;

/**
 *
 * @author jsaveta
 */
public class DeletePoint implements DataValueTransformation {

    public DeletePoint() {
    }

    @Override
    public Object execute(Object arg) {
        //on deletion of a point, be sure to delete the speed also
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
