/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations;

import static org.hobbit.spatiotemporalbenchmark.data.Generator.getConfigurations;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.spatiotemporalbenchmark.properties.Definitions;

/**
 *
 * @author jsaveta
 */
public class TransformationsCall {

    private ValueBased valuePerc = ValueBased.BLANKCHARSADDITION;
    private TimeStamp timestampPerc = TimeStamp.CHANGETIMESTAMPFORMAT;
    private Coordinates coordinatesPerc = Coordinates.POINTSTOLABEL;
    private AddRemovePoints addDeletePerc = AddRemovePoints.REMOVEPOINTS;
    private ValueSource valueSourcePerc = ValueSource.POINTSTOLABEL;
    private KeepPoints keepPointsPerc = KeepPoints.KEEP;
    private boolean keepPoint;
    private Transformation sourceTransformation;
    private Transformation valueTransformation;
    private Transformation pointTransformation;
    private Transformation transformation;

    public TransformationsCall() {
    }

   
    private static enum ValueBased {
        BLANKCHARSADDITION, BLANKCHARSDELETION, RANDOMCHARSADDITION, RANDOMCHARSDELETION, RANDOMCHARSMODIFIER, TOKENADDITION,
        TOKENDELETION, TOKENSHUFFLE, NAMESTYLEABBREVIATION, NOTRANSFORMATION
    }

    private static enum TimeStamp {
        CHANGETIMESTAMPFORMAT, NOTRANSFORMATION
    }

    private static enum Coordinates {
        CHANGECOORDINATES, POINTSTOLABEL, NOTRANSFORMATION
    }

    private static enum AddRemovePoints {
        ADDPOINTS, REMOVEPOINTS, NOTRANSFORMATION
    }

    private static enum ValueSource {
        POINTSTOLABEL, NOTRANSFORMATIONS
    }

    private static enum KeepPoints {
        KEEP, REMOVE
    }

    


    private void initializeValueBasedEntity() {
        try {
            switch (Definitions.valueBasedAllocation.getAllocation()) {
                case 0:
                    this.valuePerc = ValueBased.BLANKCHARSADDITION;
                    break;
                case 1:
                    this.valuePerc = ValueBased.BLANKCHARSDELETION;
                    break;
                case 2:
                    this.valuePerc = ValueBased.RANDOMCHARSADDITION;
                    break;
                case 3:
                    this.valuePerc = ValueBased.RANDOMCHARSDELETION;
                    break;
                case 4:
                    this.valuePerc = ValueBased.RANDOMCHARSMODIFIER;
                    break;
                case 5:
                    this.valuePerc = ValueBased.TOKENADDITION;
                    break;
                case 6:
                    this.valuePerc = ValueBased.TOKENDELETION;
                    break;
                case 7:
                    this.valuePerc = ValueBased.TOKENSHUFFLE;
                    break;
                case 8:
                    this.valuePerc = ValueBased.NAMESTYLEABBREVIATION;
                    break;
                case 9:
                    this.valuePerc = ValueBased.NOTRANSFORMATION;
                    break;

            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check value transformation percentages");
        }
    }

    private void initializeTimestampEntity() {
        try {
            switch (Definitions.timestampAllocation.getAllocation()) {
                case 0:
                    this.timestampPerc = TimeStamp.CHANGETIMESTAMPFORMAT;
                    break;
                case 1:
                    this.timestampPerc = TimeStamp.NOTRANSFORMATION;
                    break;

            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check timestamp transformation percentages");
        }
    }

    private void initializeCoordinatesEntity() {
        try {
            switch (Definitions.coordinatesAllocation.getAllocation()) {
                case 0:
                    this.coordinatesPerc = Coordinates.CHANGECOORDINATES;
                    break;
                case 1:
                    this.coordinatesPerc = Coordinates.POINTSTOLABEL;
                    break;
                case 2:
                    this.coordinatesPerc = Coordinates.NOTRANSFORMATION;
                    break;

            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check coordinates transformation percentages");
        }
    }

    private void initializeAddDeletePointsEntity() {
        try {
            switch (Definitions.addRemovePointAllocation.getAllocation()) {
                case 0:
                    this.addDeletePerc = AddRemovePoints.ADDPOINTS;
                    break;
                case 1:
                    this.addDeletePerc = AddRemovePoints.REMOVEPOINTS;
                    break;
                case 2:
                    this.addDeletePerc = AddRemovePoints.NOTRANSFORMATION;
                    break;

            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check point addition/deletion transformation percentages");
        }
    }

    private void initializeValueSourceEntity() {
        try {
            switch (Definitions.valueSourceAllocation.getAllocation()) {
                case 0:
                    this.valueSourcePerc = ValueSource.POINTSTOLABEL;
                    break;
                case 1:
                    this.valueSourcePerc = ValueSource.NOTRANSFORMATIONS;
                    break;

            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check transformation percentages");
        }
    }

    private void initializeKeepPointsEntity() {
        try {
            switch (Definitions.keepPointsAllocation.getAllocation()) {
                case 0:
                    this.keepPointsPerc = KeepPoints.KEEP;
                    break;
                case 1:
                    this.keepPointsPerc = KeepPoints.REMOVE;
                    break;

            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check transformation percentages");
        }
    }
    

    public void valueBasedCases() {
        initializeValueBasedEntity();

        valueTransformation = null;
        switch (valuePerc) {
            case BLANKCHARSADDITION:
                valueTransformation = TransformationsConfiguration.addRANDOMBLANKS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case BLANKCHARSDELETION:
                valueTransformation = TransformationsConfiguration.deleteRANDOMBLANKS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case RANDOMCHARSADDITION:
                valueTransformation = TransformationsConfiguration.addRANDOMCHARS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case RANDOMCHARSDELETION:
                valueTransformation = TransformationsConfiguration.deleteRANDOMCHARS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case RANDOMCHARSMODIFIER:
                valueTransformation = TransformationsConfiguration.substituteRANDOMCHARS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case TOKENADDITION:
                valueTransformation = TransformationsConfiguration.addTOKENS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case TOKENDELETION:
                valueTransformation = TransformationsConfiguration.deleteTOKENS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case TOKENSHUFFLE:
                valueTransformation = TransformationsConfiguration.shuffleTOKENS(getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case NAMESTYLEABBREVIATION:
                valueTransformation = TransformationsConfiguration.abbreviateNAME(); //NDOTS = 0; SCOMMANDOT = 1; ALLDOTS = 2;
                break;
            case NOTRANSFORMATION:
                break;

        }
    }

    public void TimestampCases() {
        initializeTimestampEntity();

        valueTransformation = null;
        switch (timestampPerc) {
            case CHANGETIMESTAMPFORMAT:
                valueTransformation = TransformationsConfiguration.dateFORMAT(getConfigurations().getString(Configurations.DATE_FORMAT)/*,Main.getConfigurations().getInt(Configurations.NEW_DATE_TIME_FORMAT)*/); //SHORT = 3 /MEDIUM = 2 /LONG = 1 /FULL = 0 ...				
                break;
            case NOTRANSFORMATION:
                break;
        }
    }

    public void CoordinateCases() {
        initializeCoordinatesEntity();

        valueTransformation = null;
        switch (coordinatesPerc) {
            case CHANGECOORDINATES:
                valueTransformation = TransformationsConfiguration.coordinatesCHANGE();
                break;
            case POINTSTOLABEL:
                valueTransformation = TransformationsConfiguration.coordinatesTOLABEL();
                break;
            case NOTRANSFORMATION:
                break;
        }
    }

    public void AddDeletePointsCases() {
        initializeAddDeletePointsEntity();

        pointTransformation = null;
        switch (addDeletePerc) {
            case ADDPOINTS:
                pointTransformation = TransformationsConfiguration.addPOINT();
                break;
            case REMOVEPOINTS:
                pointTransformation = TransformationsConfiguration.deletePOINT();
                break;
            case NOTRANSFORMATION:
                break;
        }
    }

    public void valueSourceCases() {
        initializeValueSourceEntity();

        sourceTransformation = null;
//        System.out.println("valueSourcePerc " +valueSourcePerc);
        switch (valueSourcePerc) {
            case POINTSTOLABEL:
                sourceTransformation = TransformationsConfiguration.coordinatesTOLABEL();
                break;
            case NOTRANSFORMATIONS:
                break;
        }
    }

    public void keepPointCases() {
        initializeKeepPointsEntity();
        switch (keepPointsPerc) {
            case KEEP:
                keepPoint = true;
                break;
            case REMOVE:
                keepPoint = false;
                break;
        }
    }

    public Transformation getSourceTransformationConfigurations() {
        return sourceTransformation;
    }

    public Transformation getValueTransformationConfiguration() {
        return valueTransformation;
    }

    
    public Transformation getAdditionDeletionPointConfiguration() {
        return pointTransformation;
    }

    public Transformation getTransformationConfiguration() {
        return transformation;
    }

    public boolean getKeepPoint() {
        return keepPoint;
    }

}