package org.example;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.example.ACService.*;

@Service
public class Scheduler {
    public static int resourcesCount = 3;
    public static final int SliceSeconds = 120;
    public static boolean StatusChange = false;
    public enum Mode{
        COOL,HEAT
    }
    public static Mode mode = Mode.COOL;
    @Scheduled(cron = "*/2 * * * * ?")
    public void ResumeTemp()
    {
        for(var it :RoomToOrder.entrySet())
        {
            if(it.getValue()!=null)
            {
                Order order = it.getValue();
                if(order.status!=Status.IN_PROGRESS)
                {
                    if(order.serviceSlice.getCurrent_temp()<defaultTemp.get(it.getKey()))
                    {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp()+1);
                    } else if ( order.serviceSlice.getCurrent_temp()>defaultTemp.get(it.getKey())) {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp()-1);
                    }
                }
            }
        }
    }
    @Scheduled(cron = "*/1 * * * * ?")
    public void UpdateTempHigh()
    {
        if(mode == Mode.COOL){
            for (Order order : servingQueue) {
                if(order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()){
                     if (order.getServiceSlice().getSpeed() == Speed.HIGH) {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() - 1);
                         order.serviceSlice.setCost(order.serviceSlice.getCost()+1);
                        if(Objects.equals(order.serviceSlice.getCurrent_temp(), order.serviceSlice.getTarget_temp()))
                        {
                            order.status=Status.PAUSED;
                            order.LastDate = LocalDateTime.now();
                            order.actions.put(order.LastDate, new Action(ActionType.Pause, order.serviceSlice.getCost()));
                            servingQueue.remove(order);
                        }
                    }
                }
            }
        }else if(mode == Mode.HEAT){
            for (Order order : servingQueue) {
                if(order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()){
                    if (order.getServiceSlice().getSpeed() == Speed.HIGH) {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + 1);
                        order.serviceSlice.setCost(order.serviceSlice.getCost()+1);
                        if(Objects.equals(order.serviceSlice.getCurrent_temp(), order.serviceSlice.getTarget_temp()))
                        {
                            order.status=Status.PAUSED;
                            order.LastDate = LocalDateTime.now();
                            order.actions.put(order.LastDate, new Action(ActionType.Pause, order.serviceSlice.getCost()));
                            servingQueue.remove(order);//移除了,但是没有加入waitingQueue
                        }
                    }
                }
            }

        }
    }
    @Scheduled(cron = "*/2 * * * * ?")
    public void UpdateTempMedium()
    {
        if(mode == Mode.COOL){
            for (Order order : servingQueue) {
                if(order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()){
                    if (order.getServiceSlice().getSpeed() == Speed.MEDIUM) {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() - 1);
                        order.serviceSlice.setCost(order.serviceSlice.getCost()+1);
                        if(Objects.equals(order.serviceSlice.getCurrent_temp(), order.serviceSlice.getTarget_temp()))
                        {
                            order.status=Status.PAUSED;
                            order.LastDate = LocalDateTime.now();
                            order.actions.put(order.LastDate, new Action(ActionType.Pause, order.serviceSlice.getCost()));
                            servingQueue.remove(order);
                        }
                    }
                }
            }
        }else if(mode == Mode.HEAT){
            for (Order order : servingQueue) {
                if(order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()){
                    if (order.getServiceSlice().getSpeed() == Speed.MEDIUM) {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + 1);
                        order.serviceSlice.setCost(order.serviceSlice.getCost()+1);
                        if(Objects.equals(order.serviceSlice.getCurrent_temp(), order.serviceSlice.getTarget_temp()))
                        {
                            order.status=Status.PAUSED;
                            order.LastDate = LocalDateTime.now();
                            order.actions.put(order.LastDate, new Action(ActionType.Pause, order.serviceSlice.getCost()));
                            servingQueue.remove(order);
                        }
                    }
                }
            }

        }
    }

    @Scheduled(cron = "*/3 * * * * ?")
    public void UpdateTempLow()
    {
        if(mode == Mode.COOL){
            for (Order order : servingQueue) {
                if(order.serviceSlice.getCurrent_temp() > order.serviceSlice.getTarget_temp()){
                    if (order.getServiceSlice().getSpeed() == Speed.LOW) {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() - 1);
                        order.serviceSlice.setCost(order.serviceSlice.getCost()+1);
                        if(Objects.equals(order.serviceSlice.getCurrent_temp(), order.serviceSlice.getTarget_temp()))
                        {
                            order.status=Status.PAUSED;
                            order.LastDate = LocalDateTime.now();
                            order.actions.put(order.LastDate, new Action(ActionType.Pause, order.serviceSlice.getCost()));
                            servingQueue.remove(order);
                        }
                    }
                }
            }
        }else if(mode == Mode.HEAT){
            for (Order order : servingQueue) {
                if(order.serviceSlice.getCurrent_temp() < order.serviceSlice.getTarget_temp()){
                    if (order.getServiceSlice().getSpeed() == Speed.LOW) {
                        order.serviceSlice.setCurrent_temp(order.serviceSlice.getCurrent_temp() + 1);
                        order.serviceSlice.setCost(order.serviceSlice.getCost()+1);
                        if(Objects.equals(order.serviceSlice.getCurrent_temp(), order.serviceSlice.getTarget_temp()))
                        {
                            order.status=Status.PAUSED;
                            order.LastDate = LocalDateTime.now();
                            order.actions.put(order.LastDate, new Action(ActionType.Pause, order.serviceSlice.getCost()));
                            servingQueue.remove(order);
                        }
                    }
                }
            }

        }
    }


    @Scheduled(cron = "*/120 * * * * ?")
    public void renewStatus() {
        StatusChange = false;
    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void checkQueue() {
        System.out.println("Checking Queue");
        if (resourcesCount > 0) {
            while (resourcesCount > 0 && !waitingQueue.isEmpty()) {
                System.out.println("Resource Available");
                resourcesCount--;
                waitingQueue.sort((order1, order2) ->
                {
                    if (!Objects.equals(order1.serviceSlice.getSpeed().getValue(), order2.serviceSlice.getSpeed().getValue()))
                        return (int) (order2.serviceSlice.getSpeed().getValue() - order1.serviceSlice.getSpeed().getValue());
                    else {
                        return order1.LastDate.compareTo(order2.LastDate);
                    }
                });
                Order order = waitingQueue.remove(0);
                order.status = Status.IN_PROGRESS;
                order.LastDate = LocalDateTime.now();
                order.actions.put(order.LastDate, new Action(ActionType.Serve, order.serviceSlice.getCost()));
                servingQueue.add(order);
                StatusChange = true;
            }
        }
        else
        {
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
                        return (int) (order2.serviceSlice.getSpeed().getValue() - order1.serviceSlice.getSpeed().getValue());
                    else {
                        return order1.LastDate.compareTo(order2.LastDate);
                    }
                });
                Order servingFrontOrder = servingQueue.get(0);
                Order waitingFrontOrder = waitingQueue.get(0);
                if (waitingFrontOrder.getServiceSlice().getSpeed().getValue() > servingFrontOrder.getServiceSlice().getSpeed().getValue()) {
                    QueueHeadSwap(servingFrontOrder, waitingFrontOrder);
                } else if (Objects.equals(waitingFrontOrder.getServiceSlice().getSpeed().getValue(), servingFrontOrder.getServiceSlice().getSpeed().getValue())) {
                    if ((!StatusChange) && LocalDateTime.now().get(ChronoField.SECOND_OF_DAY) - waitingFrontOrder.LastDate.get(ChronoField.SECOND_OF_DAY) >= SliceSeconds) {
                        QueueHeadSwap(servingFrontOrder, waitingFrontOrder);
                    }
                }


        }
    }

    private void QueueHeadSwap(Order servingFrontOrder, Order waitingFrontOrder) {
        servingQueue.remove(0);
        waitingQueue.remove(0);
        servingFrontOrder.status = Status.WAITING;
        servingFrontOrder.LastDate = LocalDateTime.now();
        servingFrontOrder.actions.put(servingFrontOrder.LastDate, new Action(ActionType.Pause, servingFrontOrder.serviceSlice.getCost()));
        waitingQueue.add(servingFrontOrder);
        waitingFrontOrder.status = Status.IN_PROGRESS;
        waitingFrontOrder.LastDate = LocalDateTime.now();
        waitingFrontOrder.actions.put(waitingFrontOrder.LastDate, new Action(ActionType.Serve, waitingFrontOrder.serviceSlice.getCost()));
        servingQueue.add(waitingFrontOrder);
        StatusChange = true;
    }
}
