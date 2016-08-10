package com.richard.configuration;

import com.richard.services.BatchResponder;
import com.richard.services.ItemService;
import com.richard.services.OrderFulfiller;
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

    @Bean
    public OrderFulfiller orderFulfiller(){
        return new OrderFulfiller();
    }

    @Bean
    public ItemService itemService(){
        return new ItemService();
    }


}
