package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Data
@AllArgsConstructor
public class ServiceSlice implements java.io.Serializable{
    private static final Logger log = LoggerFactory.getLogger(ServiceSlice.class);
    private Speed speed;
    private Double target_temp;
    private Double current_temp;
    private Double cost;
    private String mode;
    public ServiceSlice() {
    }
    public ServiceSlice(Speed speed, Double cost, Double current_temp, Double target_temp) {
        this.speed = speed;
        this.cost = cost;
        this.current_temp = current_temp;
        this.target_temp = target_temp;
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
