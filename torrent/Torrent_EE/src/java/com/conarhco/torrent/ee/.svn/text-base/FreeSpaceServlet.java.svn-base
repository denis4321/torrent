/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.ee;

import com.conarhco.torrent.downloader.DownloadClientManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Конарх
 */
public class FreeSpaceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DownloadClientManager downloadManager = (DownloadClientManager) getServletContext().getAttribute("torrents");
        HttpSession sess = req.getSession(false);
        String login = (String) sess.getAttribute("login");
        getServletContext().log("Request for freeSpace from login="+login);
        //TrackerSearchServlet.LOG.info("Request for freeSpace from login="+login);
       // JOptionPane.showInputDialog(login+" from FreeSpaceServlet");
        long freeSpace = downloadManager.getFreeSpace(login);
        getServletContext().log("Response for freeSpace from login="+login+" is "+freeSpace);
        //TrackerSearchServlet.LOG.info("Response for freeSpace from login="+login+" is "+freeSpace);
        resp.setContentType("application/json; charset=windows-1251");
        try {
            JSONObject root = new JSONObject();
            root.put("freeSpace", freeSpace);
            String text = root.toString();
            PrintWriter w = resp.getWriter();
            w.println(text);
        } catch (JSONException ex) {
            getServletContext().log("Server ERROR:"+ex.getMessage(),ex);
             //TrackerSearchServlet.LOG.info("Server ERROR:"+ex.getMessage());
            ex.printStackTrace();
        }
    }
}
