/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.ee;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * @author 777
 */
public class DetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int catID = Integer.parseInt(req.getParameter("id"));
        HttpSession sess = req.getSession();
        TrackerSession trackerSess = (TrackerSession) sess.getAttribute("tracker");
        String category = trackerSess.getCategoryById(catID);
        List<Film> list = trackerSess.getFilms(category);
        int filmId = Integer.parseInt(req.getParameter("filmId"));
        String href = list.get(filmId).getDetailsHref();
        HttpRequestHelper requestHelper = HttpRequestHelper.getGetRequest(href);
        String content=requestHelper.connect();
        System.out.println(catID+" "+filmId+" "+href);
        getServletContext().log("Request from login="+req.getSession().getAttribute("login")+" to details about filmId="+filmId+" categoryId="+catID);
        //TrackerSearchServlet.LOG.info("Request from login="+req.getSession().getAttribute("login")+" to details about filmId="+filmId+" categoryId="+catID);
        /*
        HttpGet clientGet = new HttpGet(href);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse clientResp = client.execute(clientGet);

        //StringBuilder sb = new StringBuilder();
        int v = -1;
        InputStream in = clientResp.getEntity().getContent();
        do {
            v = in.read();
            if (v >= 0) {
                sb.append(new String(new byte[]{(byte) v}, "windows-1251"));
            }
        } while (v >= 0);
        clientGet.abort();*/
        resp.setContentType("application/json; charset=windows-1251");
        try {
            JSONObject root = new JSONObject();
            Map<String, String> map = new ParseDetails().parse(content);            
            JSONArray arr = new JSONArray();
            // root.put("details", map);
            //Map<String, String> map = new ParseDetails().parse(sb.toString());
            //StringBuilder builder = new StringBuilder();
            Set<String> set = map.keySet();
            Integer i=0;
            for (String key : set) {
                JSONObject obj = new JSONObject();
                obj.put(i.toString(), key+":"+map.get(key));
               // arr.put(1, key+":"+map.get(key));//pbuilder.append(key + ":" + map.get(key) + "\n");
                //arr.put(key+":"+map.get(key));
                arr.put(obj);
            }
            root.put("details", arr);
           // System.out.println(root);
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
