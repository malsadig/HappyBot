package org.happy.events.chanserve;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;
import org.happy.listener.IrcEventListener;

/**
 * @author: Mossab
 */
public class KickEvent extends IrcEventListener {
    public KickEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(final String line) throws Exception {
        Irc.log.severe("[" + bot.getNick() + "] KICKED FROM [" + getChannel(line) + "]");
        bot.getChannels().remove(getChannel(line));
        bot.joinChannel(getChannel(line));
    }

    @Override
    protected boolean validate(final String line) {
        return isKick(line);
    }

    @Override
    protected String getCommandKeyWord() {
        return NULL_COMMAND;
    }
}
