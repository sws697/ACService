package org.example;

import org.example.jsonwrapper.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

import static org.example.ACService.*;

@RestController
@RequestMapping("/user")
public class ApiController {


    private final OrderRepository orderRepository;
    private final RegisterEntityRepository registerEntityRepository;
    public ApiController(OrderRepository orderRepository, RegisterEntityRepository registerEntityRepository) {
        this.orderRepository = orderRepository;
        this.registerEntityRepository = registerEntityRepository;
    }
    @PostMapping("/register")
    public Mono<ResponseEntity<StringWrapper>> Register(@RequestBody Mono<RegisterEntityWrapper> registerEntityWrapperMono)
    {
        return registerEntityWrapperMono.publishOn(Schedulers.boundedElastic()).mapNotNull(
                registerEntityWrapper->{
                    RegisterEntity registerEntity=registerEntityWrapper.data;
                    if(registerEntityRepository.findById(registerEntity.customer_id).map(registerEntity1 -> false).blockOptional().orElse(true))
                    {
                        return  registerEntityRepository.save(registerEntity).map(
                                registerEntity1 -> {
                                    return ResponseEntity.ok(new StringWrapper("success"));
                                }
                        ).block();
                    }
                    else
                    {
                        return ResponseEntity.status(400).body(new StringWrapper("failed to register: user already exist"));
                    }
                }
        );
    }
    @PostMapping("/check_available_room")
    public Mono<ResponseEntity<StringWrapper>> CheckAvailableRoom(@RequestBody Mono<RegisterEntityWrapper> registerEntityWrapperMono)
    {
        return registerEntityWrapperMono.mapNotNull(
          registerEntityWrapper->{
              RegisterEntity registerEntity=registerEntityWrapper.data;
              for(var entry:RoomToOrder.entrySet())
              {
                  if(entry.getValue()==null||entry.getValue().status.equals(Status.COMPLETE))
                  {
                      Order newOrder=new Order(LocalDateTime.now(),Status.INITIAL,new ServiceSlice(Speed.MEDIUM,0.0,31,25), registerEntity.customer_id, registerEntity.name,entry.getKey());
                      RoomToOrder.replace(entry.getKey(),newOrder);
                        return ResponseEntity.ok(new StringWrapper(entry.getKey()));
                  }
              }
                return ResponseEntity.status(400).body(new StringWrapper("no room available"));
          }
        );
    }
    @PostMapping("/enter_room")
    public Mono<ResponseEntity<StringWrapper>> EnterRoom(@RequestBody Mono<EnterEntityWrapper> enterEntityWrapperMono)
    {
        return enterEntityWrapperMono.map(
                enterEntityWrapper->{
                    EnterEntity enterEntity=enterEntityWrapper.data;
                    if(ACService.RoomToCustomerPassword.get(enterEntity.getRoom_id())!=null&&(RoomToCustomerPassword.get(enterEntity.getRoom_id())==null||(ACService.RoomToCustomerPassword.get(enterEntity.getRoom_id()).equals(enterEntity.getPassword()))))
                    {
                        Order nowOrder=RoomToOrder.get(enterEntity.getRoom_id());
                        if(nowOrder.getStatus().equals(Status.INITIAL)||nowOrder.status.equals(Status.OUT)){
                            nowOrder.setStatus(Status.ENTERED);
                            nowOrder.LastDate= LocalDateTime.now();
                            nowOrder.actions.put(nowOrder.LastDate,new Action(ActionType.Enter,nowOrder.serviceSlice.getCost()));
                            return ResponseEntity.ok(new StringWrapper("success"));
                        } else if (nowOrder.status.equals(Status.COMPLETE)) {
                            return ResponseEntity.status(400).body(new StringWrapper("Illegal operation"));
                        }else{
                            return ResponseEntity.status(400).body(new StringWrapper("You are already in the room"));
                        }
                    }
                    else if(ACService.RoomToCustomerPassword.get(enterEntity.getRoom_id())==null)
                    {
                        return ResponseEntity.status(400).body(new StringWrapper("room not exist"));
                    }
                    else
                    {
                        return ResponseEntity.status(400).body(new StringWrapper("failed to enter room"));
                    }
                }
        );
    }

    @PostMapping("/request_service")
    public Mono<ResponseEntity<ServiceSliceWrapper>> RequestService(@RequestBody Mono<ServiceWrapper> serviceWrapperMono)
    {
        return serviceWrapperMono.map(
                serviceWrapper->{
                    Service service=serviceWrapper.data;
                    Order nowOrder=RoomToOrder.get(service.room_id);
                    if(nowOrder==null)
                    {
                        return ResponseEntity.status(400).body(null);
                    }else{
                        if(!(nowOrder.status.equals(Status.INITIAL) || nowOrder.status.equals(Status.OUT)||nowOrder.status.equals(Status.PAUSED)))
                        {
                            return ResponseEntity.status(400).body(null);
                        }else{;
                            waitingQueue.add(nowOrder);
                            nowOrder.status=Status.WAITING;
                            nowOrder.LastDate=LocalDateTime.now();
                            nowOrder.actions.put(nowOrder.LastDate,new Action(ActionType.Request,nowOrder.serviceSlice.getCost()));
                            ServiceSliceWrapper serviceSliceWrapper=new ServiceSliceWrapper(nowOrder.serviceSlice,"ok");
                            return ResponseEntity.ok(serviceSliceWrapper);
                        }
                    }
                }
        );
    }
    @PostMapping("/info")
    public Mono<ResponseEntity<ServiceSliceWrapper>> Info(@RequestBody Mono<ServiceWrapper> serviceWrapperMono){
        return serviceWrapperMono.map(
                serviceWrapper->{
                    Service service=serviceWrapper.data;
                    Order nowOrder=RoomToOrder.get(service.room_id);
                    if(nowOrder==null)
                    {
                        return ResponseEntity.status(400).body(null);
                    } else if (nowOrder.status.equals(Status.INITIAL)||nowOrder.status.equals(Status.OUT)) {
                        ServiceSliceWrapper serviceSliceWrapper=new ServiceSliceWrapper(null,"You are not in the room");
                        return ResponseEntity.status(400).body(serviceSliceWrapper);
                    } else if (nowOrder.status.equals(Status.COMPLETE)) {
                        ServiceSliceWrapper serviceSliceWrapper=new ServiceSliceWrapper(null,"Illegal operation");
                        return ResponseEntity.status(400).body(serviceSliceWrapper);
                    } else if(nowOrder.status.equals(Status.IN_PROGRESS)){
                        LocalDateTime nowdate=LocalDateTime.now();
                        nowOrder.serviceSlice.setCost(nowOrder.serviceSlice.getSpeed().getValue()*(nowdate.get(ChronoField.SECOND_OF_DAY)-nowOrder.LastDate.get(ChronoField.SECOND_OF_DAY))+nowOrder.serviceSlice.getCost());
                        nowOrder.LastDate=nowdate;
                        nowOrder.actions.put(nowdate,new Action(ActionType.Query,nowOrder.serviceSlice.getCost()));
                        ServiceSliceWrapper serviceSliceWrapper=new ServiceSliceWrapper(nowOrder.serviceSlice,"ok");
                        return ResponseEntity.ok(serviceSliceWrapper);
                    }else{
                        LocalDateTime nowdate=LocalDateTime.now();
                        nowOrder.LastDate=nowdate;
                        nowOrder.actions.put(nowdate,new Action(ActionType.Query,nowOrder.serviceSlice.getCost()));
                        ServiceSliceWrapper serviceSliceWrapper=new ServiceSliceWrapper(nowOrder.serviceSlice,"ok");
                        return ResponseEntity.ok(serviceSliceWrapper);
                    }
                }
        );
    }
    @PostMapping("/set_speed")
    public Mono<ResponseEntity<StringWrapper>> SetSpeed(@RequestBody Mono<SpeedJsonWrapper> SpeedJsonWrapperMono) {
        return SpeedJsonWrapperMono.map(
                speedJsonWrapper -> {
                    SpeedJson speedJson = speedJsonWrapper.data;
                    Order nowOrder = RoomToOrder.get(speedJson.room_id);
                    if (nowOrder == null||!nowOrder.status.equals(Status.IN_PROGRESS)) {
                        return ResponseEntity.status(400).body(null);
                    } else {
                        LocalDateTime nowDate =LocalDateTime.now();
                        nowOrder.serviceSlice.setCost(Speed.valueOf(speedJson.speed).getValue()*(nowDate.get(ChronoField.SECOND_OF_DAY)-nowOrder.LastDate.get(ChronoField.SECOND_OF_DAY))+nowOrder.serviceSlice.getCost());
                        nowOrder.actions.put(nowDate, new Action(ActionType.ChangeSpeed, speedJson.speed,nowOrder.serviceSlice.getCost()));
                        nowOrder.serviceSlice.setSpeed(Speed.valueOf(speedJson.speed));
                        nowOrder.LastDate = nowDate;
                        StringWrapper stringWrapper = new StringWrapper("ok");
                        return ResponseEntity.ok(stringWrapper);
                    }
                }
        );
    }
    @PostMapping("/set_temp")
    public Mono<ResponseEntity<StringWrapper>> SetTemp(@RequestBody Mono<TempJsonWrapper> TempJsonWrapperMono) {
        return TempJsonWrapperMono.map(
                tempJsonWrapper -> {
                    TempJson tempJson = tempJsonWrapper.data;
                    Order nowOrder = RoomToOrder.get(tempJson.room_id);
                    if (nowOrder == null||!nowOrder.status.equals(Status.IN_PROGRESS)) {
                        return ResponseEntity.status(400).body(null);
                    } else {
                        LocalDateTime nowDate =LocalDateTime.now();
                        nowOrder.serviceSlice.setCost(nowOrder.serviceSlice.getSpeed().getValue()*(nowDate.get(ChronoField.SECOND_OF_DAY)-nowOrder.LastDate.get(ChronoField.SECOND_OF_DAY))+nowOrder.serviceSlice.getCost());
                        nowOrder.actions.put(nowDate, new Action(ActionType.ChangeTemp,String.valueOf(tempJson.temp),nowOrder.serviceSlice.getCost()));
                        nowOrder.serviceSlice.setTarget_temp(tempJson.temp);
                        nowOrder.LastDate = nowDate;
                        StringWrapper stringWrapper = new StringWrapper("ok");
                        return ResponseEntity.ok(stringWrapper);
                    }
                }
        );
    }
    @PostMapping("/pause_service")
    public Mono<ResponseEntity<StringWrapper>> PauseService(@RequestBody Mono<ServiceWrapper> serviceWrapperMono) {
        return serviceWrapperMono.map(
                serviceWrapper -> {
                    Service service = serviceWrapper.data;
                    Order nowOrder = RoomToOrder.get(service.room_id);
                    if (nowOrder == null) {
                        return ResponseEntity.status(400).body(null);
                    } else {
                        if (servingQueue.contains(nowOrder)) {
                            servingQueue.remove(nowOrder);
                            nowOrder.status = Status.PAUSED;
                            LocalDateTime nowDate =LocalDateTime.now();
                            nowOrder.serviceSlice.setCost(nowOrder.serviceSlice.getSpeed().getValue()*(nowDate.get(ChronoField.SECOND_OF_DAY)-nowOrder.LastDate.get(ChronoField.SECOND_OF_DAY))+nowOrder.serviceSlice.getCost());
                            nowOrder.actions.put(nowDate, new Action(ActionType.Pause,nowOrder.serviceSlice.getCost()));
                            nowOrder.LastDate = nowDate;
                            StringWrapper stringWrapper = new StringWrapper("ok");
                            return ResponseEntity.ok(stringWrapper);
                        } else if (waitingQueue.contains(nowOrder)) {
                            waitingQueue.remove(nowOrder);
                            nowOrder.status = Status.PAUSED;
                            LocalDateTime nowDate =LocalDateTime.now();
                            nowOrder.actions.put(nowDate, new Action(ActionType.Pause,nowOrder.serviceSlice.getCost()));
                            nowOrder.LastDate = nowDate;
                            StringWrapper stringWrapper = new StringWrapper("ok");
                            return ResponseEntity.ok(stringWrapper);
                        } else {
                            return ResponseEntity.status(400).body(null);
                        }
                    }
                }
        );
    }
    @PostMapping("/quit_room")
    public Mono<ResponseEntity<StringWrapper>> QuitRoom(@RequestBody Mono<ServiceWrapper> serviceWrapperMono) {
        return serviceWrapperMono.map(
                serviceWrapper -> {
                    Service service = serviceWrapper.data;
                    Order nowOrder = RoomToOrder.get(service.room_id);
                    if (nowOrder == null) {
                        return ResponseEntity.status(400).body(null);
                    } else {
                        if (servingQueue.contains(nowOrder)) {
                            servingQueue.remove(nowOrder);
                           LocalDateTime nowDate =LocalDateTime.now();
                            nowOrder.serviceSlice.setCost(nowOrder.serviceSlice.getSpeed().getValue()*(nowDate.get(ChronoField.SECOND_OF_DAY)-nowOrder.LastDate.get(ChronoField.SECOND_OF_DAY))+nowOrder.serviceSlice.getCost());
                            nowOrder.actions.put(nowDate, new Action(ActionType.OUT,nowOrder.serviceSlice.getCost()));
                            nowOrder.LastDate = nowDate;
                            nowOrder.status = Status.OUT;
                            StringWrapper stringWrapper = new StringWrapper("ok");
                            return ResponseEntity.ok(stringWrapper);
                        } else if (waitingQueue.contains(nowOrder)) {
                            waitingQueue.remove(nowOrder);
                            LocalDateTime nowDate =LocalDateTime.now();
                            nowOrder.actions.put(nowDate, new Action(ActionType.OUT,nowOrder.serviceSlice.getCost()));
                            nowOrder.LastDate = nowDate;
                            nowOrder.status = Status.OUT;
                            StringWrapper stringWrapper = new StringWrapper("ok");
                            return ResponseEntity.ok(stringWrapper);
                        } else if (nowOrder.status.equals(Status.PAUSED)) {
                            nowOrder.status = Status.OUT;
                            StringWrapper stringWrapper = new StringWrapper("ok");
                            return ResponseEntity.ok(stringWrapper);
                        } else {
                            return ResponseEntity.status(400).body(null);
                        }
                    }
                }
        );
    }
    @PostMapping("/check_bill")
    public Mono<ResponseEntity<Order>> CheckBill(@RequestBody Mono<StringWrapper> stringWrapperMono)
    {
        return stringWrapperMono.map(
                stringWrapper -> {
                    String string = stringWrapper.getMsg();
                    Order nowOrder = RoomToOrder.get(string);
                    if(nowOrder==null||!nowOrder.status.equals(Status.OUT)) {
                        return ResponseEntity.status(400).body(null);
                    }else{
                        LocalDateTime nowDate =LocalDateTime.now();
                        nowOrder.LastDate= nowDate;
                        return ResponseEntity.ok(nowOrder);
                    }
                }
        );
    }
    @PostMapping("/check_detailed_record")
    public Mono<ResponseEntity<Order>> CheckDetailedRecord(@RequestBody Mono<StringWrapper> stringWrapperMono)
    {
        return stringWrapperMono.map(
                stringWrapper -> {
                    String string = stringWrapper.getMsg();
                    Order nowOrder = RoomToOrder.get(string);
                    if(nowOrder==null||!nowOrder.status.equals(Status.OUT)) {
                        return ResponseEntity.status(400).body(null);
                    }else{
                        LocalDateTime nowDate =LocalDateTime.now();
                        nowOrder.LastDate= nowDate;
                        return ResponseEntity.ok(nowOrder);
                    }
                }
        );
    }
    @PostMapping("/complete")
    public Mono<ResponseEntity<?>> Complete(@RequestBody Mono<StringWrapper> stringWrapperMono)
    {
        return stringWrapperMono.publishOn(Schedulers.boundedElastic()).mapNotNull(
          stringWrapper -> {
              String string = stringWrapper.getMsg();
              Order nowOrder = RoomToOrder.get(string);
              if(nowOrder==null||!nowOrder.status.equals(Status.OUT)) {
                  return ResponseEntity.status(400).body(null);
              }else{
                    LocalDateTime nowDate =LocalDateTime.now();
                    nowOrder.LastDate= nowDate;
                    nowOrder.status=Status.COMPLETE;
                    return orderRepository.save(nowOrder).map(
                      order -> {
                          return ResponseEntity.status(200).body(null);
                      }
                    ).block();
              }
          }
        );
    }
}
