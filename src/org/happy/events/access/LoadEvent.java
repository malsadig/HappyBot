package org.happy.events.access;

import org.happy.events.RandomEvents;
import org.happy.ircbot.HappyBot;
import org.happy.listener.IrcEventListener;

/**
 * Author: Mossab
 */
public class LoadEvent extends IrcEventListener {
    public LoadEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(String line) throws Exception {
        final String load = getPredicate(line);

        for (final RandomEvents event : RandomEvents.values()) {
            if (event.getName().equals(load)) {
                if (!bot.getListenerList().contains(event.getEvent())) {
                    bot.addIrcEventListener(event.getEvent());
                    bot.printLine(getChannel(line), "loaded " + event.getName(), false);
                } else {
                    bot.printLine(getChannel(line), "already loaded " + event.getName(), false);
                }
            }
        }
    }

    @Override
    protected boolean validate(String line) {
        return basicValidation(line);
    }

    @Override
    protected String getCommandKeyWord() {
        return "load";
    }
}
