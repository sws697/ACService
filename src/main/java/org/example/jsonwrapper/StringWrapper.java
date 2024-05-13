package org.example.jsonwrapper;

import lombok.Data;

@Data
public class StringWrapper {
    private String msg;
    public StringWrapper(String msg) {
        this.msg = msg;
    }
}
