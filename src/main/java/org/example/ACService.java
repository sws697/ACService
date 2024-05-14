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
    public static Map<String,Integer> defaultTemp = new HashMap<>();
    public static ArrayList<Order> servingQueue = new ArrayList<>();
    public static ArrayList<Order> waitingQueue = new ArrayList<>();
    public static Map<String,String> RoomToCustomerPassword = new HashMap<>();
    public static Map<String,Order> RoomToOrder = new HashMap<>();
    public static Map<String,Integer> FeePerDayPerRoom = new HashMap<>();
    public static void main(String[] args) {
        RoomToOrder.put("room_1",null);
        RoomToOrder.put("room_2",null);
        RoomToOrder.put("room_3",null);
        RoomToOrder.put("room_4",null);
        RoomToOrder.put("room_5",null);
        defaultTemp.put("room_1",32);
        defaultTemp.put("room_2",28);
        defaultTemp.put("room_3",30);
        defaultTemp.put("room_4",29);
        defaultTemp.put("room_5",35);
        FeePerDayPerRoom.put("room_1",100);
        FeePerDayPerRoom.put("room_2",125);
        FeePerDayPerRoom.put("room_3",150);
        FeePerDayPerRoom.put("room_4",200);
        FeePerDayPerRoom.put("room_5",100);
        SpringApplication.run(ACService.class, args);
    }

}