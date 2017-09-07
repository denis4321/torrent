/*
 * CharsetTest.java
 * JMUnit based test
 *
 * Created on 13.11.2011, 14:08:27
 */

package com.conarhco.common;


import java.util.Enumeration;
import java.util.Hashtable;
import jmunit.framework.cldc10.*;

/**
 * @author Конарх
 */
public class Charset1Test extends TestCase {
    
    public Charset1Test() {
        //The first parameter of inherited constructor is the number of test cases
        super(2,"Charset1Test");
    }            

    public void test(int testNumber) throws Throwable {
        switch(testNumber){
            case 0:
                encodeTest();
                break;
            case 1:
                decodeTest();
            default:
                break;
        }
    }



    public void encodeTest(){
       
    }

    public void decodeTest(){
        String line="Терминатор";
        //Charset1 decoder = new Charset1();
        String actualDecode = Charset1.encode(line);
       // assertEquals("Wrong decoding","070089010009609209708401020980100",actualDecode);
        String actualEncode=Charset1.decode(actualDecode);
        assertEquals("Wrong encoding","Терминатор",actualEncode);
    }

    public void setUp() throws Throwable {
        super.setUp();
    }

    public void tearDown() {
        super.tearDown();
    }




    
}
