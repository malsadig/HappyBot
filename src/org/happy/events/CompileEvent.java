package org.happy.events;

import org.happy.ircbot.HappyBot;
import org.happy.listener.IrcEventListener;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.Arrays;

/**
 * @author: Mossab
 */
public class CompileEvent extends IrcEventListener {
    public CompileEvent(final HappyBot bot) {
        super(bot);
    }

    @Override
    protected void handleEvent(final String line) throws Exception {
        final String program = getPredicate(line);
        final String mainClass = "public class Compilation {" + program + "}";

        if (mainClass.contains("throws") || mainClass.contains("try")) {
            bot.printLine(getChannel(line), getSender(line) + ", no.", false);
            return;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        JavaFileObject file = new JavaSourceFromString("Compilation", mainClass);

        String[] compileOptions = new String[]{"-d", "out"};
        Iterable<String> compilationOptionss = Arrays.asList(compileOptions);

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, compilationOptionss, null, compilationUnits);

        boolean success = task.call();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            if (diagnostic.getKind().toString().equals("ERROR")) {
                for (final String string : diagnostic.getMessage(null).split("\n")) {
                    bot.printLine(getChannel(line), string, false);
                }
            }
        }

        if (success) {
            final PipedOutputStream po = new PipedOutputStream();
            final PipedInputStream pi = new PipedInputStream(po);
            final PrintStream original = System.out;
            System.setOut(new PrintStream(po));
            final BufferedReader reader = new BufferedReader(new InputStreamReader(pi));

            final Class t = Class.forName("Compilation");
            t.getDeclaredMethod("main", String[].class).invoke(null, new Object[]{null});

            String string;
            while ((string = reader.readLine()) != null) {
                bot.printLine(getChannel(line), string, false);
            }
            System.setOut(original);
        }
    }

    @Override
    protected boolean validate(String line) {
        return accessValidation(line);
    }

    @Override
    protected String getCommandKeyWord() {
        return "compile";
    }

    private class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}