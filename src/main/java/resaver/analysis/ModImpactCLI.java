package resaver.analysis;

import resaver.esp.ESP;
import java.nio.file.Paths;
import java.util.*;

public class ModImpactCLI {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ModImpactCLI <mod1.esp> <mod2.esp> ...");
            return;
        }
        List<ESP> mods = new ArrayList<>();
        ModMetadataParser parser = new ModMetadataParser();
        for (String arg : args) {
            try {
                ModMetadataParser.ModMetadata meta = parser.parseModMetadata(Paths.get(arg));
                ESP esp = new ESP(meta.name, meta.author, meta.version, meta.formIds, meta.modifiedRecords, meta.injectedScripts);
                mods.add(esp);
            } catch (Exception e) {
                System.out.println("Failed to parse " + arg + ": " + e.getMessage());
            }
        }
        if (mods.isEmpty()) {
            System.out.println("No valid mods to analyze.");
            return;
        }
        ModImpactAnalyzer analyzer = new ModImpactAnalyzer();
        Map<String, ModImpactAnalyzer.ModImpact> results = analyzer.analyzeMods(mods);
        System.out.println("\nMod Impact Analysis Results:");
        System.out.println("------------------------------------------------------");
        System.out.printf("%-20s | %-10s | %-10s | %-10s\n", "Mod Name", "Records", "Scripts", "Score");
        System.out.println("------------------------------------------------------");
        for (ModImpactAnalyzer.ModImpact impact : results.values()) {
            System.out.printf("%-20s | %-10d | %-10d | %-10d\n",
                impact.modName,
                impact.affectedRecords.size(),
                impact.injectedScripts.size(),
                impact.impactScore);
        }
        System.out.println("------------------------------------------------------");
    }
}
