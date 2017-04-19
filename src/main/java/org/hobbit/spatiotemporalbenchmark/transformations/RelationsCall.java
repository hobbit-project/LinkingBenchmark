/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hobbit.spatiotemporalbenchmark.transformations;

import java.util.ArrayList;
import org.hobbit.spatiotemporalbenchmark.main.Main;
import org.hobbit.spatiotemporalbenchmark.properties.Configurations;
import org.hobbit.spatiotemporalbenchmark.properties.Definitions;

/**
 *
 * @author jsaveta
 */
public class RelationsCall {

    private EqualAndRelation equalAndRelationPerc = EqualAndRelation.VALUE;
    private spatialRelation spatialRelationsPerc = spatialRelation.CONTAINS;
    private temporalRelation temporalRelationsPerc = temporalRelation.BEFORE;
    private ValueBased valuePerc = ValueBased.BLANKCHARSADDITION;
    private TimeStamp timestampPerc = TimeStamp.CHANGETIMESTAMPFORMAT;
    private Coordinates coordinatesPerc = Coordinates.POINTSTOLABEL;
    private AddRemovePoints addDeletePerc = AddRemovePoints.REMOVEPOINTS;
    private ValueSource valueSourcePerc = ValueSource.POINTSTOLABEL;
    private KeepPoints keepPointsPerc = KeepPoints.KEEP;
    private boolean keepPoint;
    private Transformation sourceTransformation;
    private Transformation equalTransformation;
    private Transformation spatialRelationTransformation;
    private Transformation temporalRelationTransformation;
    private Transformation pointTransformation;
    private ArrayList<Transformation> equalAndRelationTransformation;

    public RelationsCall() {
    }

    private static enum EqualAndRelation {
        VALUE, SPATIAL_RELATION, TEMPORAL_RELATION, VALUEandSPATIAL_RELATION, NOCHANGES
    }

    private static enum spatialRelation {
        CONTAINS, COVERED_BY, CROSSES, DISJOINT, EQUALS_SP, INTERSECTS, OVERLAPS, TOUCHES, WITHIN
    }

    private static enum temporalRelation {
        BEFORE, BEFOREinv, DURING, DURINGinv, EQUALS_TEMP, FINISHES, FINISHESinv, MEETS, MEETSinv, OVERLAPS, OVERLAPSinv, STARTS, STARTSinv
    }

    private static enum ValueBased {
        BLANKCHARSADDITION, BLANKCHARSDELETION, RANDOMCHARSADDITION, RANDOMCHARSDELETION, RANDOMCHARSMODIFIER, TOKENADDITION,
        TOKENDELETION, TOKENSHUFFLE, NAMESTYLEABBREVIATION, CHANGELANGUAGE, NOTRANSFORMATION
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

    private void initializeEqualAndRelationEntity() {
        try {

            switch (Definitions.equalAndRelationAllocation.getAllocation()) {
                case 0:
                    this.equalAndRelationPerc = EqualAndRelation.VALUE;
                    break;
                case 1:
                    this.equalAndRelationPerc = EqualAndRelation.SPATIAL_RELATION;
                    break;
                case 2:
                    this.equalAndRelationPerc = EqualAndRelation.TEMPORAL_RELATION;
                    break;
                case 3:
                    this.equalAndRelationPerc = EqualAndRelation.VALUEandSPATIAL_RELATION;
                    break;
                case 4:
                    this.equalAndRelationPerc = EqualAndRelation.NOCHANGES;
                    break;

            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check transformation percentages");
        }
    }

    private void initializeSpatialRelationsEntity() {
        try {
            switch (Definitions.spatialRelationsAllocation.getAllocation()) {
                case 0:
                    this.spatialRelationsPerc = spatialRelation.CONTAINS;
                    break;
                case 1:
                    this.spatialRelationsPerc = spatialRelation.COVERED_BY;
                    break;
                case 2:
                    this.spatialRelationsPerc = spatialRelation.CROSSES;
                    break;
                case 3:
                    this.spatialRelationsPerc = spatialRelation.DISJOINT;
                    break;
                case 4:
                    //TODO mipos na afaireso to equals!?
                    this.spatialRelationsPerc = spatialRelation.EQUALS_SP;
                    break;
                case 5:
                    this.spatialRelationsPerc = spatialRelation.INTERSECTS;
                    break;
                case 6:
                    this.spatialRelationsPerc = spatialRelation.OVERLAPS;
                    break;
                case 7:
                    this.spatialRelationsPerc = spatialRelation.TOUCHES;
                    break;
                case 8:
                    this.spatialRelationsPerc = spatialRelation.WITHIN;
                    break;
            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check spatial transformation percentages");
        }
    }

    private void initializeTemporalRelationsEntity() {
        try {
            switch (Definitions.temporalRelationsAllocation.getAllocation()) {
                case 0:
                    this.temporalRelationsPerc = temporalRelation.BEFORE;
                    break;
                case 1:
                    this.temporalRelationsPerc = temporalRelation.BEFOREinv;
                    break;
                case 2:
                    this.temporalRelationsPerc = temporalRelation.DURING;
                    break;
                case 3:
                    this.temporalRelationsPerc = temporalRelation.DURINGinv;
                    break;
                case 4:
                    //TODO mipos na afaireso to equals!?
                    this.temporalRelationsPerc = temporalRelation.EQUALS_TEMP;
                    break;
                case 5:
                    this.temporalRelationsPerc = temporalRelation.FINISHES;
                    break;
                case 6:
                    this.temporalRelationsPerc = temporalRelation.FINISHESinv;
                    break;
                case 7:
                    this.temporalRelationsPerc = temporalRelation.MEETS;
                    break;
                case 8:
                    this.temporalRelationsPerc = temporalRelation.MEETSinv;
                    break;
                case 9:
                    this.temporalRelationsPerc = temporalRelation.OVERLAPS;
                    break;
                case 10:
                    this.temporalRelationsPerc = temporalRelation.OVERLAPSinv;
                    break;
                case 11:
                    this.temporalRelationsPerc = temporalRelation.STARTS;
                    break;
                case 12:
                    this.temporalRelationsPerc = temporalRelation.STARTSinv;
                    break;
            }
        } catch (IllegalArgumentException iae) {
            System.err.println("Check temporal transformation percentages");
        }
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
                    this.valuePerc = ValueBased.CHANGELANGUAGE;
                    break;
                case 10:
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

    public void equalAndRealtionCases() {
        initializeEqualAndRelationEntity();

        equalAndRelationTransformation = new ArrayList<Transformation>();
        switch (equalAndRelationPerc) {
            case VALUE:
                equalAndRelationTransformation.add(RelationsConfiguration.value());
                break;

            case SPATIAL_RELATION:
                this.spatialRelationsCases();
                equalAndRelationTransformation.add(getSpatialRelationsConfiguration());
                break;

            case TEMPORAL_RELATION:
                this.temporalRelationsCases();
                equalAndRelationTransformation.add(getTemporalRelationsConfiguration());
                break;

            case VALUEandSPATIAL_RELATION:
                this.spatialRelationsCases();
                equalAndRelationTransformation.add(RelationsConfiguration.value());
                equalAndRelationTransformation.add(getSpatialRelationsConfiguration());
                break;

            case NOCHANGES:
                break;

        }
    }

    public void spatialRelationsCases() {
        initializeSpatialRelationsEntity();
        spatialRelationTransformation = null;
        switch (spatialRelationsPerc) {
            case CONTAINS:
                spatialRelationTransformation = RelationsConfiguration.contains();
                break;
            case COVERED_BY:
                spatialRelationTransformation = RelationsConfiguration.coveredBy();
                break;
            case CROSSES:
                spatialRelationTransformation = RelationsConfiguration.crosses();
                break;
            case DISJOINT:
                spatialRelationTransformation = RelationsConfiguration.disjoint();
                break;
            case EQUALS_SP:
                spatialRelationTransformation = RelationsConfiguration.equalsSP();
                break;
            case INTERSECTS:
                spatialRelationTransformation = RelationsConfiguration.intersects();
                break;
            case OVERLAPS:
                spatialRelationTransformation = RelationsConfiguration.overlapsSP();
                break;
            case TOUCHES:
                spatialRelationTransformation = RelationsConfiguration.touches();
                break;
            case WITHIN:
                spatialRelationTransformation = RelationsConfiguration.within();
                break;

        }
    }

    public void temporalRelationsCases() {
        initializeSpatialRelationsEntity();

        temporalRelationTransformation = null;
        switch (temporalRelationsPerc) {

            case BEFORE:
                temporalRelationTransformation = RelationsConfiguration.before();
                break;
            case BEFOREinv:
                temporalRelationTransformation = RelationsConfiguration.beforeINV();
                break;
            case DURING:
                temporalRelationTransformation = RelationsConfiguration.during();
                break;
            case DURINGinv:
                temporalRelationTransformation = RelationsConfiguration.duringINV();
                break;
            case EQUALS_TEMP:
                temporalRelationTransformation = RelationsConfiguration.equalsTEMP();
                break;
            case FINISHES:
                temporalRelationTransformation = RelationsConfiguration.finishes();
                break;
            case FINISHESinv:
                temporalRelationTransformation = RelationsConfiguration.finishesINV();
                break;
            case MEETS:
                temporalRelationTransformation = RelationsConfiguration.meets();
                break;
            case MEETSinv:
                temporalRelationTransformation = RelationsConfiguration.meetsINV();
                break;
            case OVERLAPS:
                temporalRelationTransformation = RelationsConfiguration.overlapsTEMP();
                break;
            case OVERLAPSinv:
                temporalRelationTransformation = RelationsConfiguration.overlapsTEMP_INV();
                break;
            case STARTS:
                temporalRelationTransformation = RelationsConfiguration.starts();
                break;
            case STARTSinv:
                temporalRelationTransformation = RelationsConfiguration.startsINV();
                break;

        }
    }

    public void valueBasedCases() {
        initializeValueBasedEntity();

        equalTransformation = null;
        switch (valuePerc) {
            case BLANKCHARSADDITION:
                equalTransformation = RelationsConfiguration.addRANDOMBLANKS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case BLANKCHARSDELETION:
                equalTransformation = RelationsConfiguration.deleteRANDOMBLANKS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case RANDOMCHARSADDITION:
                equalTransformation = RelationsConfiguration.addRANDOMCHARS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case RANDOMCHARSDELETION:
                equalTransformation = RelationsConfiguration.deleteRANDOMCHARS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case RANDOMCHARSMODIFIER:
                equalTransformation = RelationsConfiguration.substituteRANDOMCHARS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case TOKENADDITION:
                equalTransformation = RelationsConfiguration.addTOKENS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case TOKENDELETION:
                equalTransformation = RelationsConfiguration.deleteTOKENS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case TOKENSHUFFLE:
                equalTransformation = RelationsConfiguration.shuffleTOKENS(Main.getConfigurations().getDouble(Configurations.VALUE_SEVERITY));
                break;
            case NAMESTYLEABBREVIATION:
                equalTransformation = RelationsConfiguration.abbreviateNAME(); //NDOTS = 0; SCOMMANDOT = 1; ALLDOTS = 2;
                break;
            case CHANGELANGUAGE:
                //this app has a limit and is not working.
                //equalTransformation = RelationsConfiguration.changeLANGUAGE();				
                break;
            case NOTRANSFORMATION:
                break;

        }
    }

    public void TimestampCases() {
        initializeTimestampEntity();

        equalTransformation = null;
        switch (timestampPerc) {
            case CHANGETIMESTAMPFORMAT:
                equalTransformation = RelationsConfiguration.dateFORMAT(Main.getConfigurations().getString(Configurations.DATE_FORMAT)/*,Main.getConfigurations().getInt(Configurations.NEW_DATE_TIME_FORMAT)*/); //SHORT = 3 /MEDIUM = 2 /LONG = 1 /FULL = 0 ...				
                break;
            case NOTRANSFORMATION:
                break;
        }
    }

    public void CoordinateCases() {
        initializeCoordinatesEntity();

        equalTransformation = null;
        switch (coordinatesPerc) {
            case CHANGECOORDINATES:
                equalTransformation = RelationsConfiguration.coordinatesCHANGE();
                break;
            case POINTSTOLABEL:
                equalTransformation = RelationsConfiguration.coordinatesTOLABEL();
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
                pointTransformation = RelationsConfiguration.addPOINT();
                break;
            case REMOVEPOINTS:
                pointTransformation = RelationsConfiguration.deletePOINT();
                break;
            case NOTRANSFORMATION:
                break;
        }
    }

    public void valueSourceCases() {
        initializeValueSourceEntity();

        sourceTransformation = null;
        switch (valueSourcePerc) {
            case POINTSTOLABEL:
                sourceTransformation = RelationsConfiguration.coordinatesTOLABEL();
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

    public Transformation getEqualTransformationConfiguration() {
        return equalTransformation;
    }

    public Transformation getSpatialRelationsConfiguration() {
        return spatialRelationTransformation;
    }

    public Transformation getTemporalRelationsConfiguration() {
        return temporalRelationTransformation;
    }

    public Transformation getAdditionDeletionPointConfiguration() {
        return pointTransformation;
    }

    public ArrayList<Transformation> getEqualAndRelationConfiguration() {
        return equalAndRelationTransformation;
    }

    public boolean getKeepPoint() {
        return keepPoint;
    }

}
