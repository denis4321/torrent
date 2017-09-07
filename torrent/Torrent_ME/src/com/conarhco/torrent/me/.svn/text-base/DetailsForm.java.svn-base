/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.me;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author 777
 */
public class DetailsForm extends Form implements CommandListener{
    private final Command GO_T0_FILMS_LIST = new Command("Вернутся в категории", Command.BACK, 2);
    private final Command BACK = new Command("Назад", Command.BACK, 1);
    private final Command DOWNLOAD = new Command("Скачать", Command.OK, 2);
    private CategoryResultDisplayable list;
    private SearchResultDisplayable form;
       private FreeSpaceAlert freeSpaceAlert;
    private int catId;
    private int filmId;

    public DetailsForm(SearchResultDisplayable form, CategoryResultDisplayable list, int catId, int filmId) {
        super("Подробно");
        this.catId = catId;
        this.filmId = filmId;
        this.form=form;
        addCommand(BACK);
        addCommand(GO_T0_FILMS_LIST);
        addCommand(DOWNLOAD);
         freeSpaceAlert = new FreeSpaceAlert(this);
        //addCommand(TorrentMID.FREE_SPACE);
        setCommandListener(this);
        this.list=list;
    }

    public void commandAction(Command c, Displayable d) {
       if(c==DOWNLOAD){
            Display.getDisplay(TorrentMID.inst()).setCurrent(new DownloadConfirmDisplayable(this, catId, filmId));
       }else if(c==BACK){
           Display.getDisplay(TorrentMID.inst()).setCurrent(list);
       }else if(c==GO_T0_FILMS_LIST){
            Display.getDisplay(TorrentMID.inst()).setCurrent(form);
       }else if(c==FreeSpaceAlert.FREE_SPACE){
            freeSpaceAlert.getFreeSpace();
        }else{
           list.commandAction(c, d);
       }
    }

}
