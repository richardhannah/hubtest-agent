package com.richard;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;

/**
 * Created by richard on 21/07/2016.
 */
@SpringBootApplication
public class Application {

    private static Logger LOGGER = LogManager.getLogger(Application.class);

    @Autowired
    static NamedParameterJdbcTemplate jdbcInternalTemplate;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);


    }

}
