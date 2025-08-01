package resaver.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import resaver.analysis.ModImpactAnalyzer;
import resaver.esp.ESP;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;

public class ModImpactPanel extends BorderPane {
    private final TableView<ModImpactAnalyzer.ModImpact> table;
    private final ObservableList<ModImpactAnalyzer.ModImpact> data;

    public ModImpactPanel(List<ESP> mods) {
        this.table = new TableView<>();
        this.data = FXCollections.observableArrayList();
        setupTable();
        setCenter(table);
        runAnalysis(mods);
    }

    private void setupTable() {
        TableColumn<ModImpactAnalyzer.ModImpact, String> nameCol = new TableColumn<>("Mod Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("modName"));
        TableColumn<ModImpactAnalyzer.ModImpact, Integer> recordsCol = new TableColumn<>("Records");
        recordsCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().affectedRecords.size()).asObject());
        TableColumn<ModImpactAnalyzer.ModImpact, Integer> scriptsCol = new TableColumn<>("Scripts");
        scriptsCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().injectedScripts.size()).asObject());
        TableColumn<ModImpactAnalyzer.ModImpact, Integer> scoreCol = new TableColumn<>("Impact Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("impactScore"));
        table.getColumns().addAll(nameCol, recordsCol, scriptsCol, scoreCol);
        table.setItems(data);
    }

    public void runAnalysis(List<ESP> mods) {
        ModImpactAnalyzer analyzer = new ModImpactAnalyzer();
        Map<String, ModImpactAnalyzer.ModImpact> results = analyzer.analyzeMods(mods);
        data.setAll(results.values());
    }
}
