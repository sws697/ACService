package org.example;

import lombok.Getter;

import java.io.Serializable;
@Getter
public enum ActionType implements Serializable {
    Enter,Request,Serve,Pause,ChangeSpeed,ChangeTemp,OUT,COMPLETE,Query
}
