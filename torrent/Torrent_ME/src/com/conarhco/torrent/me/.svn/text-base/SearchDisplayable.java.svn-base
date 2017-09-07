/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.me;

import com.conarhco.common.Charset1;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

/**
 * Сделать их по дефолту английскими ловеркейс и поле пароля типа пароль
 * @author 777
 */
public class SearchDisplayable extends Form implements CommandListener {
    private TextField search;
    private TextField login;
    private TextField pass;
    private ChoiceGroup isSave;
    public final Command SEARCH = new Command("Далее", Command.OK, 2);

    public SearchDisplayable(){
        super("Главное окно");
        this.setCommandListener(this);
        this.addCommand(TorrentMID.EXIT);
        this.addCommand(SEARCH);
        search = new TextField("Поиск\n", "", 20, TextField.ANY);
        login = new TextField("Логин\n", "", 40, TextField.NON_PREDICTIVE);
        login.setInitialInputMode("UCB_BASIC_LATIN");
        pass = new TextField("Пароль\n", "", 40, TextField.PASSWORD);
        isSave = new ChoiceGroup("", Choice.MULTIPLE, new String[]{"Запомнить"}, null);
        pass.setString("torrentmetorrentme");
        login.setString("torrentme2");
        search.setString("Человек-паук 2");
        this.append(search);
        this.append(login);
        this.append(pass);
        this.append(isSave);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == SEARCH) {
            save();            
            String searchLine = getSearchLine();
            String encSearchLine=Charset1.encode(searchLine);
            System.out.println("encoded="+encSearchLine);
            TorrentMID.inst().setSessionID(null);//Сбарсываем идшник сессии
            TorrentMID.inst().callWeb("search", new String[]{"login", getLogin(), "pass", getPass(), "s", encSearchLine},
                    new CallWebProcessor() {
                        public void process(JSONObject results) throws Exception {
                            TorrentMID.inst().setSessionID(results.getString("sid"));
                            SearchResultDisplayable list = new SearchResultDisplayable();
                            JSONArray jArray = results.getJSONArray("cats");
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject obj = (JSONObject) jArray.get(i);
                                String name = obj.get("name").toString();
                                int id = Integer.parseInt(obj.get("id").toString());
                                list.addCategory(name, id);
                            }
                            Display.getDisplay(TorrentMID.inst()).setCurrent(list);
                        }
                    });
        } else {
            TorrentMID.inst().commandAction(c, d);
        }
    }

    public String getSearchLine(){
        return search.getString();
    }

    public String getLogin(){
        return login.getString();
    }

    public String getPass(){
        return pass.getString();
    }

    public void save() {
        if (this.isSave.isSelected(0)) {
            try {
                RecordStore store = RecordStore.openRecordStore("torrent", true);
                RecordEnumeration en = store.enumerateRecords(null, null, false);
                while (en.hasNextElement()) {
                    int j = en.nextRecordId();
                    store.deleteRecord(j);
                }
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(buf);
                out.writeUTF(login.getString());
                out.writeUTF(pass.getString());
                this.isSave.setSelectedIndex(0, true);
                byte[] b = buf.toByteArray();
                store.addRecord(b, 0, b.length);
            } catch (Exception ex) {
                TorrentMID.inst().showError(ex, Display.getDisplay(TorrentMID.inst()).getCurrent());
            }
        }
    }

     public void load() {
        try {
            RecordStore store = RecordStore.openRecordStore("torrent", true);
            if (store.getNumRecords() > 0) {
                byte[] buf = null;
                RecordEnumeration en = store.enumerateRecords(null, null, false);
                while (en.hasNextElement()) {
                    buf = en.nextRecord();
                }
                ByteArrayInputStream buff = new ByteArrayInputStream(buf);
                DataInputStream data = new DataInputStream(buff);
                login.setString(data.readUTF());
                pass.setString(data.readUTF());
            }
        } catch (Exception ex) {
            TorrentMID.inst().showError(ex, this);
        }
    }

}
