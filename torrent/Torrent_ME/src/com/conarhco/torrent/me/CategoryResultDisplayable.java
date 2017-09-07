/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.me;

import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

/**
 * @author 777
 */
public class CategoryResultDisplayable extends List implements CommandListener{
    private final Command BACK = new Command("Назад", Command.BACK, 1);
    private final Command DETAILS = new Command("Подробно", Command.OK, 2);
    private SearchResultDisplayable form;
    private Hashtable data = new Hashtable();
     private FreeSpaceAlert freeSpaceAlert;
   // public Alert downloadAlert = new Alert("");
    private DetailsForm details;
    private int catId;


    public CategoryResultDisplayable(SearchResultDisplayable form, int id) {
        super("", List.IMPLICIT);
        setCommandListener(this);
        this.addCommand(BACK);
        this.addCommand(DETAILS);
         freeSpaceAlert = new FreeSpaceAlert(this);
        //this.addCommand(TorrentMID.FREE_SPACE);
        this.form=form;
        catId = id;
   }

   private int getFilmIndex(){
         String selected=getString(this.getSelectedIndex());
        
         int id = ((Integer)data.get(selected)).intValue();
         return id;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == List.SELECT_COMMAND) {
            //Alert proofAlert = new DownloadConfirmDisplayable(this, catId, getFilmIndex());
            //Display.getDisplay(TorrentMID.inst()).setCurrent(proofAlert);
            Form proofForm = new DownloadConfirmDisplayable(this, catId, getFilmIndex());
            Display.getDisplay(TorrentMID.inst()).setCurrent(proofForm);
        } else if (c == BACK) {
            //this.deleteAll();
            Display.getDisplay(TorrentMID.inst()).setCurrent(form);
        }  else if (c == DETAILS) {
          
            TorrentMID.inst().callWeb("details", new String[]{"id", Integer.toString(catId),"filmId", Integer.toString(getFilmIndex())}, new CallWebProcessor() {
                public void process(JSONObject root) throws Exception {
                    details = new DetailsForm(form,CategoryResultDisplayable.this, catId, getFilmIndex());
                    JSONArray jArray = root.getJSONArray("details");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject obj = jArray.getJSONObject(i);
                        String key = (String) obj.keys().nextElement();
                        String value=obj.get(key).toString();
                        //String country=obj.get("country").toString();
                        //String title = obj.get("title").toString();
                        //long size = Long.parseLong(obj.get("size").toString());
                        //String seed = obj.get("seed").toString();
                        //int id = Integer.parseInt(obj.get("id").toString());
                        //filmsInCategory.add(title, size, seed, id);
                        StringItem item = new StringItem(null,value+"\n",StringItem.PLAIN);
                        item.setLayout(Item.LAYOUT_LEFT&Item.LAYOUT_NEWLINE_AFTER);
                        details.append(item);
                    }
                    //details.append(root.getString("details"));
                    Display.getDisplay(TorrentMID.inst()).setCurrent(details);
                }             
            });
        }else if(c==FreeSpaceAlert.FREE_SPACE){
            freeSpaceAlert.getFreeSpace();
        }else{
            TorrentMID.inst().commandAction(c, d);
        }

    }

    public void add(String title, long size, String seed, int id){
        String s=title+" размер:";//+size+" "+seed;
        //s+=(size/(1024*1024*1024)>=1 ?Math.ceil((size+0.0)/(1024*1024*1024))+"Gb " : Math.ceil((size+0.0)/(1024*1024))+"Mb ");
        s+=TorrentMID.getSizeAsString(size);
        s+="Раздающих:"+seed;
       // String s=title+" "+size+" "+seed;
        data.put(s, new Integer(id));
        append(s,null);
    }

}
