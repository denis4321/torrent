/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.me;

import com.conarhco.common.Charset;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.midlet.*;
import org.json.me.JSONObject;

/**
 * @author 777
 */
public class TorrentMID extends MIDlet implements CommandListener {
    /* public static int b;
    private final int CATEGORY_LIST = 1;
    private final int FILM_LIST = 2;
    private final int DETAILS_FORM = 3;*/
    //private final int CATEGORY_LIST=1;
    public static final Command EXIT = new Command("Выход", Command.EXIT, 1);
    public static final Command BACK = new Command("Назад", Command.BACK, 1);
    //public static final Command BACK_FROM_FREE_SPACE_ALERT = new Command("Продолжить", Command.OK, 1);
  //  private static final String SITE = "http://82.207.44.155/torrent";//"http://127.0.0.1:8080/torrent";
    private static final String SITE = "http://127.0.0.1:8084/torrent";
    //private static final String SITE = "http://127.0.0.1:8080/torrent";
    private String jsessionid = null;
    private static TorrentMID midlet;
    private SearchDisplayable form;
    private Timer timer = new Timer();
    public String login;

    
    public TorrentMID() {
        midlet = this;
    }

    protected void startApp() throws MIDletStateChangeException {
        form = new SearchDisplayable();
        form.load();
        login = form.getLogin();
        //freeSpaceAlert.setTimeout(Alert.FOREVER);
        //freeSpaceAlert.setCommandListener(this);
        Display.getDisplay(this).setCurrent(form);
    }

    public void callWeb(final String command, final String[] params, final CallWebProcessor processor) {
         //Test
        /*System.out.println("command: "+command);
        if (command.startsWith("download")) {
            System.out.println("ой-вей беня, как мене х..во :(");
            return;
        }*/
        TimerTask tt = new TimerTask() {

            public void run() {
                Displayable returnDisp = Display.getDisplay(TorrentMID.this).getCurrent();
                Alert wait = null;
                try {
                    wait = new Alert("Запрос сервера", "Подождите...", null, AlertType.INFO);
                    wait.setTimeout(Alert.FOREVER);
                    //wait.removeCommand(Alert.DISMISS_COMMAND);
                    Gauge progress = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
                    wait.setIndicator(progress);
                    wait.setCommandListener(TorrentMID.this);
                    wait.addCommand(EXIT);

                    //Отключена для теста на совпадение кодировок
                    Display.getDisplay(TorrentMID.this).setCurrent(wait);
                    StringBuffer query = new StringBuffer();
                    query.append(SITE + "/" + command);
                    if (jsessionid != null) {
                        query.append(";jsessionid=" + jsessionid);
                    }
                    query.append("?");
                    for (int i = 0; i < params.length - 1; i += 2) {
                        if (i != 0) {
                            query.append("&");
                        }
                        query.append(params[i] + "=" + params[i + 1]);
                    }
                   
                    HttpConnection c = (HttpConnection) Connector.open(query.toString());
                   //Нужна для теста на телефоне
                   /* Form f = new Form("");
                    f.append(query.toString());
                    Display.getDisplay(TorrentMID.this).setCurrent(f);
                    */
                    int rc = c.getResponseCode();
                    if (rc != 200) {
                        throw new IllegalStateException("Invalid reply from server");
                    }
                    Vector v = new Vector();
                    int r = -1;
                    InputStream in = c.openInputStream();
                    while ((r = in.read()) >= 0) {
                        v.addElement(new Byte((byte) r));
                    }
                    byte[] data = new byte[v.size()];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = ((Byte) v.elementAt(i)).byteValue();
                    }
                    c.close();
                    //String content = enc.decode(data);
                    //Парсинг возвращаемых данных
                    JSONObject reply = null;
                    if (data.length > 0) {
                        Charset enc = Charset.getCharset("cp1251");
                        String jsonStr = enc.decode(data);
                       
                        reply = new JSONObject(jsonStr);
                        //Thread.sleep(5000);
                        /*for (int i=0; i<100; i++){
                        wait.getIndicator().setValue(Gauge.INCREMENTAL_UPDATING);
                        Thread.sleep(100);
                        }*/
                    }
                    if (wait.isShown()) {
                        processor.process(reply);
                    }
                } catch (Exception ex) {
                    if (wait.isShown()) {
                        showError(ex, returnDisp);
                    } else {
                        ex.printStackTrace();
                    }
                }
            }
        };
        timer.schedule(tt, 1);
    }

    protected void pauseApp() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        //throw new UnsupportedOperationException("Not supported yet.");
        timer.cancel();
    }

    public void commandAction(Command c, Displayable d) {
        try {
            if (c == EXIT) {
                form.save();
                destroyApp(true);
                this.notifyDestroyed();
            } else if (c == BACK) {
                Display.getDisplay(this).setCurrent(form);
            }/* else if (c == TorrentMID.FREE_SPACE) {
               
                TorrentMID.inst().callWeb("free", new String[]{"login", TorrentMID.inst().getLogin()}, new CallWebProcessor() {

                    public void process(JSONObject root) throws Exception {
                        long freeSpace = Long.parseLong(root.get("freeSpace").toString());
                        freeSpaceAlert.setString(getSizeAsString(freeSpace));
                        Display.getDisplay(TorrentMID.inst()).setCurrent(freeSpaceAlert);
                    }
                });
            }*/
        } catch (Exception ex) {
            showError(ex, d);
        }
    }

    public static String getSizeAsString(long size) {        
        /*String rez="";
        String dim="";
         if (((size + 0.0) / (1024 * 1024 * 1024)) >= 1) {
             rez=((size + 0.0) / (1024 * 1024 * 1024))+"";
             dim="Gb";
         }
         else if (((size + 0.0) / (1024 * 1024)) >= 1) {
             rez=((size + 0.0) / (1024 * 1024))+"";
             dim="Mb";
         }
         else{
             rez=((size + 0.0) / (1024))+"";
             dim="Kb";
         }*/
            String rez=(((size+0.0)/(1024*1024*1024))>=1 ?(size+0.0)/(1024*1024*1024)+"" : (size+0.0)/(1024*1024)+"");
            int index = rez.indexOf(".");
            String dim=(((size+0.0)/(1024*1024*1024))>=1 ?"Gb" : "Mb");
            if (index != -1) {
                String beforeDote = rez.substring(0,index + 1);
                String afterDote = "";
                int t=rez.length()-index;
                if(t>=3){
                    afterDote= rez.substring(index + 1, index + 3);
                }else if(t>=2){
                    afterDote= rez.substring(index + 1, index + 2);
                }else{
                     afterDote= rez.substring(index + 1, index + 1);
                }
                return beforeDote + afterDote+dim;
            }
            return rez;
        }
           // return ((size + 0.0) / (1024 * 1024)) + "Mb";
        /*String res=((size+0.0)/(1024*1024*1024)>=1 ?(size+0.0)/(1024*1024*1024)+"Gb " : (size+0.0)/(1024*1024)+"Mb ");
        int index=res.indexOf(".");
        if(index!=-1){
        String beforeDote=res.substring(index+1);
        String afterDote=res.substring(index+1, index+3);
        return beforeDote+afterDote;
        }*/
        //return res;
        // return (size/(1024*1024*1024)>=1 ?Math.ceil((size+0.0)/(1024*1024*1024))+"Gb " : Math.ceil((size+0.0)/(1024*1024))+"Mb ");
   // }


    public void setSessionID(String id) {
        jsessionid = id;
       
    }

    public void showError(Exception ex, Displayable ret) {
        ex.printStackTrace();
        Alert error = new Alert("аШипка!", ex.toString(), null, AlertType.ERROR);
        error.setTimeout(5000);
        if (ret != null) {
            Display.getDisplay(midlet).setCurrent(error, ret);
        } else {
            Display.getDisplay(midlet).setCurrent(error);
        }
    }

    public static String encodeToHex(String str) {
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int ch = str.getBytes()[i] & 0xFF;
           
            if (ch < 16) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(ch));
        }
        return hex.toString();
    }

    public static TorrentMID inst() {
        return midlet;
    }

    public String getLogin() {
        return login;
    }
}
