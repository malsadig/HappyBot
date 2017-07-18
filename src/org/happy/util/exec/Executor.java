package org.happy.util.exec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Mossab
 *         <p/>
 *         Stream handler for relaying cmd prompts and their responses.
 */
class Executor extends Thread {

    private InputStream input = null;
    private String response = null;

    public Executor(InputStream input) {
        this.input = input;
    }

    public void run() {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(input));
            final StringBuilder buffer = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            response = buffer.toString();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        return response;
    }

    public InputStream getInput() {
        return input;
    }
}