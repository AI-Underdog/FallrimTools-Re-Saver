package resaver.analysis;

import java.util.*;
import java.nio.file.Path;
import resaver.esp.ESP;

/**
 * Analyzes the impact of mods by comparing their metadata and modified records.
 */
public class ModImpactAnalyzer {
    public static class ModImpact {
        public final String modName;
        public final Set<String> affectedRecords;
        public final Set<String> injectedScripts;
        public final int impactScore;
        public ModImpact(String modName, Set<String> affectedRecords, Set<String> injectedScripts, int impactScore) {
            this.modName = modName;
            this.affectedRecords = affectedRecords;
            this.injectedScripts = injectedScripts;
            this.impactScore = impactScore;
        }
    }

    /**
     * Analyze a list of ESPs and return a map of mod name to ModImpact.
     */
    public Map<String, ModImpact> analyzeMods(List<ESP> mods) {
        Map<String, ModImpact> result = new HashMap<>();
        for (ESP esp : mods) {
            Set<String> affected = esp.getModifiedRecords().keySet();
            Set<String> scripts = new HashSet<>(esp.getInjectedScripts());
            int score = affected.size() + scripts.size(); // Simple impact score
            result.put(esp.getModName(), new ModImpact(esp.getModName(), affected, scripts, score));
        }
        return result;
    }
}
