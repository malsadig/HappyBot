package org.happy.events.access;

import org.happy.events.RandomEvents;
import org.happy.ircbot.HappyBot;
import org.happy.listener.IrcEventListener;

/**
 * Author: Mossab
 */
public class RevokeEvent extends IrcEventListener {
    public RevokeEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(String line) throws Exception {
        final String predicate = getPredicate(line);

        for (final RandomEvents event : RandomEvents.values()) {
            if (event.getName().equals(predicate)) {
                bot.removeMyEventListener(event.getEvent());
                bot.printLine(getChannel(line), "removed " + event.getName(), false);
            }
        }
    }

    @Override
    protected boolean validate(String line) {
        return basicValidation(line);
    }

    @Override
    protected String getCommandKeyWord() {
        return "revoke";
    }
}