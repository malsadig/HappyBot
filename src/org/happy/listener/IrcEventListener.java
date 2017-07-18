package org.happy.listener;

import org.happy.ircbot.HappyBot;
import org.happy.ircbot.Irc;

/**
 * @author: Mossab
 */
public abstract class IrcEventListener {
    protected final static String NULL_COMMAND = "null";

    protected HappyBot bot;

    protected IrcEventListener(final HappyBot bot) {
        this.bot = bot;
    }

    protected abstract void handleEvent(final String line) throws Exception;

    protected abstract boolean validate(final String line);

    protected abstract String getCommandKeyWord();

    protected String getCommand() {
        return ":!" + getCommandKeyWord() + " ";
    }

    protected String getPredicate(final String line) {
        return line.substring(line.indexOf(getCommand()) + getCommand().length());
    }

    protected boolean basicValidation(final String line) {
        return line.contains(getCommand()) && getSender(line).equals(HappyBot.ADMIN_USER) && isMessage(line);
    }

    protected boolean accessValidation(final String line) {
        return Irc.ACCESS.containsKey(getSender(line)) && isMessage(line) && line.contains(getCommand());
    }

    protected boolean isPrivateMessage(final String line) {
        return false;
    }

    protected boolean isMessage(final String line) {
        for (final Byte b : line.getBytes()) {
            if (b.byteValue() == 123) {

            }
        }
        return line.contains(" PRIVMSG ");
    }

    protected boolean isKick(final String line) {
        return line.contains(" KICK #") && getKicked(line).equals(bot.getNick());
    }

    String getKicked(final String line) {
        final String channnel = "#" + getChannel(line) + " ";
        final String temp = line.substring(line.indexOf(channnel) + channnel.length());
        return temp.substring(0, temp.indexOf(" "));
    }

    protected boolean containsName(final String line) {
        return line.contains(":" + bot.getNick());
    }

    protected String getSender(final String line) {
        String toReturn;
        try {
            toReturn = line.substring(line.indexOf(":") + 1, line.indexOf("!"));
        } catch (final StringIndexOutOfBoundsException e) {
            return "";
        }
        return toReturn;
    }

    protected String getChannel(final String line) {
        final int beginIndex = line.lastIndexOf("#") + 1;
        int offSet = -1;
        String endString = line.substring(beginIndex);
        for (int i = 0; i < endString.length(); i++) {
            String substring = endString.substring(i, i + 1);
            if (substring.equals(" ") && offSet == -1) {
                offSet = i;
                break;
            }
        }
        return endString.substring(0, offSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IrcEventListener)) return false;

        return this.getClass().getSimpleName().toLowerCase().equals(o.getClass().getSimpleName().toLowerCase());
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().toLowerCase().hashCode();
    }
}
