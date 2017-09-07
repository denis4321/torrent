/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 *
 * @author Admin
 */
public class Charset {
    private static Hashtable sets = new Hashtable();
    final String cp1251 = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";


    private Charset(String cs) throws UnsupportedEncodingException{
        if (!"cp1251".equals(cs)) throw new UnsupportedEncodingException(cs);
    }

    public String decode (byte[] data){
        char[] res = new char[data.length];
        for (int i = 0; i<res.length; i++){
            int b = (data[i]&0xFF);
            if (b>=192){
                res[i] = cp1251.charAt(b-192);
            } else {
                res[i] = (char)b;
            }
        }
        return new String(res);
    }

    public static Charset getCharset(String cs) throws UnsupportedEncodingException{
        if (!sets.containsKey(cs)){
            sets.put(cs, new Charset(cs));
        }
        return (Charset) sets.get(cs);
    }
}
