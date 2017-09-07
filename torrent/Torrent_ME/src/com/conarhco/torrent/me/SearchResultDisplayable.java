/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.me;

import javax.microedition.lcdui.List;

import java.util.Hashtable;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

/**
 *
 * @author 777
 */
public class SearchResultDisplayable extends List implements CommandListener {

    private FreeSpaceAlert freeSpaceAlert;
    private Hashtable content = new Hashtable();
    //private CategoryResultDisplayable filmsInCategory;
    //private int catId;

    public SearchResultDisplayable() {
        super("Результаты по запросу", List.IMPLICIT);
        this.setCommandListener(this);
        this.addCommand(TorrentMID.BACK);
        freeSpaceAlert = new FreeSpaceAlert(this);
    }

    public void addCategory(String name, int id) {
        content.put(name, new Integer(id));
        append(name, null);
    }

    public int getCategoryID(String name) {
        return Integer.parseInt(content.get(name).toString());
    }

    public void commandAction(Command c, Displayable d) {
        if (c == List.SELECT_COMMAND) {
            String selected = getString(this.getSelectedIndex());
            int id = ((Integer) content.get(selected)).intValue();
            final CategoryResultDisplayable filmsInCategory = new CategoryResultDisplayable(SearchResultDisplayable.this, id);
            //setCatId(id);
            TorrentMID.inst().callWeb("films", new String[]{"id", Integer.toString(id)}, new CallWebProcessor() {

                public void process(JSONObject root) throws Exception {
                    //TorrentMID.inst().setSessionID(root.getString("sid"));
                    JSONArray jArray = root.getJSONArray("film");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject obj = (JSONObject) jArray.get(i);
                        String title = obj.get("title").toString();
                        long size = Long.parseLong(obj.get("size").toString());
                        String seed = obj.get("seed").toString();
                        int id = Integer.parseInt(obj.get("id").toString());
                        filmsInCategory.add(title, size, seed, id);
                    }
                    Display.getDisplay(TorrentMID.inst()).setCurrent(filmsInCategory);
                }
            });
        } else if(c==FreeSpaceAlert.FREE_SPACE){
            freeSpaceAlert.getFreeSpace();
        } else {
            TorrentMID.inst().commandAction(c, d);
        }
    }

    /*public int getCatId() {
    return catId;
    }

    public void setCatId(int catId) {
    this.catId = catId;
    }*/
}
