package org.happy.events.chanserve;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;
import org.happy.listener.IrcEventListener;

public class ConnectEvent extends IrcEventListener {
    public ConnectEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(final String line) throws Exception {
        Irc.log.message("[" + bot.getNick() + "] CONNECTED!");
    }

    @Override
    protected boolean validate(final String line) {
        return line.contains("hopm-siglost!service@rizon.net NOTICE " + bot.getNick() + " :");
    }

    @Override
    protected String getCommandKeyWord() {
        return NULL_COMMAND;
    }
}
