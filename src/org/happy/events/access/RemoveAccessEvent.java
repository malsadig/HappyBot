package org.happy.events.access;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;
import org.happy.listener.IrcEventListener;

import java.util.StringTokenizer;

/**
 * @author: Mossab
 */
public class RemoveAccessEvent extends IrcEventListener {
    public RemoveAccessEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(final String line) throws Exception {
        final String personAndNumber = getPredicate(line);

        final StringTokenizer tokenizer = new StringTokenizer(personAndNumber);

        final String person = tokenizer.nextToken();
        final int num = Integer.parseInt(tokenizer.nextToken());

        Integer result = Irc.ACCESS.remove(person);
        if (result != null && result != -1) {
            bot.printLine(getChannel(line), "removed " + person + " from access", false);
        }
    }

    @Override
    protected boolean validate(final String line) {
        return basicValidation(line);
    }

    @Override
    protected String getCommandKeyWord() {
        return "removeaccess";
    }
}
