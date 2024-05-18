package org.example;

import lombok.Getter;

import java.io.Serializable;
@Getter
public enum ActionType implements Serializable {
    Enter,Request,Serve, SwitchOut,Pause, ArrivedPause,ChangeSpeed,ChangeTemp,OUT,COMPLETE,Query
}
