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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.logging.Logger;
import resaver.Game;
import resaver.ess.Plugin;
import resaver.ess.PluginInfo;
import javafx.scene.control.Tab;
import resaver.gui.ModImpactPanel;

/**
 * Describes a Skyrim PEX script and will readFully and write it from iostreams.
 *
 * @author Mark Fairchild
 */
final public class ESP implements Entry {

    /**
     * Skims a mod file and extracts EDIDs and ids.
     *
     * Exceptions are not handled. At all. Not even a little bit.
     *
     * @param path The mod file to readFully, which must exist and be readable.
     * @param game The game whose mods are being read.
     * @param plugin The <code>Plugin</code> corresponding to the
     * <code>ESP</code>.
     * @param plugins The list of plugins, for correcting FormIDs.
     * @param data The <code>PluginData</code> to populate.
     * @return The PluginData.
     * @param <T> The type of <code>PluginData</code>.
     *
     * @throws PluginException
     * @throws FileNotFoundException
     * @throws ClosedByInterruptException
     * 
     */
    static public <T extends PluginData> T skimPlugin(Path path, Game game, Plugin plugin, PluginInfo plugins, T data) throws PluginException, FileNotFoundException, ClosedByInterruptException {
        Objects.requireNonNull(path);
        assert Files.isReadable(path);
        assert Files.isRegularFile(path);
        final String NAME = path.getFileName().toString();

        // Prepare input stream.
        try (FileChannel input = FileChannel.open(path, java.nio.file.StandardOpenOption.READ)) {
            final ByteBuffer BUFFER = ByteBuffer.allocateDirect((int) input.size());
            input.read(BUFFER);
            BUFFER.order(ByteOrder.LITTLE_ENDIAN);
            ((Buffer) BUFFER).flip();
          
            try {
                // first build a typed "pre" context for TES4
                final ESPContext<T> preCTX = new ESPContext<>(game, plugin, data, null);
                final RecordTes4 TES4    = new RecordTes4(BUFFER, plugin, plugins, preCTX);
                // now build the real context, also typed
                final ESPContext<T> CTX   = new ESPContext<>(game, plugin, data, TES4);

                while (BUFFER.hasRemaining()) {
                    Record.skimRecord(BUFFER, CTX);
                }

                return CTX.PLUGIN_INFO;
            } catch (RecordException|FieldException ex) {
               //LOG.warning(String.format("Error reading plugin %s\n%s", NAME, ex.getContext()));
                throw new PluginException(ex, NAME, ex.getContext());
            }

        } catch (FileNotFoundException ex) {
            LOG.warning(ex.getMessage());
            throw ex;
            
        } catch (ClosedByInterruptException ex) {
            throw ex;
            
        } catch (IOException | RuntimeException ex) {
            LOG.warning(ex.getMessage());
            throw new PluginException(ex, NAME, NAME);
            
        } 
    }

    /**
     * Creates an ESP by reading from a <code>ByteBuffer</code>.
     *
     * @param input A <code>ByteBuffer</code> for a Skyrim PEX file.
     * @param game The game whose mods are being read.
     * @param plugin The <code>Plugin</code> corresponding to the
     * <code>ESP</code>.
     * @param name The name of the plugin.
     * @param plugins The list of plugins, for correcting FormIDs.
     * @throws RecordException Exceptions aren't handled.
     * @throws FieldException Exceptions aren't handled.
     * @throws IOException Exceptions aren't handled.
     */
    public ESP(ByteBuffer input, Game game, Plugin plugin, String name, PluginInfo plugins) throws IOException, RecordException, FieldException {
        assert input.hasRemaining();
        this.RECORDS = new LinkedList<>();

        PluginData nullHandler = new PluginData(){};
        final RecordTes4 TES4 = new RecordTes4(input, plugin, plugins, new ESPContext<PluginData>(game, plugin, nullHandler, null));
        final ESPContext<PluginData> CTX = new ESPContext<>(game, plugin, nullHandler, TES4);
        CTX.pushContext(plugin.NAME);
        this.RECORDS.add(TES4);

        while (input.hasRemaining()) {
            Record record = Record.readRecord(input, CTX);
            this.RECORDS.add(record);
        }
        // Initialize new metadata fields with default values
        this.modName = name != null ? name : "";
        this.author = "";
        this.version = "";
        this.formIds = new java.util.HashSet<>();
        this.modifiedRecords = new java.util.HashMap<>();
        this.injectedScripts = new java.util.ArrayList<>();
    }

    /**
     * Write the ESP to a <code>ByteBuffer</code>.
     *
     * @param output The <code>ByteBuffer</code> to write.
     */
    @Override
    public void write(ByteBuffer output) {
        this.RECORDS.forEach(record -> record.write(output));
    }

    /**
     * @return The calculated size of the field.
     * @see Entry#calculateSize()
     */
    @Override
    public int calculateSize() {
        int sum = 0;
        sum += this.RECORDS.stream().mapToInt(v -> v.calculateSize()).sum();
        return sum;
    }

    /**
     * Pretty-prints the ESP.
     *
     * @return A string representation of the ESP.
     */
    @Override
    public String toString() {
        final StringBuilder BUF = new StringBuilder();
        this.RECORDS.forEach(record -> BUF.append(record.toString()));
        return BUF.toString();
    }

    final private List<Record> RECORDS;

    static final private Logger LOG = Logger.getLogger(ESP.class.getCanonicalName());

    private final String modName;
    private final String author;
    private final String version;
    private final Set<String> formIds;
    private final Map<String, String> modifiedRecords;
    private final List<String> injectedScripts;

    public ESP(String modName, String author, String version, Set<String> formIds, Map<String, String> modifiedRecords, List<String> injectedScripts) {
        this.modName = modName;
        this.author = author;
        this.version = version;
        this.formIds = formIds;
        this.modifiedRecords = modifiedRecords;
        this.injectedScripts = injectedScripts;
        this.RECORDS = new LinkedList<>(); // Ensure RECORDS is always initialized
    }

    public String getModName() {
        return modName;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public Set<String> getFormIds() {
        return formIds;
    }

    public Map<String, String> getModifiedRecords() {
        return modifiedRecords;
    }

    public List<String> getInjectedScripts() {
        return injectedScripts;
    }
}
