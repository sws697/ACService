package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceSlice implements java.io.Serializable{
    private static final Logger log = LoggerFactory.getLogger(ServiceSlice.class);
    private Speed speed;
    private Integer target_temp;
    private Integer current_temp;
    private Double cost;
    public ServiceSlice() {
    }
    public ServiceSlice(Speed speed, Double cost, Integer current_temp, Integer target_temp) {
        this.speed = speed;
        this.cost = cost;
        this.current_temp = current_temp;
        this.target_temp = target_temp;
    }

    public Double getCost() {
        return cost;
    }

    public Integer getCurrent_temp() {
        return current_temp;
    }

    public Integer getTarget_temp() {
        return target_temp;
    }

    public void setTarget_temp(Integer target_temp) {
        this.target_temp = target_temp;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void setCurrent_temp(Integer current_temp) {
        this.current_temp = current_temp;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "ServiceSlice{" +
                "speed=" + speed +
                ", target_temp=" + target_temp +
                ", current_temp=" + current_temp +
                ", cost=" + cost +
                '}';
    }

}
