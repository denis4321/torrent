/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.se;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author dprokopiuk
 */
public class DownloaderClientMain {

    public long sizeCounter;
    public final static String servletName = "getTorrent";
    private PopupMenu popup = new PopupMenu();
    private MenuItem exitMenuItem = new MenuItem("Exit");
    private MenuItem openMenuItem = new MenuItem("Open");
    private MenuItem historyMenuItem = new MenuItem("History");
    private MenuItem cleanQueue = new MenuItem("Clean queue");
    private Settings settings = new Settings();
    private TorrentWindow window = new TorrentWindow(settings);
    public final static String site = "http://82.207.44.155/torrent";//"http://127.0.0.1:8080/torrent";
    private static Timer t = new Timer(true);//основной
    private static Timer pingTimer = new Timer(true);
    // private static Timer errorT = new Timer(true);//Для звкрытия диалогов с ошибками
    private ServerRequestTask task = null;
    private static final ByteArrayOutputStream buf = new ByteArrayOutputStream();
    private static final PrintStream err = new PrintStream(buf, true);
    public final static String LOG = "log";
    private static Map<File, Long> torrentMap = new LinkedHashMap<File, Long>();
    public HistoryWindow historyWindow = new HistoryWindow(t);

    static {
        try {
            FileHandler historyHandler = new FileHandler("%t/torrent_se_acivity.log", true);
            historyHandler.setLevel(Level.CONFIG);
            historyHandler.setFormatter(new SimpleFormatter());
            FileHandler errorHandler = new FileHandler("%t/torrent_se_error.log", false);
            errorHandler.setLevel(Level.FINER);
            errorHandler.setFilter(new Filter() {

                public boolean isLoggable(LogRecord record) {
                    return record.getLevel() == Level.FINER;
                }
            });
            errorHandler.setFormatter(new SimpleFormatter());
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            Logger log = Logger.getLogger(LOG);
            log.setLevel(Level.ALL);
            log.setUseParentHandlers(false);
            log.addHandler(historyHandler);
            log.addHandler(errorHandler);
            log.addHandler(consoleHandler);
        } catch (Exception ex) {
            showError(null, ex);
        }
    }

    public DownloaderClientMain() throws AWTException {
        final SystemTray tray = SystemTray.getSystemTray();
        Image iconImg = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/2.gif"));
        final TrayIcon trayIcon = new TrayIcon(iconImg, "TorrentME", popup);
        popup.add(openMenuItem);
        popup.add(historyMenuItem);
        //      popup.add(cleanQueue);
        popup.add(exitMenuItem);
        cleanQueue.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = JOptionPane.showConfirmDialog(null, "Do you really want to empty the Queue?", "Approve", JOptionPane.YES_NO_OPTION);
                if (index == JOptionPane.OK_OPTION) {
                    clearTorrentsQueue();
                }
            }
        });

        openMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                window.setVisible(true);
            }
        });
        historyMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                historyWindow.setVisible(true);
            }
        });


        exitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                t.cancel();
                pingTimer.cancel();
                Logger.getLogger(LOG).info("-------------------------Torrent SE client stopped-------------------------");
                System.exit(0);
            }
        });
        trayIcon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                window.setVisible(true);
            }
        });

        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
        Logger.getLogger(LOG).info("Torrent SE client started");
        settings.load();
        if (!settings.isSet()) {
            window.setVisible(true);
        }
        t.scheduleAtFixedRate(new TimerTask() {//Таймер таск для скачивания

            @Override
            public void run() {
                File f = null;
                long size = -1;
                synchronized (DownloaderClientMain.class) {
                    if (!torrentMap.isEmpty()) {
                        f = torrentMap.keySet().iterator().next();
                        size = torrentMap.get(f);
                        long length = settings.getStorePath().getFreeSpace();
                        if ((length - sizeCounter - size - f.length()) < 0) {
                            f = null;
                        }
                    } else {
                        sizeCounter = 0;
                    }
                }
                if (f != null) {
                    try {
                        Runtime.getRuntime().exec("bittorrent \"" + f.getAbsolutePath() + "\"");
                        sizeCounter += torrentMap.get(f) + f.length();
                        long length = settings.getStorePath().getFreeSpace();
                        String freeSizeLeft = getSizeAsString(length - sizeCounter);
                        Logger.getLogger(LOG).info("file " + f.getName() + " has been started downloading (free space after downloading is going to be " + freeSizeLeft);
                        torrentMap.remove(f);
                    } catch (Exception ex) {
                        DownloaderClientMain.showError(null, ex);
                    }

                }
            }
        }, 1, 1000);
        callWeb();
        settings.addPropertyChangeListener("rate", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                callWeb();
            }
        });
    }

    private void clearTorrentsQueue() {
        synchronized (DownloaderClientMain.class) {
            torrentMap.clear();
        }
    }

    public static void main(String[] args) throws AWTException {
        /*SwingUtilities.invokeLater(new Runnable() {

        public void run() {
        try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        ex.printStackTrace();
        }
        }
        });*/
        Logger.getLogger(LOG).info("-------------------------Starting Torrent SE client...-------------------------");
        Logger.getLogger(LOG).config("server address is " + site);
        try {
            Logger.getLogger(LOG).config("user temp directory for logs is set to " + new File(System.getProperty("java.io.tmpdir")).getCanonicalPath());
        } catch (IOException ex) {
            showError(null, ex);
        }
        new DownloaderClientMain();
    }

    private void callWeb() {
        if (task != null) {
            task.cancel();
        }
        task = new ServerRequestTask(settings);
        Logger.getLogger(LOG).info("Start syncing with server, delay=" + settings.getRate() + " minute");
        pingTimer.scheduleAtFixedRate(task, 1, settings.getRate() * 1000);//For test
        //t.scheduleAtFixedRate(task, 1, settings.getRate() * 60 * 1000);
    }

    /**
     * показывать все ошибки через этот метод
     * @param parent
     * @param ex
     */
    public synchronized static void showError(Component parent, Throwable ex) {
        Logger.getLogger(LOG).throwing(null, null, ex);
        buf.reset();
        ex.printStackTrace(err);
        String txt = "<html><body bgcolor=#ff0000><pre>" + buf.toString() + "</pre></body></html>";
        JOptionPane error = new JOptionPane(new JLabel(txt), JOptionPane.ERROR_MESSAGE);
        final JDialog errorDiag = error.createDialog(parent, "Ошибка");
        final TimerTask errorTask = new TimerTask() {//Закрывает окно с ошибкой, чтобы не блокировать остальные

            @Override
            public void run() {
                errorDiag.dispose();
            }
        };
        errorDiag.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentHidden(ComponentEvent e) {
                errorTask.cancel();
            }
        });
        t.schedule(errorTask, 30000);
        errorDiag.setVisible(true);
    }

    //сохраняет файлы на закачку в очередь
    public synchronized static void addTorrentsToDownload(Map<File, Long> torrents) {
        torrentMap.putAll(torrents);
    }

    public synchronized static File getFileToDownload(long value) {
        Set<File> set = torrentMap.keySet();
        for (File f : set) {
            if (torrentMap.get(f) == value) {
                return f;
            }
        }
        throw new IllegalStateException();
    }

    private String getSizeAsString(long size) {
        String rez = (((size + 0.0) / (1024 * 1024 * 1024)) >= 1 ? (size + 0.0) / (1024 * 1024 * 1024) + "" : (size + 0.0) / (1024 * 1024) + "");
        int index = rez.indexOf(".");
        String dim = (((size + 0.0) / (1024 * 1024 * 1024)) >= 1 ? "Gb" : "Mb");
        if (index != -1) {
            String beforeDote = rez.substring(0, index + 1);
            String afterDote = "";
            int delta = rez.length() - index;
            if (delta >= 3) {
                afterDote = rez.substring(index + 1, index + 3);
            } else if (delta >= 2) {
                afterDote = rez.substring(index + 1, index + 2);
            } else {
                afterDote = rez.substring(index + 1, index + 1);
            }
            return beforeDote + afterDote + dim;
        }
        return rez;
    }
}
