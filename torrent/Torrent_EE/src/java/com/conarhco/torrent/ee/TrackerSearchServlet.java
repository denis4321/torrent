/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.ee;

import com.conarhco.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TODO: Сервер вообще не разговорчивый. логгировать все основные действия - поиск файла с указанием логина (МЕ), сколько категорий - записей найдено
 * загрузку торрент-файла и постановку в очередь имя категрии/имя файла (МЕ)
 * передачу торрент-файла на загрузку (СЕ)
  * @author Конарх
 */
public class TrackerSearchServlet extends HttpServlet {
    private final String loginUrl = "http://login.rutracker.org/forum/login.php";
    private final String searchUrl = "http://rutracker.org/forum/tracker.php";
   // public final static Logger LOG = Logger.getLogger("torrent");


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("pass");
        String searchEnc = req.getParameter("s");
        System.out.println("query="+searchEnc);
        getServletContext().log("Request was detected. It's parameters are: login="+login+", pass="+pass+", s="+searchEnc);
        //LOG.info("Request was detected. It's parameters are: login="+login+", pass="+pass+", s="+searchEnc);
        String search = null;
        if (searchEnc!=null&&!searchEnc.isEmpty()){
           System.out.println("before="+searchEnc);
            //------Первоначальная перекодировка-----
           // search = StringToHexDecoder.decode(searchEnc);//TODO: Если класс не нужен, то его убрать

           //------Перекодирует из строки вида 43,56,-75-----
           //search=testEncoding(searchEnc);
           
            //------Через HashMap-----
            //search=this.decodeWithHashtable(searchEnc);
            try {
                //search=(String) Class.forName("com.conarhco.common.Charset1").getMethod("encode").invoke(null, searchEnc);
                search=encode(searchEnc);// Class.forName("com/conarhco/common/Charset1.class").getMethod("encode").invoke(null, searchEnc);
                System.out.println("after="+search);
            } catch (Exception ex) {
                getServletContext().log("Server ERROR:"+ex.getMessage(),ex);
               // LOG.info("Server ERROR:"+ex.getMessage());
                ex.printStackTrace();
            }
            getServletContext().log("Search parameter was encoded as "+search);
           //LOG.info("Search parameter was encoded as "+search);
           System.out.println("after="+search);
        }
        getServletContext().log("encoded request parameters are login="+login+" password="+pass+" search="+search);
        //LOG.info("encoded request parameters are login="+login+" password="+pass+" search="+search);
        if (search == null) {
            //TODO: Для теста
            login = "torrentme2";
            pass = "torrentmetorrentme";
            search = "Человек-паук 2";
        }
        HttpRequestHelper request = HttpRequestHelper.getPostRequest(loginUrl);
        request.addParameter("login_username", login);
        request.addParameter("login_password", pass);
        request.addParameter("login", "Вход");
        //----------Для входа без авторизации. ВАЖНО: Надо зайти через броузер и получить куки bb_data
        request.connect();
        Cookie authCookie = request.getCookies().get(0);
        /*BasicClientCookie authCookie = new BasicClientCookie("bb_data", "1-22822406-sv91jZ4kfQCOvbr2mvCH-1389309083-1316379484-1316379484-2581627258-0");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        authCookie.setExpiryDate(cal.getTime());
        authCookie.setPath("/forum/");
        authCookie.setDomain(".rutracker.org");*/
        //----------------------------------------------------
        TrackerSession trackerSess = new TrackerSession(authCookie);
        HttpRequestHelper requestSearch = HttpRequestHelper.getPostRequest(searchUrl, authCookie);
        requestSearch.addParameter("nm", search);
        requestSearch.addParameter("max", "1");
        requestSearch.addParameter("to", "1");
        String nextPageHref=null;
        do{
            String content = requestSearch.connect();
         
            nextPageHref=trackerSess.parseFilmList(content);
            
            if (nextPageHref!=null){
                requestSearch = HttpRequestHelper.getGetRequest(nextPageHref, authCookie);
            }
        }
        while (nextPageHref != null);
        HttpSession sess = req.getSession();
        sess.setAttribute("login", login);
        sess.setAttribute("search", search);
        sess.setAttribute("tracker", trackerSess);
        //Runtime.getRuntime().exec("calc.exe");
        resp.setContentType("application/json; charset=windows-1251");
        //resp.setContentType("text/plain; charset=windows-1251");//Для отладки
        try {
            JSONObject root = new JSONObject();
            root.put("sid", sess.getId());
            JSONArray arr = new JSONArray();
            int id = 0;
            getServletContext().log("For search parameter="+search+" "+trackerSess.getCatrgories().size()+" categories were found");
           // LOG.info("For search parameter="+search+" "+trackerSess.getCatrgories().size()+" categories were found");
            for (String cat : trackerSess.getCatrgories()){
                JSONObject obj = new JSONObject();
                obj.put("name", cat);
                obj.put("id", id++);
                getServletContext().log(id+"."+cat);
               // LOG.info(id+"."+cat);
                arr.put(obj);
            }
            root.put("cats", arr);
            String text = root.toString();
            PrintWriter w = resp.getWriter();
            w.println(text);
        } catch (JSONException ex) {
            getServletContext().log("Server ERROR:"+ex.getMessage(),ex);
            //LOG.info("Server ERROR:"+ex.getMessage());
            ex.printStackTrace();
        }
    }


    /*
     * Нужен для перекодирования по-новому
     */
    private String testEncoding(String src) {
        List<Byte> bytes = new ArrayList<Byte>();
        Scanner sc = new Scanner(src);
        sc.useDelimiter(",");
        while (sc.hasNext()) {
            bytes.add(Byte.valueOf(sc.next()));
        }
        byte[] ar = new byte[bytes.size()];
        int i=0;
        for(Byte b: bytes){
            ar[i]=b;
            i++;
        }
        return new String(ar);
    }

    private String decodeWithHashtable(String src) {
        char[] chars = src.toCharArray();
        String rez = "";
        for (int i = 0; i < chars.length; i++) {
            String str = chars[i] + "";
            rez += getDecodeTable().containsKey(str) ? getDecodeTable().get(str) : str;
        }
        return rez;
    }

    /**
     * Приделать общую библиотеку для ЕЕ и МЕ и сделать в билдах соответвующее включение ее. НЕ ЗАБУДЬ: МЕ компилится под 1.4, соотв - лучше в МЕ
     * @return
     */
    private Map getDecodeTable(){
        Map decodeTable = new HashMap();
        decodeTable.put("q", "й");
        decodeTable.put("w", "ц");
        decodeTable.put("e", "у");
        decodeTable.put("r", "к");
        decodeTable.put("t", "е");
        decodeTable.put("y", "н");
        decodeTable.put("u", "г");
        decodeTable.put("i", "ш");
        decodeTable.put("o", "щ");
        decodeTable.put("p", "з");
        decodeTable.put("{", "х");
        decodeTable.put("}", "ъ");
        decodeTable.put("a", "ф");
        decodeTable.put("s", "ы");
        decodeTable.put("d", "в");
        decodeTable.put("f", "а");
        decodeTable.put("g", "п");
        decodeTable.put("h", "р");
        decodeTable.put("j", "о");
        decodeTable.put("k", "л");
        decodeTable.put("l", "д");
        decodeTable.put(";", "ж");
        decodeTable.put("'", "э");
        decodeTable.put("z", "я");
        decodeTable.put("x", "ч");
        decodeTable.put("c", "с");
        decodeTable.put("v", "м");
        decodeTable.put("b", "и");
        decodeTable.put("n", "т");
        decodeTable.put("m", "ь");
        decodeTable.put("<", "б");
        decodeTable.put(">", "ю");
        decodeTable.put("$", " ");
        return decodeTable;
    }

    private final static String CONTENT = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            + "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя"
            + "1234567890_+-=()<>?/`!@#$%^&*\"";

    public static String encode(String line) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            int b = (int) CONTENT.indexOf(c);
            if (b < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    public static String decode(String line) {
        StringBuffer rez = new StringBuffer();
        for (int i = 0; i < line.length(); i += 2) {
            int index = Integer.parseInt(line.substring(i, i + 2), 16);
            char c = CONTENT.charAt(index);
            rez.append(c);
        }
        return rez.toString();
    }


   
    
}
