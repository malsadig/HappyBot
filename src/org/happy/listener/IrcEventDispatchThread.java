package org.happy.listener;

import org.happy.ircbot.HappyBot;

import java.io.IOException;
import java.util.List;

/**
 * @author: Mossab
 */
public class IrcEventDispatchThread implements Runnable {
    private final HappyBot bot;

    public IrcEventDispatchThread(final HappyBot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        while (!bot.isStopped()) {
            String line = null;
            try {
                line = bot.getReader().readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bot.setReadLine(line);
            if (line != null) {
                System.out.println(line);
                final List<IrcEventListener> list = bot.getListenerList();
                for (int i = 0; i < list.size(); i++) {
                    try {
                        if (list.get(i).validate(line)) {
                            list.get(i).handleEvent(line);
                            bot.getWriter().flush();
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
