package resaver.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Dialog for selecting a game/mod directory, either manually or by searching.
 */
public class GameDirectoryChooserDialog extends JDialog {
    private JTextField directoryField;
    private JButton browseButton;
    private JButton searchButton;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    private File selectedDirectory;

    public static File showDialog(Window parent) {
        GameDirectoryChooserDialog dialog = new GameDirectoryChooserDialog(parent);
        dialog.setVisible(true);
        return dialog.selectedDirectory;
    }

    private GameDirectoryChooserDialog(Window parent) {
        super(parent, "Select Game Directory", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 180));

        directoryField = new JTextField();
        browseButton = new JButton("Browse...");
        searchButton = new JButton("Search");
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        statusLabel = new JLabel(" ");

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(directoryField, BorderLayout.CENTER);
        inputPanel.add(browseButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(new JLabel("Enter game/mod directory or search:"), BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.PAGE_END);

        browseButton.addActionListener(this::onBrowse);
        searchButton.addActionListener(this::onSearch);
        okButton.addActionListener(this::onOK);
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    private void onBrowse(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            directoryField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void onSearch(ActionEvent e) {
        statusLabel.setText("Searching for game directories, please wait...");
        searchButton.setEnabled(false);
        SwingWorker<List<File>, Void> worker = new SwingWorker<List<File>, Void>() {
            @Override
            protected List<File> doInBackground() {
                // TODO: Implement robust search logic (scan drives, registry, etc.)
                // For now, just simulate with a delay and return an empty list.
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                return java.util.Collections.emptyList();
            }
            @Override
            protected void done() {
                searchButton.setEnabled(true);
                try {
                    List<File> found = get();
                    if (found.isEmpty()) {
                        statusLabel.setText("No game directories found. Please enter manually.");
                    } else {
                        File choice = (File) JOptionPane.showInputDialog(
                            GameDirectoryChooserDialog.this,
                            "Select a directory:",
                            "Game Directories Found",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            found.toArray(),
                            found.get(0)
                        );
                        if (choice != null) {
                            directoryField.setText(choice.getAbsolutePath());
                        }
                        statusLabel.setText(" ");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    statusLabel.setText("Error during search.");
                }
            }
        };
        worker.execute();
    }

    private void onOK(ActionEvent e) {
        String path = directoryField.getText().trim();
        if (path.isEmpty()) {
            statusLabel.setText("Please enter or select a directory.");
            return;
        }
        File dir = new File(path);
        if (!dir.isDirectory()) {
            statusLabel.setText("Not a valid directory.");
            return;
        }
        selectedDirectory = dir;
        dispose();
    }
}
