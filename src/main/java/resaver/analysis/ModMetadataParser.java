package resaver.analysis;

import resaver.esp.ESP;
import resaver.esp.ESPParser;
import java.util.*;
import java.nio.file.Path;

/**
 * Parses mod metadata from ESP/ESM files to extract mod impact information.
 */
public class ModMetadataParser {
    
    /**
     * Parses an ESP/ESM file to extract mod metadata.
     */
    public ModMetadata parseModMetadata(Path espPath) {
        ESP esp = new ESPParser().parse(espPath);
        return new ModMetadata(
            esp.getModName(),
            esp.getAuthor(),
            esp.getVersion(),
            esp.getFormIds(),
            esp.getModifiedRecords(),
            esp.getInjectedScripts()
        );
    }

    /**
     * Represents metadata about a mod.
     */
    public static class ModMetadata {
        public final String name;
        public final String author;
        public final String version;
        public final Set<String> formIds;
        public final Map<String, String> modifiedRecords; // Record ID -> Modified value
        public final List<String> injectedScripts;

        public ModMetadata(String name, String author, String version, 
                          Set<String> formIds, Map<String, String> modifiedRecords, 
                          List<String> injectedScripts) {
            this.name = name;
            this.author = author;
            this.version = version;
            this.formIds = formIds;
            this.modifiedRecords = modifiedRecords;
            this.injectedScripts = injectedScripts;
        }
    }

    private static final Map<String, String> RECORD_TYPES = new HashMap<>();
    static {
        RECORD_TYPES.put("TES4", "Header");
        RECORD_TYPES.put("GMST", "Game Setting");
        RECORD_TYPES.put("KYWD", "Keyword");
        RECORD_TYPES.put("LCRT", "Location Reference Type");
        RECORD_TYPES.put("AACT", "Action");
        RECORD_TYPES.put("TXST", "Texture Set");
        RECORD_TYPES.put("GLOB", "Global");
        RECORD_TYPES.put("CLAS", "Class");
        RECORD_TYPES.put("FACT", "Faction");
        RECORD_TYPES.put("HDPT", "Head Part");
        RECORD_TYPES.put("EYES", "Eyes");
        RECORD_TYPES.put("RACE", "Race");
        RECORD_TYPES.put("SOUN", "Sound");
        RECORD_TYPES.put("ASPC", "Acoustic Space");
        RECORD_TYPES.put("SKIL", "Skill");
        RECORD_TYPES.put("MGEF", "Magic Effect");
        RECORD_TYPES.put("SCPT", "Script");
        RECORD_TYPES.put("LTEX", "Landscape Texture");
        RECORD_TYPES.put("ENCH", "Enchantment");
        RECORD_TYPES.put("SPEL", "Spell");
        RECORD_TYPES.put("SCRL", "Scroll");
        RECORD_TYPES.put("ACTI", "Activator");
        RECORD_TYPES.put("TACT", "Talking Activator");
        RECORD_TYPES.put("ARMO", "Armor");
        RECORD_TYPES.put("BOOK", "Book");
        RECORD_TYPES.put("CONT", "Container");
        RECORD_TYPES.put("DOOR", "Door");
        RECORD_TYPES.put("INGR", "Ingredient");
        RECORD_TYPES.put("LIGH", "Light");
        RECORD_TYPES.put("MISC", "Miscellaneous");
        RECORD_TYPES.put("APPA", "Apparatus");
        RECORD_TYPES.put("STAT", "Static");
        RECORD_TYPES.put("SCOL", "Static Collection");
        RECORD_TYPES.put("MSTT", "Movable Static");
        RECORD_TYPES.put("GRAS", "Grass");
        RECORD_TYPES.put("TREE", "Tree");
        RECORD_TYPES.put("FLOR", "Flora");
        RECORD_TYPES.put("FURN", "Furniture");
        RECORD_TYPES.put("WEAP", "Weapon");
        RECORD_TYPES.put("AMMO", "Ammo");
        RECORD_TYPES.put("NPC_", "Non-Player Character");
        RECORD_TYPES.put("LVLN", "Leveled NPC");
        RECORD_TYPES.put("KEYM", "Key");
        RECORD_TYPES.put("ALCH", "Alchemy Item");
        RECORD_TYPES.put("IDLM", "Idle Marker");
        RECORD_TYPES.put("NOTE", "Note");
        RECORD_TYPES.put("COBJ", "Constructible Object");
        RECORD_TYPES.put("PROJ", "Projectile");
        RECORD_TYPES.put("HAZD", "Hazard");
        RECORD_TYPES.put("SLGM", "Soul Gem");
        RECORD_TYPES.put("LVLI", "Leveled Item");
        RECORD_TYPES.put("WTHR", "Weather");
        RECORD_TYPES.put("CLMT", "Climate");
        RECORD_TYPES.put("SPGD", "Shader Particle Geometry");
        RECORD_TYPES.put("RFCT", "Visual Effect");
        RECORD_TYPES.put("REGN", "Region");
        RECORD_TYPES.put("NAVI", "Navigation Mesh");
        RECORD_TYPES.put("CELL", "Cell");
        RECORD_TYPES.put("REFR", "Object Reference");
        RECORD_TYPES.put("ACHR", "Placed NPC");
        RECORD_TYPES.put("PMIS", "Placed Missile");
        RECORD_TYPES.put("PARW", "Placed Arrow");
        RECORD_TYPES.put("PGRE", "Placed Projectile");
        RECORD_TYPES.put("PBEA", "Placed Beam");
        RECORD_TYPES.put("PFLA", "Placed Flame");
        RECORD_TYPES.put("PCON", "Placed Cone");
        RECORD_TYPES.put("PBAR", "Placed Barrier");
        RECORD_TYPES.put("PHZD", "Placed Hazard");
        RECORD_TYPES.put("WRLD", "Worldspace");
        RECORD_TYPES.put("LAND", "Landscape");
        RECORD_TYPES.put("NAVM", "Navigation Mesh");
        RECORD_TYPES.put("TLOD", "Tree LOD");
        RECORD_TYPES.put("DIAL", "Dialogue Topic");
        RECORD_TYPES.put("INFO", "Dialogue Info");
        RECORD_TYPES.put("QUST", "Quest");
        RECORD_TYPES.put("IDLE", "Idle Animation");
        RECORD_TYPES.put("PACK", "Package");
        RECORD_TYPES.put("CSTY", "Combat Style");
        RECORD_TYPES.put("LSCR", "Load Screen");
        RECORD_TYPES.put("LVSP", "Leveled Spell");
        RECORD_TYPES.put("ANIO", "Animated Object");
        RECORD_TYPES.put("WATR", "Water");
        RECORD_TYPES.put("EFSH", "Effect Shader");
        RECORD_TYPES.put("EXPL", "Explosion");
        RECORD_TYPES.put("DEBR", "Debris");
        RECORD_TYPES.put("IMGS", "Image Space");
        RECORD_TYPES.put("IMAD", "Image Space Modifier");
        RECORD_TYPES.put("FLST", "Form List");
        RECORD_TYPES.put("PERK", "Perk");
        RECORD_TYPES.put("BPTD", "Body Part Data");
        RECORD_TYPES.put("ADDN", "Addon Node");
        RECORD_TYPES.put("AVIF", "Actor Value Information");
        RECORD_TYPES.put("CAMS", "Camera Shot");
        RECORD_TYPES.put("CPTH", "Camera Path");
        RECORD_TYPES.put("VTYP", "Voice Type");
        RECORD_TYPES.put("MATT", "Material Type");
        RECORD_TYPES.put("IPCT", "Impact");
        RECORD_TYPES.put("IPDS", "Impact Data Set");
        RECORD_TYPES.put("ECZN", "Encounter Zone");
        RECORD_TYPES.put("LCTN", "Location");
        RECORD_TYPES.put("MESG", "Message");
        RECORD_TYPES.put("DOBJ", "Default Object Manager");
        RECORD_TYPES.put("LGTM", "Lighting Template");
        RECORD_TYPES.put("MUSC", "Music");
        RECORD_TYPES.put("FSTP", "Footstep");
        RECORD_TYPES.put("FSTS", "Footstep Set");
        RECORD_TYPES.put("SMBN", "Story Manager Branch Node");
        RECORD_TYPES.put("SMQN", "Story Manager Quest Node");
        RECORD_TYPES.put("SMEN", "Story Manager Event Node");
        RECORD_TYPES.put("DLBR", "Dialog Branch");
        RECORD_TYPES.put("MUST", "Music Track");
        RECORD_TYPES.put("DLVW", "Dialog View");
        RECORD_TYPES.put("WOOP", "Word of Power");
        RECORD_TYPES.put("SHOU", "Shout");
        RECORD_TYPES.put("EQUP", "Equip Slot");
        RECORD_TYPES.put("RELA", "Relationship");
        RECORD_TYPES.put("SCEN", "Scene");
        RECORD_TYPES.put("ASTP", "Association Type");
        RECORD_TYPES.put("OTFT", "Outfit");
        RECORD_TYPES.put("ARTO", "Art Object");
        RECORD_TYPES.put("MATO", "Material Object");
        RECORD_TYPES.put("MOVT", "Movement Type");
        RECORD_TYPES.put("SNDR", "Sound Descriptor");
        RECORD_TYPES.put("DUAL", "Dual Cast Data");
        RECORD_TYPES.put("SNCT", "Sound Category");
        RECORD_TYPES.put("SOPM", "Sound Output Model");
        RECORD_TYPES.put("COLL", "Collision Layer");
        RECORD_TYPES.put("CLFM", "Color Form");
        RECORD_TYPES.put("REVB", "Reverb Parameters");
        RECORD_TYPES.put("PKIN", "Pack-In");
        RECORD_TYPES.put("RFGP", "Reference Group");
        RECORD_TYPES.put("AMDL", "Aim Model");
        RECORD_TYPES.put("LAYR", "Layer");
        RECORD_TYPES.put("COJC", "Component");
        RECORD_TYPES.put("OMOD", "Object Modification");
        RECORD_TYPES.put("MSWP", "Material Swap");
        RECORD_TYPES.put("ZOOM", "Zoom");
        RECORD_TYPES.put("INNR", "Instance Naming Rules");
        RECORD_TYPES.put("KSSM", "Key");
        RECORD_TYPES.put("SFBK", "Sound Files");
        RECORD_TYPES.put("AFSM", "Animation File Set");
        RECORD_TYPES.put("RELA", "Relationship");
        RECORD_TYPES.put("VOLI", "Volumetric Lighting");
    }
}
