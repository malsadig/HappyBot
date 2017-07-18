package org.happy.events.chanserve;

import org.happy.ircbot.HappyBot;
import org.happy.listener.IrcEventListener;

/**
 * @author: Mossab
 */
public class PingEvent extends IrcEventListener {
    public PingEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(final String line) throws Exception {
        bot.getWriter().write("PONG " + line.substring(5) + "\r\n");
    }

    @Override
    protected boolean validate(final String line) {
        return line.startsWith("PING ");
    }

    @Override
    protected String getCommandKeyWord() {
        return NULL_COMMAND;
    }
}
