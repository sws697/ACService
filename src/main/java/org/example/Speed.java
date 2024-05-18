package org.example;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Speed implements Serializable {
    low(1/3.0), medium(1/2.0), high(1.0);
    private final Double value;
     Speed(Double value){
         this.value = value;
   }


}
