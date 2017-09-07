/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.se;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author 777
 */
public class ServerRequestTask extends TimerTask {

    private Settings settings;
    private URL url;
    private SocketException lastEx = null;

    public ServerRequestTask(Settings settings) {
        super();
        this.settings = settings;
    }

    @Override
    public void run() {
        try {
            long freeSpace = getFreeSpace(settings.getStorePath());
            String login = settings.getLogin();
            url = new URL(DownloaderClientMain.site + "/" + DownloaderClientMain.servletName + "?login=" + login + "&freeSpace=" + freeSpace);
            System.out.println("Sending to server: " + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int rc = conn.getResponseCode();
            System.out.println("Server reply: " + rc);
            if (rc != 200) {
                Logger.getLogger(DownloaderClientMain.LOG).warning("server has gone");
            } else {
                StringBuilder content = new StringBuilder();
                int v = -1;
                InputStream in = conn.getInputStream();
                while ((v = in.read()) >= 0) {
                    content.append(new String(new byte[]{(byte) v}, "windows-1251"));
                }
                Map<File, Long> torrents = parseTorrentFiles(content.toString());
                DownloaderClientMain.addTorrentsToDownload(torrents);
                //connsetDoOutput(true);
                //conn.setRequestProperty("Content-type", "text/xml");
                if (lastEx != null) {
                    Logger.getLogger(DownloaderClientMain.LOG).warning("connection to server established");
                    lastEx = null;
                }
            }
        } catch (SocketException ex) {
            //System.out.println(ex);
            if (!(lastEx == null ? "" : lastEx.toString()).equals(ex.toString())) {
                lastEx = ex;
                Logger.getLogger(DownloaderClientMain.LOG).warning("connection to server lost");
                DownloaderClientMain.showError(null, ex);
            }
        } catch (Exception ex) {
            DownloaderClientMain.showError(null, ex);
        }

    }

    private long getFreeSpace(File file) {
        while (file.getParentFile() != null) {
            file = file.getParentFile();
        }
        return file.getUsableSpace();
    }

    /**
     * Парсит из текста джейсоновский массив. По мере парсинга создает и пишет в директорию для скачивания .торрент файлы
     * Пишет в лог "файл такой-то (длина) получен для скачивания"
     * @param полученный контент
     * @return мапу файлов для скачивания с размерами. Порядок файлов совпадает с порядком в массиве
     */
    private Map<File, Long> parseTorrentFiles(String resp) {
        Map<File, Long> map = new LinkedHashMap<File, Long>();
        try {
            JSONArray jArray = new JSONArray(resp);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = (JSONObject) jArray.get(i);
                String name = obj.get("name").toString();
                long size = Long.parseLong(obj.get("size").toString());

                File file = new File(settings.getStorePath(), name + ".torrent");

                //while(settings.getStorePath().get){

                //}
                File[] files = settings.getStorePath().listFiles(new FileFilter() {

                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".torrent");
                    }
                });
                List<File> fileList = Arrays.asList(files);
                int j = 1;
                String names = "";
                while (fileList.contains(file)) {
                    names = name + "file" + j++;
                    file = new File(settings.getStorePath(), names + ".torrent");
                }
                name = names;

                //for(int j=0;j<files.length;j++){
                //  files[i].getName().equals(name+".torrent");

                //}

                //File file = new File(settings.getStorePath(), name+".torrent");


                FileOutputStream fOut = new FileOutputStream(file);
                String torrent = obj.get("torrent").toString();
                StringTokenizer st = new StringTokenizer(torrent, ",");
                int index = 0;
                List<Byte> bytes = new ArrayList<Byte>();
                while (st.hasMoreElements()) {
                    String s = st.nextElement().toString();
                    if (index == 0) {
                        s = s.substring(1, s.length());
                    }
                    if (s.indexOf("]") != -1) {
                        s = s.substring(0, s.length() - 1);
                    }
                    bytes.add(Byte.parseByte(s));
                    index++;
                }
                byte[] buf = new byte[bytes.size()];
                for (int g = 0; g < bytes.size(); g++) {
                    buf[g] = bytes.get(g);
                }
                fOut.write(buf, 0, buf.length);
                fOut.close();
                map.put(file, size);
                Logger.getLogger(DownloaderClientMain.LOG).info("file " + name + ".torrent size(" + size + " bytes) has been created");
            }
        } catch (Exception ex) {
            DownloaderClientMain.showError(null, ex);
        }
        return map;
    }
}
