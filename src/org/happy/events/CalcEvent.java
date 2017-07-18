package org.happy.events;

import org.happy.ircbot.HappyBot;
import org.happy.listener.IrcEventListener;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Author: Mossab
 */
public class CalcEvent extends IrcEventListener {
    public CalcEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(String line) throws Exception {
        final String program = getPredicate(line);

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        bot.printLine(getChannel(line), engine.eval(program).toString(), false);
    }

    @Override
    protected boolean validate(String line) {
        return isMessage(line) && line.contains(getCommand());
    }

    @Override
    protected String getCommandKeyWord() {
        return "calc";
    }
}
