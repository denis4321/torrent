/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.ee;

import com.conarhco.torrent.downloader.DownloadClientManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 * @author Конарх
 */
public class LifeListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("torrents", new DownloadClientManager());
    }

    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("torrents", null);
    }
}
