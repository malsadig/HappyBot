package org.happy.events;

import org.happy.ircbot.Irc;
import org.happy.listener.IrcEventListener;

public enum RandomEvents {
    CLEVER_BOT(new CleverBotEvent(Irc.happyBot)),
    COMPILE(new CompileEvent(Irc.happyBot)),
    ECHO(new EchoEvent(Irc.happyBot)),
    CALC(new CalcEvent(Irc.happyBot)),
    BLACKLIST(new BlackListEvent(Irc.happyBot)),
    PROTECT(new ProtectEvent(Irc.happyBot));

    private IrcEventListener event;

    RandomEvents(IrcEventListener event) {
        this.event = event;
    }

    public IrcEventListener getEvent() {
        return event;
    }

    public String getName() {
        return this.name().toLowerCase();
    }
}
