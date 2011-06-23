package com.fasterxml.jackson.module.afterburner;

import java.io.*;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

public class ManualTest
{
    public final static class IntBean
    {
        public int getA() { return 37; }
        public int getB() { return -123; }
        public int getC() { return 0; }
        public int getD() { return 999999; }

        public int getE() { return 1; }
        public int getF() { return 21; }
        public int getG() { return 345; }
        public int getH() { return 99; }
    }
    
    public static void main(String[] args) throws Exception
    {
//        JsonFactory f = new org.codehaus.jackson.smile.SmileFactory();
        JsonFactory f = new JsonFactory();
        ObjectMapper mapperSlow = new ObjectMapper(f);
        ObjectMapper mapperFast = new ObjectMapper(f);
        
        // !!! TEST -- to get profile info, comment out:
//        mapperSlow.registerModule(new AfterburnerModule());

        mapperFast.registerModule(new AfterburnerModule());
        new ManualTest().testWith(mapperSlow, mapperFast);
    }

    private void testWith(ObjectMapper slowMapper, ObjectMapper fastMapper)
        throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean fast = true;
        final IntBean bean = new IntBean();
        
        while (true) {
            long now = System.currentTimeMillis();

            ObjectMapper m = fast ? fastMapper : slowMapper;
            int len = 0;
            
            for (int i = 0; i < 399999; ++i) {
                out.reset();
                m.writeValue(out, bean);
                len = out.size();
            }
            long time = System.currentTimeMillis() - now;
            
            System.out.println("Mapper (fast: "+fast+"; "+len+"); took "+time+" msecs");

            fast = !fast;
        }
    }
   
}