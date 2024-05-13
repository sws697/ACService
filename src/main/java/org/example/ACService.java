package org.example;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableR2dbcAuditing
@EnableScheduling
public class ACService {

    public static ArrayList<Order> servingQueue = new ArrayList<>();
    public static ArrayList<Order> waitingQueue = new ArrayList<>();
    public static Map<String,String> RoomToCustomerPassword = new HashMap<>();
    public static Map<String,Order> RoomToOrder = new HashMap<>();
    public static void main(String[] args) {
        RoomToOrder.put("room_1",null);
        RoomToOrder.put("room_2",null);
        RoomToOrder.put("room_3",null);
        RoomToOrder.put("room_4",null);
        RoomToOrder.put("room_5",null);
        SpringApplication.run(ACService.class, args);
    }

}