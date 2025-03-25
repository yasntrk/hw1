package view;

import javax.swing.*;
import java.awt.*;

public class GuiPreview {
    public static void showHtml(String htmlContent) {
        JFrame frame = new JFrame("Schedule Preview");
        JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
        editorPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
