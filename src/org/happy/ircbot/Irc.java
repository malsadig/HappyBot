package org.happy.ircbot;

import org.happy.util.gui.IrcGui;
import org.happy.util.gui.Logger;

import javax.swing.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Irc {
    public static Logger log;
    public final static String HOST_NAME = "irc.rizon.net";
    public static final int PORT = 6667;

    public static final Map<String, Integer> ACCESS = new HashMap<String, Integer>();

    public static final List<String> blackList = new ArrayList<String>();
    public static final List<String> protect = new ArrayList<String>();

    public static HappyBot happyBot;

    public static final PrintStream ps = System.out;

    public static void main(String... args) {
        ACCESS.put("HappyBot", 0);
        // add other uses here for access to commands

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log = new Logger();
                log.setVisible(true);
                new IrcGui().setVisible(true);
            }
        });
    }
}
