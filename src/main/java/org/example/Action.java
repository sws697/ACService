package org.example;

import lombok.Data;

import java.io.Serializable;
@Data
public class Action implements Serializable {
    private ActionType actionType;
    private String actionDescription;
    private Double cost;
    public Action(ActionType actionType,Double cost) {
        this.cost=cost;
        this.actionType = actionType;
    }

    public Action(ActionType actionType, String actionDescription,Double cost) {
        this.actionType = actionType;
        this.actionDescription = actionDescription;
        this.cost=cost;
    }
    public Action(){}
}
