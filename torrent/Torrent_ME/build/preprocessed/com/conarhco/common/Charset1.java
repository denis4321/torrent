/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conarhco.common;

/**
 *
 * @author Конарх
 */
public class Charset1 {

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
