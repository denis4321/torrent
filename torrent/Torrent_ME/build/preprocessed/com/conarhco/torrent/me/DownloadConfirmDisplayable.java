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
import org.json.me.JSONObject;

/**
 * @author Конарх
 */
public class DownloadConfirmDisplayable extends Form implements CommandListener{
    public static final Command okAlertCommand = new Command("Скачать", Command.OK, 1);
    private static final Command cancelAlertCommand = new Command("Отмена", Command.CANCEL, 1);
    private static final Command GET_DOWLOADED = new Command("Продолжить", Command.SCREEN, 1);
    private Displayable parent;
    private int catId;
    private int filmId;
    
    public DownloadConfirmDisplayable(Displayable parent, int catId, int filmId){
        super("Подтверждение");
        this.parent = parent;
        this.catId = catId;
        this.filmId = filmId;
        this.append("\n\n\nВы действительно хотите скачать этот фильм?");
        /*setType(AlertType.INFO);
        setTimeout(Alert.FOREVER);
        setString("Скачать выбранный фильм?");*/
        addCommand(okAlertCommand);
        addCommand(cancelAlertCommand);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == okAlertCommand) {
            //Display.getDisplay(TorrentMID.inst()).setCurrent(parent);
            //Здесь возможно нужна задержка, чтобы успел переключиться на предка с алерта
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }*/
            TorrentMID.inst().callWeb("download", new String[]{"id", Integer.toString(catId), "filmId", Integer.toString(filmId)}, new CallWebProcessor() {

                public void process(JSONObject root) throws Exception {
                    Form responseForm  = new Form("Ответ");
                    responseForm.append("\n\n\nСервер начал загруку");
                    responseForm.addCommand(GET_DOWLOADED);
                    responseForm.setCommandListener(DownloadConfirmDisplayable.this);
                    Display.getDisplay(TorrentMID.inst()).setCurrent(responseForm);
                   /* Alert responseAlert = new Alert("Ответ");
                    responseAlert.setType(AlertType.INFO);
                    responseAlert.setTimeout(Alert.FOREVER);
                    //int responseCode = Integer.parseInt(root.get("responseCode").toString());
                    responseAlert.setString("Сервер начал загрузку");
                    //responseAlert.addCommand(GET_DOWLOADED);
                    //responseAlert.setCommandListener(DownloadConfirmDisplayable.this);
                    //Display.getDisplay(TorrentMID.inst()).setCurrent(responseAlert);
                    //System.out.println(parent);
                    Display.getDisplay(TorrentMID.inst()).setCurrent(responseAlert);
                    //Display.getDisplay(TorrentMID.inst()).setCurrent(parent);
                    //Display.getDisplay(TorrentMID.inst()).setCurrent(CategoryResultDisplayable.this);*/
                }
            });
        } else if(c==GET_DOWLOADED || c == cancelAlertCommand){
              Display.getDisplay(TorrentMID.inst()).setCurrent(parent);
        } /*else if (c == cancelAlertCommand) {
            Display.getDisplay(TorrentMID.inst()).setCurrent(parent);
        }*/
    }
}
