package org.happy.ircbot;

import org.happy.events.access.AddAccessEvent;
import org.happy.events.access.LoadEvent;
import org.happy.events.access.RemoveAccessEvent;
import org.happy.events.access.RevokeEvent;
import org.happy.events.chanserve.ConnectEvent;
import org.happy.events.chanserve.CtcpEvent;
import org.happy.events.chanserve.KickEvent;
import org.happy.events.chanserve.PingEvent;
import org.happy.listener.IrcEventDispatchThread;
import org.happy.listener.IrcEventListener;
import org.happy.util.cleverbot.ChatterBot;
import org.happy.util.cleverbot.ChatterBotFactory;
import org.happy.util.cleverbot.ChatterBotSession;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HappyBot {

    public static final String ADMIN_USER = "ADMIN_USER_NAME_HERE";

    private boolean identified = false;

    private ArrayList<String> channels;

    private final Socket socket;

    private boolean stop;

    private String readLine;

    private BufferedReader reader;
    private BufferedWriter writer;

    private List<IrcEventListener> listenerList;

    private Thread thread;

    private String nick;
    private String login;

    private ChatterBotFactory cbf;
    private ChatterBot cb;
    private ChatterBotSession cbs;

    public HappyBot(final String nick, final String login) throws Exception {
        this.listenerList = new ArrayList<IrcEventListener>();

        this.channels = new ArrayList<String>();
        this.socket = new Socket(Irc.HOST_NAME, Irc.PORT);

        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.nick = nick;
        this.login = login;

        this.cbf = new ChatterBotFactory();
        this.cb = cbf.create();
        this.cbs = cb.createSession();

        this.addIrcEventListener(new ConnectEvent(this));
        this.addIrcEventListener(new PingEvent(this));
        this.addIrcEventListener(new KickEvent(this));
        this.addIrcEventListener(new AddAccessEvent(this));
        this.addIrcEventListener(new RemoveAccessEvent(this));
        this.addIrcEventListener(new LoadEvent(this));
        this.addIrcEventListener(new RevokeEvent(this));
        this.addIrcEventListener(new CtcpEvent(this));

        this.thread = new Thread(new IrcEventDispatchThread(this));
        this.thread.start();
    }

    public void addIrcEventListener(final IrcEventListener listener) {
        listenerList.add(listener);
    }

    public void removeMyEventListener(final IrcEventListener listener) {
        listenerList.remove(listener);
    }

    public void exit() throws IOException {
        Irc.log.message("[" + nick + "] SHUTTING DOWN...");
        stop = true;
        if (reader != null) {
            reader.close();
        }
        if (writer != null) {
            writer.close();
        }
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        Irc.log.message("[" + nick + "] SHUT DOWN!");
    }

    public boolean connect() throws IOException {
        if (reader != null) {
            String line = null;
            do {
                if (line == null) {
                    writer.write("NICK " + nick + "\r\n");
                    writer.write("USER " + login + " 8 * : HappyBot\r\n");
                    writer.flush();
                } else if (line.contains("433")) {
                    Irc.log.severe("[" + nick + "] is already in use in channel, retrying as: [" + nick + "_" + "]");
                    nick += "_";
                    Irc.log.severe("Nickname is already in use, retrying as " + nick + ".");
                } else if (line.contains("004")) {
                    return true;
                }
            } while ((line = reader.readLine()) != null);
        }
        return false;
    }

    private boolean leaveChannel(final String channel) {
        try {
            if (channels.contains(channel)) {
                writer.write("LEAVE " + channel);
                writer.flush();
                channels.remove(channel);
                return true;
            } else {
                Irc.log.severe("Not in channel [" + channel + "]. Can't leave.");
                return false;
            }
        } catch (final IOException e) {
            Irc.log.severe("Could not leave channel [" + channel + "].");
            Irc.log.severe(e.getMessage());
            return false;
        }
    }

    public void joinChannel(final String channel) {
        try {
            if (getNick().equals("MossaBot") && !identified) {
                writer.write("PRIVMSG NickServ :IDENTIFY mossabis" + "\r\n");
                identified = true;
            }
            if (!channels.contains(channel)) {
                writer.write("JOIN #" + channel + "\r\n");
                writer.flush();
                channels.add(channel);
                Irc.log.message("[" + nick + "] Joined channel [" + channel + "].");
                return;
            } else {
                Irc.log.severe("Already in channel [" + channel + "]. Can't join.");
                return;
            }
        } catch (final IOException e) {
            Irc.log.severe("Could not connect to channel [" + channel + "].");
            Irc.log.severe(e.getMessage());
            return;
        }
    }

    public boolean printLine(final String channel, final String line, final boolean toServer) {
        try {
            if (channels.contains(channel)) {
                if (!toServer) {
                    writer.write("PRIVMSG #" + channel + " :" + line + "\r\n");
                    writer.flush();
                    Irc.log.message("[" + nick + "] Printed line: [" + line + "] in channel [" + channel + "].");
                    return true;
                } else {
                    writer.write("SQUERY " + line + "\r\n");
                    writer.flush();
                    Irc.log.message("[" + nick + "] Printed line: [" + line + "] to server.");
                    return true;
                }
            } else {
                Irc.log.severe("[" + nick + "] Not in that channel you derp [" + channel + "].");
                return false;
            }
        } catch (final IOException e) {
            Irc.log.severe("[" + nick + "] Could not print line: [" + line + "] in channel [" + channel + "].");
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<String> channels) {
        this.channels = channels;
    }

    public boolean isStopped() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public String getReadLine() {
        return readLine;
    }

    public void setReadLine(String readLine) {
        this.readLine = readLine;
    }

    public List<IrcEventListener> getListenerList() {
        return listenerList;
    }

    public void setListenerList(List<IrcEventListener> listenerList) {
        this.listenerList = listenerList;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ChatterBotFactory getCbf() {
        return cbf;
    }

    public void setCbf(ChatterBotFactory cbf) {
        this.cbf = cbf;
    }

    public ChatterBot getCb() {
        return cb;
    }

    public void setCb(ChatterBot cb) {
        this.cb = cb;
    }

    public ChatterBotSession getCbs() {
        return cbs;
    }

    public void setCbs(ChatterBotSession cbs) {
        this.cbs = cbs;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }
}
