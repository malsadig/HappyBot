package org.happy.util.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author: Mossab
 */
public class Logger extends JFrame {
    private final JTextArea log;

    public Logger() {
        JScrollPane loggerScrollPane = new JScrollPane();
        log = new JTextArea();

        setTitle("Logger");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 540);
        setResizable(false);

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        loggerScrollPane.setViewportView(log);
        pane.add(loggerScrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(getOwner());

    }

    public void severe(final String string) {
        log.append("[SEVERE] " + string + "\n");
    }

    public void message(final String string) {
        log.append(string + "\n");
    }
}
