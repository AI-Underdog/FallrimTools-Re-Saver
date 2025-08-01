/*
 * Copyright 2018 Mark Fairchild.
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
import java.util.Objects;
import java.util.Optional;

/**
 * Describes a ChangeForm containing a relationship.
 *
 * @author Mark Fairchild
 */
final public class ChangeFormRela extends GeneralElement implements ChangeFormData {

    /**
         * Creates a new <code>ChangeForm</code> by reading from a
         * <code>ByteBuffer</code>. No error handling is performed.
         *
         * @param input The input buffer containing the data to read.
         * @param changeFlags The set of change flags indicating which fields are present in this ChangeForm.
         * @param refid The reference ID of the ChangeForm, used to determine the type and relationships.
         * @param context The <code>ESSContext</code> providing context information for parsing.
         * @throws ElementException If there is an error parsing the ChangeForm data.
         */
        public ChangeFormRela(ByteBuffer input, Flags.Int changeFlags, RefID refid, ESS.ESSContext context) throws ElementException {
        Objects.requireNonNull(input);
        if (changeFlags.getFlag(ChangeFlagConstantsRela.UNK0)) {
            this.FLAGS = Optional.of(new ChangeFormFlags(input));
        } else {
            this.FLAGS = Optional.empty();
        }
        
        RefID person1 = null, person2 = null, association = null;
        Integer rank = null;
        
        try {
            if (refid.getType() == RefID.Type.CREATED) {
                person1 = super.readRefID(input, "PERSON1", context);
                person2 = super.readRefID(input, "PERSON2", context);
                association = super.readRefID(input, "ASSOCIATION", context);                
            }
            if (changeFlags.getFlag(ChangeFlagConstantsRela.RANK)) {
                rank = super.readInt(input, "RANK");
            }
            
        } catch (UnparsedException ex) {
            throw new ElementException("Unparsed data in RELA", ex, this);            
        } catch (RuntimeException ex) {
            super.readUnparsed(input);
            throw new ElementException("Error reading RELA", ex, this);
        } finally {
            PERSON1 = person1;
            PERSON2 = person2;
            ASSOCIATION = association;
            RANK = rank;
        }
    }

    /**
     * @return The ChangeForm flags if present
     */
    public Optional<ChangeFormFlags> getFlags() {
        return FLAGS;
    }

    /**
     * @see ChangeFormData#getChangeConstants() 
     * @return 
     */
    @Override
    public ChangeFlagConstants[] getChangeConstants() {
        return ChangeFlagConstantsRela.values();
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
        BUILDER.append("<html><h3>RELATIONSHIP Change Form</h3>");
        
        if (FLAGS.isPresent()) {
            BUILDER.append("<p>Change Form Flags: ").append(FLAGS.get()).append("</p>");
        }
        
        if (PERSON1 != null) {
            BUILDER.append("<p>Person 1: ").append(PERSON1).append("</p>");
        }
        
        if (PERSON2 != null) {
            BUILDER.append("<p>Person 2: ").append(PERSON2).append("</p>");
        }
        
        if (ASSOCIATION != null) {
            BUILDER.append("<p>Association: ").append(ASSOCIATION).append("</p>");
        }
        
        if (RANK != null) {
            BUILDER.append("<p>Rank: ").append(RANK).append("</p>");
        }
        
        BUILDER.append("</html>");
        return BUILDER.toString();
    }

    /**
     * @return String representation of the relationship
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Relationship");
        if (PERSON1 != null && PERSON2 != null) {
            sb.append(": ").append(PERSON1).append(" -> ").append(PERSON2);
        }
        if (ASSOCIATION != null) {
            sb.append(" (").append(ASSOCIATION).append(")");
        }
        if (RANK != null) {
            sb.append(" [Rank: ").append(RANK).append("]");
        }
        return sb.toString();
    }
    
    final private Optional<ChangeFormFlags> FLAGS;    
    final public RefID PERSON1;
    final public RefID PERSON2;
    final public RefID ASSOCIATION;
    final public Integer RANK;
    
    static public enum ChangeFlagConstantsRela implements ChangeFlagConstants {
        /**
         * Unknown flag 0. The specific purpose of this flag is not documented.
         * If more information becomes available, update this comment and consider renaming.
         */
        UNK0(0), 
        RANK(1);
        
        /**
         * Returns the flag position.
         *
         * @return
         */
        @Override
        public int getPosition() {
            return this.VAL;
        }

        private ChangeFlagConstantsRela(int n) {
            this.VAL = n;
        }

        final private int VAL;
    }
}
