package org.happy.events;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;
import org.happy.listener.IrcEventListener;

public class BlackListEvent extends IrcEventListener {
    public BlackListEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(String line) throws Exception {
        final String toBlackList = getPredicate(line);

        if (Irc.blackList.contains(getSender(line)) && line.contains("JOIN")) {
            bot.printLine(bot.getChannels().get(0), ".k " + getSender(line), false);
        } else {
            if (!Irc.blackList.contains(toBlackList)) {
                Irc.blackList.add(toBlackList);
            }
        }
    }

    @Override
    protected boolean validate(String line) {
        return basicValidation(line) || (Irc.blackList.contains(getSender(line)) && line.contains("JOIN"));
    }

    @Override
    protected String getCommandKeyWord() {
        return "blacklist";
    }
}
