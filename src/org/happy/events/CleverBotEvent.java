package org.happy.events;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;
import org.happy.listener.IrcEventListener;

public class CleverBotEvent extends IrcEventListener {
    public CleverBotEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(String line) throws Exception {
        bot.printLine(getChannel(line), getSender(line) + ": " + bot.getCbs().think(line), false);
    }

    @Override
    protected boolean validate(String line) {
        return Irc.ACCESS.containsKey(getSender(line)) && isMessage(line) && line.contains(bot.getNick());
    }

    @Override
    protected String getCommandKeyWord() {
        return NULL_COMMAND;
    }
}