/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.se;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Переделать под таймерТаск
 * @author Denis
 */
public class LogTask extends TimerTask{
    private JTextArea textArea;
    private File logFile;
    private long oldSizeOfFile = -1;

    public LogTask(JTextArea textArea, File logFile){
        this.textArea=textArea;
        this.logFile=logFile;
        //oldSizeOfFile = logFile.lastModified();
        //setPriority(Thread.NORM_PRIORITY-2);
        //textArea.setText(readFromLog(logFile));
    }

    @Override
    public void run() {
        long currentSpace = logFile.length();//переделать под ласт модифайд или ленгтх
        //System.out.println(System.currentTimeMillis()+": lastModified: old="+oldSizeOfFile+" curr="+currentSpace);
        if (oldSizeOfFile < currentSpace) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    String line;
                    try {
                        line = readFromLog(logFile);
                        textArea.setText(line);
                    } catch (IOException ex) {
                        DownloaderClientMain.showError(null, ex);
                    }
                }
            });
            oldSizeOfFile = currentSpace;
            //Thread.sleep(delay);
        }
    }

     private String readFromLog(File logFile) throws FileNotFoundException, IOException  {
        FileReader reader = null;
        reader = new FileReader(logFile);
        StringBuilder sb = new StringBuilder();
        BufferedReader buff = new BufferedReader(reader);
        String line = "";
        while ((line = buff.readLine()) != null) {
            sb.append(line + "\n");
        }
        buff.close();
        reader.close();
        return sb.toString();
    }

}
