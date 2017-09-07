/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.ee;

import com.conarhco.torrent.downloader.DownloadClientManager;
import com.conarhco.torrent.downloader.TorrentFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * @author 777
 */
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession sess = req.getSession();
            int catID = Integer.parseInt(req.getParameter("id"));
            TrackerSession trackerSess = (TrackerSession) sess.getAttribute("tracker");
            String category = trackerSess.getCategoryById(catID);
            List<Film> list = trackerSess.getFilms(category);
            int filmId = Integer.parseInt(req.getParameter("filmId"));
            String downLoadHref = list.get(filmId).getDownloadHref();
            long size=list.get(filmId).getSize();
            getServletContext().log("Request from login="+req.getSession().getAttribute("login")+" to download film with filmId="+filmId);
            //TrackerSearchServlet.LOG.info("Request from login="+req.getSession().getAttribute("login")+" to download film with filmId="+filmId);
            //System.out.println("download requrst: "+req.getQueryString());
            //Загрузка торрент-файла
            String cookieValue = downLoadHref.substring(downLoadHref.lastIndexOf("=") + 1);
          
            Cookie authCookie = trackerSess.getTrackerCookie();
            BasicClientCookie downloadCookie = new BasicClientCookie("bb_dl", cookieValue);
            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.YEAR, 1);
            //downloadCookie.setExpiryDate(cal.getTime());
            downloadCookie.setPath("/forum/");
            downloadCookie.setDomain(".rutracker.org");
            //System.out.println("Trying to download file: "+downLoadHref);
            HttpRequestHelper requestHelper = HttpRequestHelper.getPostRequest(downLoadHref, authCookie, downloadCookie);
            byte[] torrent = requestHelper.getByteContent();
            //System.out.println(downLoadHref+" downloaded, size="+torrent.length);
            //Test
           // File torrentFile = new File(System.getProperty("user.home")+"/Desktop/me.torrent");
           
           // FileOutputStream out = new FileOutputStream(torrentFile);
            //out.write(torrent);
            //out.close();
            String login=req.getSession().getAttribute("login").toString();
            String search=req.getSession().getAttribute("search").toString();
            TorrentFile downloadFile = new TorrentFile(search, size, torrent);
            DownloadClientManager clientMan = (DownloadClientManager) getServletContext().getAttribute("torrents");
            clientMan.addTorrentFile(login,downloadFile);
            //System.out.println("Setting file to download queue for "+login);
            resp.setContentType("application/json; charset=windows-1251");
            resp.setStatus(HttpServletResponse.SC_OK);
            getServletContext().log("Response for login="+login+" with parameter filmId="+filmId+" has been send (size="+torrent.length+" bytes)");
            //TrackerSearchServlet.LOG.info("Response for login="+login+" with parameter filmId="+filmId+" has been send (size="+torrent.length+" bytes)");
            //System.out.println("response 200 sent to client");
        } catch (Exception ex) {
            getServletContext().log("Server ERROR:"+ex.getMessage(),ex);
            //.LOG.info("Server ERROR:"+ex.getMessage());
            ex.printStackTrace();
        }
    }
}

