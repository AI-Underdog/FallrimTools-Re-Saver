// filepath: c:\Users\emili\Desktop\Re-SaverUpdated7-27-2025V1.1\src\main\java\resaver\ess\ChangeFormQust.java
package resaver.ess;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import resaver.Analysis;
import static resaver.ess.ChangeFlagConstantsQust.*;

/**
 * Describes a ChangeForm containing a QUST record.
 *
 * @author Mark Fairchild
 */
public class ChangeFormQust extends GeneralElement implements ChangeFormData {

    // Field declarations - change from private to final for better immutability
    private final Flags.Short QUEST_FLAGS;
    private final float SCRIPT_DELAY;
    private final QuestStage[] QUEST_STAGES;
    private final QuestObjective[] QUEST_OBJECTIVES;
    private final QuestRunData QUEST_RUN_DATA;
    private final byte ALREADY_RUN;

    /**
     * Creates a new ChangeFormQust by reading from a ByteBuffer.
     */
    public ChangeFormQust(ByteBuffer input, Flags.Int changeFlags, RefID refid, ESS.ESSContext context) throws ElementException {
        Objects.requireNonNull(input);
        
        // Initialize fields with default values
        Flags.Short questFlags = null;
        float scriptDelay = Float.NaN;
        QuestStage[] questStages = null;
        QuestObjective[] questObjectives = null;
        QuestRunData questRunData = null;
        byte alreadyRun = 0;
        
        try {
            if (changeFlags.getFlag(CHANGE_QUEST_FLAGS)) {
                questFlags = super.readElement(input, "QUEST_FLAGS", Flags::readShortFlags);
            }
            
            if (changeFlags.getFlag(CHANGE_QUEST_SCRIPT_DELAY)) {
                scriptDelay = super.readFloat(input, "SCRIPT_DELAY");
            }
            
            // Read quest stages if present
            if (changeFlags.getFlag(CHANGE_QUEST_STAGES)) {
                int stageCount = super.readInt(input, "STAGE_COUNT");
                questStages = (QuestStage[]) super.readElements(input, "QUEST_STAGES", stageCount, 
                    i -> new QuestStage(i, context));
            }
            
            // Read quest objectives if present
            if (changeFlags.getFlag(CHANGE_QUEST_OBJECTIVES)) {
                int objectiveCount = super.readInt(input, "OBJECTIVE_COUNT");
                questObjectives = (QuestObjective[]) super.readElements(input, "QUEST_OBJECTIVES", objectiveCount, 
                    i -> new QuestObjective(i, context));
            }
            
            // Read quest run data if present
            if (changeFlags.getFlag(CHANGE_QUEST_RUNDATA)) {
                questRunData = new QuestRunData(input, context);
            }
            
            if (changeFlags.getFlag(CHANGE_QUEST_ALREADY_RUN)) {
                alreadyRun = super.readByte(input, "ALREADY_RUN");
            }
            
        } catch (RuntimeException ex) {
            super.readUnparsed(input);
            throw new ElementException("Error reading QUST", ex, this);
        } finally {
            // Assign to final fields
            this.QUEST_FLAGS = questFlags;
            this.SCRIPT_DELAY = scriptDelay;
            this.QUEST_STAGES = questStages;
            this.QUEST_OBJECTIVES = questObjectives;
            this.QUEST_RUN_DATA = questRunData;
            this.ALREADY_RUN = alreadyRun;
        }
    }

    // Getter methods
    public Flags.Short getQuestFlags() {
        return this.QUEST_FLAGS;
    }

    public float getScriptDelay() {
        return this.SCRIPT_DELAY;
    }

    public QuestStage[] getQuestStages() {
        return this.QUEST_STAGES;
    }

    public QuestObjective[] getQuestObjectives() {
        return this.QUEST_OBJECTIVES;
    }

    public QuestRunData getQuestRunData() {
        return this.QUEST_RUN_DATA;
    }

    public byte getAlreadyRun() {
        return this.ALREADY_RUN;
    }

    /**
     * @see ChangeFormData#getChangeConstants() 
     * @return 
     */
    @Override
    public ChangeFlagConstants[] getChangeConstants() {
        return ChangeFlagConstantsQust.values();
    }
    
    /**
     * @see AnalyzableElement#getInfo(resaver.Analysis, resaver.ess.ESS)
     * @param analysis
     * @param save
     * @return
     */
    @Override
    public String getInfo(Optional<resaver.Analysis> analysis, ESS save) {
        final StringBuilder BUILDER = new StringBuilder();
        
        BUILDER.append("<html><h3>QUEST Change Form</h3>");
        
        if (super.hasVal("FULLNAME")) {
            BUILDER.append("<p>Name: ").append(super.getVal("FULLNAME")).append("</p>");
        }
        
        if (QUEST_FLAGS != null) {
            BUILDER.append("<p>Quest Flags: ").append(QUEST_FLAGS).append("</p>");
        }
        
        if (!Float.isNaN(SCRIPT_DELAY)) {
            BUILDER.append("<p>Script Delay: ").append(SCRIPT_DELAY).append("</p>");
        }
        
        if (QUEST_STAGES != null) {
            BUILDER.append("<p>Stages: ").append(QUEST_STAGES.length).append("</p>");
        }
        
        if (QUEST_OBJECTIVES != null) {
            BUILDER.append("<p>Objectives: ").append(QUEST_OBJECTIVES.length).append("</p>");
        }
        
        if (QUEST_RUN_DATA != null) {
            BUILDER.append("<p>Quest Run Data: ").append(QUEST_RUN_DATA).append("</p>");
        }
        
        BUILDER.append("</html>");
        return BUILDER.toString();
    }

    /**
     * @return String representation of the quest
     */
    @Override
    public String toString() {
        if (super.hasVal("FULLNAME")) {
            return super.getVal("FULLNAME").toString();
        }
        
        StringBuilder sb = new StringBuilder("Quest");
        if (QUEST_STAGES != null && QUEST_STAGES.length > 0) {
            sb.append(" (").append(QUEST_STAGES.length).append(" stages)");
        }
        if (QUEST_OBJECTIVES != null && QUEST_OBJECTIVES.length > 0) {
            sb.append(" (").append(QUEST_OBJECTIVES.length).append(" objectives)");
        }
        return sb.toString();
    }

    // Inner class for Quest Stage
    static private class QuestStage extends GeneralElement {

        public QuestStage(ByteBuffer input, ESS.ESSContext context) throws ElementException {
            this.STAGE = super.readShort(input, "STAGE");
            this.STATUS = super.readElement(input, "STATUS", Flags::readByteFlags);
        }

        @Override
        public String toString() {
            return String.format("Stage %d with flag %s", STAGE, STATUS);
        }
        
        final public short STAGE;
        final public Flags.Byte STATUS;
    }

    // Inner class for Quest Objective
    static private class QuestObjective extends GeneralElement {
        
        public QuestObjective(ByteBuffer input, ESS.ESSContext context) throws ElementException {
            this.OBJECTIVE_ID = super.readShort(input, "OBJECTIVE_ID");
            this.STATUS = super.readElement(input, "STATUS", Flags::readByteFlags);
        }
        
        @Override
        public String toString() {
            return String.format("Objective %d with status %s", OBJECTIVE_ID, STATUS);
        }
        
        final public short OBJECTIVE_ID;
        final public Flags.Byte STATUS;
    }

    // Inner class for Quest Run Data
    static private class QuestRunData extends GeneralElement {

        public QuestRunData(ByteBuffer input, ESS.ESSContext context) throws ElementException {
            this.UNK = super.readByte(input, "UNK");
            this.COUNT1 = super.readInt(input, "COUNT1");
            this.ITEMS1 = (QuestRunDataItem1[]) super.readElements(input, "ITEMS1", COUNT1, 
                i -> new QuestRunDataItem1(i, context));
            this.COUNT2 = super.readInt(input, "COUNT2");
            this.ITEMS2 = (QuestRunDataItem2[]) super.readElements(input, "ITEMS2", COUNT2, 
                i -> new QuestRunDataItem2(i, context));
            this.FLAG = super.readElement(input, "FLAG", Flags::readByteFlags);
            this.ITEM3 = FLAG.allZero() ? null : new QuestRunDataItem3(input, context);
        }
        
        @Override
        public String toString() {
            return String.format("QuestRunData: unk=%d, %d items1, %d items2, flag=%s, item3=%s", 
                UNK, ITEMS1.length, ITEMS2.length, FLAG, ITEM3 != null ? "present" : "null");
        }
        
        final public byte UNK;
        final public int COUNT1;
        final public QuestRunDataItem1[] ITEMS1;
        final public int COUNT2;
        final public QuestRunDataItem2[] ITEMS2;
        final public Flags.Byte FLAG;
        final public QuestRunDataItem3 ITEM3;
    }

    // Inner classes for Quest Run Data Items
    static private class QuestRunDataItem1 extends GeneralElement {
        public QuestRunDataItem1(ByteBuffer input, ESS.ESSContext context) throws ElementException {
            this.DATA = super.readInt(input, "DATA");
        }
        
        @Override
        public String toString() {
            return String.format("Item1: %d", DATA);
        }
        
        final public int DATA;
    }

    static private class QuestRunDataItem2 extends GeneralElement {
        public QuestRunDataItem2(ByteBuffer input, ESS.ESSContext context) throws ElementException {
            this.DATA = super.readInt(input, "DATA");
        }
        
        @Override
        public String toString() {
            return String.format("Item2: %d", DATA);
        }
        
        final public int DATA;
    }

    static private class QuestRunDataItem3 extends GeneralElement {
        public QuestRunDataItem3(ByteBuffer input, ESS.ESSContext context) throws ElementException {
            this.DATA = super.readInt(input, "DATA");
        }
        
        @Override
        public String toString() {
            return String.format("Item3: %d", DATA);
        }
        
        final public int DATA;
    }
}