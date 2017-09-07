/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.ee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.http.cookie.Cookie;

/**
 *
 * @author dprokopiuk
 */
public class TrackerSession {
    //private List<String> categories = new ArrayList<String>();
    private Map<String, List<Film>> films = new TreeMap<String, List<Film>>();
    private Cookie trackerCookie;
    public static final String SITE="http://rutracker.org/forum/";

    public TrackerSession(Cookie trackerCookie){
        this.trackerCookie = trackerCookie;
    }

    public Cookie getTrackerCookie(){
        return trackerCookie;
    }

    public String getCategoryById(int id){
        Set<String> set=films.keySet();
        int i=0;
        for(String s:set){
            if(i==id){
               
                return s;
            }
            i++;
        }
        throw new IllegalArgumentException("no String category for id="+id);
    }

    public List<Film> getFilms(String category){
        return films.get(category);
    }

    public Set<String> getCatrgories(){
        return films.keySet();
    }

    public void add(String category, Film film){
       
        if(films.get(category)==null){
            films.put(category,new ArrayList<Film>());
        }
       /* if(!categories.contains(category)){
           categories.add(category);
        }*/
         films.get(category).add(film);
    }

   public String parseFilmList(String src){
        String contentTable=getTableContent(src);
        List<String> columnList=getSearchBlocks(contentTable,"<tr","</tr>");
        List<List<String>> rowList = new ArrayList<List<String>>();
        int i=0;

        //<td class="row1 tCenter" title="проверено"><span class="tor-icon tor-approved">&radic;</span></td>

        for (String s : columnList) {
            if (!(i == 0 || i == columnList.size() - 1)) {
                List<String> tdList=getSearchBlocks(s, "<td", "</td>");
                String iconValue=tdList.get(1);
                String span="<span class=\"tor-icon tor-approved\">";
                int start=iconValue.indexOf(span)+span.length();
                int end = iconValue.indexOf("</span>");
               
                if(iconValue.substring(start, end).equals("&radic;")){
                    rowList.add(tdList);
                }
            }
            i++;
        }
        i=0;
       for (List<String> list : rowList) {
           i++;
           Film film = new Film();
           String category = null;
           category = setFilmProperties(list, film);
           add(category, film);
        }
      /* Set<String> keys=films.keySet();
       i=0;
      
       for(String key:keys){
         
       }*/


     
        String nextFilmListHref = getNextFilmListHref(src);

       
        return nextFilmListHref;
        //return "";
    }



    private String getNextFilmListHref(String res){
        int nextPageIndex = res.indexOf(">След.", 0);
      
        if (nextPageIndex != -1) {
            boolean getNext = false;
            int i = nextPageIndex;
            String href="";
            while (!getNext) {
               
                if (res.substring(i - 2, i).equals("<a")) {
                    getNext = true;
                    String sub = res.substring(i, nextPageIndex);
                    href=sub.substring(sub.indexOf("href=\"")+6, sub.lastIndexOf("\""));
                    break;
                }
                i--;
            }
            
            //href=removeContent(href, "amp;");
            int index = href.indexOf("amp;");
            int index2 = index + 4;
            int n = href.length();
            String nextHref = SITE + href.substring(0, index) + href.substring(index2, n);
            
            //String nextHref=SITE+href;
           
            return nextHref;
        } else return null;
    }

    /*public boolean hasMorePages(){
        return (nextHref!=null && nextHref.length()>SITE.length());
    }*/

     private String getTableContent(String res){
        String content="";
        String searchId="<table class=\"forumline tablesorter\" id=\"tor-tbl\">";
        
        int tableIndex = res.indexOf(searchId)+searchId.length();
        if (tableIndex != -1) {
            int tagCloseIndex=res.indexOf("</table>",tableIndex);
           
            content=res.substring(tableIndex,tagCloseIndex);
        }
        return content;
    }

    private List<String> getSearchBlocks(String tableContent, String startTag, String endTag){
        List<String> list = new ArrayList<String>();
        int firstIndex=tableContent.indexOf(startTag)+startTag.length();
        int lastIndex=tableContent.indexOf(endTag,firstIndex)+endTag.length();
        while((firstIndex-startTag.length())>=0){
                String element = tableContent.substring(firstIndex, lastIndex);
                list.add(element);
                firstIndex = tableContent.indexOf(startTag, lastIndex);
                lastIndex = tableContent.indexOf(endTag, firstIndex) + endTag.length();
        }
        return list;
    }

    private String getLineContent(String src, String startElement, String endElement, boolean hasBorder){
        List<String> list=getSearchBlocks(src, startElement, endElement);
        String res=list.get(0);
        int delLength=(hasBorder?0:endElement.length());
        return res.substring(0,res.length()-delLength);
    }

    private String setFilmProperties(List<String> rowList, Film film){
        //throw new UnsupportedOperationException(":(");
        setNameAttributes(rowList, film);
        setSizeAttributes(rowList, film);
        setSeedAttributes(rowList, film);
        return getCategory(rowList);
        /*setNameAttributes(rowList, film);
        setSizeAttributes(rowList, film);
        setSeedAttributes(rowList, film);*/
    }



    private String getCategory(List<String> rowList){
        String s = rowList.get(2);
        String a = getLineContent(s, "<a", "</a>", true);
        return getLineContent(a, "\">", "</a>", false);
    }

    private void setNameAttributes(List<String> rowList, Film film) {
        String s = rowList.get(3);
        String a = getLineContent(s, "<a", "</a>", true);
        String nameHref = getLineContent(a, "href=\"", "\">", false);
        film.setDetailsHref(nameHref);
        String nameTitle = getLineContent(a, "\">", "</a>", false);
        while(nameTitle.indexOf("<wbr>")!=-1){
            int index=nameTitle.indexOf("<wbr>");
            String before=nameTitle.substring(0,index);
            String after=nameTitle.substring(index+5,nameTitle.length());
            nameTitle=before+after;
        }
        //nameTitle.replace("<wbr>","");
        film.setTitle(nameTitle);
    }

    private void setSizeAttributes(List<String> rowList, Film film) {
        String s = rowList.get(5);
       
        String a = getLineContent(s, "<a", "</a>", true);
        String sizeHref = getLineContent(a, "href=\"", "\">", false);
        String sizeLine = getLineContent(a, "\">", "</a>", false);
        Double size=Double.parseDouble(sizeLine.substring(0, sizeLine.indexOf("&")));
        String sizeDimension=sizeLine.substring(sizeLine.indexOf("&nbsp;")+"&nbsp;".length(),sizeLine.indexOf(" ")).toLowerCase();
        long sizeInBytes = -1;
        if(sizeDimension.contains("gb")){
            sizeInBytes=(long) (size * 1024 * 1024 * 1024);
        }
        if(sizeDimension.contains("mb")){
             sizeInBytes=(long) (size * 1024 * 1024);
        }
        film.setDownloadHref(sizeHref);
        film.setSize(sizeInBytes);
    }

    private void setSeedAttributes(List<String> rowList, Film film) {
        String s = rowList.get(6);
        int seed = Integer.parseInt(getLineContent(s, "<b>", "</b>", false));
        film.setSeed(seed);
    }

    /*public static String test() throws Exception{
        FileInputStream in = new FileInputStream(new File("src.txt"));
        StringBuilder sb = new StringBuilder();
        int r=-1;
        while((r=in.read())>-1){
            sb.append(new String(new byte[]{(byte)r},"cp1251"));
        }
        return sb.toString();
    }*/


    private String removeContent(String src, String removedItem){
        String res="";
        while(src.indexOf(removedItem)!=-1){
            int index=src.indexOf(removedItem);
            String before=src.substring(0,index);
            String after=src.substring(index+removedItem.length(),src.length());
            res=before+after;
        }
        return res;
    }
}
