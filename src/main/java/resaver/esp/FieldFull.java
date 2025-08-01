/*
 * Copyright 2016 Mark Fairchild.
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
package resaver.esp;

import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import resaver.IString;

/**
 * FieldFull represents a FULL (fullname) field.
 */
public class FieldFull extends FieldSimple {
    /**
     * @param ctx generic plugin‐data context
     */
    public FieldFull(IString code,
                     ByteBuffer input,
                     int size,
                     boolean big,
                     ESPContext<? extends PluginData> ctx)
    {
        super(code,input,size,big,ctx);

        if (ctx.TES4.getHeader().isLocalized()) {
            assert super.getData().length == 4;
            int val = super.getByteBuffer().getInt();
            this.IDX = val;
            this.STR = null;
        } else if (super.getData().length == 0) {
            this.STR = null;
            this.IDX = -1;
        } else {
            this.STR = new String(super.getData(), UTF_8);
            this.IDX = -1;
        }
    }

    /**
     * @return A flag indicating whether the field stores a string.
     */
    public boolean hasString() {
        return null != this.STR;
    }

    /**
     * @return A flag indicating whether the field stores a stringtable index.
     */
    public boolean hasIndex() {
        return this.IDX != -1;
    }

    /**
     * @return The string value of the FULL.
     */
    public String getString() {
        assert this.hasString();
        return this.STR;
    }

    /**
     * @return The stringtable index of the FULL.
     */
    public int getIndex() {
        assert this.hasIndex();
        return this.IDX;
    }

    /**
     * Returns a String representation of the Field, which will just be the code
     * string.
     *
     * @return A string representation.
     *
     */
    @Override
    public String toString() {
        final StringBuilder BUF = new StringBuilder();
        if (this.hasIndex()) {
            BUF.append(this.getCode()).append("=").append(String.format("%08x", this.IDX));
        } else if (this.hasString()) {
            BUF.append(this.getCode()).append("=").append(this.STR);
        } else {
            BUF.append(this.getCode()).append("=");
        }

        return BUF.toString();
    }

    final private String STR;
    final private int IDX;
}
