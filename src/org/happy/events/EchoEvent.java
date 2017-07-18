package org.happy.events;

import org.happy.ircbot.HappyBot;
import org.happy.listener.IrcEventListener;

/**
 * @author: Mossab
 */
public class EchoEvent extends IrcEventListener {
    public EchoEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(final String line) throws Exception {
        final String send = getPredicate(line);
        bot.printLine(getChannel(line), send, false);
    }

    @Override
    protected boolean validate(final String line) {
        return accessValidation(line);
    }

    @Override
    protected String getCommandKeyWord() {
        return "echo";
    }
}
