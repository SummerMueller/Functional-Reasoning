package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends JFrame implements Runnable {
    private JEditorPane editorPane;
    private JButton refreshButton;
    private Reasoning reasoning;

    public UI(Reasoning reasoning) {
        this.reasoning = reasoning;
        setTitle("Physics-Based Reasoner Output");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setFont(new Font("Verdana", Font.PLAIN, 16)); // Set font to Verdana

        refreshButton = new JButton("Refresh Output");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editorPane.setText(formatErrorLog(reasoning.getErrorLog()));
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(editorPane), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        setVisible(true);

        // Initial load of error data
        editorPane.setText(formatErrorLog(reasoning.getErrorLog()));
    }

    // Format the error log for better presentation
    private String formatErrorLog(String rawLog) {
        StringBuilder formattedLog = new StringBuilder();
        formattedLog.append("<html><head><style>")
                .append("body { font-family: Verdana; color: #333; } ")
                .append("h2 { color: #007BFF; } ") // Header color
                .append(".error { color: red; font-weight: bold; } ") // Error type
                .append(".warning { color: orange; } ") // Warning style
                .append(".info { color: green; } ") // Info style
                .append("</style></head><body>");

        // Split the raw log into lines
        String[] lines = rawLog.split("\n");
        for (String line : lines) {
            if (line.startsWith("BalanceLawsII") || line.startsWith("IncompleteTopologyII")
                    || line.startsWith("BalanceLawsI")|| line.startsWith("IncompleteTopologyI")
                    || line.startsWith("DanglingNode")|| line.startsWith("UnknownFunction")
                    || line.startsWith("FunctionalKB")){
                formattedLog.append("<div class='error'>").append(formatLine(line)).append("</div>"); // Highlight specific lines
            } else {
                formattedLog.append("<div>").append(formatLine(line)).append("</div>"); // Regular line
            }
        }

        formattedLog.append("</body></html>"); // Close HTML tags
        return formattedLog.toString();
    }

    // Format the individual lines for better readability
    private String formatLine(String line) {
        // Split the line by commas to format it nicely
        String[] parts = line.split(", ");
        StringBuilder formattedLine = new StringBuilder();
        formattedLine.append(parts[0]); // Append the first part

        // Append the rest with indentation
        for (int i = 1; i < parts.length; i++) {
            formattedLine.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;").append(parts[i]); // Add indentation
        }

        return formattedLine.toString();
    }

    @Override
    public void run() {}

    public static void main(String[] args) {
        Reasoning reasoning = new Reasoning();
        reasoning.main(); // Collect errors before launching the UI
        SwingUtilities.invokeLater(() -> new UI(reasoning));
    }
}
