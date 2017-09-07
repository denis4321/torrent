/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.downloader;

import com.conarhco.torrent.ee.TrackerSearchServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Получает от клиента запрос на торренты и информацию о свободном месте. Отдает JSON с списком торрентов (имя, перекодированный в хекс массив, длину в лонге)
 * @author Конарх
 */
public class GetTorrentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DownloadClientManager downloadManager = (DownloadClientManager)getServletContext().getAttribute("torrents");
        long freeSpace = Long.parseLong(req.getParameter("freeSpace"));
        String login = req.getParameter("login");
        getServletContext().log("Request for toorents from SE for login="+login+" freeSpace="+freeSpace);
        //TrackerSearchServlet.LOG.info("Request for toorents from SE for login="+login+" freeSpace="+freeSpace);
        downloadManager.setFreeSpace(login, freeSpace);
        Set<TorrentFile> list = downloadManager.getFilmsToDownload(login);
       
        resp.setContentType("application/json; charset=windows-1251");
        try {
            JSONArray root = new JSONArray();
            for (TorrentFile t : list) {
                JSONObject obj = new JSONObject();
                obj.put("name", t.getName());
                Collection<Byte> torrent = encodeFilm(t.getTorrent());
                obj.put("torrent", torrent);
                obj.put("size", t.getSize());
                root.put(obj);
            }
            String text = root.toString();
            PrintWriter w = resp.getWriter();
            w.println(text);
        } catch (JSONException ex) {
            getServletContext().log("Server ERROR:"+ex.getMessage(), ex);
            //TrackerSearchServlet.LOG.info("Server ERROR:"+ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Collection<Byte> encodeFilm(byte[] bytes) {
        List<Byte> list = new ArrayList<Byte>();
        for(byte b:bytes){
            list.add(b);
        }
        return list;//sb.toString();
    }
}
