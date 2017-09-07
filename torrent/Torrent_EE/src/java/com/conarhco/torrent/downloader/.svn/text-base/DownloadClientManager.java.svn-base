/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.downloader;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Конарх
 */
public class DownloadClientManager {
  //  private  final Object ADD_REMOVE_MONITOR = new Object();
   // private final Object FREE_SPACE_MONITOR = new Object();
    private final Map<String, Set<TorrentFile>> downloadFilesMap = new HashMap<String, Set<TorrentFile>>();
    private final Map<String,Long> freeSpaceMap = new HashMap<String,Long>();

    public DownloadClientManager(){

    }

    public void addTorrentFile(String login, TorrentFile torrent){
        synchronized(downloadFilesMap){
            Set<TorrentFile> list = downloadFilesMap.get(login);
            if(list==null){
                list = new LinkedHashSet<TorrentFile>();
                downloadFilesMap.put(login, list);
            }
            int i=2;
            String name = torrent.getName();
            while(list.contains(torrent)){
                torrent.setName(name+" file "+i);
                i++;
            }
            list.add(torrent);
        }
    }

    /**
     * @param login
     * return
     */
    public Set<TorrentFile> getFilmsToDownload(String login){
        synchronized(downloadFilesMap){
            Set<TorrentFile> list = downloadFilesMap.get(login);
            Set<TorrentFile> reply = new LinkedHashSet<TorrentFile>();
            if (list!=null){
                reply.addAll(list);
                list.clear();
            }
            return reply;
        }
    }

    public void setFreeSpace(String login, long free){
        synchronized(freeSpaceMap){
            freeSpaceMap.put(login, free);
        }
    }

    public long getFreeSpace(String login){
        synchronized(freeSpaceMap){
            return freeSpaceMap.get(login);
        }
    }

}
