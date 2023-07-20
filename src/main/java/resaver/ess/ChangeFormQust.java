/*
 * Copyright 2023 Mark Fairchild.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package resaver.ess;

import java.nio.ByteBuffer;
import java.util.Arrays;
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

    /**
     * Creates a new <code>ChangeForm</code>.
     *
     * @param input The input stream.
     * @param flags The change form flags.
     * @param context The <code>ESSContext</code> info.
     * @throws ElementException
     * 
     */
    public ChangeFormQust(ByteBuffer input, Flags.Int flags, ESS.ESSContext context) throws ElementException {
        this(input, flags, false, context);
    }
    
    /**
     * Creates a new <code>ChangeForm</code>.
     *
     * @param input The input stream.
     * @param flags The change form flags.
     * @param context The <code>ESSContext</code> info.
     * @param inline Indicates that the changeform appears as an element
     * of another changeform so unparsed data in <code>input</code> should be 
     * ignored.
     * @throws ElementException
     * 
     */
    public ChangeFormQust(ByteBuffer input, Flags.Int flags, boolean inline, ESS.ESSContext context) throws ElementException {
        Objects.requireNonNull(input);

        if (flags.getFlag(CHANGE_FORM_FLAGS)) {
            this.CHANGEFORMFLAGS = super.readElement(input, CHANGE_FORM_FLAGS, in -> new ChangeFormFlags(in));
        } else {
            this.CHANGEFORMFLAGS = null;
        }

        try {
            this.QUEST_FLAGS = flags.getFlag(CHANGE_QUEST_FLAGS) 
                    ? super.readElement(input, "QUEST_FLAGS", Flags::readShortFlags)
                    : null;

            this.SCRIPT_DELAY = flags.getFlag(CHANGE_QUEST_SCRIPT_DELAY)
                    ? super.readFloat(input, "SCRIPT_DELAY")
                    : Float.NaN;
            
            this.QUEST_STAGES = flags.getFlag(CHANGE_QUEST_STAGES)
                    ? (QuestStage[]) super.readVSElemArray(input, "QUEST_STAGES", i -> new QuestStage(i, context))
                    : null;
                
            this.QUEST_OBJECTIVES = flags.getFlag(CHANGE_QUEST_OBJECTIVES)
                    ? (QuestObjective[]) super.readVSElemArray(input, "QUEST_OBJECTIVES", i -> new QuestObjective(i, context))
                    : null;
            
            this.QUEST_RUN_DATA = flags.getFlag(CHANGE_QUEST_RUNDATA)
                    ? new QuestRunData(input, context)
                    : null;
            
            
            
            
            this.ALREADY_RUN = flags.getFlag(CHANGE_QUEST_ALREADY_RUN)
                    ? super.readByte(input, "ALREADY_RUN")
                    : 0;
            
        } catch (UnparsedException ex) {
            throw new ElementException("Unparsed data in QUST", ex, this);            
        } catch (RuntimeException ex) {
            super.readUnparsed(input);
            throw new ElementException("Error reading QUST", ex, this);
        }
    }

    /**
     * @return The <code>ChangeFormFlags</code> field.
     */
    public ChangeFormFlags getChangeFormFlags() {
        return this.CHANGEFORMFLAGS;
    }

    /**
     * @return String representation.
     */
    @Override
    public String toString() {
        return super.hasVal("FULLNAME") ? super.getVal("FULLNAME").toString() : "";
    }

    /**
     * @see ChangeFormData#getChangeConstants() 
     * @return 
     */
    @Override
    public ChangeFlagConstants[] getChangeConstants() {
        return ChangeFlagConstantsNPC.values();
    }
    
    /**
     * @see AnalyzableElement#getInfo(resaver.Analysis, resaver.ess.ESS)
     * @param analysis
     * @param save
     * @return
     */
    @Override
    public String getInfo(Optional<resaver.Analysis> analysis, ESS save) {
        //final StringBuilder BUILDER = new StringBuilder();
        //return BUILDER.toString();

        return super.toString();
    }

    /**
     * @see AnalyzableElement#matches(resaver.Analysis, resaver.Mod)
     * @param analysis
     * @param mod
     * @return
     */
    @Override
    public boolean matches(Optional<Analysis> analysis, String mod) {
        return false;
    }

    final private ChangeFormFlags CHANGEFORMFLAGS;
    final private Flags.Short QUEST_FLAGS;
    final private float SCRIPT_DELAY;
    final private QuestStage[] QUEST_STAGES;
    final private QuestObjective[] QUEST_OBJECTIVES;
    final private QuestRunData QUEST_RUN_DATA;

    final private byte ALREADY_RUN;
    
    static private class QuestStage extends GeneralElement {

        public QuestStage(ByteBuffer input, ESS.ESSContext context) throws ElementException{
            this.STAGE = super.readShort(input, "STAGE");
            this.STATUS = super.readElement(input, "STATUS", Flags::readByteFlags);
        }

        @Override
        public String toString() {
            return String.format("Stage %d with flag %s", STAGE, STATUS);
        }
        
        public short STAGE;
        public Flags STATUS;
    }

    static private class QuestObjective extends GeneralElement {

        public QuestObjective(ByteBuffer input, ESS.ESSContext context) throws ElementException{
            this.UNK1 = super.readInt(input, "UNK1");
            this.UNK2 = super.readInt(input, "UNK2");
        }

        @Override
        public String toString() {
            return String.format("Objects %d:%d", UNK1, UNK2);
        }
        
        public int UNK1;
        public int UNK2;
    }
    
    
    static private class QuestRunData extends GeneralElement {

        public QuestRunData(ByteBuffer input, ESS.ESSContext context) throws ElementException{
            UNK = super.readByte(input, "UNK");
            COUNT1 = super.readInt(input, "COUNT1");
            ITEMS1 = (QuestRunDataItem1[]) super.readElements(input, "ITEMS1", COUNT1, i -> new QuestRunDataItem1(i, context));
            COUNT2 = super.readInt(input, "COUNT2");
            ITEMS2 = (QuestRunDataItem2[]) super.readElements(input, "ITEMS2", COUNT2, i -> new QuestRunDataItem2(i, context));
            FLAG = super.readElement(input, "FLAG", Flags::readByteFlags);
            ITEM3 = FLAG.allZero() ? null : new QuestRunDataItem3(input, context);
        }
        
        final public byte UNK;
        final public int COUNT1;
        final public QuestRunDataItem1[] ITEMS1;
        final public int COUNT2;
        final public QuestRunDataItem2[] ITEMS2;
        final public Flags.Byte FLAG;
        final public QuestRunDataItem3 ITEM3;
    }
    
    static private class QuestRunDataItem1 extends GeneralElement {

        public QuestRunDataItem1(ByteBuffer input, ESS.ESSContext context) throws ElementException{
            UNK = super.readInt(input, "UNK");
            FLAGS = super.readElement(input, "FLAG", Flags::readByteFlags);
            REFS = (RefID[]) super.readElements(input, "REFS", (FLAGS.allZero() ? 1 : 5), context::readRefID);
        }
        
        @Override
        public String toString() {
            return String.format("RunDataItem1 %d [%s] %s", UNK, FLAGS, Arrays.toString(REFS));
        }
        
        final public int UNK;
        final public Flags.Byte FLAGS;
        final public RefID[] REFS;
    }
    
    static private class QuestRunDataItem2 extends GeneralElement {

        public QuestRunDataItem2(ByteBuffer input, ESS.ESSContext context) throws ElementException{
            super.readInt(input, "UNK");
            super.readRefID(input, "REF", context);
        }
    }
    
    static private class QuestRunDataItem3 extends GeneralElement {

        public QuestRunDataItem3(ByteBuffer input, ESS.ESSContext context) throws ElementException{
            super.readInt(input, "UNK1");
            super.readFloat(input, "UNK2");
            int count = super.readInt(input, "COUNT");
            super.readElements(input, "ITEMS", count, i -> new QuestRunDataItem3Data(i, context));
        }
    }
    
    static private class QuestRunDataItem3Data extends GeneralElement {

        public QuestRunDataItem3Data(ByteBuffer input, ESS.ESSContext context) throws ElementException{
            int type = super.readInt(input, "TYPE");
            switch (type) {
                case 1:
                case 2:
                case 4:
                    super.readRefID(input, "UNK_REF", context);
                    break;
                case 3:
                    super.readInt(input, "UNK_INT");
                    break;
                default:
                    throw new IllegalStateException("Not expecting type " + type);
            }
        }
    }
    
}
