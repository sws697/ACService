package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@SpringBootApplication
@EnableR2dbcAuditing
@EnableScheduling
public class ACService {
    public static Map<String,Integer> defaultTempSummer = new HashMap<>();
    public static Map<String,Integer> defaultTempWinter=new HashMap<>();
    public static ArrayList<Order> servingQueue = new ArrayList<>();
    public static ArrayList<Order> waitingQueue = new ArrayList<>();
    public static Map<String,String> RoomToCustomerPassword = new HashMap<>();
    public static Map<String,Order> RoomToOrder = new HashMap<>();

    public static Map<String,Integer> FeePerDayPerRoom = new HashMap<>();
    public static void main(String[] args) {
        RoomToOrder.put("1",null);
        RoomToOrder.put("2",null);
        RoomToOrder.put("3",null);
        RoomToOrder.put("4",null);
        RoomToOrder.put("5",null);
        defaultTempSummer.put("1",32);
        defaultTempSummer.put("2",28);
        defaultTempSummer.put("3",30);
        defaultTempSummer.put("4",29);
        defaultTempSummer.put("5",35);
        defaultTempWinter.put("1",10);
        defaultTempWinter.put("2",15);
        defaultTempWinter.put("3",18);
        defaultTempWinter.put("4",12);
        defaultTempWinter.put("5",14);
        FeePerDayPerRoom.put("1",100);
        FeePerDayPerRoom.put("2",125);
        FeePerDayPerRoom.put("3",150);
        FeePerDayPerRoom.put("4",200);
        FeePerDayPerRoom.put("5",100);
        RoomToCustomerPassword.put("1","1");
        RoomToCustomerPassword.put("2","1");
        RoomToCustomerPassword.put("3","1");
        RoomToCustomerPassword.put("4","5");
        RoomToCustomerPassword.put("5","1");
        SpringApplication.run(ACService.class, args);
    }

}