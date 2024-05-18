package org.example;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static org.example.ACService.*;

@Service
public class Scheduler {
    public static int resourcesCount = 3;
    public static final int SliceSeconds = 20;
    public static boolean StatusChange = false;
    public static LocalDateTime LastStatusChange = LocalDateTime.now();
    public static ArrayList<Order> ServingCacheQueue=new ArrayList<>();
    public enum Mode {
        COOL, HEAT
    }

    public static PrintWriter out;

    static {
        try {
            out = new PrintWriter(new FileOutputStream("log2.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Mode mode = Mode.HEAT;


    @Scheduled(fixedRate = 100)
    public void ResumeTemp() {
        for (var it : RoomToOrder.entrySet()) {
            if (it.getValue() != null) {
                Order order = it.getValue();
                if (order.status != Status.IN_PROGRESS) {
                    if (mode == Mode.COOL) {
                        if (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ResumeTimer.get(ChronoField.SECOND_OF_DAY) >= 20 && order.serviceSlice.getCurrent_temp() < defaultTempSummer.get(it.getKey())) {
                            order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + 1);
                            order.ResumeTimer = LocalDateTime.now();
                        } else if (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ResumeTimer.get(ChronoField.SECOND_OF_DAY) >= 20 && order.serviceSlice.getCurrent_temp() > defaultTempSummer.get(it.getKey())) {
                            order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() - 1);
                            order.ResumeTimer = LocalDateTime.now();
                        }
                    } else {
                        if (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ResumeTimer.get(ChronoField.SECOND_OF_DAY) >= 20 && order.serviceSlice.getCurrent_temp() < defaultTempWinter.get(it.getKey())) {
                            order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + 1);
                            order.ResumeTimer = LocalDateTime.now();
                        } else if (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ResumeTimer.get(ChronoField.SECOND_OF_DAY) >= 20 && order.serviceSlice.getCurrent_temp() > defaultTempWinter.get(it.getKey())) {
                            order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() - 1);
                            order.ResumeTimer = LocalDateTime.now();
                        }
                    }
                }
            }
        }
    }


    @Scheduled(fixedRate = 100)
    public void renewStatus() {
//        StatusChange = false;
        if (StatusChange && LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - LastStatusChange.get(ChronoField.SECOND_OF_DAY) >= SliceSeconds) {
            StatusChange = false;
        }
    }

    @Scheduled(fixedRate = 100)
    public void Serving() {
        if (mode == Mode.COOL) {
            Iterator<Order> iterator = servingQueue.iterator();
            while (iterator.hasNext()) {
                Order order = iterator.next();
                if ((LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ServingTimer.get(ChronoField.SECOND_OF_DAY)) >= 10) {
                if (order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()) {
                    ServingMinus(iterator, order);
                }
//                } else if (order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()) {
//                    ServingAdd(iterator, order);
//                } else {
//                    order.ServingTimer = LocalDateTime.now();
//                    order.serviceSlice.setCost(order.serviceSlice.getCost() + 1);
//                }
            }
//                } else if (order.serviceSlice.getSpeed().equals(Speed.medium) && (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ServingTimer.get(ChronoField.SECOND_OF_DAY)) >= 20) {
//                    if (order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()) {
//                        ServingMinus(iterator, order);
//                    } else if (order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()) {
//                        ServingAdd(iterator, order);
//                    } else {
//                        order.ServingTimer = LocalDateTime.now();
//                        order.serviceSlice.setCost(order.serviceSlice.getCost() + 1);
//                    }
//
//
//                } else if (order.serviceSlice.getSpeed().equals(Speed.high) && (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ServingTimer.get(ChronoField.SECOND_OF_DAY)) >= 10) {
//                    if (order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()) {
//                        ServingMinus(iterator, order);
//                    } else if (order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()) {
//                        ServingAdd(iterator, order);
//
//                    } else {
//                        order.ServingTimer = LocalDateTime.now();
//                        order.serviceSlice.setCost(order.serviceSlice.getCost() + 1);
//                    }
//                }
            }
        } else {
            Iterator<Order> iterator = servingQueue.iterator();
            while (iterator.hasNext()) {
                Order order = iterator.next();
                if ((LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ServingTimer.get(ChronoField.SECOND_OF_DAY)) >= 10) {
                    if (order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()) {
                        ServingAdd(iterator, order);
                    }
//                    } else if (order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()) {
//                        ServingMinus(iterator, order);
//                    } else {
//                        order.ServingTimer = LocalDateTime.now();
//                        order.serviceSlice.setCost(order.serviceSlice.getCost() + 1);
//                    }
                }
//                } else if (order.serviceSlice.getSpeed().equals(Speed.medium) && (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ServingTimer.get(ChronoField.SECOND_OF_DAY) )>= 20) {
//                    if (order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()) {
//                        ServingAdd(iterator, order);
//                    }else if (order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()) {
//                        ServingMinus(iterator, order);
//                    } else {
//                        order.ServingTimer = LocalDateTime.now();
//                        order.serviceSlice.setCost(order.serviceSlice.getCost() + 1);
//                    }
//                } else if (order.serviceSlice.getSpeed().equals(Speed.high) && (LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - order.ServingTimer.get(ChronoField.SECOND_OF_DAY) )>= 10) {
//                    if (order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()) {
//                        ServingAdd(iterator, order);
//                    }else if (order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()) {
//                        ServingMinus(iterator, order);
//                    } else {
//                        order.ServingTimer = LocalDateTime.now();
//                        order.serviceSlice.setCost(order.serviceSlice.getCost() + 1);
//                    }
//                }
            }
        }

    }

    private void ServingAdd(Iterator<Order> iterator, Order order) {
        if(order.serviceSlice.getSpeed().equals(Speed.low)){
            order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + Speed.low.getValue());
            order.serviceSlice.setCost(order.serviceSlice.getCost() + Speed.low.getValue());
        } else if (order.serviceSlice.getSpeed().equals(Speed.medium)) {
            order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + Speed.medium.getValue());
            order.serviceSlice.setCost(order.serviceSlice.getCost() + Speed.medium.getValue());
        } else if (order.serviceSlice.getSpeed().equals(Speed.high)) {
            order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + Speed.high.getValue());
            order.serviceSlice.setCost(order.serviceSlice.getCost() + Speed.high.getValue());
        }
        AddCostAndCheckTemp(iterator, order);
    }

    private void AddCostAndCheckTemp(Iterator<Order> iterator, Order order) {
        order.ServingTimer = LocalDateTime.now();
        if (Math.abs(order.serviceSlice.getCurrent_temp()-order.serviceSlice.getTarget_temp())<1e-3) {
            order.status = Status.WAITING;
            order.LastDate = LocalDateTime.now();
            order.actions.put(order.LastDate, new Action(ActionType.Pause, order.serviceSlice.getCost()));
            iterator.remove();
            StatusChange = true;
            LastStatusChange = LocalDateTime.now();
            ServingCacheQueue.add(order);
            order.CacheTimer = LocalDateTime.now();
            order.ResumeTimer = LocalDateTime.now();
            resourcesCount++;
        }//让位，导致位置出现空闲，不会进入时间片调度
    }

    private void ServingMinus(Iterator<Order> iterator, Order order) {
        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() - 1);
        AddCostAndCheckTemp(iterator, order);
    }

    @Scheduled(fixedRate = 100)
    public void CheckServingCacheQueue(){
        Iterator<Order> iterator = ServingCacheQueue.iterator();
        while(iterator.hasNext()){
            Order order = iterator.next();
            if(LocalDateTime.now().get(ChronoField.SECOND_OF_DAY)-order.CacheTimer.get(ChronoField.SECOND_OF_DAY)>=SliceSeconds){
                iterator.remove();
                waitingQueue.add(order);

            }
        }
    }

    @Scheduled(fixedRate = 100)
    public void checkQueue() {
//        System.out.println("Checking Queue");
        out.println("Service Queue:");
        for (var order : servingQueue) {
            out.println(LocalDateTime.now().toString() + ":");
            out.print(order.RoomId.toString() + ":  ");
            out.println(order.serviceSlice.toString());

        }
        out.println("Waiting Queue:");
        for (var order : waitingQueue) {
            out.println(LocalDateTime.now().toString() + ":");
            out.print(order.RoomId.toString() + ":  ");
            out.println(order.serviceSlice.toString());

        }
        out.flush();
        if (!waitingQueue.isEmpty()) {
            if (resourcesCount > 0) {
                while (resourcesCount > 0 && !waitingQueue.isEmpty()) {
//                    System.out.println("Resource Available");
                    resourcesCount--;
                    waitingQueue.sort((order1, order2) ->
                    {
                        if (!Objects.equals(order1.serviceSlice.getSpeed().getValue(), order2.serviceSlice.getSpeed().getValue()))
                            return (int) (order2.serviceSlice.getSpeed().getValue() * 1000 - order1.serviceSlice.getSpeed().getValue() * 1000);
                        else {
                            return order1.LastDate.compareTo(order2.LastDate);
                        }
                    });
                    if (!waitingQueue.isEmpty()) {
                        Order order = waitingQueue.remove(0);
                        order.status = Status.IN_PROGRESS;
                        order.ServingTimer = LocalDateTime.now();
                        order.LastDate = LocalDateTime.now();
                        order.actions.put(order.LastDate, new Action(ActionType.Serve, order.serviceSlice.getCost()));
                        servingQueue.add(order);
                        StatusChange = true;
                        LastStatusChange = LocalDateTime.now();
                        renewStatus();
                    }
                }
            } else {
//                System.out.println(resourcesCount);
                servingQueue.sort((order1, order2) ->
                {
                    if (!Objects.equals(order1.serviceSlice.getSpeed().getValue(), order2.serviceSlice.getSpeed().getValue()))
                        return (int) (order1.serviceSlice.getSpeed().getValue() * 1000 - order2.serviceSlice.getSpeed().getValue() * 1000);
                    else {
                        return order1.LastDate.compareTo(order2.LastDate);
                    }
                });
                waitingQueue.sort((order1, order2) ->
                {
                    if (!Objects.equals(order1.serviceSlice.getSpeed().getValue(), order2.serviceSlice.getSpeed().getValue()))
                        return (int) (order2.serviceSlice.getSpeed().getValue() * 1000 - order1.serviceSlice.getSpeed().getValue() * 1000);
                    else {
                        return order1.LastDate.compareTo(order2.LastDate);
                    }
                });
                if (!servingQueue.isEmpty() && !waitingQueue.isEmpty()) {
                    Order servingFrontOrder = servingQueue.get(0);
                    Order waitingFrontOrder = waitingQueue.get(0);
                    if (waitingFrontOrder.getServiceSlice().getSpeed().getValue() > servingFrontOrder.getServiceSlice().getSpeed().getValue()) {
                        QueueHeadSwap(servingFrontOrder, waitingFrontOrder);
                    } else if (Objects.equals(waitingFrontOrder.getServiceSlice().getSpeed().getValue(), servingFrontOrder.getServiceSlice().getSpeed().getValue())) {
                        int diff = LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - waitingFrontOrder.LastDate.get(ChronoField.SECOND_OF_DAY);
                        if ((!StatusChange) && diff >= SliceSeconds) {
                            QueueHeadSwap(servingFrontOrder, waitingFrontOrder);
                        }
                    }
                }


            }
        }
//        System.out.println(waitingQueue.toString());
//        System.out.println(servingQueue.toString());
    }

    private void QueueHeadSwap(Order servingFrontOrder, Order waitingFrontOrder) {
        servingQueue.remove(0);
        servingFrontOrder.ResumeTimer = LocalDateTime.now();
        waitingQueue.remove(0);
        servingFrontOrder.status = Status.WAITING;
        servingFrontOrder.LastDate = LocalDateTime.now();
        servingFrontOrder.actions.put(servingFrontOrder.LastDate, new Action(ActionType.Pause, servingFrontOrder.serviceSlice.getCost()));
        waitingQueue.add(servingFrontOrder);
        waitingFrontOrder.status = Status.IN_PROGRESS;
        waitingFrontOrder.LastDate = LocalDateTime.now();
        waitingFrontOrder.ServingTimer = LocalDateTime.now();
        waitingFrontOrder.actions.put(waitingFrontOrder.LastDate, new Action(ActionType.Serve, waitingFrontOrder.serviceSlice.getCost()));
        servingQueue.add(waitingFrontOrder);
        StatusChange = true;
        LastStatusChange = LocalDateTime.now();
    }
}
