package org.happy.events;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;
import org.happy.listener.IrcEventListener;

/**
 * Author: Mossab
 */
public class ProtectEvent extends IrcEventListener {
    private static final String LOOK_FOR = ".kb ";

    public ProtectEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(String line) throws Exception {
        if (line.contains(LOOK_FOR)) {
            final String toProtect = line.substring(line.indexOf(LOOK_FOR) + LOOK_FOR.length());

            if (Irc.protect.contains(toProtect)) {
                bot.printLine(getChannel(line), ".unban " + toProtect, false);
            }
        } else {
            final String toProtect = getPredicate(line);
            if (!Irc.protect.contains(toProtect)) {
                Irc.protect.add(toProtect);
            }
        }
    }

    @Override
    protected boolean validate(String line) {
        if (line.contains(LOOK_FOR)) {
            final String toProtect = line.substring(line.indexOf(LOOK_FOR) + LOOK_FOR.length());

            return Irc.protect.contains(toProtect);
        }

        return basicValidation(line);
    }

    @Override
    protected String getCommandKeyWord() {
        return "protect";
    }
}
