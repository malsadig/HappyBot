package org.happy.util.exec;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author: Mossab
 */
public class Processor {
    public static String getResponse(final String command) {
        Runtime rt = Runtime.getRuntime();
        Executor output;
        try {
            Process proc = rt.exec(command);
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println(command);
            out.close();

            output = new Executor(proc.getInputStream());
            int exitVal = 0;

            output.start();
            output.join(3000);
            exitVal = proc.waitFor();
            return output.getResponse();
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
