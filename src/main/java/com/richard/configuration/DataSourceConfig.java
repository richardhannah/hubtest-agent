package com.richard.configuration;

import com.richard.dal.HubtestAgentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Created by richard on 26/07/2016.
 */
@Configuration
public class DataSourceConfig {


    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource internalDataSource()
    {
        return DataSourceBuilder.create().build();
    }


    @Autowired
    @Qualifier("internalDataSource")
    private DataSource internalDataSource;

    @Bean
    public NamedParameterJdbcTemplate jdbcInternalTemplate() throws PropertyVetoException {
        return new NamedParameterJdbcTemplate(internalDataSource);
    }

    @Bean
    public HubtestAgentDao hubtestAgentDao(){
        return new HubtestAgentDao();
    }


}
