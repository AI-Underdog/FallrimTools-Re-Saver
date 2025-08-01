package resaver.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import resaver.analysis.ModImpactAnalyzer;
import resaver.esp.ESP;

import java.util.*;

public class ModImpactTestWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create mock ESP data for testing
        ESP esp1 = new ESP("ModA", "AuthorA", "1.0",
                new HashSet<>(Arrays.asList("0001", "0002")),
                new HashMap<String, String>() {{ put("0001", "Record1"); }},
                Arrays.asList("ScriptA"));
        ESP esp2 = new ESP("ModB", "AuthorB", "2.0",
                new HashSet<>(Arrays.asList("0003")),
                new HashMap<String, String>() {{ put("0003", "Record2"); }},
                Arrays.asList("ScriptB", "ScriptC"));
        List<ESP> mods = Arrays.asList(esp1, esp2);

        ModImpactPanel panel = new ModImpactPanel(mods);
        Scene scene = new Scene(panel, 800, 600);
        primaryStage.setTitle("Mod Impact Analysis Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
