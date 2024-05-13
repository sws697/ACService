package org.example;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Objects;

import static org.example.ACService.servingQueue;
import static org.example.ACService.waitingQueue;

@Service
public class Scheduler {
    public static int resourcesCount=3;
    public static final int SliceSeconds=120;
    @Scheduled(cron = "*/5 * * * * ?")
    public void checkQueue()
    {
        System.out.println("Checking Queue");
        if(resourcesCount>0)
        {
            while(resourcesCount>0 && !waitingQueue.isEmpty()){
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
            }
            if(!servingQueue.isEmpty())
            {
                for(Order order:servingQueue)
                {
                    LocalDateTime nowdate=LocalDateTime.now();
                    long diff=nowdate.get(ChronoField.SECOND_OF_DAY)-order.LastDate.get(ChronoField.SECOND_OF_DAY);
                    if(diff>=SliceSeconds)
                    {
                        order.LastDate=nowdate;
                        order.actions.put(order.LastDate,new Action(ActionType.Pause,order.serviceSlice.getCost()));
                        order.status=Status.WAITING;
                        resourcesCount++;
                    }
                }
            }
        }
        else
        {
            for(Order order:servingQueue)
            {
                LocalDateTime nowdate=LocalDateTime.now();
                long diff=nowdate.get(ChronoField.SECOND_OF_DAY)-order.LastDate.get(ChronoField.SECOND_OF_DAY);
                if(diff>=SliceSeconds)
                {
                    order.LastDate=nowdate;
                    order.actions.put(order.LastDate,new Action(ActionType.Pause,order.serviceSlice.getCost()));
                    order.status=Status.WAITING;
                    resourcesCount++;
                }
            }
            waitingQueue.sort((order1,order2)->
            {
                if(!Objects.equals(order1.serviceSlice.getSpeed().getValue(), order2.serviceSlice.getSpeed().getValue()))
                    return (int) (order2.serviceSlice.getSpeed().getValue()-order1.serviceSlice.getSpeed().getValue());
                else{
                    return order1.LastDate.compareTo(order2.LastDate);
                }
            });


        }
    }
}
