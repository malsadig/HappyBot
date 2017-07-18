package org.happy.util.gui;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.Random;

@SuppressWarnings("unchecked")
public class IrcGui extends JFrame {
    private JList nickList;
    private JList chanList;
    private JTextField toSay;

    public IrcGui() {
        JScrollPane nickListScrollPane = new JScrollPane();
        nickList = new JList();
        JScrollPane chanListScrollPane = new JScrollPane();
        chanList = new JList();
        JLabel nickLabel = new JLabel();
        JLabel channelLabel = new JLabel();
        JButton addNick = new JButton();
        JButton addChanel = new JButton();
        JButton delete = new JButton();
        toSay = new JTextField();
        JButton speak = new JButton();
        JButton privateMessage = new JButton();

        setTitle("IRC Bot");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(425, 225);
        setLocationRelativeTo(getOwner());

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        chanList.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                changeChans();
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });
        nickList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                changeChans();
            }
        });
        nickListScrollPane.setViewportView(nickList);
        contentPane.add(nickListScrollPane);
        nickListScrollPane.setBounds(210, 30, 200, 95);

        chanListScrollPane.setViewportView(chanList);
        contentPane.add(chanListScrollPane);
        chanListScrollPane.setBounds(10, 30, 195, 95);

        nickLabel.setText("Nick:");
        contentPane.add(nickLabel);
        nickLabel.setBounds(new Rectangle(new Point(210, 10), nickLabel.getPreferredSize()));

        channelLabel.setText("Channel:");
        contentPane.add(channelLabel);
        channelLabel.setBounds(new Rectangle(new Point(10, 10), channelLabel.getPreferredSize()));

        addNick.setText("Add Nick");
        contentPane.add(addNick);
        addNick.setBounds(320, 130, 89, addNick.getPreferredSize().height);
        addNick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String nick = JOptionPane.showInputDialog(null, "Nick name?");
                ListModel model = nickList.getModel();
                DefaultListModel listModel = new DefaultListModel();
                for (int i = 0; i < model.getSize(); i++) {
                    listModel.addElement(model.getElementAt(i));
                }
                if (nick != null && !nick.equals("") && nick.length() > 6 && !listModel.contains(nick)) {
                    listModel = new DefaultListModel();
                    for (int i = 0; i < model.getSize(); i++) {
                        listModel.addElement(model.getElementAt(i));
                    }
                    listModel.addElement(nick);
                    nickList.setModel(listModel);

                    final String login = generateLogin();
                    Irc.log.message("[" + nick + "] CONNECTING...");
                    JOptionPane.showMessageDialog(null, "Created bot " + nick + " with login " + login + ".");

                    HappyBot bot;
                    try {
                        bot = new HappyBot(nick, login);
                        if (bot.connect()) {
                            Irc.happyBot = bot;
                        }
                    } catch (final Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            private String generateLogin() {
                final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                char[] text = new char[15];
                for (int i = 0; i < 15; i++) {
                    text[i] = characters.charAt(new Random().nextInt(characters.length()));
                }
                return new String(text);
            }
        });

        addChanel.setText("Add Channel to Nick");
        contentPane.add(addChanel);
        addChanel.setBounds(210, 160, 200, addChanel.getPreferredSize().height);
        addChanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nickList.isSelectionEmpty()) {
                    final String botName = nickList.getSelectedValue().toString();
                    final String channel = JOptionPane.showInputDialog(null, "Channel?");

                    ListModel model = chanList.getModel();
                    DefaultListModel listModel = new DefaultListModel();
                    for (int i = 0; i < model.getSize(); i++) {
                        listModel.addElement(model.getElementAt(i));
                    }

                    if (channel != null && !channel.equals("")) {
                        if (!Irc.happyBot.getChannels().contains(channel)) {
                            Irc.happyBot.joinChannel(channel);
                        }
                    }
                }
            }
        });

        delete.setText("Delete Nick");
        contentPane.add(delete);
        delete.setBounds(210, 130, 105, delete.getPreferredSize().height);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nickList.isSelectionEmpty()) {
                    final String botName = nickList.getSelectedValue().toString();

                    try {
                        Irc.happyBot.exit();
                        ListModel model = nickList.getModel();
                        DefaultListModel listModel = new DefaultListModel();
                        for (int i = 0; i < model.getSize(); i++) {
                            if (!model.getElementAt(i).toString().equals(botName)) {
                                listModel.addElement(model.getElementAt(i));
                            }
                        }
                        nickList.setModel(listModel);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        contentPane.add(toSay);
        toSay.setBounds(10, 130, 195, 25);

        speak.setText("Say");
        contentPane.add(speak);
        speak.setBounds(new Rectangle(new Point(10, 160), speak.getPreferredSize()));
        speak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nickList.getSelectedValue() != null && chanList.getSelectedValue() != null) {
                    if (toSay.getText() != null && !toSay.getText().equals("")) {
                        final String nick = nickList.getSelectedValue().toString();
                        final String channel = chanList.getSelectedValue().toString();
                        Irc.happyBot.printLine(channel, toSay.getText(), false);
                        toSay.setText("");
                    }
                }
            }
        });

        privateMessage.setText("Private Message");
        contentPane.add(privateMessage);
        privateMessage.setBounds(70, 160, 135, privateMessage.getPreferredSize().height);
    }

    private void changeChans() {
        if (nickList.getSelectedValue() != null) {
            final DefaultListModel model = new DefaultListModel();
            for (String chan : Irc.happyBot.getChannels()) {
                model.addElement(chan);
            }
            chanList.setModel(model);
        }
    }
}