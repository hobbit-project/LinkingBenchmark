/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark;

import java.util.ArrayList;
import org.openrdf.model.Model;

/**
 *
 * @author jsaveta
 */
public class Trace {

    public ArrayList<Model> pointsOfTrace;

    public Trace() {
        this.pointsOfTrace = new ArrayList<Model>();
    }

    //getter
    public ArrayList<Model> getPointsOfTrace() {
        return this.pointsOfTrace;
    }

    //setter
    public void setPointsOfTrace(ArrayList<Model> p) {
        this.pointsOfTrace = p;
    }

    public void addPointsOfTrace(Model p) {
        this.pointsOfTrace.add(p);
    }

    public void addPointsOfTrace(int index, Model p) {
        this.pointsOfTrace.add(index, p);
    }
    
    public int size() {
        return this.pointsOfTrace.size();
    }
}
