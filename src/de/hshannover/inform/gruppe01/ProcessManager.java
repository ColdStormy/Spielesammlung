package de.hshannover.inform.gruppe01;

import java.io.File;
import java.io.IOException;

/**
 * Created by jannis on 07.12.17.
 */
public class ProcessManager {

    private GUI gui;
    private Process process = null;
    private String jar = null;

    public ProcessManager(GUI g) {
        gui = g;
    }

    public void setJar(String pathToJar) {
        jar = pathToJar;
    }

    /**
     *
     * @return on success true
     */
    public boolean run() {
        System.out.println("Starting jar: "+jar);
        if( process != null || jar == null ) {
            // process already running or no jar specified
            return false;
        }
        try {
            process = Runtime.getRuntime().exec("java -jar "+jar);
            return startExitDetector();
        } catch (IOException e) {
            System.out.println("Jar possibly missing or wrong path..?");
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean startExitDetector() {
        if( process == null )
            throw new IllegalArgumentException("Process is null..");

        ProcessExitDetector processExitDetector;
        try {
            processExitDetector = new ProcessExitDetector(process);
        } catch (IllegalArgumentException e) {
            return false;
        }
        processExitDetector.addProcessListener(new ProcessListener() {
            @Override
            public void processFinished(Process p) {
                gui.gameFinished();
                process = null;
            }
        });
        processExitDetector.start();

        return true;
    }

}
