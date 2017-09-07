/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.torrent.common;

/**
 *
 * @author Конарх
 */
public class StringToHexDecoder {

    public static String decode(String hex){
        byte[] searchBuf = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            searchBuf[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return new String(searchBuf);
    }

}
