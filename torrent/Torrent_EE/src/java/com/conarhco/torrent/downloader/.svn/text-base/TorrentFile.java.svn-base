/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.downloader;

/**
 *
 * @author Конарх
 */
public class TorrentFile {
    private String name;
    private long size;
    private byte[] torrent;

    public TorrentFile(String name, long size, byte[] torrent) {
        this.name = name;
        this.size = size;
        this.torrent = torrent;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public byte[] getTorrent() {
        return torrent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass()==TorrentFile.class) {
            return name.equalsIgnoreCase(((TorrentFile) obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name+" size="+size;
    }




}
