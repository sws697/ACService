package org.example;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.*;
@Data
@Table("ACssorder")
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
    public Integer CheckInDays;
    public Integer CheckInFee;
    @Transient
    public LocalDateTime ServingTimer;
    @Transient
    public LocalDateTime ResumeTimer;
    @Transient
    public LocalDateTime WaitingTimer;
    @Transient
    public LocalDateTime ServingInTimer;
    public Order(LocalDateTime LastDate, Status status, ServiceSlice serviceSlice, Long CustomerId, String CustomerName, String RoomId) {
        this.LastDate = LastDate;
        this.status = status;
        this.serviceSlice = serviceSlice;
        this.actions = new TreeMap<>();
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
