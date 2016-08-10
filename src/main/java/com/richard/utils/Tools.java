package com.richard.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by richard on 08/08/2016.
 */
public class Tools {

    private static Logger LOGGER = LogManager.getLogger(Tools.class);

    public static void wait(int milliseconds){

        try {
            java.lang.Thread.sleep(milliseconds);
        } catch (Exception ex) {
            LOGGER.error("Thread sleep failed");
        }
    }

    public static String serialize(Object obj) {

        String response = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            response = mapper.writeValueAsString(obj);
        }
        catch (Exception ex){}

        return response;
    }


}
