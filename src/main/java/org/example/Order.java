package org.example;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.*;
@Data
@Table("ACsorder")
@AllArgsConstructor
public class Order {
    private @Id Long id;
    @LastModifiedDate
    public LocalDateTime LastDate;
    public Status status;
    @Column("actions")
    public Map<LocalDateTime,Action> actions;
    @Column("service_slice")
    public ServiceSlice serviceSlice;
    public Long CustomerId;
    public String CustomerName;
    public String RoomId;
    public Order(LocalDateTime LastDate, Status status, ServiceSlice serviceSlice, Long CustomerId, String CustomerName, String RoomId) {
        this.LastDate = LastDate;
        this.status = status;
        this.serviceSlice = serviceSlice;
        this.actions = new HashMap<>();
        this.CustomerId = CustomerId;
        this.CustomerName = CustomerName;
        this.RoomId = RoomId;
    }
    public Order(LocalDateTime LastDate, Status status, ServiceSlice serviceSlice, Long CustomerId, String CustomerName, String RoomId, Map<LocalDateTime,Action> actions, Long id) {
        this.LastDate = LastDate;
        this.status = status;
        this.serviceSlice = serviceSlice;
        this.actions = actions;
        this.CustomerId = CustomerId;
        this.CustomerName = CustomerName;
        this.RoomId = RoomId;
        this.id = id;
    }
    public Order(){}

}
