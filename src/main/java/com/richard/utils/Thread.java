package com.richard.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by richard on 08/08/2016.
 */
public class Thread {

    private static Logger LOGGER = LogManager.getLogger(Thread.class);

    public static void wait(int milliseconds){

        try {
            java.lang.Thread.sleep(milliseconds);
        } catch (Exception ex) {
            LOGGER.error("Thread sleep failed");
        }
    }


}
