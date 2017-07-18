package org.happy.events.chanserve;

import org.happy.ircbot.HappyBot;
import org.happy.listener.IrcEventListener;

/**
 * Author: Mossab
 */
public class CtcpEvent extends IrcEventListener {
    public CtcpEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(String line) throws Exception {
        bot.getWriter().write("PRIVMSG " + getSender(line) + " :" + bot.getNick() + " 0.0.1" + "\r\n");
    }

    @Override
    protected boolean validate(String line) {
        return line.contains("PRIVMSG " + bot.getNick() + " : VERSION");
    }

    @Override
    protected String getCommandKeyWord() {
        return NULL_COMMAND;
    }
}
