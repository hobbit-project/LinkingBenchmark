/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations;

import org.hobbit.spatiotemporalbenchmark.transformations.topology.CONTAINS;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.COVERED_BY;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.CROSSES;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.DISJOINT;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.EQUALS_SP;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.INTERSECTS;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.OVERLAPS_SP;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.TOUCHES;
import org.hobbit.spatiotemporalbenchmark.transformations.topology.WITHIN;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.BEFORE;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.BEFOREinverse;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.DURING;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.DURINGinverse;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.EQUALS_TEMP;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.FINISHES;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.FINISHESinverse;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.MEETS;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.MEETSinverse;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.OVERLAPS_TEMP;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.OVERLAPSinverse_TEMP;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.STARTS;
import org.hobbit.spatiotemporalbenchmark.transformations.temporal.STARTSinverse;
import org.hobbit.spatiotemporalbenchmark.transformations.value.AddPoint;
import org.hobbit.spatiotemporalbenchmark.transformations.value.BlankCharsAddition;
import org.hobbit.spatiotemporalbenchmark.transformations.value.BlankCharsDeletion;
import org.hobbit.spatiotemporalbenchmark.transformations.value.ChangeCoordinatesFormat;
import org.hobbit.spatiotemporalbenchmark.transformations.value.ChangeDateFormat;
import org.hobbit.spatiotemporalbenchmark.transformations.value.ChangeLanguage;
import org.hobbit.spatiotemporalbenchmark.transformations.value.CoordinatesToAddress;
import org.hobbit.spatiotemporalbenchmark.transformations.value.DeletePoint;
import org.hobbit.spatiotemporalbenchmark.transformations.value.NameStyleAbbreviation;
import org.hobbit.spatiotemporalbenchmark.transformations.value.RandomCharsAddition;
import org.hobbit.spatiotemporalbenchmark.transformations.value.RandomCharsDeletion;
import org.hobbit.spatiotemporalbenchmark.transformations.value.RandomCharsModifier;
import org.hobbit.spatiotemporalbenchmark.transformations.value.TimestampFormat;
import org.hobbit.spatiotemporalbenchmark.transformations.value.TokenAddition;
import org.hobbit.spatiotemporalbenchmark.transformations.value.TokenDeletion;
import org.hobbit.spatiotemporalbenchmark.transformations.value.TokenShuffle;
import org.hobbit.spatiotemporalbenchmark.transformations.value.VALUE;

/**
 *
 * @author jsaveta
 */
public class RelationsConfiguration {

    //value transformations
    public static VALUE value() {
        return new VALUE();
    }

    public static BlankCharsAddition addRANDOMBLANKS(double severity) {
        return new BlankCharsAddition(severity);
    }

    public static BlankCharsDeletion deleteRANDOMBLANKS(double severity) {
        return new BlankCharsDeletion(severity);
    }

    public static RandomCharsDeletion deleteRANDOMCHARS(double severity) {
        return new RandomCharsDeletion(severity);
    }

    public static RandomCharsAddition addRANDOMCHARS(double severity) {
        return new RandomCharsAddition(severity);
    }

    public static RandomCharsModifier substituteRANDOMCHARS(double severity) {
        return new RandomCharsModifier(severity);
    }

    public static TokenAddition addTOKENS(double severity) {
        return new TokenAddition(severity);
    }

    public static TokenDeletion deleteTOKENS(double severity) {
        return new TokenDeletion(severity);
    }

    public static TokenShuffle shuffleTOKENS(double severity) {
        return new TokenShuffle(severity);
    }

    public static NameStyleAbbreviation abbreviateNAME() {
        return new NameStyleAbbreviation();
    }

//    public static ChangeLanguage changeLANGUAGE() {
//        return new ChangeLanguage();
//    }
    public static TimestampFormat timestampFORMAT(String sourceFormat, int format) {
        return new TimestampFormat(format);
    }

    public static ChangeDateFormat dateFORMAT(String sourceFormat) {
        return new ChangeDateFormat(sourceFormat);
    }

    public static ChangeCoordinatesFormat coordinatesCHANGE() {
        return new ChangeCoordinatesFormat();
    }

    public static CoordinatesToAddress coordinatesTOLABEL() {
        return new CoordinatesToAddress();
    }

    public static AddPoint addPOINT() {
        return new AddPoint();
    }

    public static DeletePoint deletePOINT() {
        return new DeletePoint();
    }

    //SPATIAL RELATIONS
    public static CONTAINS contains() {
        return new CONTAINS();
    }

    public static COVERED_BY coveredBy() {
        return new COVERED_BY();
    }

    public static CROSSES crosses() {
        return new CROSSES();
    }

    public static DISJOINT disjoint() {
        return new DISJOINT();
    }

    public static EQUALS_SP equalsSP() {
        return new EQUALS_SP();
    }

    public static INTERSECTS intersects() {
        return new INTERSECTS();
    }

    public static OVERLAPS_SP overlapsSP() {
        return new OVERLAPS_SP();
    }

    public static TOUCHES touches() {
        return new TOUCHES();
    }

    public static WITHIN within() {
        return new WITHIN();
    }

    //TEMPORAL RELATIONS
    public static BEFORE before() {
        return new BEFORE();
    }

    public static BEFOREinverse beforeINV() {
        return new BEFOREinverse();
    }

    public static DURING during() {
        return new DURING();
    }

    public static DURINGinverse duringINV() {
        return new DURINGinverse();
    }

    public static EQUALS_TEMP equalsTEMP() {
        return new EQUALS_TEMP();
    }

    public static FINISHES finishes() {
        return new FINISHES();
    }

    public static FINISHESinverse finishesINV() {
        return new FINISHESinverse();
    }

    public static MEETS meets() {
        return new MEETS();
    }

    public static MEETSinverse meetsINV() {
        return new MEETSinverse();
    }

    public static OVERLAPS_TEMP overlapsTEMP() {
        return new OVERLAPS_TEMP();
    }

    public static OVERLAPSinverse_TEMP overlapsTEMP_INV() {
        return new OVERLAPSinverse_TEMP();
    }

    public static STARTS starts() {
        return new STARTS();
    }

    public static STARTSinverse startsINV() {
        return new STARTSinverse();
    }

}
