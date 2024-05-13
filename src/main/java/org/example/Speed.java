package org.example;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Speed implements Serializable {
    LOW(0.1), MEDIUM(0.3), HIGH(0.5);
    private final Double value;
     Speed(Double value){
         this.value = value;
   }


}
