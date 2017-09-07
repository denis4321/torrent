/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.ee;

/**
 *
 * @author dprokopiuk
 */
public class Film {
    private String title;
    private String detailsHref;
    private long size;
    private String downloadHref;
    private int seed;
    //private int lich;
    
    public Film() {
    }

    public String getDetailsHref() {
        return detailsHref;
    }

    public void setDetailsHref(String detailsHref) {
        //./viewtopic.php?t=3676224
        this.detailsHref = "http://rutracker.org/forum"+detailsHref.substring(1,detailsHref.length());
    }

    public String getDownloadHref() {
        return downloadHref;
    }

    public void setDownloadHref(String downloadHref) {
        this.downloadHref = downloadHref;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("\ntitle= "+this.getTitle());
       sb.append("\ntitle href= "+this.getDetailsHref());
       sb.append("\nsize= "+getSize()+" bytes");
       sb.append("\nsize href= "+this.getDownloadHref());
       sb.append("\nseed= "+this.getSeed());
       sb.append("\n-----------------");
       return sb.toString();
    }
    
}
