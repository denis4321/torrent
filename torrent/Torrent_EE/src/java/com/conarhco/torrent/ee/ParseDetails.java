/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.torrent.ee;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 777
 */
public class ParseDetails {

    public ParseDetails() {
    }

    private List<String> getSearchBlocks(String tableContent, String startTag, String endTag) {
        List<String> list = new ArrayList<String>();
        int firstIndex = tableContent.indexOf(startTag) + startTag.length();
        int lastIndex = tableContent.indexOf(endTag, firstIndex) + endTag.length();
        while ((firstIndex - startTag.length()) >= 0) {
            String element = tableContent.substring(firstIndex, lastIndex);
            list.add(element);
            firstIndex = tableContent.indexOf(startTag, lastIndex);
            lastIndex = tableContent.indexOf(endTag, firstIndex) + endTag.length();
        }
        return list;
    }

    private Map<String, String> getSpan(String src) {
        String searchItem = "<span class=\"post-b\">";
        Map<String, String> map = new LinkedHashMap<String, String>();
        int firstIndex = src.indexOf(searchItem);
        int nextIndex = src.indexOf(searchItem, firstIndex + 1);
        while (nextIndex != -1) {
            String item = removeElement(src.substring(firstIndex + searchItem.length(), nextIndex));
            item = removeElement(item);
            if (item.indexOf(":") != -1) {
                String key = item.substring(0, item.indexOf(":")).trim();
                String value = item.substring(item.indexOf(":") + 1).trim();
                if (value.trim().length() > 0) {
                    map.put(key, value);
                }
            }
            firstIndex = nextIndex;
            nextIndex = src.indexOf(searchItem, firstIndex + 1);
        }
        return map;
    }

    private String removeElement(String src) {
        if (src.indexOf("</span>") != -1) {
            src = src.substring(0, src.indexOf("</span>")) + src.substring(src.indexOf("</span>") + 7, src.length() - 1);
        }
        if (src.indexOf("<br />") != -1) {
            if (src.indexOf(")") != -1) {
                src = src.substring(0, src.length() - 5);
            } else {
                src = src.substring(0, src.length() - 6);
            }
        }
        if (src.indexOf("<span") != -1) {
            String s1 = src.substring(0, src.indexOf("<span"));
            String s2 = src.substring(src.indexOf(">") + 1, src.length());
            src = s1 + s2;
        }
        if (src.indexOf("<a") != -1) {
            String s1 = src.substring(0, src.indexOf("<a"));
            String s2 = src.substring(src.indexOf("</a>") + 4, src.length());
            src = s1 + s2;
        }
        if (src.indexOf("<var") != -1) {
            String s1 = src.substring(0, src.indexOf("<var"));
            String s2 = src.substring(src.indexOf("</var>") + 6, src.length());
            src = s1 + s2;
        }
        if (src.indexOf("</spa") != -1) {
            src = src.substring(0, src.indexOf("</spa"));
        }
        if (src.indexOf("<span") != -1) {
            src = src.substring(0, src.indexOf("<span"));
        }
        if (src.indexOf("<") != -1) {
            src = src.substring(0, src.indexOf("<"));
        }
        if (src.endsWith("-")) {
            src = src.substring(0, src.indexOf("-"));
        }
        return src;
    }

    public Map<String, String> parse(String src) {
        List<String> list = getSearchBlocks(src, "<table class=\"topic\" id=\"topic_main\" cellpadding=\"0\" cellspacing=\"0\">", "</table>");
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String s : list) {
            map.putAll(getSpan(s));
        }
        map.remove("Описание");
        map.remove("Главы");
        return map;
    }

    //public String getDownloadHref(){

      //  return "";
   // }
}
