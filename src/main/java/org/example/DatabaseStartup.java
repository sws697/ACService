package org.example;

import org.example.convertor.ActionToJsonConvertor;
import org.example.convertor.ActionsToJsonConvertor;
import org.example.convertor.ServiceSliceToJsonConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.test.StepVerifier;

@Configuration
public class DatabaseStartup {
    @Bean
    CommandLineRunner initDatabase(R2dbcEntityTemplate template, ActionsToJsonConvertor actionsToJsonConvertor, ServiceSliceToJsonConverter serviceSliceToJsonConverter, ActionToJsonConvertor actionToJsonConvertor){
        return args ->{
             template.getDatabaseClient().sql("CREATE TABLE IF NOT EXISTS register (customer_id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), password VARCHAR(255))").fetch().rowsUpdated()
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();
//            template.insert(RegisterEntity.class)
//                    .using(new RegisterEntity("Alice", 2L, "123456"))
//                    .as(StepVerifier::create)
//                    .expectNextCount(1)
//                    .verifyComplete();
            template.getDatabaseClient().sql("CREATE TABLE IF NOT EXISTS ACssorder (id BIGINT PRIMARY KEY AUTO_INCREMENT, last_date TIMESTAMP, status VARCHAR(255), actions JSON, service_slice JSON, customer_id BIGINT, customer_name VARCHAR(255), room_id VARCHAR(255), check_in_days INT, check_in_fee INT)").fetch().rowsUpdated()
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();
//            Map<LocalDateTime,Action> actions=new HashMap<>();
//            actions.put(LocalDateTime.now(),new Action(ActionType.ChangeTemp,"26",0.0));
//
//
//            Order order2=new Order(LocalDateTime.now(),Status.INITIAL,new ServiceSlice(Speed.low,0.0,25,26),2L,"Alice","101",actions,2L);
//            String serviceSliceJsonString=serviceSliceToJsonConverter.convert(order2.serviceSlice);
//            System.out.println(serviceSliceJsonString);
//            String actionJsonString=actionToJsonConvertor.convert(new Action(ActionType.ChangeTemp,"26",0.0));
//            System.out.println(actionJsonString);
//            String actionsJsonString=actionsToJsonConvertor.convert(order2.actions);
//            System.out.println(actionsJsonString);
//
//            template.insert(Order.class)
//                    .using(new Order(LocalDateTime.now(),Status.INITIAL,new ServiceSlice(Speed.low,0.0,25,26),2L,"Alice","101",actions,1L))
//                    .as(StepVerifier::create)
//                    .expectNextCount(1)
//                    .verifyComplete();
//            Order order1=template.selectOne(Query.query(Criteria.where("id").is(1L)),Order.class).block();
//            String serviceSliceJsonString=serviceSliceToJsonConverter.convert(order1.serviceSlice);
//            System.out.println(serviceSliceJsonString);
        };
    }
}
