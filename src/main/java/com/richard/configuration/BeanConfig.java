package com.richard.configuration;

import com.richard.services.BatchResponder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by richard on 28/07/2016.
 */
@Configuration
public class BeanConfig {


    @Bean
    public BatchResponder batchResponder(){
        return new BatchResponder();
    }


}
