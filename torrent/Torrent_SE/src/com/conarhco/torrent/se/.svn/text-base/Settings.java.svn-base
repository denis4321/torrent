/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.se;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.beans.*;
import java.util.logging.Logger;

/**
 * @author dprokopiuk
 */
public class Settings {
    private PropertyChangeSupport listener = new PropertyChangeSupport(this);
    private static final int DEFAULT_RATE = 5;
    private int rate = DEFAULT_RATE;
    private File storePath = null;
    private String login = null;

    public Settings() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getRate() {
        return rate;
    }

    /**
     * bound
     * @param rate
     */
    public void setRate(int rate) {
        int old = this.rate;
        this.rate = rate;
        listener.firePropertyChange("rate", old, rate);
    }


    public File getStorePath() {
        return storePath;
    }

    public void setStorePath(File storePath) {
        this.storePath = storePath;
    }

    public boolean isSet() {
        return login != null && storePath != null;
    }

    public void save() throws BackingStoreException {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.putInt("rate", rate);
        if (login != null) {
            prefs.put("login", login);
        }
        if (storePath != null) {
            prefs.put("storePath", storePath.getAbsolutePath());
        }
        prefs.sync();
        Logger.getLogger(DownloaderClientMain.LOG).info("user properties saved");
    }

    public void load() {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        setRate(prefs.getInt("rate", DEFAULT_RATE));
        setLogin(prefs.get("login", null));
        String storePathStr = prefs.get("storePath", null);
        if (storePathStr != null) {
            this.setStorePath(new File(storePathStr));
        }
        Logger.getLogger(DownloaderClientMain.LOG).info("user properties loaded");
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
        listener.addPropertyChangeListener(propertyName, l);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        listener.removePropertyChangeListener(l);
    }

    @Override
    public String toString() {
        if (login == null || rate == 0 || storePath == null) {
            return "SETTINGS WERE NOT FOUNDED";
        }
        return "login=" + getLogin() + "\nrate=" + getRate() + "\npath=" + getStorePath().getAbsolutePath();
    }
}
