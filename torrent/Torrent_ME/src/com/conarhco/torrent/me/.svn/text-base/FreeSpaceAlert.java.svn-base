/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.me;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import org.json.me.JSONObject;

/**
 * Отдельный класс алерта свободного места
 * @author Конарх
 */
public class FreeSpaceAlert extends Alert {
    public static final Command FREE_SPACE = new Command("Свободное место", Command.SCREEN, 2);
    private Displayable caller;

    public FreeSpaceAlert(Displayable caller){
        super("Свободное место");
        this.caller = caller;
        setTimeout(3000);
        //Инициализация
        caller.addCommand(FREE_SPACE);
    }

    //Реализация каллВеба и показа алерта с возвратом к коллеру
    public void getFreeSpace(){
         TorrentMID.inst().callWeb("free", new String[]{"login", TorrentMID.inst().getLogin()}, new CallWebProcessor() {

                    public void process(JSONObject root) throws Exception {
                        long freeSpace = Long.parseLong(root.get("freeSpace").toString());
                        FreeSpaceAlert.this.setString(TorrentMID.getSizeAsString(freeSpace));
                        Display.getDisplay(TorrentMID.inst()).setCurrent(FreeSpaceAlert.this, caller);
                    }
                });
    }

    

}
