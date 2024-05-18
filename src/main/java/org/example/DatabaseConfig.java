package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.example.convertor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    private final ObjectMapper objectMapper;
    public DatabaseConfig(Environment env, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                        .option(ConnectionFactoryOptions.DRIVER, "mysql")
                        .option(ConnectionFactoryOptions.HOST, "127.0.0.1")
                        .option(ConnectionFactoryOptions.PORT, 3306)
                        .option(ConnectionFactoryOptions.USER, "root")
                        .option(ConnectionFactoryOptions.PASSWORD, "123456")
                        .option(ConnectionFactoryOptions.DATABASE, "acorder")
                .build());
    }
    @Override
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Converter<?,?>> converters=new ArrayList<>();
        converters.add(new ServiceSliceToJsonConverter(objectMapper));
        converters.add(new ActionsToJsonConvertor(objectMapper));
        converters.add(new ActionToJsonConvertor(objectMapper));
        converters.add(new JsonToServiceSliceConverter(objectMapper));
        converters.add(new JsonToActionsConvertor(objectMapper));
        converters.add(new JsonToActionConvertor(objectMapper));
        return new R2dbcCustomConversions(getStoreConversions(),converters);
    }
}