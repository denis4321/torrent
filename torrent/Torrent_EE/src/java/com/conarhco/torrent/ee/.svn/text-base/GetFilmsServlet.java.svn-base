/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.ee;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Конарх
 */
public class GetFilmsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int catID = Integer.parseInt(req.getParameter("id"));

            HttpSession sess = req.getSession();
            getServletContext().log("Request from login="+sess.getAttribute("login")+" for category with id="+catID);
            //TrackerSearchServlet.LOG.info("Request from login="+sess.getAttribute("login")+" for category with id="+catID);
            TrackerSession trackerSess = (TrackerSession) sess.getAttribute("tracker");
            String category = trackerSess.getCategoryById(catID);
            getServletContext().log("Category for request from login="+sess.getAttribute("login")+" with paremeter id="+catID+" is "+category);
            //TrackerSearchServlet.LOG.info("Category for request from login="+sess.getAttribute("login")+" with paremeter id="+catID+" is "+category);
            List<Film> list = trackerSess.getFilms(category);
            Collections.sort(list, new Comparator<Film>(){

                public int compare(Film o1, Film o2) {
                    return o2.getSeed()-o1.getSeed();
                }

            });
            //sess.setAttribute("films", list);
            resp.setContentType("application/json; charset=windows-1251");
            JSONObject root = new JSONObject();
            JSONArray arr = new JSONArray();
            int id = 0;
            getServletContext().log("Category for request from login="+sess.getAttribute("login")+" with paremeter id="+catID+" and category="+category+" "+list.size()+" films were found");
            //TrackerSearchServlet.LOG.info("Category for request from login="+sess.getAttribute("login")+" with paremeter id="+catID+" and category="+category+" "+list.size()+" films were found");
            for (Film film : list) {
                JSONObject obj = new JSONObject();
                obj.put("title", film.getTitle());
                obj.put("size", film.getSize());
                obj.put("seed", film.getSeed());
                getServletContext().log(id+"."+film.getTitle());
                //TrackerSearchServlet.LOG.info(id+"."+film.getTitle());
                obj.put("id", id++);
                arr.put(obj);
            }
            root.put("film", arr);
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
