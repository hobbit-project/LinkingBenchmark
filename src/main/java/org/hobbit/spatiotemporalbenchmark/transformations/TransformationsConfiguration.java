/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations;

import org.hobbit.spatiotemporalbenchmark.transformations.value.AddPoint;
import org.hobbit.spatiotemporalbenchmark.transformations.value.BlankCharsAddition;
import org.hobbit.spatiotemporalbenchmark.transformations.value.BlankCharsDeletion;
import org.hobbit.spatiotemporalbenchmark.transformations.value.ChangeCoordinatesFormat;
import org.hobbit.spatiotemporalbenchmark.transformations.value.ChangeDateFormat;
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
public class TransformationsConfiguration {

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

}
